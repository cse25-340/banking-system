package bank.boundary;

import bank.MainApp;
import bank.control.CustomerController;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/** The form for registering a new customer and choosing a login. */
public class RegisterCustomerView {

    private final MainApp app;
    private final CustomerController controller;

    private final TextField firstNameField = Ui.field("First name");
    private final TextField surnameField = Ui.field("Surname");
    private final TextField addressField = Ui.field("Address");
    private final TextField phoneField = Ui.field("Phone number");
    private final TextField usernameField = Ui.field("Choose a username");
    private final PasswordField passwordField = Ui.passwordField("Choose a password (6+ characters)");
    private final Label messageLabel = Ui.message();

    public RegisterCustomerView(MainApp app, CustomerController controller) {
        this.app = app;
        this.controller = controller;
    }

    public void show() {
        VBox root = Ui.page(
                Ui.title("Register as a customer"),
                firstNameField, surnameField, addressField, phoneField, usernameField, passwordField,
                Ui.button("Register", this::handleRegister),
                messageLabel,
                Ui.button("Back to login", app::showLogin));
        app.setContent(root);
    }

    private void handleRegister() {
        String[] d = getCustomerDetails();
        try {
            controller.registerCustomer(d[0], d[1], d[2], d[3], d[4], d[5]);
            clearFields();
            showSuccess("Registration complete. You can now log in.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    /** @return first name, surname, address, phone, username, password */
    public String[] getCustomerDetails() {
        return new String[]{
                firstNameField.getText(), surnameField.getText(), addressField.getText(),
                phoneField.getText(), usernameField.getText(), passwordField.getText()};
    }

    public void showSuccess(String msg) {
        Ui.showMessage(messageLabel, msg, true);
    }

    public void showError(String msg) {
        Ui.showMessage(messageLabel, msg, false);
    }

    private void clearFields() {
        firstNameField.clear();
        surnameField.clear();
        addressField.clear();
        phoneField.clear();
        usernameField.clear();
        passwordField.clear();
    }
}
