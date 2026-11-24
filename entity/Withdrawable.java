package bank.entity;

import java.math.BigDecimal;

/** Implemented by accounts that allow withdrawals (Investment and Cheque). */
public interface Withdrawable {

    /**
     * @param amount the amount to take out (must be more than zero)
     * @return true if the withdrawal was made, false if the balance was too low
     */
    boolean withdraw(BigDecimal amount);
}
