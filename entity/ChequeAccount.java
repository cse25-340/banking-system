package bank.entity;

import java.math.BigDecimal;

/** Used for salary payments. Allows deposits and withdrawals, earns no interest. Needs an employer. */
public class ChequeAccount extends Account implements Withdrawable {

    private final Employer employer;

    public ChequeAccount(String accountNumber, String branch, Customer customer, Employer employer) {
        super(accountNumber, branch, customer);
        if (employer == null) {
            throw new IllegalArgumentException("A Cheque account needs the employer's details");
        }
        this.employer = employer;
    }

    @Override
    public String getAccountType() {
        return "Cheque";
    }

    @Override
    public boolean withdraw(BigDecimal amount) {
        return debit(amount, "Withdrawal");
    }

    public Employer getEmployer() {
        return employer;
    }
}
