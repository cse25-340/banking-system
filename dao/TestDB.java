package bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDB {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("SUCCESS: Connected to H2 Database!");
                DatabaseSetup.createTables();

                String sql = """
                    MERGE INTO customers (username, first_name, surname, address, phone, password)
                    KEY (username) VALUES (?, ?, ?, ?, ?, ?)
                    """;

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    for (int i = 1; i <= 10; i++) {
                        ps.setString(1, "user" + i);
                        ps.setString(2, "First" + i);
                        ps.setString(3, "Surname" + i);
                        ps.setString(4, "Plot " + i + ", Gaborone");
                        ps.setString(5, "7100000" + i);
                        ps.setString(6, "pass" + i);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                try (Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery("SELECT * FROM customers")) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        System.out.println(
                                count + ". " +
                                        rs.getString("username") + " | " +
                                        rs.getString("first_name") + " | " +
                                        rs.getString("surname") + " | " +
                                        rs.getString("address") + " | " +
                                        rs.getString("phone")
                        );
                    }
                    System.out.println("Total records in customers table: " + count);
                }
            }
        } catch (SQLException e) {
            System.out.println("FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}