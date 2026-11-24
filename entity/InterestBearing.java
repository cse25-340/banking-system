package bank.entity;

import java.math.BigDecimal;

/** Implemented by accounts that earn interest (Savings and Investment). */
public interface InterestBearing {

    /** @return the monthly interest rate as a fraction, e.g. 0.05 for 5% */
    BigDecimal getInterestRate();

    /** @return the interest earned this month on the current balance */
    BigDecimal calculateInterest();

    /** Adds this month's interest to the balance and records it as a transaction. */
    void payInterest();
}
