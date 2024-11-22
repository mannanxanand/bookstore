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

public class LoginView extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: grey;");

        // Create header bar
        HBox header = createHeader();
        root.setTop(header);

        // Create the login form
        VBox loginBoxWrapper = createLoginForm(primaryStage);
        root.setCenter(loginBoxWrapper);

        // Create the scene and stage
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Login");
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

    private VBox createLoginForm(Stage primaryStage) {
        VBox loginBox = new VBox(20);
        loginBox.setPadding(new Insets(40));
        loginBox.setAlignment(Pos.CENTER_LEFT);
        loginBox.setStyle("-fx-background-color: white;");
        loginBox.setPrefHeight(450);

        // "Sign In" text
        Label signInLbl = new Label("Sign In");
        signInLbl.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        signInLbl.setTextFill(Color.MAROON);
        signInLbl.setAlignment(Pos.CENTER_LEFT);

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

        // Add fields to textFieldsBox
        textFieldsBox.getChildren().addAll(userLbl, userTxt, passLbl, passTxt);

        // Keep me signed in checkbox
        CheckBox keepSignedInChk = new CheckBox("Keep me signed in");
        keepSignedInChk.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Login button
        Button loginBtn = new Button("Login");
        loginBtn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        loginBtn.setStyle("-fx-background-color: yellow; -fx-text-fill: maroon;");
        loginBtn.setPrefSize(120, 40);

        loginBtn.setOnAction(event -> {
            String username = userTxt.getText();
            String password = passTxt.getText();
            if (isValidCredentials(username, password)) {
                try {
                    new SellerViewApp().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((Stage) loginBtn.getScene().getWindow()).close();
            } else {
                showAlert("Invalid Login", "The username or password is incorrect.");
            }
        });

        // Forgot Password link
        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        forgotPasswordLink.setOnAction(event -> showAlert("Forgot Password", "Please contact support to reset your password."));

        // Sign Up link
        Hyperlink signUpLink = new Hyperlink("Don't have an account? Sign Up");
        signUpLink.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        signUpLink.setOnAction(event -> {
            try {
                new SignUpView().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loginBox.getChildren().addAll(signInLbl, textFieldsBox, keepSignedInChk, loginBtn, forgotPasswordLink, signUpLink);
        VBox loginBoxWrapper = new VBox(loginBox);
        loginBoxWrapper.setAlignment(Pos.CENTER);
        loginBoxWrapper.setStyle("-fx-background-color: white;");
        VBox.setVgrow(loginBoxWrapper, Priority.ALWAYS);

        return loginBoxWrapper;
    }

    private boolean isValidCredentials(String username, String password) {
        User user = UserStorage.getUser(username);
        return user != null && user.getHashedPassword().equals(hashPassword(password));
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
