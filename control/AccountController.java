package bank.control;

import bank.dao.AccountDAO;
import bank.entity.Account;
import bank.entity.ChequeAccount;
import bank.entity.Customer;
import bank.entity.Employer;
import bank.entity.InvestmentAccount;
import bank.entity.SavingsAccount;
import bank.entity.Withdrawable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Opens accounts of the right type and lists a customer's accounts. */
public class AccountController {

    private static final String BRANCH = "Main Branch";

    private final BankData data = BankData.getInstance();
    private final AccountDAO accountDAO = new AccountDAO();
    private final LoginController loginController;

    public AccountController(LoginController loginController) {
        this.loginController = loginController;
    }

    /** Opens an account for the logged-in customer (version used by the screen, takes text). */
    public Account openAccount(String type, String initialDeposit, String employerName, String employerAddress) {
        Customer customer = loginController.getCurrentCustomer();
        if (customer == null) {
            throw new IllegalStateException("Only a logged-in customer can open an account");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Please choose an account type");
        }
        BigDecimal deposit = Amounts.parseOptional(initialDeposit);
        Employer employer = null;
        if (type.trim().equalsIgnoreCase("cheque")) {
            employer = new Employer(employerName, employerAddress);
        }
        return openAccount(customer, type, deposit, employer);
    }

    public Account openAccount(Customer customer, String type, BigDecimal initialDeposit, Employer employer) {
        String kind = type.trim().toLowerCase();
        String number = data.nextAccountNumber(kind);
        Account account;
        switch (kind) {
            case "savings":
                account = new SavingsAccount(number, BRANCH, customer);
                if (initialDeposit != null && initialDeposit.signum() > 0) {
                    account.deposit(initialDeposit, "Opening deposit");
                }
                break;
            case "investment":
                account = new InvestmentAccount(number, BRANCH, customer, initialDeposit);
                break;
            case "cheque":
                account = new ChequeAccount(number, BRANCH, customer, employer);
                if (initialDeposit != null && initialDeposit.signum() > 0) {
                    account.deposit(initialDeposit, "Opening deposit");
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown account type: " + type);
        }
        customer.addAccount(account);
        data.getAccounts().put(number, account);
        accountDAO.saveAccount(account, customer);
        return account;
    }

    public List<Account> getAccounts(Customer customer) {
        if (customer == null) {
            return new ArrayList<>();
        }
        if (customer.getAccounts().isEmpty()) {
            List<Account> loaded = accountDAO.findAccountsByCustomer(customer);
            for (Account acc : loaded) {
                customer.addAccount(acc);
                data.getAccounts().putIfAbsent(acc.getAccountNumber(), acc);
            }
        }
        return new ArrayList<>(customer.getAccounts());
    }

    /** @return the logged-in customer's accounts */
    public List<Account> getAccounts() {
        return getAccounts(loginController.getCurrentCustomer());
    }

    /** @return only the accounts that allow withdrawals (Investment and Cheque) */
    public List<Account> getWithdrawableAccounts() {
        List<Account> result = new ArrayList<>();
        for (Account account : getAccounts()) {
            if (account instanceof Withdrawable) {
                result.add(account);
            }
        }
        return result;
    }
}