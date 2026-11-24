package bank;

import bank.control.AccountController;
import bank.control.CustomerController;
import bank.control.InterestController;
import bank.control.LoginController;
import bank.control.TransactionController;
import bank.entity.Account;

import java.math.BigDecimal;

/**
 * Week 3 check of the controllers, without any screens.
 * Every line should say PASS.
 */
public class ControllerDemo {

    private static int failures = 0;

    public static void main(String[] args) {
        LoginController login = new LoginController();
        CustomerController customers = new CustomerController();
        AccountController accounts = new AccountController(login);
        TransactionController transactions = new TransactionController();
        InterestController interest = new InterestController();

        check("Admin can log in", login.login("admin", "admin123") && login.isAdmin());
        login.logout();
        check("Logged out", login.getCurrentUser() == null);
        check("Wrong password is refused", !login.login("admin", "nope"));
        check("Empty password is refused (no crash)", !login.login("admin", ""));

        customers.registerCustomer("Kabo", "Moyo", "Plot 123, Gaborone", "71234567", "kabo", "secret1");
        check("Registered customer can log in", login.login("kabo", "secret1") && !login.isAdmin());
        check("Display name is the customer's name", "Kabo Moyo".equals(login.getDisplayName()));

        check("Duplicate username rejected", fails(() ->
                customers.registerCustomer("A", "B", "Addr", "71234567", "kabo", "secret1")));
        check("Short password rejected", fails(() ->
                customers.registerCustomer("A", "B", "Addr", "71234567", "newuser", "123")));
        check("Bad phone number rejected", fails(() ->
                customers.registerCustomer("A", "B", "Addr", "abc", "newuser", "secret1")));

        Account savings = accounts.openAccount("Savings", "", null, null);
        Account invest = accounts.openAccount("Investment", "1000", null, null);
        Account cheque = accounts.openAccount("Cheque", "", "Acme Ltd", "Plot 9, Francistown");
        check("Customer holds 3 accounts", accounts.getAccounts().size() == 3);
        check("Account numbers are unique and typed", savings.getAccountNumber().startsWith("SAV")
                && invest.getAccountNumber().startsWith("INV") && cheque.getAccountNumber().startsWith("CHQ"));
        check("Investment below BWP500 rejected", fails(() -> accounts.openAccount("Investment", "100", null, null)));
        check("Cheque without employer rejected", fails(() -> accounts.openAccount("Cheque", "", "", "")));
        check("Only 2 accounts allow withdrawals", accounts.getWithdrawableAccounts().size() == 2);

        check("Deposit into Savings works", transactions.deposit(savings.getAccountNumber(), "1000"));
        check("Savings balance is 1000.00", "1000.00".equals(transactions.getBalance(savings.getAccountNumber())));
        check("Deposit of text is rejected", fails(() -> transactions.deposit(savings.getAccountNumber(), "abc")));
        check("Deposit of 0 is rejected", fails(() -> transactions.deposit(savings.getAccountNumber(), "0")));
        check("Deposit with 3 decimals is rejected", fails(() -> transactions.deposit(savings.getAccountNumber(), "1.234")));
        check("Savings withdrawal is refused", fails(() -> transactions.withdraw(savings.getAccountNumber(), "10")));
        check("Withdraw 200 from Investment works", transactions.withdraw(invest.getAccountNumber(), "200"));
        check("Withdraw too much returns false", !transactions.withdraw(invest.getAccountNumber(), "99999"));
        check("Investment balance is 800.00", "800.00".equals(transactions.getBalance(invest.getAccountNumber())));
        check("Unknown account is rejected", fails(() -> transactions.deposit("NOPE", "10")));

        login.logout();
        login.login("admin", "admin123");
        check("Interest paid to 2 accounts (Savings and Investment)", interest.payMonthlyInterest() == 2);
        check("Savings is now 1000.50", "1000.50".equals(transactions.getBalance(savings.getAccountNumber())));
        check("Investment is now 840.00", "840.00".equals(transactions.getBalance(invest.getAccountNumber())));
        check("Cheque earned nothing", "0.00".equals(transactions.getBalance(cheque.getAccountNumber())));
        check("Investment history has 3 records", transactions.getHistory(invest.getAccountNumber()).size() == 3);

        System.out.println();
        System.out.println(failures == 0 ? "All checks passed." : failures + " check(s) FAILED.");
    }

    private static boolean fails(Runnable action) {
        try {
            action.run();
            return false;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return true;
        }
    }

    private static void check(String description, boolean passed) {
        System.out.println((passed ? "PASS  " : "FAIL  ") + description);
        if (!passed) {
            failures++;
        }
    }
}
