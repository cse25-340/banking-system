package bank.boundary;

import bank.MainApp;
import bank.control.AccountController;
import bank.control.TransactionController;
import bank.entity.Account;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/** The screen for depositing money into one of the customer's accounts. */
public class DepositView {

    private final MainApp app;
    private final AccountController accountController;
    private final TransactionController controller;

    private final ComboBox<Account> accountBox = new ComboBox<>();
    private final TextField amountField = Ui.field("Amount in BWP");
    private final Label messageLabel = Ui.message();

    public DepositView(MainApp app, AccountController accountController, TransactionController controller) {
        this.app = app;
        this.accountController = accountController;
        this.controller = controller;
    }

    public void show() {
        loadAccounts();
        accountBox.setPromptText("Choose an account");
        VBox root = Ui.page(
                Ui.title("Deposit funds"),
                accountBox, amountField,
                Ui.button("Deposit", this::handleDeposit),
                messageLabel,
                Ui.button("Back to menu", app::showDashboard));
        app.setContent(root);
    }

    private void loadAccounts() {
        accountBox.getItems().setAll(accountController.getAccounts());
    }

    private void handleDeposit() {
        try {
            controller.deposit(getAccountNumber(), getAmount());
            String newBalance = controller.getBalance(getAccountNumber());
            amountField.clear();
            loadAccounts();
            showSuccess(newBalance);
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    public String getAccountNumber() {
        Account selected = accountBox.getValue();
        return selected == null ? null : selected.getAccountNumber();
    }

    public String getAmount() {
        return amountField.getText();
    }

    public void showSuccess(String newBalance) {
        Ui.showMessage(messageLabel, "Deposit successful. New balance: BWP" + newBalance, true);
    }

    public void showError(String msg) {
        Ui.showMessage(messageLabel, msg, false);
    }
}
