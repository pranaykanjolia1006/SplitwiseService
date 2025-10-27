import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SplitwiseService implements ISplitwiseService{

    private HashMap<User, List<Debt>> usersToDebtList;
    public SplitwiseService() {
        this.usersToDebtList = new HashMap<>();
    }

    // TODO CLEAN THIS
    class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key , V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }
        public void setKey(K key) {
            this.key = key;
        }
        public V getValue() {
            return value;
        }
        public void setValue(V value) {
            this.value = value;
        }
    }

    private List<Pair<User, Float>> getUsersBalanceBySign(int sign) {
        
        List<Pair<User, Float>> usersBalance = new ArrayList<>();

        for (Map.Entry<User, List<Debt>> userDebtEntry : usersToDebtList.entrySet()) {
            Float totalAmount = Float.valueOf(0.0f);
            for (Debt debt : userDebtEntry.getValue()) {
                if (sign > 0 && debt.getOwedTo().equals(userDebtEntry.getKey())) {
                    totalAmount += debt.getAmount();
                } else if (sign < 0 && debt.getOwesTo().equals(userDebtEntry.getKey())) {
                    totalAmount += debt.getAmount();
                }
            }
            if (totalAmount * sign > 0) {
                usersBalance.add(new Pair(userDebtEntry.getKey(), totalAmount));
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
                Float negAmt = negativeBalanceUsers.get(i).getValue();
                
                if (negAmt == 0 || posAmt < negAmt) {
                    continue;
                }

                Float tempPosAmt = Float.valueOf(posAmt);
                Float tempNegAmt = Float.valueOf(negAmt);
                
                posAmt = posAmt - negAmt;
                negAmt = 0f;

                List<Debt> simplifiedBalanceList = getSimplifiedBalanceList(positiveBalanceUsers, negativeBalanceUsers);
                if (minTransactions == null || minTransactions.size() > simplifiedBalanceList.size() + 1) {
                    minTransactions = simplifiedBalanceList;                    
                    minTransactions.add(new Debt(userWithNegAmt, userWithPosAmt, Float.valueOf(tempNegAmt)));
                }

                posAmt = tempPosAmt;
                negAmt = tempNegAmt;
            }
        }
        return minTransactions;
    }

    private void simplifyDebts() {
        List<Pair<User, Float>> positiveBalanceUsers = getUsersBalanceBySign(1);
        List<Pair<User, Float>> negativeBalanceUsers = getUsersBalanceBySign(-1);

        List<Debt> simplifiedDebts = getSimplifiedBalanceList(positiveBalanceUsers, negativeBalanceUsers);

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

        simplifyDebts();
        return;
    }

    void updateDebtList(User paidByUser, User paidForUser, Float amount) {

        List<Debt> paidForUserDebtList = usersToDebtList.get(paidForUser);

        boolean debtNotFound = false;
        for (int i=0; i < paidForUserDebtList.size() && !debtNotFound; i++) {

            Debt debt = paidForUserDebtList.get(i);
            User owedTo = paidForUserDebtList.get(i).getOwedTo(); 
            User owesTo = paidForUserDebtList.get(i).getOwesTo();

            if (owedTo.equals(paidByUser)) {
                debt.setAmount(debt.getAmount() + amount);
                debtNotFound = true;
            } else if (owesTo.equals(paidByUser)) {
                debtNotFound = true;
                Float splitAmount = debt.getAmount() - amount;
                if (splitAmount < 0) {
                    debt.setOwedTo(owesTo);
                    debt.setOwesTo(owedTo);
                    debt.setAmount(-splitAmount);
                }    
            }
        }

        if (!debtNotFound) {
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
            debtSet.addAll(debtList.stream().collect(Collectors.toSet()));
        }

        if (debtSet.size() == 0) {
            System.out.println("No balances");
            return;
        }
        for (Debt debt: debtSet) {
            System.out.println(debt.getOwesTo().getName() + " owes to " + debt.getOwedTo().getName() + ": " + debt.getAmount());
        }
    }

    @Override
    public void getDebtByUserId(User user) {
        Set<Debt> debtSet = new HashSet<>();

        List<Debt> userDebtList = usersToDebtList.get(user);
        if (userDebtList == null || userDebtList.size() == 0) {
            System.out.println("No balances");
            return;
        }

        debtSet.addAll(userDebtList.stream().collect(Collectors.toSet()));
        if (debtSet.size() == 0) {
            System.out.println("No balances");
            return;
        }

        for (Debt debt: debtSet) {
            System.out.println(debt.getOwesTo().getName() + " owes to " + debt.getOwedTo().getName() + ": " + debt.getAmount());
        }
    }
    
}
