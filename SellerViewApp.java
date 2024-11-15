// 360 Group 40

// Seller View Prototype

package com.example.bookstore;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SellerViewApp extends Application {
    private TextField titleTxt;
    private TextField authorTxt;
    private TextField origPriceTxt;
    private TextField calcPriceTxt;
    private TextArea notesTxt;
    private ImageView bookImgView;
    private ChoiceBox<String> condChoice;
    private MenuButton catMenuBtn;
    private List<CheckBox> catCheckBoxes;

    @Override
    public void start(Stage primaryStage) {

        // Make the header bar
        HBox header = createHeader();

        // Main content area
        GridPane content = createContent();

        // Disclaimers at the bottom
        VBox disclaimers = createDisclaimers();

        // Put everything together
        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(content);
        root.setBottom(disclaimers);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Seller View");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: grey;");
        header.setPadding(new Insets(10));
        header.setSpacing(20);

        // View dropdown (Seller View)
        MenuButton viewMenu = new MenuButton("Seller View \u25BE"); // The down arrow
        viewMenu.setStyle("-fx-background-color: yellow;"); // Yellow background
        MenuItem buyerViewItem = new MenuItem("Buyer View");
        viewMenu.getItems().add(buyerViewItem);
        // Not adding actions right now

        // Title in the middle
        Label titleLabel = new Label("ASU SUN DEVIL BOOKSTORE");
        titleLabel.setTextFill(Color.MAROON);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Log Out button
        Button logoutBtn = new Button("Log Out");
        logoutBtn.setStyle("-fx-background-color: yellow;"); // Yellow background

        // Spacers to push title to center
        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        header.getChildren().addAll(viewMenu, leftSpacer, titleLabel, rightSpacer, logoutBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        return header;
    }

    private GridPane createContent() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(20);
        grid.setVgap(15);

        int row = 0;

        // Title field
        Label titleLbl = new Label("Title:");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titleTxt = new TextField();
        grid.add(titleLbl, 0, row);
        grid.add(titleTxt, 1, row);
        row++;

        // Author field
        Label authorLbl = new Label("Author:");
        authorLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        authorTxt = new TextField();
        grid.add(authorLbl, 0, row);
        grid.add(authorTxt, 1, row);
        row++;

        // Category selection
        Label catLbl = new Label("Category:");
        catLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        // Dropdown with checkboxes
        catMenuBtn = new MenuButton("Select Categories");
        catCheckBoxes = new ArrayList<>();
        String[] categories = {
                "Natural Science",
                "Computer",
                "Maths",
                "English Lang.",
                "Other"
        };
        for (String category : categories) {
            CheckBox checkBox = new CheckBox(category);
            catCheckBoxes.add(checkBox);
            CustomMenuItem customMenuItem = new CustomMenuItem(checkBox);
            customMenuItem.setHideOnClick(false); // Keep it open
            catMenuBtn.getItems().add(customMenuItem);

            // Update the button text when selection changes
            checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> updateCatMenuBtnText());
        }

        grid.add(catLbl, 0, row);
        grid.add(catMenuBtn, 1, row);
        row++;

        // Book Condition
        Label condLbl = new Label("Book Condition:");
        condLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        condChoice = new ChoiceBox<>();
        condChoice.getItems().addAll(
                "Used (Like New)",
                "Moderate",
                "Heavily Used"
        );
        condChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> calculatePrice());
        grid.add(condLbl, 0, row);
        grid.add(condChoice, 1, row);
        row++;

        // Price fields
        Label priceLbl = new Label("Price*:");
        priceLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        origPriceTxt = new TextField();
        origPriceTxt.setPromptText("Input Original price of book");
        origPriceTxt.textProperty().addListener((obs, oldVal, newVal) -> calculatePrice());

        calcPriceTxt = new TextField();
        calcPriceTxt.setPromptText("System Calculated Price");
        calcPriceTxt.setEditable(false);

        HBox priceBox = new HBox(10, origPriceTxt, calcPriceTxt);
        grid.add(priceLbl, 0, row);
        grid.add(priceBox, 1, row);
        row++;

        // Notes
        Label notesLbl = new Label("(Optional) Notes:");
        notesLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        notesTxt = new TextArea();
        notesTxt.setPrefRowCount(3);
        grid.add(notesLbl, 0, row);
        grid.add(notesTxt, 1, row);
        row++;

        // Right side with image and buttons
        VBox rightBox = new VBox(10);
        rightBox.setAlignment(Pos.TOP_CENTER);

        // Placeholder image
        bookImgView = new ImageView();
        bookImgView.setFitWidth(200);
        bookImgView.setFitHeight(250);
        bookImgView.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        rightBox.getChildren().add(bookImgView);

        // Add Photo button
        Button addPhotoBtn = new Button("Add Photo");
        addPhotoBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        addPhotoBtn.setOnAction(e -> addPhoto());
        rightBox.getChildren().add(addPhotoBtn);

        // LIST MY BOOK* button
        Button listBookBtn = new Button("LIST MY BOOK*");
        listBookBtn.setStyle("-fx-background-color: yellow; -fx-text-fill: maroon;");
        listBookBtn.setOnAction(e -> listMyBook());
        rightBox.getChildren().add(listBookBtn);

        // Add the rightBox to the grid
        grid.add(rightBox, 2, 0, 1, GridPane.REMAINING);

        return grid;
    }

    // Update the category button text
    private void updateCatMenuBtnText() {
        List<String> selectedCategories = new ArrayList<>();
        for (CheckBox checkBox : catCheckBoxes) {
            if (checkBox.isSelected()) {
                selectedCategories.add(checkBox.getText());
            }
        }

        if (selectedCategories.isEmpty()) {
            catMenuBtn.setText("Select Categories");
        } else {
            catMenuBtn.setText(String.join(", ", selectedCategories));
        }
    }

    private VBox createDisclaimers() {
        VBox disclaimers = new VBox(5);
        disclaimers.setPadding(new Insets(10));

        // Price disclaimer
        TextFlow priceDisclaimer = new TextFlow();
        Text priceAsterisk = new Text("*Price");
        priceAsterisk.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Text priceText = new Text(": To ensure our services run smoothly, we take a ");
        Text boldText = new Text("20% cut of your listed price");
        boldText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Text remainingText = new Text(". System calculated price accounts for additional taxes.");
        priceDisclaimer.getChildren().addAll(priceAsterisk, priceText, boldText, remainingText);

        // Approval disclaimer
        TextFlow approvalDisclaimer = new TextFlow();
        Text approvalAsterisk = new Text("*Subject to Approval");
        approvalAsterisk.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        approvalAsterisk.setFill(Color.MAROON);
        Text approvalText = new Text(": To ensure our book listing system is not abused, we have a manual review process.\nMost Books are approved within 2-4 Business Days. We request at most 7 Business Days for Approval.");
        approvalDisclaimer.getChildren().addAll(approvalAsterisk, approvalText);

        disclaimers.getChildren().addAll(priceDisclaimer, approvalDisclaimer);

        return disclaimers;
    }

    private void calculatePrice() {
        String condition = condChoice.getValue();
        String originalPriceStr = origPriceTxt.getText();

        if (condition != null && !originalPriceStr.isEmpty()) {
            try {
                double originalPrice = Double.parseDouble(originalPriceStr);
                double deductionPercent = 0.0;

                // Deduction based on condition
                switch (condition) {
                    case "Used (Like New)":
                        deductionPercent = 0.20;
                        break;
                    case "Moderate":
                        deductionPercent = 0.30;
                        break;
                    case "Heavily Used":
                        deductionPercent = 0.40;
                        break;
                }

                double priceAfterDeduction = originalPrice * (1 - deductionPercent);

                // Apply taxes (assumed at 8%)
                double taxRate = 0.08;
                double finalPrice = priceAfterDeduction * (1 + taxRate);

                calcPriceTxt.setText(String.format("$%.2f", finalPrice));

            } catch (NumberFormatException e) {
                calcPriceTxt.setText("Invalid Price");
            }
        } else {
            calcPriceTxt.setText("");
        }
    }

    private void addPhoto() {
        // Open a file chooser to select an image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Book Image");
        // Only show image files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image bookImage = new Image(selectedFile.toURI().toString(), 200, 250, true, true);
            bookImgView.setImage(bookImage);
        }
    }

    private void listMyBook() {
        // Collect selected categories
        List<String> selectedCategories = new ArrayList<>();
        for (CheckBox checkBox : catCheckBoxes) {
            if (checkBox.isSelected()) {
                selectedCategories.add(checkBox.getText());
            }
        }

        // Prepare the message to show
        String message = "Title: " + titleTxt.getText() +
                "\nAuthor: " + authorTxt.getText() +
                "\nCategories: " + String.join(", ", selectedCategories) +
                "\nCondition: " + condChoice.getValue() +
                "\nOriginal Price: " + origPriceTxt.getText() +
                "\nCalculated Price: " + calcPriceTxt.getText() +
                "\nNotes: " + notesTxt.getText() +
                "\nYour book has been submitted and is subject to approval.";

        // Show an alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Book Listed");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
