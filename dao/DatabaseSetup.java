package bank.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    public static void createTables() {
        String customersTable = """
            CREATE TABLE IF NOT EXISTS customers (
                username VARCHAR(50) PRIMARY KEY,
                first_name VARCHAR(100) NOT NULL,
                surname VARCHAR(100) NOT NULL,
                address VARCHAR(255) NOT NULL,
                phone VARCHAR(30) NOT NULL,
                password VARCHAR(100) NOT NULL
            );
            """;

        String accountsTable = """
            CREATE TABLE IF NOT EXISTS accounts (
                account_number VARCHAR(50) PRIMARY KEY,
                customer_username VARCHAR(50) NOT NULL,
                account_type VARCHAR(30) NOT NULL,
                balance DECIMAL(15, 2) NOT NULL,
                employer_name VARCHAR(150),
                employer_address VARCHAR(255),
                FOREIGN KEY (customer_username) REFERENCES customers(username)
                    ON DELETE CASCADE
            );
            """;

        String transactionsTable = """
            CREATE TABLE IF NOT EXISTS transactions (
                transaction_id INT AUTO_INCREMENT PRIMARY KEY,
                account_number VARCHAR(50) NOT NULL,
                transaction_type VARCHAR(30) NOT NULL,
                amount DECIMAL(15, 2) NOT NULL,
                balance_after DECIMAL(15, 2) NOT NULL,
                timestamp VARCHAR(50) NOT NULL,
                FOREIGN KEY (account_number) REFERENCES accounts(account_number)
                    ON DELETE CASCADE
            );
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(customersTable);
            stmt.execute(accountsTable);
            stmt.execute(transactionsTable);
            System.out.println("Database tables ready!");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating tables", e);
        }
    }
}