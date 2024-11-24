package com.example.bookstore.views;

import com.example.bookstore.models.User;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class SellerMainView extends Application {
    private User currentUser;

    public SellerMainView(User user) {
        this.currentUser = user;
    }

    @Override
    public void start(Stage primaryStage) {
        // Create main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // Create header bar
        HBox header = createHeader(primaryStage);
        root.setTop(header);

        // Create content area
        VBox content = createContent(primaryStage);
        root.setCenter(content);

        // Create the scene and stage
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Seller Main View");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader(Stage primaryStage) {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #d3d3d3;"); // Light grey color
        header.setPadding(new Insets(10));
        header.setSpacing(20);

        // View dropdown
        MenuButton viewMenu = new MenuButton("Seller View \u25BE"); // Down arrow
        viewMenu.setStyle("-fx-background-color: yellow;");

        MenuItem buyerViewItem = new MenuItem("Buyer View");
        buyerViewItem.setOnAction(e -> {
            try {
                new BuyerView(currentUser).start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        viewMenu.getItems().add(buyerViewItem);

        MenuItem sellerViewItem = new MenuItem("Seller View");
        sellerViewItem.setOnAction(e -> {
            // Already in Seller View
        });
        viewMenu.getItems().add(sellerViewItem);

        // Title in the middle
        Label titleLabel = new Label("ASU SUN DEVIL BOOKSTORE");
        titleLabel.setTextFill(Color.MAROON);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Logout button
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: yellow;");
        logoutBtn.setOnAction(e -> {
            // Logout and return to Guest View
            try {
                new GuestView().start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Spacers to push title to center
        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        header.getChildren().addAll(viewMenu, leftSpacer, titleLabel, rightSpacer, logoutBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        return header;
    }

    private VBox createContent(Stage primaryStage) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(50));
        content.setAlignment(Pos.CENTER);

        Button viewListingsBtn = new Button("View Your Listings");
        viewListingsBtn.setPrefSize(200, 50);
        viewListingsBtn.setStyle("-fx-background-color: maroon; -fx-text-fill: white; -fx-font-size: 16px;");
        viewListingsBtn.setOnAction(e -> {
            try {
                new SellerListingsView(currentUser).start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button listNewBookBtn = new Button("List a New Book");
        listNewBookBtn.setPrefSize(200, 50);
        listNewBookBtn.setStyle("-fx-background-color: maroon; -fx-text-fill: white; -fx-font-size: 16px;");
        listNewBookBtn.setOnAction(e -> {
            try {
                new SellerViewApp(currentUser).start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        content.getChildren().addAll(viewListingsBtn, listNewBookBtn);
        return content;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
