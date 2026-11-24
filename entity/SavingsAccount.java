package bank.entity;

import java.math.BigDecimal;

/** Earns 0.05% monthly interest. Does not allow withdrawals (it is not Withdrawable). */
public class SavingsAccount extends Account implements InterestBearing {

    private static final BigDecimal RATE = new BigDecimal("0.0005");

    public SavingsAccount(String accountNumber, String branch, Customer customer) {
        super(accountNumber, branch, customer);
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

    @Override
    public BigDecimal getInterestRate() {
        return RATE;
    }

    @Override
    public BigDecimal calculateInterest() {
        return money(getBalance().multiply(RATE));
    }

    @Override
    public void payInterest() {
        BigDecimal interest = calculateInterest();
        if (interest.signum() > 0) {
            credit(interest, TransactionType.INTEREST, "Monthly interest");
        }
    }
}
