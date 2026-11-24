package bank.boundary;

import bank.MainApp;
import bank.control.AccountController;
import bank.control.TransactionController;
import bank.entity.Account;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/** The screen for withdrawing money. Only accounts that allow withdrawals are listed. */
public class WithdrawView {

    private final MainApp app;
    private final AccountController accountController;
    private final TransactionController controller;

    private final ComboBox<Account> accountBox = new ComboBox<>();
    private final TextField amountField = Ui.field("Amount in BWP");
    private final Label messageLabel = Ui.message();

    public WithdrawView(MainApp app, AccountController accountController, TransactionController controller) {
        this.app = app;
        this.accountController = accountController;
        this.controller = controller;
    }

    public void show() {
        loadAccounts();
        accountBox.setPromptText("Choose an account");
        VBox root = Ui.page(
                Ui.title("Withdraw funds"),
                new Label("Savings accounts do not allow withdrawals."),
                accountBox, amountField,
                Ui.button("Withdraw", this::handleWithdraw),
                messageLabel,
                Ui.button("Back to menu", app::showDashboard));
        app.setContent(root);
    }

    private void loadAccounts() {
        accountBox.getItems().setAll(accountController.getWithdrawableAccounts());
    }

    private void handleWithdraw() {
        try {
            if (controller.withdraw(getAccountNumber(), getAmount())) {
                String newBalance = controller.getBalance(getAccountNumber());
                amountField.clear();
                loadAccounts();
                showSuccess(newBalance);
            } else {
                showError("Insufficient funds. The withdrawal was not made.");
            }
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
        Ui.showMessage(messageLabel, "Withdrawal successful. New balance: BWP" + newBalance, true);
    }

    public void showError(String msg) {
        Ui.showMessage(messageLabel, msg, false);
    }
}
