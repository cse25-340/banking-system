package bank;

import bank.entity.Account;
import bank.entity.ChequeAccount;
import bank.entity.Customer;
import bank.entity.Employer;
import bank.entity.InterestBearing;
import bank.entity.InvestmentAccount;
import bank.entity.SavingsAccount;
import bank.entity.Transaction;
import bank.entity.User;
import bank.entity.Withdrawable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Week 1 check of the core model. No GUI and no database yet.
 * Run it and make sure every line says PASS.
 */
public class CoreModelDemo {

    private static int failures = 0;

    public static void main(String[] args) {
        Customer kabo = new Customer("C001", "Kabo", "Moyo", "Plot 123, Gaborone", "71234567");

        // A customer can hold all three account types
        SavingsAccount savings = new SavingsAccount("S1001", "Main Branch", kabo);
        InvestmentAccount invest = new InvestmentAccount("I2001", "Main Branch", kabo, new BigDecimal("1000.00"));
        ChequeAccount cheque = new ChequeAccount("C3001", "Main Branch", kabo,
                new Employer("Acme Ltd", "Plot 9, Francistown"));
        kabo.addAccount(savings);
        kabo.addAccount(invest);
        kabo.addAccount(cheque);
        check("Customer holds 3 accounts", kabo.getAccounts().size() == 3);

        // Deposits (any account) and overloading
        savings.deposit(new BigDecimal("1000"));
        cheque.deposit(new BigDecimal("2500.50"), "Salary");
        check("Savings balance after deposit is 1000.00", savings.getBalance().compareTo(new BigDecimal("1000.00")) == 0);
        check("Cheque balance after salary is 2500.50", cheque.getBalance().compareTo(new BigDecimal("2500.50")) == 0);

        // Investment account rules
        boolean rejected = false;
        try {
            new InvestmentAccount("I2002", "Main Branch", kabo, new BigDecimal("499.99"));
        } catch (IllegalArgumentException e) {
            rejected = true;
        }
        check("Investment account rejects opening deposit below BWP500", rejected);

        // Cheque account needs an employer
        rejected = false;
        try {
            new ChequeAccount("C3002", "Main Branch", kabo, null);
        } catch (IllegalArgumentException e) {
            rejected = true;
        }
        check("Cheque account rejects missing employer", rejected);

        // Withdrawals
        check("Savings is not Withdrawable", !(savings instanceof Withdrawable));
        check("Withdraw 200 from Investment works", invest.withdraw(new BigDecimal("200")));
        check("Investment balance is 800.00", invest.getBalance().compareTo(new BigDecimal("800.00")) == 0);
        check("Withdraw more than balance returns false", !cheque.withdraw(new BigDecimal("999999")));
        check("Balance unchanged after failed withdrawal", cheque.getBalance().compareTo(new BigDecimal("2500.50")) == 0);

        // Invalid amounts
        rejected = false;
        try {
            savings.deposit(new BigDecimal("-5"));
        } catch (IllegalArgumentException e) {
            rejected = true;
        }
        check("Negative deposit is rejected", rejected);

        // Interest: polymorphism through the InterestBearing interface
        List<Account> all = new ArrayList<>(kabo.getAccounts());
        for (Account a : all) {
            System.out.println("   " + a + "  [" + a.getAccountType() + "]");
            if (a instanceof InterestBearing) {
                ((InterestBearing) a).payInterest();
            }
        }
        check("Savings interest 0.05% of 1000 = 0.50, balance 1000.50",
                savings.getBalance().compareTo(new BigDecimal("1000.50")) == 0);
        check("Investment interest 5% of 800 = 40, balance 840.00",
                invest.getBalance().compareTo(new BigDecimal("840.00")) == 0);
        check("Cheque account earns no interest", cheque.getBalance().compareTo(new BigDecimal("2500.50")) == 0);

        // Transaction history
        check("Investment history: opening deposit, withdrawal, interest (3)", invest.getTransactions().size() == 3);
        for (Transaction t : invest.getTransactions()) {
            System.out.println("   " + t);
        }

        // Login details
        User admin = new User("admin", "secret123", "ADMIN");
        check("Correct password accepted", admin.verifyPassword("secret123"));
        check("Wrong password rejected", !admin.verifyPassword("wrong"));
        check("Password is stored hashed", !admin.getPasswordHash().contains("secret123"));

        System.out.println();
        System.out.println(failures == 0 ? "All checks passed." : failures + " check(s) FAILED.");
    }

    private static void check(String description, boolean passed) {
        System.out.println((passed ? "PASS  " : "FAIL  ") + description);
        if (!passed) {
            failures++;
        }
    }
}
