package service.splitwise.domain;

import service.User.User;

public class Transaction {

    private User fromUser; // gives the money
    private User toUser; // get the money
    private Double amount;

    public Transaction(User toUser, User fromUser, Double amount) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
