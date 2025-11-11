package service.splitwise.domain;

import service.User.User;

import java.util.Map;

public class PaidBy {
    private Map<User, Double> users;

    public Map<User, Double> getUsers() {
        return users;
    }

    public void setUsers(Map<User, Double> users) {
        this.users = users;
    }
}
