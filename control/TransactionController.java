package bank.control;

import bank.entity.Account;
import bank.entity.Transaction;
import bank.entity.Withdrawable;

import java.math.BigDecimal;
import java.util.List;

/** Validates amounts, then deposits, withdraws and returns transaction history. */
public class TransactionController {

    private final BankData data = BankData.getInstance();

    public boolean deposit(String accountNumber, String amount) {
        return deposit(accountNumber, Amounts.parse(amount));
    }

    public boolean deposit(String accountNumber, BigDecimal amount) {
        Account account = findAccount(accountNumber);
        if (!validateAmount(amount)) {
            throw new IllegalArgumentException("Enter an amount more than zero, with at most 2 decimal places");
        }
        account.deposit(amount);
        return true;
    }

    public boolean withdraw(String accountNumber, String amount) {
        return withdraw(accountNumber, Amounts.parse(amount));
    }

    /** @return true if done, false if the balance is too low */
    public boolean withdraw(String accountNumber, BigDecimal amount) {
        Account account = findAccount(accountNumber);
        if (!(account instanceof Withdrawable)) {
            throw new IllegalArgumentException("This type of account does not allow withdrawals");
        }
        if (!validateAmount(amount)) {
            throw new IllegalArgumentException("Enter an amount more than zero, with at most 2 decimal places");
        }
        return ((Withdrawable) account).withdraw(amount);
    }

    public List<Transaction> getHistory(String accountNumber) {
        return findAccount(accountNumber).getTransactions();
    }

    /** @return the balance as text, e.g. "1000.00" */
    public String getBalance(String accountNumber) {
        return findAccount(accountNumber).getBalance().toPlainString();
    }

    private boolean validateAmount(BigDecimal amount) {
        return amount != null && amount.signum() > 0 && amount.scale() <= 2;
    }

    private Account findAccount(String accountNumber) {
        Account account = accountNumber == null ? null : data.getAccounts().get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Please choose an account");
        }
        return account;
    }
}
