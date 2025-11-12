import service.User.User;
import service.splitwise.ISplitwiseService;
import service.splitwise.SplitwiseService;
import service.splitwise.domain.ExactSplitDetails;
import service.splitwise.domain.PaidBy;
import service.splitwise.domain.PaidFor;
import service.splitwise.domain.SplitType;
import service.splitwise.request.SplitBillRequest;

import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {
    
        User user1 = new User("user1", "user1", "user1", "user1");
        User user2 = new User("user2", "user2", "user2", "user2");
        User user3 = new User("user3", "user3", "user3", "user3");
        User user4 = new User("user4", "user4", "user4", "user4");
        User user5 = new User("user5", "user5", "user5", "user5");
        ISplitwiseService splitwiseService = new SplitwiseService();

        // ********************************************************************************** //
        SplitBillRequest splitBillRequest1 = new SplitBillRequest();
        PaidBy paidBy1 = new PaidBy();
        Map<User, Double> usersAmount1 = new HashMap<>();
        usersAmount1.put(user1, 100.0);
        paidBy1.setUsers(usersAmount1);

        PaidFor paidFor1 = new PaidFor();
        Map<User, Double> userToExactAmt1 = new HashMap<>();
        userToExactAmt1.put(user4, 100.0);
        ExactSplitDetails exactSplitDetails1 = new ExactSplitDetails();
        exactSplitDetails1.setUserToAmount(userToExactAmt1);
        paidFor1.setExactSplitDetails(exactSplitDetails1);

        splitBillRequest1.setPaidBy(paidBy1);
        splitBillRequest1.setPaidFor(paidFor1);
        splitBillRequest1.setTotalAmount(100.0);
        splitBillRequest1.setSplitType(SplitType.SPLIT_TYPE_EXACT);

        splitwiseService.splitBill(splitBillRequest1);
        // ********************************************************************************** //
        SplitBillRequest splitBillRequest2 = new SplitBillRequest();
        PaidBy paidBy2 = new PaidBy();
        Map<User, Double> usersAmount2 = new HashMap<>();
        usersAmount2.put(user4, 100.0);
        paidBy2.setUsers(usersAmount2);

        PaidFor paidFor2 = new PaidFor();
        Map<User, Double> userToExactAmt2 = new HashMap<>();
        userToExactAmt2.put(user5, 100.0);
        ExactSplitDetails exactSplitDetails2 = new ExactSplitDetails();
        exactSplitDetails2.setUserToAmount(userToExactAmt2);
        paidFor2.setExactSplitDetails(exactSplitDetails2);

        splitBillRequest2.setPaidBy(paidBy2);
        splitBillRequest2.setPaidFor(paidFor2);
        splitBillRequest2.setTotalAmount(25.0);
        splitBillRequest2.setSplitType(SplitType.SPLIT_TYPE_EXACT);

        splitwiseService.splitBill(splitBillRequest2);
        // ********************************************************************************** //

        SplitBillRequest splitBillRequest3 = new SplitBillRequest();
        PaidBy paidBy3 = new PaidBy();
        Map<User, Double> usersAmount3 = new HashMap<>();
        usersAmount2.put(user5, 500.0);
        paidBy3.setUsers(usersAmount2);

        PaidFor paidFor3 = new PaidFor();
        Map<User, Double> userToExactAmt3 = new HashMap<>();
        userToExactAmt3.put(user3, 500.0);
        ExactSplitDetails exactSplitDetails3 = new ExactSplitDetails();
        exactSplitDetails3.setUserToAmount(userToExactAmt3);
        paidFor3.setExactSplitDetails(exactSplitDetails3);

        splitBillRequest3.setPaidBy(paidBy3);
        splitBillRequest3.setPaidFor(paidFor3);
        splitBillRequest3.setTotalAmount(500.0);
        splitBillRequest3.setSplitType(SplitType.SPLIT_TYPE_EXACT);

        splitwiseService.splitBill(splitBillRequest3);
        // ********************************************************************************** //
        SplitBillRequest splitBillRequest4 = new SplitBillRequest();
        PaidBy paidBy4 = new PaidBy();
        Map<User, Double> usersAmount4 = new HashMap<>();
        usersAmount4.put(user3, 400.0);
        paidBy4.setUsers(usersAmount4);

        PaidFor paidFor4 = new PaidFor();
        Map<User, Double> userToExactAmt4 = new HashMap<>();
        userToExactAmt4.put(user4, 400.0);
        ExactSplitDetails exactSplitDetails4 = new ExactSplitDetails();
        exactSplitDetails4.setUserToAmount(userToExactAmt4);
        paidFor4.setExactSplitDetails(exactSplitDetails4);

        splitBillRequest4.setPaidBy(paidBy4);
        splitBillRequest4.setPaidFor(paidFor4);
        splitBillRequest4.setTotalAmount(400.0);
        splitBillRequest4.setSplitType(SplitType.SPLIT_TYPE_EXACT);

        splitwiseService.splitBill(splitBillRequest4);
        // ********************************************************************************** //
        SplitBillRequest splitBillRequest5 = new SplitBillRequest();
        PaidBy paidBy5 = new PaidBy();
        Map<User, Double> usersAmount5 = new HashMap<>();
        usersAmount5.put(user4, 600.0);
        paidBy5.setUsers(usersAmount5);

        PaidFor paidFor5 = new PaidFor();
        Map<User, Double> userToExactAmt5 = new HashMap<>();
        userToExactAmt5.put(user2, 600.0);
        ExactSplitDetails exactSplitDetails5 = new ExactSplitDetails();
        exactSplitDetails5.setUserToAmount(userToExactAmt5);
        paidFor5.setExactSplitDetails(exactSplitDetails5);

        splitBillRequest5.setPaidBy(paidBy5);
        splitBillRequest5.setPaidFor(paidFor5);
        splitBillRequest5.setTotalAmount(600.0);
        splitBillRequest5.setSplitType(SplitType.SPLIT_TYPE_EXACT);

        splitwiseService.splitBill(splitBillRequest5);
        // ********************************************************************************** //
        SplitBillRequest splitBillRequest6 = new SplitBillRequest();
        PaidBy paidBy6 = new PaidBy();
        Map<User, Double> usersAmount6 = new HashMap<>();
        usersAmount6.put(user2, 300.0);
        paidBy6.setUsers(usersAmount6);

        PaidFor paidFor6 = new PaidFor();
        Map<User, Double> userToExactAmt6 = new HashMap<>();
        userToExactAmt6.put(user1, 300.0);
        ExactSplitDetails exactSplitDetails6 = new ExactSplitDetails();
        exactSplitDetails6.setUserToAmount(userToExactAmt6);
        paidFor6.setExactSplitDetails(exactSplitDetails6);

        splitBillRequest6.setPaidBy(paidBy6);
        splitBillRequest6.setPaidFor(paidFor6);
        splitBillRequest6.setTotalAmount(300.0);
        splitBillRequest6.setSplitType(SplitType.SPLIT_TYPE_EXACT);

        splitwiseService.splitBill(splitBillRequest6);
        // ********************************************************************************** //

        splitwiseService.getAllTransactions();
    }
}



// user1 paid 50 and user2 paid 50 on behalf of all 4 users
// print complete pending transactions


// user3 paid 25 for user1
// print complete pending transactions

