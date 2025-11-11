package service.splitwise.domain;

import service.User.User;
import java.util.Map;

public class ExactSplitDetails {
    private Map<User, Double> userToAmount;

    public Map<User, Double> getUserToAmount() {
        return userToAmount;
    }

    public void setUserToAmount(Map<User, Double> userToAmount) {
        this.userToAmount = userToAmount;
    }
}
