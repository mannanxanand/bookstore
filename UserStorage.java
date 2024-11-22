package com.example.bookstore;

import java.util.HashMap;
import java.util.Map;

public class UserStorage {
    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public static User getUser(String username) {
        return users.get(username);
    }

    public static boolean usernameExists(String username) {
        return users.containsKey(username);
    }
}
