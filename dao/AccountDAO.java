package bank.dao;

import bank.entity.Account;
import bank.entity.ChequeAccount;
import bank.entity.Customer;
import bank.entity.Employer;
import bank.entity.InvestmentAccount;
import bank.entity.SavingsAccount;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public void saveAccount(Account account, Customer customer) {
        String accountType = getAccountType(account);
        String employerInfo = null;

        if (account instanceof ChequeAccount cheque && cheque.getEmployer() != null) {
            employerInfo = cheque.getEmployer().toString();
        }

        String sql = """
            MERGE INTO accounts (
                account_number, customer_id, account_type, balance, employer_info
            ) KEY (account_number) VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account.getAccountNumber());
            stmt.setString(2, customer.getCustomerId());
            stmt.setString(3, accountType);
            stmt.setBigDecimal(4, account.getBalance());
            stmt.setString(5, employerInfo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save account: " + account.getAccountNumber(), e);
        }
    }

    public void updateBalance(String accountNumber, BigDecimal newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, newBalance);
            stmt.setString(2, accountNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update balance for: " + accountNumber, e);
        }
    }

    public List<Account> findAccountsByCustomer(Customer customer) {
        String sql = """
            SELECT account_number, account_type, balance, employer_info
            FROM accounts
            WHERE customer_id = ?
            """;
        List<Account> accounts = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getCustomerId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Account acc = rebuildAccount(
                            rs.getString("account_number"),
                            rs.getString("account_type"),
                            rs.getBigDecimal("balance"),
                            rs.getString("employer_info"),
                            customer
                    );
                    if (acc != null) {
                        accounts.add(acc);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load accounts for customer: " + customer.getCustomerId(), e);
        }
        return accounts;
    }

    private Account rebuildAccount(String number, String type, BigDecimal balance,
                                   String employerInfo, Customer customer) {
        String branch = "Main Branch";
        switch (type.toUpperCase()) {
            case "SAVINGS" -> {
                SavingsAccount sav = new SavingsAccount(number, branch, customer);
                if (balance != null && balance.signum() > 0) {
                    sav.deposit(balance, "Loaded from DB");
                }
                return sav;
            }
            case "INVESTMENT" -> {
                BigDecimal initial = balance != null ? balance : BigDecimal.ZERO;
                return new InvestmentAccount(number, branch, customer, initial);
            }
            case "CHEQUE" -> {
                Employer emp = (employerInfo != null && !employerInfo.isBlank())
                        ? new Employer(employerInfo, "")
                        : null;
                ChequeAccount chq = new ChequeAccount(number, branch, customer, emp);
                if (balance != null && balance.signum() > 0) {
                    chq.deposit(balance, "Loaded from DB");
                }
                return chq;
            }
            default -> {
                return null;
            }
        }
    }

    private String getAccountType(Account account) {
        if (account instanceof SavingsAccount) return "SAVINGS";
        if (account instanceof InvestmentAccount) return "INVESTMENT";
        if (account instanceof ChequeAccount) return "CHEQUE";
        return "UNKNOWN";
    }
}