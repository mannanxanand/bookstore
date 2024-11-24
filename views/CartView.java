package com.example.bookstore.views;

import com.example.bookstore.models.Book;
import com.example.bookstore.models.Cart;
import com.example.bookstore.models.User;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CartView extends Application {
    private User currentUser;
    private Label cartCountLabel;

    public CartView(User user) {
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
        primaryStage.setTitle("Cart");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader(Stage primaryStage) {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #d3d3d3;"); // Light grey color
        header.setPadding(new Insets(10));
        header.setSpacing(20);

        // View dropdown
        String currentView = "Buyer View";
        MenuButton viewMenu = new MenuButton(currentView + " \u25BE"); // Down arrow
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

        if (currentUser.getRole().equals("Seller")) {
            MenuItem sellerViewItem = new MenuItem("Seller View");
            sellerViewItem.setOnAction(e -> {
                try {
                    new SellerMainView(currentUser).start(primaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            viewMenu.getItems().add(sellerViewItem);
        }

        // Title in the middle
        Label titleLabel = new Label("ASU SUN DEVIL BOOKSTORE");
        titleLabel.setTextFill(Color.MAROON);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Cart icon with count
        StackPane cartIcon = new StackPane();
        Image cartImage = new Image(getClass().getResource("/images/cart_icon.png").toExternalForm());
        ImageView cartImageView = new ImageView(cartImage);
        cartImageView.setFitWidth(30);
        cartImageView.setFitHeight(30);

        cartCountLabel = new Label(String.valueOf(Cart.getInstance().getItemCount()));
        cartCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        cartCountLabel.setTextFill(Color.MAROON);
        cartCountLabel.setVisible(Cart.getInstance().getItemCount() > 0);

        cartIcon.getChildren().addAll(cartImageView, cartCountLabel);
        cartIcon.setOnMouseClicked(e -> {
            // Already in Cart View
        });

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
        Region middleSpacer = new Region();
        HBox.setHgrow(middleSpacer, Priority.ALWAYS);

        header.getChildren().addAll(viewMenu, leftSpacer, titleLabel, middleSpacer, cartIcon, logoutBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        return header;
    }

    private VBox createContent(Stage primaryStage) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Left side - Cart Items
        VBox cartItemsBox = new VBox(10);
        cartItemsBox.setPrefWidth(600);

        Label myBagLabel = new Label("My Bag (" + Cart.getInstance().getItemCount() + ")");
        myBagLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Separator separator = new Separator();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox itemsBox = new VBox(10);
        for (Book book : Cart.getInstance().getItems()) {
            HBox itemBox = createCartItem(book);
            itemsBox.getChildren().add(itemBox);
        }
        scrollPane.setContent(itemsBox);

        Button clearCartBtn = new Button("Clear Cart");
        clearCartBtn.setStyle("-fx-background-color: maroon; -fx-text-fill: white;");
        clearCartBtn.setOnAction(e -> {
            Cart.getInstance().clearCart();
            cartCountLabel.setText("0");
            cartCountLabel.setVisible(false);
            try {
                new CartView(currentUser).start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        cartItemsBox.getChildren().addAll(myBagLabel, separator, scrollPane, clearCartBtn);

        // Right side - Order Summary
        VBox orderSummaryBox = new VBox(10);
        orderSummaryBox.setAlignment(Pos.TOP_CENTER);
        orderSummaryBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        orderSummaryBox.setPadding(new Insets(10));

        Label orderSummaryLabel = new Label("Order Summary");
        orderSummaryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label subtotalLabel = new Label("Subtotal: $" + String.format("%.2f", Cart.getInstance().getTotalPrice()));
        subtotalLabel.setFont(Font.font("Arial", 14));

        Button checkoutBtn = new Button("CHECKOUT");
        checkoutBtn.setStyle("-fx-background-color: maroon; -fx-text-fill: white; -fx-font-size: 16px;");
        checkoutBtn.setOnAction(e -> {
            // Proceed to checkout
            processCheckout(primaryStage);
        });

        orderSummaryBox.getChildren().addAll(orderSummaryLabel, subtotalLabel, checkoutBtn);

        // Main content layout
        HBox mainContent = new HBox(20);
        mainContent.getChildren().addAll(cartItemsBox, orderSummaryBox);

        // Go Back button
        Button goBackBtn = new Button("GO BACK");
        goBackBtn.setStyle("-fx-background-color: maroon; -fx-text-fill: white;");
        goBackBtn.setOnAction(e -> {
            try {
                new BuyerView(currentUser).start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox goBackBox = new HBox(goBackBtn);
        goBackBox.setAlignment(Pos.BOTTOM_RIGHT);
        HBox.setHgrow(goBackBox, Priority.ALWAYS);

        content.getChildren().addAll(mainContent, goBackBox);

        return content;
    }

    private HBox createCartItem(Book book) {
        HBox itemBox = new HBox(10);
        itemBox.setPadding(new Insets(5));
        itemBox.setStyle("-fx-border-color: grey; -fx-border-width: 1;");

        ImageView bookImageView = new ImageView();
        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            Image image = new Image(book.getImageUrl(), 80, 120, true, true);
            bookImageView.setImage(image);
        } else {
            Image placeholderImage = new Image(getClass().getResource("/images/placeholder.png").toExternalForm(), 80, 120, true, true);
            bookImageView.setImage(placeholderImage);
        }

        VBox infoBox = new VBox(5);

        Label categoryLabel = new Label(book.getCategory());
        categoryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Label titleLabel = new Label(book.getTitle());
        titleLabel.setFont(Font.font("Arial", 14));

        Label authorLabel = new Label("Author: " + book.getAuthor());
        authorLabel.setFont(Font.font("Arial", 12));

        Label conditionLabel = new Label("Condition: " + book.getCondition());
        conditionLabel.setFont(Font.font("Arial", 12));

        infoBox.getChildren().addAll(categoryLabel, titleLabel, authorLabel, conditionLabel);

        Label priceLabel = new Label("$" + book.getCalculatedPrice());
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        priceLabel.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(priceLabel, Priority.ALWAYS);

        Button removeBtn = new Button("Remove");
        removeBtn.setOnAction(e -> {
            Cart.getInstance().removeBook(book);
            cartCountLabel.setText(String.valueOf(Cart.getInstance().getItemCount()));
            cartCountLabel.setVisible(Cart.getInstance().getItemCount() > 0);
            try {
                new CartView(currentUser).start(new Stage());
                ((Stage) removeBtn.getScene().getWindow()).close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        itemBox.getChildren().addAll(bookImageView, infoBox, priceLabel, removeBtn);

        return itemBox;
    }

    private void processCheckout(Stage primaryStage) {
        // Enhanced checkout dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Checkout");
        dialog.setHeaderText("Confirm Checkout");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        // Shipping Address
        Label addressLabel = new Label("Shipping Address:");
        addressLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextField streetField = new TextField();
        streetField.setPromptText("Street Address");

        TextField cityField = new TextField();
        cityField.setPromptText("City");

        TextField stateField = new TextField();
        stateField.setPromptText("State");

        TextField zipField = new TextField();
        zipField.setPromptText("ZIP Code");

        grid.add(addressLabel, 0, 0, 2, 1);
        grid.add(new Label("Street:"), 0, 1);
        grid.add(streetField, 1, 1);
        grid.add(new Label("City:"), 0, 2);
        grid.add(cityField, 1, 2);
        grid.add(new Label("State:"), 0, 3);
        grid.add(stateField, 1, 3);
        grid.add(new Label("ZIP Code:"), 0, 4);
        grid.add(zipField, 1, 4);

        // Payment Method
        Label paymentLabel = new Label("Payment Method:");
        paymentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        ToggleGroup paymentGroup = new ToggleGroup();
        RadioButton creditCardRadio = new RadioButton("Credit Card");
        RadioButton debitCardRadio = new RadioButton("Debit Card");
        RadioButton paypalRadio = new RadioButton("PayPal");

        creditCardRadio.setToggleGroup(paymentGroup);
        debitCardRadio.setToggleGroup(paymentGroup);
        paypalRadio.setToggleGroup(paymentGroup);

        grid.add(paymentLabel, 0, 5, 2, 1);
        grid.add(creditCardRadio, 0, 6);
        grid.add(debitCardRadio, 1, 6);
        grid.add(paypalRadio, 0, 7);

        // Card Details
        Label cardDetailsLabel = new Label("Card Details:");
        cardDetailsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cardDetailsLabel.setVisible(false);

        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Card Number");
        cardNumberField.setVisible(false);

        TextField expDateField = new TextField();
        expDateField.setPromptText("MM/YY");
        expDateField.setVisible(false);

        TextField cvvField = new TextField();
        cvvField.setPromptText("CVV");
        cvvField.setVisible(false);

        grid.add(cardDetailsLabel, 0, 8, 2, 1);
        grid.add(new Label("Card Number:"), 0, 9);
        grid.add(cardNumberField, 1, 9);
        grid.add(new Label("Exp Date:"), 0, 10);
        grid.add(expDateField, 1, 10);
        grid.add(new Label("CVV:"), 0, 11);
        grid.add(cvvField, 1, 11);

        // Show card details fields when Credit/Debit Card is selected
        paymentGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            boolean showCardDetails = newToggle == creditCardRadio || newToggle == debitCardRadio;
            cardDetailsLabel.setVisible(showCardDetails);
            cardNumberField.setVisible(showCardDetails);
            expDateField.setVisible(showCardDetails);
            cvvField.setVisible(showCardDetails);
        });

        // Order Summary
        Label orderSummaryLabel = new Label("Order Summary:");
        orderSummaryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label totalAmountLabel = new Label("Total Amount: $" + String.format("%.2f", Cart.getInstance().getTotalPrice()));
        totalAmountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        grid.add(orderSummaryLabel, 0, 12, 2, 1);
        grid.add(totalAmountLabel, 0, 13, 2, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                // Validate fields
                if (streetField.getText().isEmpty() || cityField.getText().isEmpty() ||
                        stateField.getText().isEmpty() || zipField.getText().isEmpty() ||
                        paymentGroup.getSelectedToggle() == null) {
                    showAlert("Error", "Please enter all the required information.");
                    return null;
                }

                if ((paymentGroup.getSelectedToggle() == creditCardRadio ||
                        paymentGroup.getSelectedToggle() == debitCardRadio) &&
                        (cardNumberField.getText().isEmpty() || expDateField.getText().isEmpty() || cvvField.getText().isEmpty())) {
                    showAlert("Error", "Please enter all the card details.");
                    return null;
                }

                // Process payment (simulate)
                showAlert("Payment Successful", "Your order has been placed successfully.");
                Cart.getInstance().clearCart();
                cartCountLabel.setText("0");
                cartCountLabel.setVisible(false);
                try {
                    new BuyerView(currentUser).start(primaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
