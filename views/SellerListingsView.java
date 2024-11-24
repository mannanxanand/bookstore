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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class SellerListingsView extends Application {
    private User currentUser;
    private GridPane bookGrid;

    public SellerListingsView(User user) {
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
        primaryStage.setTitle("Your Listings");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader(Stage primaryStage) {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #d3d3d3;");
        header.setPadding(new Insets(10));
        header.setSpacing(20);

        // View dropdown
        MenuButton viewMenu = new MenuButton("Seller View \u25BE");
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
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        // Title
        Label listingsLabel = new Label("Your Listings");
        listingsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Book grid
        bookGrid = new GridPane();
        bookGrid.setHgap(10);
        bookGrid.setVgap(10);

        // Fetch seller's books from database
        BookDAO bookDAO = new BookDAO();
        List<Book> books = bookDAO.getBooksBySellerId(currentUser.getId());

        // Display books in grid
        int col = 0;
        int row = 0;
        for (Book book : books) {
            VBox bookItem = createBookItem(book, primaryStage);
            bookGrid.add(bookItem, col, row);
            col++;
            if (col > 4) {
                col = 0;
                row++;
            }
        }

        // Go Back button
        Button goBackBtn = new Button("GO BACK");
        goBackBtn.setStyle("-fx-background-color: maroon; -fx-text-fill: white;");
        goBackBtn.setOnAction(e -> {
            try {
                new SellerMainView(currentUser).start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox goBackBox = new HBox(goBackBtn);
        goBackBox.setAlignment(Pos.BOTTOM_RIGHT);
        goBackBox.setPadding(new Insets(10));

        content.getChildren().addAll(listingsLabel, bookGrid, goBackBox);
        return content;
    }

    private VBox createBookItem(Book book, Stage primaryStage) {
        VBox bookBox = new VBox(5);
        bookBox.setPadding(new Insets(5));
        bookBox.setStyle("-fx-border-color: maroon; -fx-border-width: 1;");

        ImageView bookImageView = new ImageView();
        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            Image image = new Image(book.getImageUrl(), 100, 150, true, true);
            bookImageView.setImage(image);
        } else {
            // Placeholder image
            Image placeholderImage = new Image(getClass().getResource("/images/placeholder.png").toExternalForm(), 100, 150, true, true);
            bookImageView.setImage(placeholderImage);
        }

        Label titleLabel = new Label(book.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label priceLabel = new Label("Price: $" + String.format("%.2f", book.getCalculatedPrice()));
        priceLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        Label conditionLabel = new Label("[" + book.getCondition() + "]");
        conditionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        bookBox.getChildren().addAll(bookImageView, titleLabel, priceLabel, conditionLabel);

        // Add click handler to open SellerBookDetailView
        bookBox.setOnMouseClicked((MouseEvent e) -> {
            try {
                new SellerBookDetailView(currentUser, book).start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        return bookBox;
    }
}
