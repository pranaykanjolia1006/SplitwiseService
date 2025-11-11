package service.User;

public class User {
    
    private String username;
    private String emailId;
    private String phoneNumber;
    private String name;

    // TODO implement builder pattern for this
    public User(String username, String emailId, String phoneNumber, String name) {
        this.username = username;
        this.emailId = emailId;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmailId() {
        return emailId;
    }
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


}
