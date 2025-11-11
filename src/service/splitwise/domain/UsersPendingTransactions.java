package service.splitwise.domain;

import service.User.User;

import java.util.Map;

public class UsersPendingTransactions {
    private Map<User, Double> positivePendingTransactionUsers;
    private Map<User, Double> negativePendingTransactionUsers;

    public Map<User, Double> getPositivePendingTransactionUsers() {
        return positivePendingTransactionUsers;
    }

    public void setPositivePendingTransactionUsers(Map<User, Double> positivePendingTransactionUsers) {
        this.positivePendingTransactionUsers = positivePendingTransactionUsers;
    }

    public Map<User, Double> getNegativePendingTransactionUsers() {
        return negativePendingTransactionUsers;
    }

    public void setNegativePendingTransactionUsers(Map<User, Double> negativePendingTransactionUsers) {
        this.negativePendingTransactionUsers = negativePendingTransactionUsers;
    }
}
