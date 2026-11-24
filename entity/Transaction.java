package bank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private final String transactionId;
    private final TransactionType type;
    private final BigDecimal amount;
    private final LocalDateTime date;
    private final String description;

    /** Creates a new transaction stamped with the current time. */
    public Transaction(TransactionType type, BigDecimal amount, String description) {
        this(UUID.randomUUID().toString(), type, amount, LocalDateTime.now(), description);
    }

    /** Full constructor, used later when loading a saved transaction from the database. */
    public Transaction(String transactionId, TransactionType type, BigDecimal amount,
                       LocalDateTime date, String description) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return type + " BWP" + amount + " (" + description + ") at " + date;
    }
}
