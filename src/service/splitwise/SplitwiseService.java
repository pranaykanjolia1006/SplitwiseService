package service.splitwise;

import service.User.User;
import service.splitwise.domain.Transaction;
import service.splitwise.domain.UsersPendingTransactions;
import service.splitwise.request.SplitBillRequest;
import service.splitwise.response.SplitBillResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SplitwiseService implements ISplitwiseService {

    private HashMap<User, List<Transaction>> usersToTransactions;
    public SplitwiseService() {
        this.usersToTransactions = new HashMap<>();
    }

    @Override
    public void getAllTransactions() {
        Set<Transaction> transactionsSet = new HashSet<>();
        for (List<Transaction> transactions: usersToTransactions.values()) {
            transactionsSet.addAll(new HashSet<>(transactions));
        }

        if (transactionsSet.isEmpty()) {
            System.out.println("No balances");
            return;
        }
        System.out.println("Total balances: " + transactionsSet.size());
        for (Transaction transaction: transactionsSet) {
            System.out.println(transaction.getFromUser().getName() + " owes to " + transaction.getToUser().getName() + ": " + transaction.getAmount());
        }
        System.out.println("**********************");
    }

    @Override
    public void getTransactionsByUser(User user) {
        List<Transaction> transactions = usersToTransactions.get(user);
        if (transactions == null || transactions.isEmpty()) {
            System.out.println("No balances");
            return;
        }

        Set<Transaction> transactionsSet = new HashSet<>(transactions);
        for (Transaction transaction: transactionsSet) {
            System.out.println(transaction.getFromUser().getName() + " owes to " + transaction.getToUser().getName() + ": " + transaction.getAmount());
        }
        System.out.println("**********************");
    }

    @Override
    public SplitBillResponse splitBill(SplitBillRequest splitBillRequest) {
        switch (splitBillRequest.getSplitType()) {
            case SPLIT_TYPE_EQUAL:
                this.splitBillEqually(splitBillRequest);
                break;
            case SPLIT_TYPE_EXACT:
                this.splitBillByExactAmount(splitBillRequest);
                break;
        }

        return new SplitBillResponse().splitBillResponseWithSuccess("Bill splitted successfully");
    }

    private Map<User, Double> getUsersCurrentPendingAmounts() {

        Map<User, Double> currentPendingAmounts = new HashMap<>();

        Set<Transaction>  oldPendingTransactionsSet = new HashSet<>();
        for (List<Transaction> transactions : usersToTransactions.values()) {
            oldPendingTransactionsSet.addAll(new HashSet<>(transactions));
        }

        for (Transaction transaction : oldPendingTransactionsSet) {
            User fromUser = transaction.getFromUser();
            User toUser = transaction.getToUser();

            if (!currentPendingAmounts.containsKey(fromUser)) {
                currentPendingAmounts.put(fromUser, 0.0);
            }
            if (!currentPendingAmounts.containsKey(toUser)) {
                currentPendingAmounts.put(toUser, 0.0);
            }

            currentPendingAmounts.put(fromUser, currentPendingAmounts.get(fromUser) - transaction.getAmount());
            currentPendingAmounts.put(toUser, currentPendingAmounts.get(toUser) + transaction.getAmount());
        }
        return currentPendingAmounts;
    }

    private UsersPendingTransactions getUsersPendingAmount(Map<User, Double> newPendingAmounts) {
        Map<User, Double> currentPendingAmounts = getUsersCurrentPendingAmounts();
        Map<User, Double> updatedPendingAmounts = new HashMap<>();

        for (Map.Entry<User, Double> entry : currentPendingAmounts.entrySet()) {
            if (!updatedPendingAmounts.containsKey(entry.getKey())) {
                updatedPendingAmounts.put(entry.getKey(), entry.getValue());
            }
            if (newPendingAmounts.containsKey(entry.getKey())) {
                updatedPendingAmounts.put(entry.getKey(), newPendingAmounts.get(entry.getKey()) + entry.getValue());
            }
        }

        for(Map.Entry<User, Double> entry : newPendingAmounts.entrySet()) {
            if (!updatedPendingAmounts.containsKey(entry.getKey())) {
                updatedPendingAmounts.put(entry.getKey(), entry.getValue());
            }
        }

        UsersPendingTransactions usersPendingTransactions = new UsersPendingTransactions();
        Map<User, Double> positivePendingAmountUsers = new HashMap<>();
        Map<User, Double> negativePendingAmountUsers = new HashMap<>();

        for (Map.Entry<User, Double> pendingAmountUser : updatedPendingAmounts.entrySet()) {
            User user = pendingAmountUser.getKey();
            Double value = pendingAmountUser.getValue();

            if (value > 0) {
                positivePendingAmountUsers.put(user, value);
            } else if (value < 0) {
                negativePendingAmountUsers.put(user, value*-1.0);
            }
        }

        usersPendingTransactions.setNegativePendingTransactionUsers(negativePendingAmountUsers);
        usersPendingTransactions.setPositivePendingTransactionUsers(positivePendingAmountUsers);

        return usersPendingTransactions;
    }

    private List<Transaction> getSimplifiedBalanceList(UsersPendingTransactions usersPendingTransactions) {
        Map<User, Double> positivePendingTransactionUsers = usersPendingTransactions.getPositivePendingTransactionUsers();
        Map<User, Double> negativePendingTransactionUsers = usersPendingTransactions.getNegativePendingTransactionUsers();

        List<Transaction> minTransactions = null;
        for (Map.Entry<User, Double> posEntry : positivePendingTransactionUsers.entrySet()) {
            User posUser = posEntry.getKey();
            Double posValue = posEntry.getValue();


            for (Map.Entry<User, Double> negEntry : negativePendingTransactionUsers.entrySet()) {
                User negUser = negEntry.getKey();
                Double negValue = negEntry.getValue();
                Map<User, Double> updatedPositivePendingTransactionUsers = new HashMap<>(positivePendingTransactionUsers);
                Map<User, Double> updatedNegativePendingTransactionUsers = new HashMap<>(negativePendingTransactionUsers);
                UsersPendingTransactions updatedUsersPendingTransactions = new UsersPendingTransactions();
                if (posValue > negValue) {
                    updatedNegativePendingTransactionUsers.remove(negUser);
                    updatedPositivePendingTransactionUsers.put(posUser, posValue - negValue);
                } else if (negValue > posValue) {
                    updatedPositivePendingTransactionUsers.remove(posUser);
                    updatedNegativePendingTransactionUsers.put(negUser, negValue - posValue);
                } else {
                    updatedPositivePendingTransactionUsers.remove(posUser);
                    updatedNegativePendingTransactionUsers.remove(negUser);
                }
                updatedUsersPendingTransactions.setPositivePendingTransactionUsers(updatedPositivePendingTransactionUsers);
                updatedUsersPendingTransactions.setNegativePendingTransactionUsers(updatedNegativePendingTransactionUsers);

                List<Transaction> transactions = getSimplifiedBalanceList(updatedUsersPendingTransactions);
                if (transactions == null) {
                    transactions = new ArrayList<>();
                }
                transactions.add(new Transaction(posUser, negUser, Math.min(posValue, negValue)));
                if (minTransactions == null || minTransactions.size() > transactions.size()) {
                    minTransactions = transactions;
                }
            }
        }

        return minTransactions;
    }

    private void updateUserTransactionsMap(List<Transaction> transactions) {
        usersToTransactions = new HashMap<>();
        for (Transaction transaction : transactions) {
            User toUser = transaction.getToUser();
            User fromUser = transaction.getFromUser();
            if (!usersToTransactions.containsKey(toUser)) {
                usersToTransactions.put(toUser, new ArrayList<>());
            }
            if (!usersToTransactions.containsKey(fromUser)) {
                usersToTransactions.put(fromUser, new ArrayList<>());
            }

            usersToTransactions.get(toUser).add(transaction);
            usersToTransactions.get(fromUser).add(transaction);
        }
    }

    void splitBillEqually(SplitBillRequest splitBillRequest) {
        Map<User, Double> newPendingAmount = new HashMap<>(splitBillRequest.getPaidBy().getUsers());
        Double equalAmountSplit = splitBillRequest.getTotalAmount() / splitBillRequest.getPaidFor().getUsers().size();

        for (User user: splitBillRequest.getPaidFor().getUsers()) {
            if (!newPendingAmount.containsKey(user)) {
                newPendingAmount.put(user, 0.0);
            }

            newPendingAmount.put(user, newPendingAmount.get(user) - equalAmountSplit);
        }

        UsersPendingTransactions usersPendingTransactions = getUsersPendingAmount(newPendingAmount);
        List<Transaction> minTransactions = getSimplifiedBalanceList(usersPendingTransactions);
        updateUserTransactionsMap(minTransactions);
    }

    private void splitBillByExactAmount(SplitBillRequest splitBillRequest) {
        Map<User, Double> newPendingAmount = new HashMap<>(splitBillRequest.getPaidBy().getUsers());

        for (Map.Entry<User, Double> entry : splitBillRequest.getPaidFor().getExactSplitDetails().getUserToAmount().entrySet()) {
            if (!newPendingAmount.containsKey(entry.getKey())) {
                newPendingAmount.put(entry.getKey(), 0.0);
            }

            newPendingAmount.put(entry.getKey(), newPendingAmount.get(entry.getKey()) - entry.getValue());
        }

        UsersPendingTransactions usersPendingTransactions = getUsersPendingAmount(newPendingAmount);
        List<Transaction> minTransactions = getSimplifiedBalanceList(usersPendingTransactions);
        updateUserTransactionsMap(minTransactions);
    }
}
