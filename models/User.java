package com.example.bookstore.models;

public class User {
    private int id;
    private String username;
    private String hashedPassword;
    private String role;

    // Constructor without ID (for new users)
    public User(String username, String hashedPassword, String role) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    // Constructor with ID (for existing users)
    public User(int id, String username, String hashedPassword, String role) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    // Default constructor
    public User() {
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id= id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username= username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword= hashedPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role= role;
    }
}
