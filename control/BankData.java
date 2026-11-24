package bank.control;

import bank.entity.Account;
import bank.entity.Customer;
import bank.entity.User;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Temporary in-memory store shared by the controllers.
 * In Week 4 the DAO classes (CustomerDAO, AccountDAO, ...) replace it with a real database.
 */
public class BankData {

    private static final BankData INSTANCE = new BankData();

    private final Map<String, Customer> customers = new LinkedHashMap<>();
    private final Map<String, User> users = new LinkedHashMap<>();
    private final Map<String, Account> accounts = new LinkedHashMap<>();

    private int customerSeq = 0;
    private int savingsSeq = 1000;
    private int investmentSeq = 2000;
    private int chequeSeq = 3000;

    private BankData() {
        // A staff user who can run the monthly interest payment
        users.put("admin", new User("admin", "admin123", "ADMIN"));
    }

    public static BankData getInstance() {
        return INSTANCE;
    }

    public Map<String, Customer> getCustomers() {
        return customers;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public String nextCustomerId() {
        customerSeq++;
        return String.format("C%03d", customerSeq);
    }

    /** @param type "savings", "investment" or "cheque" */
    public String nextAccountNumber(String type) {
        switch (type) {
            case "savings":
                return "SAV" + (++savingsSeq);
            case "investment":
                return "INV" + (++investmentSeq);
            case "cheque":
                return "CHQ" + (++chequeSeq);
            default:
                throw new IllegalArgumentException("Unknown account type: " + type);
        }
    }
}
