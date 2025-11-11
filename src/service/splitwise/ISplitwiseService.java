import java.util.List;

public interface ISplitwiseService {
    void createSplitBill(User paidByUser, List<User> users, SplitType splitType, Float totalAmount, List<Float> amountSplit);
    void getCompleteDebt();
    void getDebtByUserId(User user);
}
