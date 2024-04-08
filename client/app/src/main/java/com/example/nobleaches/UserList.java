package com.example.nobleaches;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    private final List<UserData> userList;

    public UserList() {
        userList = new ArrayList<>();
        userList.add(new UserData("1006019","Mithun", "Password123", "Mithun@mymail.sutd.edu.sg"));
        userList.add(new UserData("1006020","Roshan", "Password123", "Roshan@mymail.sutd.edu.sg"));
        userList.add(new UserData("1006021","Raphael", "Password123","Raphael@mymail.sutd.edu.sg"));
        userList.add(new UserData("1006022","Saif", "Password123", "Saif@mymail.sutd.edu.sg"));

    }

    public List<UserData> getUserList() {
        return userList;
    }

    public void addUser(UserData user) {
        userList.add(user);
    }
}
