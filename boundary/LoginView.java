package bank.boundary;

import bank.MainApp;
import bank.control.LoginController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/** The login screen. Collects a username and password and shows the result. */
public class LoginView {

    private final MainApp app;
    private final LoginController controller;

    private final TextField usernameField = Ui.field("Username");
    private final PasswordField passwordField = Ui.passwordField("Password");
    private final Button loginButton = new Button("Login");
    private final Label messageLabel = Ui.message();

    public LoginView(MainApp app, LoginController controller) {
        this.app = app;
        this.controller = controller;
    }

    public void show() {
        loginButton.setMinWidth(180);
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(e -> handleLogin());
        VBox root = Ui.page(
                Ui.title("Banking System"),
                new Label("Please log in"),
                usernameField,
                passwordField,
                loginButton,
                messageLabel,
                Ui.button("Register as a new customer", app::showRegister));
        app.setContent(root);
    }

    private void handleLogin() {
        if (controller.login(getUsername(), getPassword())) {
            showDashboard();
        } else {
            passwordField.clear();
            showError("Invalid username or password");
        }
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void showError(String msg) {
        Ui.showMessage(messageLabel, msg, false);
    }

    public void showDashboard() {
        app.showDashboard();
    }
}
