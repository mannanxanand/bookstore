package com.example.bookstore.views;

import com.example.bookstore.models.User;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminView extends Application {
    private User currentUser;

    // Constructor to pass the current user
    public AdminView(User user) {
        this.currentUser = user;
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Header
        Label header = new Label("Admin Dashboard");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        BorderPane.setAlignment(header, Pos.CENTER);
        root.setTop(header);

        // Content
        Label welcomeLabel = new Label("Welcome, " + currentUser.getUsername() + "!");
        welcomeLabel.setFont(Font.font("Arial", 18));
        BorderPane.setAlignment(welcomeLabel, Pos.CENTER);
        root.setCenter(welcomeLabel);

        // Additional admin functionalities can be added here

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Admin View");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Entry point if you want to run AdminView independently
    public static void main(String[] args) {
        launch(args);
    }
}
