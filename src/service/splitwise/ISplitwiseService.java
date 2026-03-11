package service.splitwise;

import service.User.User;
import service.splitwise.request.SplitBillRequest;
import service.splitwise.response.SplitBillResponse;


public interface ISplitwiseService {
    SplitBillResponse splitBill(SplitBillRequest splitBillRequest);
    void getAllTransactions();
    void getTransactionsByUser(User user);
}




