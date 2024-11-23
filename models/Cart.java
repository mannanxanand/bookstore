package com.example.bookstore.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance = null;
    private List<Book> items;

    private Cart() {
        items = new ArrayList<>();
    }

    public static Cart getInstance() {
        if (instance == null)
            instance = new Cart();
        return instance;
    }

    public void addBook(Book book) {
        items.add(book);
    }

    public void removeBook(Book book) {
        items.remove(book);
    }

    public void clearCart() {
        items.clear();
    }

    public List<Book> getItems() {
        return items;
    }

    public double getTotalPrice() {
        double total = 0;
        for (Book book : items) {
            total += book.getCalculatedPrice();
        }
        return total;
    }

    public int getItemCount() {
        return items.size();
    }
}
