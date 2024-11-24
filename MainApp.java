package com.example.bookstore;

import com.example.bookstore.views.GuestView;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Start with the Guest View
        new GuestView().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
