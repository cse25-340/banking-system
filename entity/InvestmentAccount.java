package bank.entity;

import java.math.BigDecimal;

/** Earns 5% monthly interest and allows withdrawals. Needs an opening deposit of BWP500.00. */
public class InvestmentAccount extends Account implements InterestBearing, Withdrawable {

    private static final BigDecimal RATE = new BigDecimal("0.05");
    public static final BigDecimal MIN_OPENING_DEPOSIT = new BigDecimal("500.00");

    public InvestmentAccount(String accountNumber, String branch, Customer customer,
                             BigDecimal initialDeposit) {
        super(accountNumber, branch, customer);
        if (initialDeposit == null || initialDeposit.compareTo(MIN_OPENING_DEPOSIT) < 0) {
            throw new IllegalArgumentException(
                    "An Investment account needs an opening deposit of at least BWP" + MIN_OPENING_DEPOSIT);
        }
        credit(initialDeposit, TransactionType.DEPOSIT, "Opening deposit");
    }

    @Override
    public String getAccountType() {
        return "Investment";
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

    @Override
    public boolean withdraw(BigDecimal amount) {
        return debit(amount, "Withdrawal");
    }
}
