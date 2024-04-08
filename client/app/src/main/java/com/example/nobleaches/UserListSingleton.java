package com.example.nobleaches;

public class UserListSingleton {
    private static final UserList instance = new UserList();

    private UserListSingleton() {}

    public static UserList getInstance() {
        return instance;
    }
}
