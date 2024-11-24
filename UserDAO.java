package com.example.bookstore.dao;

import com.example.bookstore.models.User;
import com.example.bookstore.utils.DatabaseConnection;

import java.sql.*;

public class UserDAO {

    /**
     * Adds a new user to the database.
     *
     * @param user The user to add.
     */
    public void addUser(User user) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getHashedPassword()); // Store hashed password
            pstmt.setString(3, user.getRole());
            pstmt.executeUpdate();

            // Retrieve the generated user ID
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }

            // Debugging statements
            System.out.println("User added: " + user.getUsername());
            System.out.println("Hashed password stored: " + user.getHashedPassword());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a user from the database by username.
     *
     * @param username The username to search for.
     * @return The User object if found; otherwise, null.
     */
    public User getUser(String username) {
        String query = "SELECT id, username, password, role FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );

                // Debugging statements
                System.out.println("User retrieved: " + user.getUsername());
                System.out.println("Hashed password retrieved: " + user.getHashedPassword());

                return user;
            } else {
                // Debugging statement
                System.out.println("User not found: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if a username already exists in the database.
     *
     * @param username The username to check.
     * @return True if the username exists; otherwise, false.
     */
    public boolean usernameExists(String username) {
        String query = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();

            // Debugging statement
            System.out.println("Username exists check: " + username + " - " + exists);

            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}