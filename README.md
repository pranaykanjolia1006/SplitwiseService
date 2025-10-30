Requirements:


1. User: Each user should have a userId, name, email, mobile number.
2. Split fuunctionality : EQUAL or EXACT
3. Users can make a post a bill and split b/w any available users.
4. Application shows who owes to whom and how much: user level and global level
5. Amount rounded off to 2 decimal places



User {
    name and other stuff
}

splitType class


createSplitBill(payUserId, totalAmount, splitType, userId list)
simplifyDebt(participatedUserIds)

Users on who owes to whom and amount 

Debt {

    OwedTo who gets money
    OwesTo who gives moeny
    amount


}

A owes 600 to B

algo:

a b c d

a 1000
bcd -> 250 to him
a,d

A => {B , -150} {C, +150}, {D, -1250}
D => 1000 for A , 2000 for B, 3000

C = {D , +700} {A, -150}
D = {A, +1250}  {C , -700}
B =  {A, +150}




A  -> B -> D -> C  -> A 

      x. ->  D  -> x








