import java.util.Arrays;

public class App {
    public static void main(String[] args) throws Exception {
    
        User user1 = new User("user1", "user1", "user1", "user1");
        User user2 = new User("user2", "user2", "user2", "user2");
        User user3 = new User("user3", "user3", "user3", "user3");
        User user4 = new User("user4", "user4", "user4", "user4");

        
        ISplitwiseService splitwiseService = new SplitwiseService();
        
        splitwiseService.createSplitBill(user2, Arrays.asList(user1) , SplitType.SPLIT_TYPE_EQUAL, 200.0f, null);
        splitwiseService.createSplitBill(user4, Arrays.asList(user3) , SplitType.SPLIT_TYPE_EQUAL, 400.0f, null);
        splitwiseService.createSplitBill(user2, Arrays.asList(user4) , SplitType.SPLIT_TYPE_EQUAL, 200.0f, null);
        splitwiseService.getCompleteDebt();
        // splitwiseService.createSplitBill(user3, Arrays.asList(user2) , SplitType.SPLIT_TYPE_EQUAL, 1000.0f, null);
        // splitwiseService.getCompleteDebt();


        // splitwiseService.createSplitBill(user4, Arrays.asList(user2) , SplitType.SPLIT_TYPE_EQUAL, 200.0f, null);
        // splitwiseService.GetDebtByUserId(user4);
        // splitwiseService.GetDebtByUserId(user1);
        // splitwiseService.CreateSplitBill(user1, Arrays.asList(user2, user3) , SplitType.SPLIT_TYPE_EXACT, 1250.0f, Arrays.asList(370.0f, 880.0f));
        // splitwiseService.GetDebtByUserId(user1);
    }
}
