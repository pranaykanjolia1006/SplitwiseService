package service.splitwise.domain;

import service.User.User;
import java.util.List;

public class PaidFor {
    private List<User> users;
    private ExactSplitDetails exactSplitDetails;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public ExactSplitDetails getExactSplitDetails() {
        return exactSplitDetails;
    }

    public void setExactSplitDetails(ExactSplitDetails exactSplitDetails) {
        this.exactSplitDetails = exactSplitDetails;
    }
}
