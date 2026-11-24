package bank.dao;

import bank.control.BankData;
import bank.entity.Customer;
import bank.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public void saveCustomer(Customer customer) {
        // Handled when saving the full customer + login record in saveUser(...)
    }

    public void saveUser(User user, String password) {
        BankData data = BankData.getInstance();
        Customer customer = data.getCustomers().get(user.getCustomerId());
        if (customer == null) {
            return;
        }

        String sql = """
            MERGE INTO customers (username, first_name, surname, address, phone, password)
            KEY (username) VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getSurname());
            stmt.setString(4, customer.getAddress());
            stmt.setString(5, customer.getPhone());
            stmt.setString(6, password);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save customer login: " + user.getUsername(), e);
        }
    }

    public Customer findById(String username) {
        String sql = """
            SELECT username, first_name, surname, address, phone
            FROM customers
            WHERE username = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getString("username"),
                            rs.getString("first_name"),
                            rs.getString("surname"),
                            rs.getString("address"),
                            rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find customer: " + username, e);
        }
        return null;
    }

    public User findUserByUsername(String username) {
        String sql = "SELECT username, password FROM customers WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String dbUsername = rs.getString("username");
                    String dbPassword = rs.getString("password");
                    return new User(dbUsername, dbPassword, "CUSTOMER", dbUsername);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load user: " + username, e);
        }
        return null;
    }

    public List<Customer> findAllCustomers() {
        String sql = "SELECT username, first_name, surname, address, phone FROM customers";
        List<Customer> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Customer(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("surname"),
                        rs.getString("address"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load customers", e);
        }
        return list;
    }

    public List<User> findAllUsers() {
        String sql = "SELECT username, password FROM customers";
        List<User> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String dbUsername = rs.getString("username");
                String dbPassword = rs.getString("password");
                list.add(new User(dbUsername, dbPassword, "CUSTOMER", dbUsername));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load users", e);
        }
        return list;
    }
}