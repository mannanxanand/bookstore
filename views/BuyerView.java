package com.example.bookstore.views;

import com.example.bookstore.dao.BookDAO;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.Cart;
import com.example.bookstore.models.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.*;

public class BuyerView extends Application {
    private User currentUser;
    private Label cartCountLabel;
    private List<Book> allBooks; // To store all books fetched from the database
    private ObservableList<Book> displayedBooks; // Books currently displayed
    private GridPane bookGrid; // Grid to display books

    // For filtering
    private Set<String> selectedConditions = new HashSet<>();
    private String selectedPriceRange = "";
    private Set<String> selectedCategories = new HashSet<>();

    public BuyerView(User user) {
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
        Scene scene = new Scene(root, 900, 800);
        primaryStage.setTitle("Buyer View");
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
            // Already in Buyer View
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
            // Open Cart View
            try {
                new CartView(currentUser).start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        // Search bar and buttons
        HBox searchBarArea = new HBox(10);
        searchBarArea.setAlignment(Pos.CENTER_LEFT);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Enter book or author name");
        searchBar.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;");
        searchBar.setPrefWidth(400);

        // Search Button with Icon
        Button searchButton = new Button();
        ImageView searchIcon = new ImageView(new Image(getClass().getResource("/images/search_icon.png").toExternalForm()));
        searchIcon.setFitWidth(16);
        searchIcon.setFitHeight(16);
        searchButton.setGraphic(searchIcon);
        searchButton.setOnAction(e -> applyFilters(searchBar.getText()));

        // Filter Button with Icon
        Button filterButton = new Button(" Filter");
        ImageView filterIcon = new ImageView(new Image(getClass().getResource("/images/filter_icon.png").toExternalForm()));
        filterIcon.setFitWidth(16);
        filterIcon.setFitHeight(16);
        filterButton.setGraphic(filterIcon);
        filterButton.setOnAction(e -> showFilterOptions(filterButton));

        // Category Button with Icon
        Button categoryButton = new Button(" Category");
        ImageView categoryIcon = new ImageView(new Image(getClass().getResource("/images/category_icon.png").toExternalForm()));
        categoryIcon.setFitWidth(16);
        categoryIcon.setFitHeight(16);
        categoryButton.setGraphic(categoryIcon);
        categoryButton.setOnAction(e -> showCategoryOptions(categoryButton));

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
            try {
                new IndividualBookView(currentUser, book).start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        return bookBox;
    }

    private void showFilterOptions(Button filterButton) {
        // Create a context menu for filters
        ContextMenu filterMenu = new ContextMenu();

        // Condition filters
        Label conditionLabel = new Label("By Condition:");
        conditionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        CheckBox likeNewCheck = new CheckBox("Used (Like New)");

        CheckBox moderateCheck = new CheckBox("Moderate");

        CheckBox heavilyUsedCheck = new CheckBox("Heavily Used");

        likeNewCheck.setSelected(selectedConditions.contains("Used (Like New)"));
        moderateCheck.setSelected(selectedConditions.contains("Moderate"));
        heavilyUsedCheck.setSelected(selectedConditions.contains("Heavily Used"));

        // Price filters (Radio buttons)
        Label priceLabel = new Label("By Price:");
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        ToggleGroup priceGroup = new ToggleGroup();
        RadioButton under25 = new RadioButton("Under $25");
        RadioButton under50 = new RadioButton("Under $50");
        RadioButton under100 = new RadioButton("Under $100");

        under25.setToggleGroup(priceGroup);
        under50.setToggleGroup(priceGroup);
        under100.setToggleGroup(priceGroup);

        if (selectedPriceRange.equals("Under $25")) {
            under25.setSelected(true);
        } else if (selectedPriceRange.equals("Under $50")) {
            under50.setSelected(true);
        } else if (selectedPriceRange.equals("Under $100")) {
            under100.setSelected(true);
        }

        // Apply Filters button

        Button applyFiltersBtn = new Button("Apply Filters");

        applyFiltersBtn.setOnAction(e -> {

            // Update selected conditions
            selectedConditions.clear();
            if (likeNewCheck.isSelected()) selectedConditions.add("Used (Like New)");
            if (moderateCheck.isSelected()) selectedConditions.add("Moderate");
            if (heavilyUsedCheck.isSelected()) selectedConditions.add("Heavily Used");

            // Update selected price range
            if (under25.isSelected()) selectedPriceRange = "Under $25";
            else if (under50.isSelected()) selectedPriceRange = "Under $50";
            else if (under100.isSelected()) selectedPriceRange = "Under $100";
            else selectedPriceRange = "";

            // Apply filters
            applyFilters(null);

            // Close the menu
            filterMenu.hide();
        });
         VBox menuContent = new VBox(5,

                new Separator(),
                conditionLabel,
                likeNewCheck,
                moderateCheck,
                heavilyUsedCheck,
                new Separator(),
                priceLabel,
                under25,
                under50,
                under100,
                new Separator(),
                applyFiltersBtn

        );

        menuContent.setPadding(new Insets(10));
        CustomMenuItem customItem = new CustomMenuItem(menuContent);
        customItem.setHideOnClick(false);

        filterMenu.getItems().add(customItem);

        // Show the context menu below the Filter button
        filterMenu.show(filterButton, Side.BOTTOM, 0, 0);
    }

    private void showCategoryOptions(Button categoryButton) {
        // Create a context menu for categories
        ContextMenu categoryMenu = new ContextMenu();
        CheckBox naturalScienceCheck = new CheckBox("Natural Science");
        CheckBox computerCheck = new CheckBox("Computer");
        CheckBox mathsCheck = new CheckBox("Maths");
        CheckBox englishLangCheck = new CheckBox("English Lang.");
        CheckBox otherCheck = new CheckBox("Other");
        naturalScienceCheck.setSelected(selectedCategories.contains("Natural Science"));
        computerCheck.setSelected(selectedCategories.contains("Computer"));
        mathsCheck.setSelected(selectedCategories.contains("Maths"));
        englishLangCheck.setSelected(selectedCategories.contains("English Lang."));
        otherCheck.setSelected(selectedCategories.contains("Other"));

        // Apply Categories button

        Button applyCategoriesBtn = new Button("Apply Categories");

        applyCategoriesBtn.setOnAction(e -> {
            selectedCategories.clear();
            if (naturalScienceCheck.isSelected()) selectedCategories.add("Natural Science");
            if (computerCheck.isSelected()) selectedCategories.add("Computer");
            if (mathsCheck.isSelected()) selectedCategories.add("Maths");
            if (englishLangCheck.isSelected()) selectedCategories.add("English Lang.");
            if (otherCheck.isSelected()) selectedCategories.add("Other");

            // Apply filters
            applyFilters(null);
            // Close the menu
            categoryMenu.hide();
        });

        VBox menuContent = new VBox(5,

                naturalScienceCheck,
                computerCheck,
                mathsCheck,
                englishLangCheck,
                otherCheck,
                new Separator(),
                applyCategoriesBtn

        );

        menuContent.setPadding(new Insets(10));


        CustomMenuItem customItem = new CustomMenuItem(menuContent);
        customItem.setHideOnClick(false);
        categoryMenu.getItems().add(customItem);

        // Show the context menu below the Category button
        categoryMenu.show(categoryButton, Side.BOTTOM, 0, 0);
    }

    private void applyFilters(String searchText) {
        // Fetch latest books from database
        fetchBooksFromDatabase();
        displayedBooks.clear();

        for (Book book : allBooks) {
            boolean matches = true;

            // Search text filter
            if (searchText != null && !searchText.isEmpty()) {
                String lowerSearch = searchText.toLowerCase();
                if (!book.getTitle().toLowerCase().contains(lowerSearch) && !book.getAuthor().toLowerCase().contains(lowerSearch)) {
                    matches = false;
                }
            }

            // Condition filter
            if (!selectedConditions.isEmpty() && !selectedConditions.contains(book.getCondition())) {
                matches = false;
            }

            // Price filter
            if (!selectedPriceRange.isEmpty()) {
                double price = book.getCalculatedPrice();
                switch (selectedPriceRange) {
                    case "Under $25":
                        if (price >= 25) matches = false;
                        break;
                    case "Under $50":
                        if (price >= 50) matches = false;
                        break;
                    case "Under $100":
                        if (price >= 100) matches = false;
                        break;
                }
            }

            // Category filter
            if (!selectedCategories.isEmpty()) {
                boolean categoryMatch = false;
                for (String category : selectedCategories) {
                    if (book.getCategory().contains(category)) {
                        categoryMatch = true;
                        break;
                    }
                }
                if (!categoryMatch) matches = false;
            }

            if (matches) {
                displayedBooks.add(book);
            }
        }

        // Update the book grid
        updateBookGrid(new Stage());
    }
}
