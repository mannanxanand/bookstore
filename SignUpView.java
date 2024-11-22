package com.example.bookstore;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpView extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: grey;");

        // Create header bar
        HBox header = createHeader();
        root.setTop(header);

        // Create the sign-up form
        VBox signUpBoxWrapper = createSignUpForm(primaryStage);
        root.setCenter(signUpBoxWrapper);

        // Create the scene and stage
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Sign Up");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: grey;");
        header.setPadding(new Insets(10));
        header.setSpacing(20);

        // Role text
        Label roleLabel = new Label("Guest");
        roleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        roleLabel.setTextFill(Color.BLACK);
        roleLabel.setStyle("-fx-background-color: yellow;");
        roleLabel.setPrefSize(150, 40);
        roleLabel.setAlignment(Pos.CENTER);

        // Title in the middle
        Label titleLabel = new Label("ASU SUN DEVIL BOOKSTORE");
        titleLabel.setTextFill(Color.MAROON);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        // Adjust spacing for title
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        // Spacers to adjust title position
        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        header.getChildren().addAll(roleLabel, leftSpacer, titleLabel, rightSpacer);
        header.setAlignment(Pos.CENTER_LEFT);

        return header;
    }

    private VBox createSignUpForm(Stage primaryStage) {
        VBox signUpBox = new VBox(20);
        signUpBox.setPadding(new Insets(40));
        signUpBox.setAlignment(Pos.CENTER_LEFT);
        signUpBox.setStyle("-fx-background-color: white;");
        signUpBox.setPrefHeight(450);

        // "Sign Up" text
        Label signUpLbl = new Label("Sign Up");
        signUpLbl.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        signUpLbl.setTextFill(Color.MAROON);
        signUpLbl.setAlignment(Pos.CENTER_LEFT);

        VBox textFieldsBox = new VBox(20);
        textFieldsBox.setAlignment(Pos.CENTER_LEFT);

        // Username field
        Label userLbl = new Label("Username:");
        userLbl.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        TextField userTxt = new TextField();
        userTxt.setFont(Font.font("Arial", 18));
        userTxt.setMaxWidth(400);

        // Password field
        Label passLbl = new Label("Password:");
        passLbl.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        PasswordField passTxt = new PasswordField();
        passTxt.setFont(Font.font("Arial", 18));
        passTxt.setMaxWidth(400);

        // Re-enter Password field
        Label reEnterPassLbl = new Label("Re-enter Password:");
        reEnterPassLbl.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        PasswordField reEnterPassTxt = new PasswordField();
        reEnterPassTxt.setFont(Font.font("Arial", 18));
        reEnterPassTxt.setMaxWidth(400);

        // Add fields to textFieldsBox
        textFieldsBox.getChildren().addAll(userLbl, userTxt, passLbl, passTxt, reEnterPassLbl, reEnterPassTxt);

        // Checkbox for "Sign Up as Seller"
        CheckBox signUpAsSellerChk = new CheckBox("Sign Up as Seller");
        signUpAsSellerChk.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Sign Up button
        Button signUpBtn = new Button("Sign Up");
        signUpBtn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        signUpBtn.setStyle("-fx-background-color: yellow; -fx-text-fill: maroon;");
        signUpBtn.setPrefSize(120, 40);
        signUpBtn.setOnAction(event -> {
            String username = userTxt.getText();
            String password = passTxt.getText();
            String reEnterPassword = reEnterPassTxt.getText();
            if (password.equals(reEnterPassword) && !username.isEmpty() && !password.isEmpty()) {
                if (UserStorage.usernameExists(username)) {
                    showAlert("Error", "Username already exists.");
                } else {
                    String hashedPassword = hashPassword(password);
                    User newUser = new User(username, hashedPassword);
                    if (signUpAsSellerChk.isSelected()) {
                        newUser.setRole("Seller");
                    } else {
                        newUser.setRole("User");
                    }
                    UserStorage.addUser(newUser);
                    showAlert("Sign Up Successful", "Welcome, " + username + "!");
                    new LoginView().start(primaryStage); // Redirect back to login
                }
            } else if (!password.equals(reEnterPassword)) {
                showAlert("Error", "Passwords do not match.");
            } else {
                showAlert("Error", "All fields are required.");
            }
        });

        signUpBox.getChildren().addAll(signUpLbl, textFieldsBox, signUpAsSellerChk, signUpBtn);
        VBox signUpBoxWrapper = new VBox(signUpBox);
        signUpBoxWrapper.setAlignment(Pos.CENTER);
        signUpBoxWrapper.setStyle("-fx-background-color: white;");
        VBox.setVgrow(signUpBoxWrapper, Priority.ALWAYS);

        return signUpBoxWrapper;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
