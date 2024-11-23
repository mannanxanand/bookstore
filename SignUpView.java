package com.example.bookstore.views;

import com.example.bookstore.dao.UserDAO;
import com.example.bookstore.models.User;
import com.example.bookstore.utils.PasswordUtils;
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

public class SignUpView extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // Create header bar
        HBox header = createHeader(primaryStage);
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

    private HBox createHeader(Stage primaryStage) {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #d3d3d3;");
        header.setPadding(new Insets(10));
        header.setSpacing(20);

        // View dropdown
        MenuButton viewMenu = new MenuButton("Guest View \u25BE");
        viewMenu.setStyle("-fx-background-color: yellow;");
        // No other views for guest

        // Title in the middle
        Label titleLabel = new Label("ASU SUN DEVIL BOOKSTORE");
        titleLabel.setTextFill(Color.MAROON);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Login button
        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: yellow;");
        loginBtn.setOnAction(e -> {
            try {
                new LoginView().start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Spacers to push title to center
        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        header.getChildren().addAll(viewMenu, leftSpacer, titleLabel, rightSpacer, loginBtn);
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
            String username = userTxt.getText().trim();
            String password = passTxt.getText();
            String reEnterPassword = reEnterPassTxt.getText();

            // Input validation
            if (username.isEmpty() || password.isEmpty() || reEnterPassword.isEmpty()) {
                showAlert("Error", "All fields are required.");
                return;
            }

            if (!password.equals(reEnterPassword)) {
                showAlert("Error", "Passwords do not match.");
                return;
            }

            UserDAO userDAO = new UserDAO();

            // Check if username already exists
            if (userDAO.usernameExists(username)) {
                showAlert("Error", "Username already exists.");
                return;
            }

            // Hash the password
            String hashedPassword = PasswordUtils.hashPassword(password);
            System.out.println("Hashed password during sign-up: " + hashedPassword);

            // Determine user role
            String role = signUpAsSellerChk.isSelected() ? "Seller" : "Buyer";

            // Create new user and add to database
            User newUser = new User(username, hashedPassword, role);
            userDAO.addUser(newUser);

            // Automatically log in the user and redirect to BuyerView
            showAlert("Sign Up Successful", "Welcome, " + username + "!");

            try {
                newUser = userDAO.getUser(username); // Get the user with ID
                new BuyerView(newUser).start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        signUpBox.getChildren().addAll(signUpLbl, textFieldsBox, signUpAsSellerChk, signUpBtn);
        VBox signUpBoxWrapper = new VBox(signUpBox);
        signUpBoxWrapper.setAlignment(Pos.CENTER);
        signUpBoxWrapper.setStyle("-fx-background-color: white;");
        VBox.setVgrow(signUpBoxWrapper, Priority.ALWAYS);

        return signUpBoxWrapper;
    }

    /**
     * Displays an alert dialog with the given title and message.
     *
     * @param title   The title of the alert.
     * @param message The content message of the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Main method to launch the Sign-Up view.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
