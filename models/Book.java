package com.example.bookstore.models;

public class Book {
    private int id;
    private String title;
    private String author;
    private String category;
    private String condition;
    private double originalPrice;
    private double calculatedPrice;
    private String notes;
    private String imageUrl;
    private int sellerId;

    public Book() {
        // Default constructor
    }

    // Parameterized constructor
    public Book(int id, String title, String author, String category, String condition, double originalPrice,
                double calculatedPrice, String notes, String imageUrl, int sellerId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.condition = condition;
        this.originalPrice = originalPrice;
        this.calculatedPrice = calculatedPrice;
        this.notes = notes;
        this.imageUrl = imageUrl;
        this.sellerId = sellerId;
    }

    // Getters and Setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id= id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title= title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author= author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category= category;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition= condition;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice= originalPrice;
    }

    public double getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(double calculatedPrice) {
        this.calculatedPrice= calculatedPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes= notes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl= imageUrl;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId= sellerId;
    }
}
