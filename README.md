# ASU Sun Devil Bookstore- Group 40

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [Team Members](#team-members)
- [Project Structure](#project-structure)
- [Database](#database)
- [Payment Integration](#payment-integration)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Introduction

Welcome to the **ASU Sun Devil Bookstore** project! Developed as part of the CSE 360 course by Group 40, this JavaFX-based desktop application facilitates seamless interactions between buyers and sellers in a university bookstore setting. Whether you're looking to purchase textbooks or list your used books for sale, our application provides an intuitive and efficient platform to meet your needs.

## Features

### **For Buyers:**
- **Browse Books:** Explore a wide range of books across various categories.
- **Search & Filter:** Utilize multi-select filters for conditions, price ranges, and categories to find exactly what you're looking for.
- **Shopping Cart:** Add books to your cart, view cart contents, and proceed to checkout.
- **Checkout Process:** Secure payment integration with Stripe, including shipping address and detailed payment options.

### **For Sellers:**
- **List Books:** Easily list your books with details like title, author, category, condition, and price.
- **Manage Listings:** View and manage your listed books, edit details, or remove listings.
- **Notifications:** Receive confirmations upon successful listing submissions.

### **General:**
- **User Authentication:** Secure login system differentiating between buyers and sellers.
- **Responsive UI:** Clean and user-friendly interface designed with JavaFX.
- **Database Integration:** Robust SQL database handling all data operations efficiently.

## Technologies Used

- **JavaFX:** For building the graphical user interface.
- **Java:** The core programming language.
- **MySQL:** Database management system for storing user and book data.
- **Stripe API:** For handling secure payment transactions.
- **Maven:** Project management and build automation tool.

## Getting Started

### Prerequisites

Before you begin, ensure you have met the following requirements:

- **Java Development Kit (JDK) 22:** [Download Here](https://www.oracle.com/java/technologies/javase-jdk22-downloads.html)
- **Maven:** [Installation Guide](https://maven.apache.org/install.html)
- **MySQL Server:** [Download Here](https://dev.mysql.com/downloads/mysql/)
- **Git:** [Installation Guide](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
- **IntelliJ IDEA:** [Download Here](https://www.jetbrains.com/idea/download/) (Recommended)

### Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/yourusername/asu-sun-devil-bookstore.git
   cd asu-sun-devil-bookstore
   ```

2. **Set Up the Database:**

   - Open MySQL Workbench or your preferred MySQL client.
   - Run the provided SQL script to set up the `bookstore` database.

3. **Configure Database Connection:**

   - Update `DatabaseConnection.java` with your MySQL credentials:
     ```java
     private static final String URL = "jdbc:mysql://localhost:3306/bookstore";
     private static final String USER = "your_username";
     private static final String PASSWORD = "your_password";
     ```

4. **Build the Project:**

   Use Maven to build the project:
   ```bash
   mvn clean install
   ```

5. **Run the Application:**

   - Open the project in IntelliJ IDEA.
   - Run `MainApp.java` to start the application.

## Usage

### **Buyer View:**
- Browse books, use filters, add books to the cart, and proceed to checkout.

### **Seller View:**
- List new books and manage your existing listings.

### **Admin View:**
- Approve or reject book listings (admin functionality can be expanded).

## Team Members

- **Mannan Anand** - Scrum Master
- **Khoa Vo** - Project Manager
- **Vaibhav Bharadwaj** - Team Member
- **Hung Tran** - Team Member
- **Quang Tran** - Team Member

## Project Structure

```plaintext
src/
├── com.example.bookstore
│   ├── MainApp.java
│   ├── dao/
│   ├── models/
│   ├── utils/
│   ├── views/
│   │   ├── BuyerView.java
│   │   ├── CartView.java
│   │   ├── GuestView.java
│   │   ├── SellerViewApp.java
│   │   └── ...
├── resources/
│   ├── images/
│   └── styles/
```

## Database

### Schema
```sql
CREATE DATABASE bookstore;
USE bookstore;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('Buyer', 'Seller', 'Admin') NOT NULL DEFAULT 'Buyer'
);

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    category VARCHAR(255),
    book_condition VARCHAR(50),
    original_price DECIMAL(10, 2),
    calculated_price DECIMAL(10, 2),
    notes TEXT,
    image_url VARCHAR(255),
    seller_id INT,
    FOREIGN KEY (seller_id) REFERENCES users(id)
);
```

## Payment Integration

This project **intends to use** the Stripe API for payment processing. To enable payments:

1. **Create a Stripe Account:** [Stripe Signup](https://stripe.com)
2. **Obtain API Keys:** From the Stripe dashboard.
3. **Integrate with Application:** Update the `StripePayment.java` class with your API keys.

*Currently under development.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature-branch`).
3. Commit changes (`git commit -m "Add feature"`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a Pull Request.

## License

This project is licensed under the MIT License.

## Contact

For questions or feedback, reach out to **Mannan Anand** at [mannan.anand@asu.edu](mailto:mannan.anand@asu.edu).
