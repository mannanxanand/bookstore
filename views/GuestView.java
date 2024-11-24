package com.example.bookstore.views;

import com.example.bookstore.dao.BookDAO;
import com.example.bookstore.models.Book;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.*;

public class GuestView extends Application {
    private Label cartCountLabel;
    private List<Book> allBooks; // To store all books fetched from the database
    private ObservableList<Book> displayedBooks; // Books currently displayed
    private GridPane bookGrid; // Grid to display books

    // For filtering
    private Set<String> selectedConditions = new HashSet<>();
    private String selectedPriceRange = "";
    private Set<String> selectedCategories = new HashSet<>();

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
        Scene scene = new Scene(root, 900, 800);
        primaryStage.setTitle("Guest View");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader(Stage primaryStage) {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #d3d3d3;"); // Light grey color
        header.setPadding(new Insets(10));
        header.setSpacing(20);

        // View dropdown
        MenuButton viewMenu = new MenuButton("Guest View \u25BE"); // Down arrow
        viewMenu.setStyle("-fx-background-color: yellow;");
        // No other views available for guest

        // Title in the middle
        Label titleLabel = new Label("ASU SUN DEVIL BOOKSTORE");
        titleLabel.setTextFill(Color.MAROON);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Cart icon with count (hidden for guest)
        StackPane cartIcon = new StackPane();
        ImageView cartImageView = new ImageView();
        cartImageView.setFitWidth(30);
        cartImageView.setFitHeight(30);

        cartCountLabel = new Label("0");
        cartCountLabel.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 12;");
        cartCountLabel.setVisible(false);

        cartIcon.getChildren().addAll(cartImageView, cartCountLabel);
        cartIcon.setOnMouseClicked(e -> {
            // Prompt to login
            showLoginPrompt(primaryStage);
        });

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
        Region middleSpacer = new Region();
        HBox.setHgrow(middleSpacer, Priority.ALWAYS);

        header.getChildren().addAll(viewMenu, leftSpacer, titleLabel, middleSpacer, cartIcon, loginBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        return header;
    }

    private VBox createContent(Stage primaryStage) {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        // Search bar and buttons
        HBox searchBarArea = new HBox(10);
        searchBarArea.setAlignment(Pos.CENTER_LEFT);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Enter book or author name");
        searchBar.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;");
        searchBar.setPrefWidth(400);
        searchBar.setOnMouseClicked(e -> showLoginPrompt(primaryStage));

        // Search Button with Icon
        Button searchButton = new Button();
        ImageView searchIcon = new ImageView(new Image(getClass().getResource("/images/search_icon.png").toExternalForm()));
        searchIcon.setFitWidth(16);
        searchIcon.setFitHeight(16);
        searchButton.setGraphic(searchIcon);
        searchButton.setOnAction(e -> showLoginPrompt(primaryStage));

        // Filter Button with Icon
        Button filterButton = new Button(" Filter");
        ImageView filterIcon = new ImageView(new Image(getClass().getResource("/images/filter_icon.png").toExternalForm()));
        filterIcon.setFitWidth(16);
        filterIcon.setFitHeight(16);
        filterButton.setGraphic(filterIcon);
        filterButton.setOnAction(e -> showLoginPrompt(primaryStage));

        // Category Button with Icon
        Button categoryButton = new Button(" Category");
        ImageView categoryIcon = new ImageView(new Image(getClass().getResource("/images/category_icon.png").toExternalForm()));
        categoryIcon.setFitWidth(16);
        categoryIcon.setFitHeight(16);
        categoryButton.setGraphic(categoryIcon);
        categoryButton.setOnAction(e -> showLoginPrompt(primaryStage));

        searchBarArea.getChildren().addAll(searchBar, searchButton, filterButton, categoryButton);

        // Promotional banner
        Label promoLabel = new Label("Big Sale! Up to 50% off on selected books!");
        promoLabel.setStyle("-fx-background-color: yellow; -fx-text-fill: maroon; -fx-border-color: maroon; -fx-border-width: 2;");
        promoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32)); // Double the text size
        promoLabel.setAlignment(Pos.CENTER);
        promoLabel.setMaxWidth(Double.MAX_VALUE);
        promoLabel.setPadding(new Insets(20)); // Increase padding for size

        // Trending Books
        Label trendingLabel = new Label("Trending Books");
        trendingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Book grid
        bookGrid = new GridPane();
        bookGrid.setHgap(10);
        bookGrid.setVgap(10);

        // Fetch books from database
        fetchBooksFromDatabase();

        // Display books in grid
        updateBookGrid(primaryStage);

        content.getChildren().addAll(searchBarArea, promoLabel, trendingLabel, bookGrid);
        return content;
    }

    private void fetchBooksFromDatabase() {
        BookDAO bookDAO = new BookDAO();
        allBooks = bookDAO.getAllBooks();
        displayedBooks = FXCollections.observableArrayList();

        // Limit to 5 trending books
        List<Book> trendingBooks = allBooks.size() > 5 ? allBooks.subList(0, 5) : allBooks;
        displayedBooks.addAll(trendingBooks);
    }

    private void updateBookGrid(Stage primaryStage) {
        bookGrid.getChildren().clear();
        int col = 0;
        int row = 0;
        for (Book book : displayedBooks) {
            VBox bookItem = createBookItem(book, primaryStage);
            bookGrid.add(bookItem, col, row);
            col++;
            if (col > 4) {
                col = 0;
                row++;
            }
        }
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
        bookBox.setOnMouseClicked(e -> {
            showLoginPrompt(primaryStage);
        });

        return bookBox;
    }

    private void showLoginPrompt(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Required");
        alert.setHeaderText(null);
        alert.setContentText("Please login or sign up to access this feature.");
        alert.showAndWait();

        // Redirect to login
        try {
            new LoginView().start(primaryStage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
