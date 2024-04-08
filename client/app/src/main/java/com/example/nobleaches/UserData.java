package com.example.nobleaches;

import java.util.List;

public class UserData {
    private String userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private List userHistory;

    // Constructor
    public UserData(String userId, String userName, String userPassword, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
    }

    public UserData(String Guest){
        this.userId = Guest;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List getUserHistory() {
        return userHistory;
    }

    @Override
    public String toString() {
        return "UserData { email: " + userEmail + ", password: " + userPassword + ", userId: " + userId + ", userName: " + userName + " }";
    }
}

