package bank.boundary;

import bank.MainApp;
import bank.control.AccountController;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/** The screen for opening a Savings, Investment or Cheque account. */
public class OpenAccountView {

    private final MainApp app;
    private final AccountController controller;

    private final ComboBox<String> accountTypeBox = new ComboBox<>();
    private final TextField initialDepositField = Ui.field("Initial deposit in BWP");
    private final TextField employerNameField = Ui.field("Employer (company) name");
    private final TextField employerAddressField = Ui.field("Employer (company) address");
    private final Label hintLabel = new Label();
    private final Label messageLabel = Ui.message();

    public OpenAccountView(MainApp app, AccountController controller) {
        this.app = app;
        this.controller = controller;
    }

    public void show() {
        accountTypeBox.getItems().setAll("Savings", "Investment", "Cheque");
        accountTypeBox.setPromptText("Choose an account type");
        accountTypeBox.setOnAction(e -> updateFields());
        hintLabel.setWrapText(true);
        Ui.limit(hintLabel, Ui.FIELD_WIDTH);
        updateFields();
        VBox root = Ui.page(
                Ui.title("Open an account"),
                accountTypeBox, hintLabel, initialDepositField, employerNameField, employerAddressField,
                Ui.button("Open account", this::handleOpen),
                messageLabel,
                Ui.button("Back to menu", app::showDashboard));
        app.setContent(root);
    }

    /** Shows only the fields that the chosen account type needs. */
    private void updateFields() {
        String type = accountTypeBox.getValue();
        boolean cheque = "Cheque".equals(type);
        employerNameField.setVisible(cheque);
        employerNameField.setManaged(cheque);
        employerAddressField.setVisible(cheque);
        employerAddressField.setManaged(cheque);
        if ("Investment".equals(type)) {
            hintLabel.setText("Investment accounts need an opening deposit of at least BWP500.00.");
        } else if ("Cheque".equals(type)) {
            hintLabel.setText("Cheque accounts are for employed customers. Enter your employer's details.");
        } else if ("Savings".equals(type)) {
            hintLabel.setText("Savings accounts earn interest but do not allow withdrawals.");
        } else {
            hintLabel.setText("");
        }
    }

    private void handleOpen() {
        String[] d = getAccountDetails();
        try {
            controller.openAccount(d[0], d[1], d[2], d[3]);
            showSuccess("Your " + d[0] + " account has been opened.");
            initialDepositField.clear();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            showError(ex.getMessage());
        }
    }

    /** @return account type, initial deposit, employer name, employer address */
    public String[] getAccountDetails() {
        return new String[]{
                accountTypeBox.getValue(), initialDepositField.getText(),
                employerNameField.getText(), employerAddressField.getText()};
    }

    public void showSuccess(String msg) {
        Ui.showMessage(messageLabel, msg, true);
    }

    public void showError(String msg) {
        Ui.showMessage(messageLabel, msg, false);
    }
}
