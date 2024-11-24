package com.example.bookstore.views;

import com.example.bookstore.dao.BookDAO;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.User;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Optional;

public class SellerBookDetailView extends Application {
    private User currentUser;
    private Book selectedBook;

    public SellerBookDetailView(User user, Book book) {
        this.currentUser = user;
        this.selectedBook = book;
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
        GridPane content = createContent(primaryStage);
        root.setCenter(content);

        // Create the scene and stage
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Book Details");
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
            try {
                new SellerMainView(currentUser).start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
        Region middleSpacer = new Region();
        HBox.setHgrow(middleSpacer, Priority.ALWAYS);

        header.getChildren().addAll(viewMenu, leftSpacer, titleLabel, middleSpacer, logoutBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        return header;
    }

    private GridPane createContent(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(20);
        grid.setVgap(15);

        // Left side - Book Image
        ImageView bookImageView = new ImageView();
        if (selectedBook.getImageUrl() != null && !selectedBook.getImageUrl().isEmpty()) {
            Image image = new Image(selectedBook.getImageUrl(), 200, 300, true, true);
            bookImageView.setImage(image);
        } else {
            Image placeholderImage = new Image(getClass().getResource("/images/placeholder.png").toExternalForm(), 200, 300, true, true);
            bookImageView.setImage(placeholderImage);
        }
        grid.add(bookImageView, 0, 0, 1, GridPane.REMAINING);

        // Right side - Book Details
        VBox detailsBox = new VBox(10);
        detailsBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Title: " + selectedBook.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label authorLabel = new Label("Author: " + selectedBook.getAuthor());
        authorLabel.setFont(Font.font("Arial", 14));

        Label categoryLabel = new Label("Category: " + selectedBook.getCategory());
        categoryLabel.setFont(Font.font("Arial", 14));

        Label conditionLabel = new Label("Condition: " + selectedBook.getCondition());
        conditionLabel.setFont(Font.font("Arial", 14));

        Label priceLabel = new Label("Price: $" + selectedBook.getCalculatedPrice());
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label notesLabel = new Label("Notes:");
        notesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextArea notesArea = new TextArea(selectedBook.getNotes());
        notesArea.setWrapText(true);
        notesArea.setEditable(false);
        notesArea.setMaxWidth(300);
        notesArea.setPrefRowCount(4);

        // Delete Listing button
        Button deleteListingBtn = new Button("DELETE LISTING");
        deleteListingBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 16px;");
        deleteListingBtn.setOnAction(e -> {
            deleteListing(primaryStage);
        });

        // Go Back button
        Button goBackBtn = new Button("GO BACK");
        goBackBtn.setStyle("-fx-background-color: maroon; -fx-text-fill: white;");
        goBackBtn.setOnAction(e -> {
            try {
                new SellerListingsView(currentUser).start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox buttonBox = new HBox(10, deleteListingBtn, goBackBtn);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        detailsBox.getChildren().addAll(titleLabel, authorLabel, categoryLabel, conditionLabel, priceLabel,
                notesLabel, notesArea, buttonBox);

        grid.add(detailsBox, 1, 0);

        return grid;
    }

    private void deleteListing(Stage primaryStage) {
        // Confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Listing");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this listing?");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the book from the database
            BookDAO bookDAO = new BookDAO();
            bookDAO.deleteBook(selectedBook.getId());

            // Show confirmation
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Listing Deleted");
            infoAlert.setHeaderText(null);
            infoAlert.setContentText("The listing has been deleted.");
            infoAlert.showAndWait();

            // Go back to the listings view
            try {
                new SellerListingsView(currentUser).start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
