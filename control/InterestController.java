package bank.control;

import bank.entity.Account;
import bank.entity.InterestBearing;

/** Pays monthly interest to every account that earns it. */
public class InterestController {

    private final BankData data = BankData.getInstance();

    /** @return how many accounts received interest */
    public int payMonthlyInterest() {
        int paid = 0;
        for (Account account : data.getAccounts().values()) {
            if (account instanceof InterestBearing) {
                InterestBearing earner = (InterestBearing) account;
                if (earner.calculateInterest().signum() > 0) {
                    earner.payInterest();
                    paid++;
                }
            }
        }
        return paid;
    }
}
