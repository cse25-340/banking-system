package bank.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class Account {

    private final String accountNumber;
    private BigDecimal balance = BigDecimal.ZERO.setScale(2);
    private final String branch;
    private final LocalDate dateOpened;
    private final Customer customer;
    private final List<Transaction> transactions = new ArrayList<>();

    protected Account(String accountNumber, String branch, Customer customer) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required");
        }
        if (customer == null) {
            throw new IllegalArgumentException("An account must belong to a customer");
        }
        this.accountNumber = accountNumber.trim();
        this.branch = branch;
        this.customer = customer;
        this.dateOpened = LocalDate.now();
    }

    /** Deposits money. Overloaded: this version uses a default note. */
    public void deposit(BigDecimal amount) {
        deposit(amount, "Deposit");
    }

    /** Deposits money with a note describing it (overloaded version). */
    public void deposit(BigDecimal amount, String note) {
        validateAmount(amount);
        credit(amount, TransactionType.DEPOSIT, note);
    }

    /** Adds money to the balance and records it. Used by deposits and interest. */
    protected final void credit(BigDecimal amount, TransactionType type, String description) {
        BigDecimal rounded = money(amount);
        balance = balance.add(rounded);
        transactions.add(new Transaction(type, rounded, description));
    }

    /**
     * Takes money out of the balance and records it.
     * @return true if done, false if the balance is too low
     */
    protected final boolean debit(BigDecimal amount, String description) {
        validateAmount(amount);
        BigDecimal rounded = money(amount);
        if (rounded.compareTo(balance) > 0) {
            return false;
        }
        balance = balance.subtract(rounded);
        transactions.add(new Transaction(TransactionType.WITHDRAWAL, rounded, description));
        return true;
    }

    /** Each subclass returns its own type name, e.g. "Savings". */
    public abstract String getAccountType();

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBranch() {
        return branch;
    }

    public LocalDate getDateOpened() {
        return dateOpened;
    }

    public Customer getCustomer() {
        return customer;
    }

    /** @return a read-only view of this account's transaction history */
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    protected static BigDecimal money(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    protected static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be more than zero");
        }
    }

    @Override
    public String toString() {
        return getAccountType() + " " + accountNumber + " (BWP" + balance + ")";
    }
}
