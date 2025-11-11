import common.domain.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SplitwiseService implements ISplitwiseService{

    private HashMap<User, List<Debt>> usersToDebtList;
    public SplitwiseService() {
        this.usersToDebtList = new HashMap<>();
    }

    private List<Pair<User, Float>> getUsersBalanceBySign(int sign) {

//        for (Map.Entry<User, List<Debt>> userDebtEntry : usersToDebtList.entrySet()) {
//            System.out.println("Key:" + userDebtEntry.getKey().getName());
//            for (Debt debt : userDebtEntry.getValue()) {
//                System.out.println(debt.getOwesTo().getName() + " owes to " + debt.getOwedTo().getName() + ": " + debt.getAmount());
//            }
//        }
//        System.out.println("sbhsbuhbaubbauinsibybsyubausb");
        List<Pair<User, Float>> usersBalance = new ArrayList<>();

        for (Map.Entry<User, List<Debt>> userDebtEntry : usersToDebtList.entrySet()) {
            Float totalAmount = 0.0f;
            for (Debt debt : userDebtEntry.getValue()) {
                if (debt.getOwedTo().equals(userDebtEntry.getKey())) {
                    totalAmount += debt.getAmount();
                } else if (debt.getOwesTo().equals(userDebtEntry.getKey())) {
                    totalAmount -= debt.getAmount();
                }
            }
            if (totalAmount * sign > 0) {
                usersBalance.add(new Pair<>(userDebtEntry.getKey(), Math.abs(totalAmount)));
            }
        }

        return usersBalance;
    }

    private List<Debt> getSimplifiedBalanceList(List<Pair<User, Float>> positiveBalanceUsers, List<Pair<User, Float>> negativeBalanceUsers) {
        List<Debt> minTransactions = null;
        for (int i = 0; i<positiveBalanceUsers.size();i++) {
            User userWithPosAmt = positiveBalanceUsers.get(i).getKey();
            Float posAmt = positiveBalanceUsers.get(i).getValue();
            if (posAmt == 0) {
                continue;
            }

            for ( int j=0;j<negativeBalanceUsers.size();j++) {
                User userWithNegAmt = negativeBalanceUsers.get(j).getKey();
                Float negAmt = negativeBalanceUsers.get(j).getValue();
                
                if (negAmt == 0) {
                    continue;
                }

                Float tempPosAmt = posAmt;
                Float tempNegAmt = negAmt;
                
                posAmt = Math.max(tempPosAmt - tempNegAmt,0f);
                negAmt = Math.max(tempNegAmt - tempPosAmt,0f);
                positiveBalanceUsers.get(i).setValue(posAmt);
                negativeBalanceUsers.get(j).setValue(negAmt);

                List<Debt> simplifiedBalanceList = getSimplifiedBalanceList(positiveBalanceUsers, negativeBalanceUsers);
                if (simplifiedBalanceList == null) {
                    simplifiedBalanceList = new ArrayList<>();
                }
                simplifiedBalanceList.add(new Debt(userWithNegAmt, userWithPosAmt, tempNegAmt));
                if (minTransactions == null || minTransactions.size() > simplifiedBalanceList.size()) {
                    minTransactions = simplifiedBalanceList;
                }

                positiveBalanceUsers.get(i).setValue(tempPosAmt);
                negativeBalanceUsers.get(j).setValue(tempNegAmt);
            }
        }

        return minTransactions;
    }

    private void simplifyDebts() {
        List<Pair<User, Float>> positiveBalanceUsers = getUsersBalanceBySign(1);
        List<Pair<User, Float>> negativeBalanceUsers = getUsersBalanceBySign(-1);

        System.out.println("Positive balance users:" + positiveBalanceUsers.size());
        positiveBalanceUsers.forEach(p -> {
            System.out.println(p.getKey().getName());
            System.out.println(p.getValue());
        });
        System.out.println("Negative balance users:" + negativeBalanceUsers.size());
        negativeBalanceUsers.forEach(p -> {
            System.out.println(p.getKey().getName());
            System.out.println(p.getValue());
        });


        List<Debt> simplifiedDebts = getSimplifiedBalanceList(positiveBalanceUsers, negativeBalanceUsers);
        System.out.println("simplifyDebts:" + simplifiedDebts.size());

        usersToDebtList = new HashMap<>();
        for (Debt debt : simplifiedDebts) {
            User owesTo = debt.getOwesTo();
            User owedTo = debt.getOwedTo();

            if (!usersToDebtList.containsKey(owesTo)) {
                usersToDebtList.put(owesTo, new ArrayList<>());
            }
            if (!usersToDebtList.containsKey(owedTo)) {
                usersToDebtList.put(owedTo, new ArrayList<>());
            }

            usersToDebtList.get(owesTo).add(debt);
            usersToDebtList.get(owedTo).add(debt);
        }
    }

    @Override
    public void createSplitBill(User paidByUser, List<User> users, SplitType splitType, Float totalAmount, List<Float> amountSplit) {
        
        if (!usersToDebtList.containsKey(paidByUser)) {
            usersToDebtList.put(paidByUser, new ArrayList<>());
        }

        for (User user: users) {
            if (!usersToDebtList.containsKey(user)) {
                usersToDebtList.put(user, new ArrayList<>());
            }
        }

        switch (splitType) {
            case SPLIT_TYPE_EQUAL:
                this.splitBillEqually(paidByUser, users, totalAmount);
                break;
            case SPLIT_TYPE_EXACT:
                this.splitBillBySpecifiedAmount(paidByUser, users, totalAmount, amountSplit);
                break;
        }

//        simplifyDebts();
//        getCompleteDebt();
        simplifyDebts();
        getCompleteDebt();
        return;
    }

    void updateDebtList(User paidByUser, User paidForUser, Float amount) {

        List<Debt> paidForUserDebtList = usersToDebtList.get(paidForUser);

        boolean debtFound = false;
        for (int i=0; i < paidForUserDebtList.size() && !debtFound; i++) {

            Debt debt = paidForUserDebtList.get(i);
            User owedTo = paidForUserDebtList.get(i).getOwedTo(); 
            User owesTo = paidForUserDebtList.get(i).getOwesTo();

            if (owedTo.equals(paidByUser)) {
                debt.setAmount(debt.getAmount() + amount);
                debtFound = true;
            } else if (owesTo.equals(paidByUser)) {
                debtFound = true;
                Float splitAmount = debt.getAmount() - amount;
                if (splitAmount < 0) {
                    debt.setOwedTo(owesTo);
                    debt.setOwesTo(owedTo);
                    debt.setAmount(-splitAmount);
                }    
            }
        }

        if (!debtFound) {
            Debt newDebt = new Debt(paidForUser, paidByUser, amount);
            usersToDebtList.get(paidByUser).add(newDebt);
            paidForUserDebtList.add(newDebt);
        }
    }

    void splitBillEqually(User paidByUser, List<User> users, Float amount) {
        Float equalSplitAmount = amount / (users.size() + 1);
        for (User user: users) {
            updateDebtList(paidByUser, user, equalSplitAmount);
        }
        
        return;
    }

    void splitBillBySpecifiedAmount(User paidByUser, List<User> users, Float amount, List<Float> amountSplit) {
        if (users.size() != amountSplit.size()) {
            System.out.println("Error: Size of the userId list doesn not match amount split list");
            return;
        }
        Float totalSum = Float.valueOf(0);
        for (Float val : amountSplit) {
            totalSum += val;
        }

        
        if (!totalSum.equals(amount)) {
            System.out.println("Error: Amount validation failed");
            return;
        }

        for (int i=0; i < users.size(); i++) {
            updateDebtList(paidByUser, users.get(i), amountSplit.get(i));
        }
    }

    @Override
    public void getCompleteDebt() {
        Set<Debt> debtSet = new HashSet<>();

        for (List<Debt> debtList: usersToDebtList.values()) {
            debtSet.addAll(new HashSet<>(debtList));
        }

        if (debtSet.isEmpty()) {
            System.out.println("No balances");
            return;
        }
        System.out.println("Total balances: " + debtSet.size());
        for (Debt debt: debtSet) {
            System.out.println(debt.getOwesTo().getName() + " owes to " + debt.getOwedTo().getName() + ": " + debt.getAmount());
        }
        System.out.println("**********************");
    }

    @Override
    public void getDebtByUserId(User user) {
        Set<Debt> debtSet = new HashSet<>();

        List<Debt> userDebtList = usersToDebtList.get(user);
        if (userDebtList == null || userDebtList.isEmpty()) {
            System.out.println("No balances");
            return;
        }

        debtSet.addAll(new HashSet<>(userDebtList));
        if (debtSet.isEmpty()) {
            System.out.println("No balances");
            return;
        }

        for (Debt debt: debtSet) {
            System.out.println(debt.getOwesTo().getName() + " owes to " + debt.getOwedTo().getName() + ": " + debt.getAmount());
        }
        System.out.println("**********************");

    }
    
}
