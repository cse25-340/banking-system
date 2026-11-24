package bank.dao;

import bank.entity.Account;
import bank.entity.Transaction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void saveTransaction(String accountNumber, String type, BigDecimal amount,
                                BigDecimal balanceAfter, String description) {
        String sql = """
            INSERT INTO transactions (
                account_number, transaction_type, amount, balance_after, description, created_at
            ) VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, type);
            stmt.setBigDecimal(3, amount);
            stmt.setBigDecimal(4, balanceAfter);
            stmt.setString(5, description);
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save transaction for account: " + accountNumber, e);
        }
    }

    public List<Transaction> findTransactionsByAccount(Account account) {
        String sql = """
            SELECT transaction_type, amount, balance_after, description, created_at
            FROM transactions
            WHERE account_number = ?
            ORDER BY id ASC
            """;
        List<Transaction> history = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account.getAccountNumber());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("transaction_type");
                    BigDecimal amount = rs.getBigDecimal("amount");
                    BigDecimal balanceAfter = rs.getBigDecimal("balance_after");
                    String description = rs.getString("description");
                    Timestamp ts = rs.getTimestamp("created_at");
                    LocalDateTime time = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();

                    Transaction tx = tryBuildTransaction(account, type, amount, balanceAfter, description, time);
                    if (tx != null) {
                        history.add(tx);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load transactions for: " + account.getAccountNumber(), e);
        }
        return history;
    }

    private Transaction tryBuildTransaction(Account account, String type, BigDecimal amount,
                                            BigDecimal balanceAfter, String description, LocalDateTime time) {
        return null;
    }
}