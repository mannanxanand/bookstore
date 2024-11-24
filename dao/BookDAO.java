package com.example.bookstore.dao;

import com.example.bookstore.models.Book;
import com.example.bookstore.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public void addBook(Book book) {
        String query = "INSERT INTO books (title, author, category, book_condition, original_price, calculated_price, notes, image_url, seller_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setString(4, book.getCondition()); // Corrected field name
            pstmt.setDouble(5, book.getOriginalPrice());
            pstmt.setDouble(6, book.getCalculatedPrice());
            pstmt.setString(7, book.getNotes());
            pstmt.setString(8, book.getImageUrl());
            pstmt.setInt(9, book.getSellerId());
            pstmt.executeUpdate();

            // Get the generated book ID
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                book.setId(rs.getInt(1));
            }

            // Debugging statement
            System.out.println("Book added: " + book.getTitle());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Book book = new Book();
                // Set book properties
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategory(rs.getString("category"));
                book.setCondition(rs.getString("book_condition")); // Corrected field name
                book.setOriginalPrice(rs.getDouble("original_price"));
                book.setCalculatedPrice(rs.getDouble("calculated_price"));
                book.setNotes(rs.getString("notes"));
                book.setImageUrl(rs.getString("image_url"));
                book.setSellerId(rs.getInt("seller_id"));

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Search books with filters
    public List<Book> searchBooks(String searchText, List<String> categories, List<String> conditions, double maxPrice) {
        List<Book> books = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM books WHERE 1=1");

        if (searchText != null && !searchText.isEmpty()) {
            query.append(" AND (title LIKE ? OR author LIKE ?)");
        }

        if (categories != null && !categories.isEmpty()) {
            query.append(" AND (");
            for (int i = 0; i < categories.size(); i++) {
                query.append("category LIKE ?");
                if (i < categories.size() - 1) {
                    query.append(" OR ");
                }
            }
            query.append(")");
        }

        if (conditions != null && !conditions.isEmpty()) {
            query.append(" AND book_condition IN (");
            for (int i = 0; i < conditions.size(); i++) {
                query.append("?");
                if (i < conditions.size() - 1) {
                    query.append(", ");
                }
            }
            query.append(")");
        }

        if (maxPrice > 0) {
            query.append(" AND calculated_price <= ?");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;

            if (searchText != null && !searchText.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchText + "%");
                pstmt.setString(paramIndex++, "%" + searchText + "%");
            }

            if (categories != null && !categories.isEmpty()) {
                for (String category : categories) {
                    pstmt.setString(paramIndex++, "%" + category + "%");
                }
            }

            if (conditions != null && !conditions.isEmpty()) {
                for (String condition : conditions) {
                    pstmt.setString(paramIndex++, condition);
                }
            }

            if (maxPrice > 0) {
                pstmt.setDouble(paramIndex++, maxPrice);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Book book = new Book();
                // Set book properties
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategory(rs.getString("category"));
                book.setCondition(rs.getString("book_condition")); // Corrected field name
                book.setOriginalPrice(rs.getDouble("original_price"));
                book.setCalculatedPrice(rs.getDouble("calculated_price"));
                book.setNotes(rs.getString("notes"));
                book.setImageUrl(rs.getString("image_url"));
                book.setSellerId(rs.getInt("seller_id"));

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Get a single book by ID
    public Book getBookById(int id) {
        String query = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Book book = new Book();
                // Set book properties
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategory(rs.getString("category"));
                book.setCondition(rs.getString("book_condition")); // Corrected field name
                book.setOriginalPrice(rs.getDouble("original_price"));
                book.setCalculatedPrice(rs.getDouble("calculated_price"));
                book.setNotes(rs.getString("notes"));
                book.setImageUrl(rs.getString("image_url"));
                book.setSellerId(rs.getInt("seller_id"));

                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update a book
    public void updateBook(Book book) {
        String query = "UPDATE books SET title = ?, author = ?, category = ?, book_condition = ?, original_price = ?, calculated_price = ?, notes = ?, image_url = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setString(4, book.getCondition()); // Corrected field name
            pstmt.setDouble(5, book.getOriginalPrice());
            pstmt.setDouble(6, book.getCalculatedPrice());
            pstmt.setString(7, book.getNotes());
            pstmt.setString(8, book.getImageUrl());
            pstmt.setInt(9, book.getId());
            pstmt.executeUpdate();

            // Debugging statement
            System.out.println("Book updated: " + book.getTitle());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a book
    public void deleteBook(int id) {
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            // Debugging statement
            System.out.println("Book deleted with ID: " + id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get books by seller ID
    public List<Book> getBooksBySellerId(int sellerId) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE seller_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, sellerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                // Set book properties
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategory(rs.getString("category"));
                book.setCondition(rs.getString("book_condition")); // Corrected field name
                book.setOriginalPrice(rs.getDouble("original_price"));
                book.setCalculatedPrice(rs.getDouble("calculated_price"));
                book.setNotes(rs.getString("notes"));
                book.setImageUrl(rs.getString("image_url"));
                book.setSellerId(rs.getInt("seller_id"));

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Additional methods can be added as needed
}
