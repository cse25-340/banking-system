package bank.boundary;

import bank.MainApp;
import bank.control.LoginController;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** The menu shown after login. Customers see banking options, the administrator sees interest payment. */
public class DashboardView {

    private final MainApp app;
    private final LoginController controller;

    public DashboardView(MainApp app, LoginController controller) {
        this.app = app;
        this.controller = controller;
    }

    public void show() {
        VBox root = Ui.page(
                Ui.title("Welcome, " + controller.getDisplayName()),
                new Label(controller.isAdmin() ? "Bank administrator menu" : "What would you like to do?"));
        if (controller.isAdmin()) {
            root.getChildren().add(Ui.button("Pay monthly interest", app::showInterest));
        } else {
            root.getChildren().add(Ui.button("Open an account", app::showOpenAccount));
            root.getChildren().add(Ui.button("My accounts and history", app::showAccounts));
            root.getChildren().add(Ui.button("Deposit funds", app::showDeposit));
            root.getChildren().add(Ui.button("Withdraw funds", app::showWithdraw));
        }
        root.getChildren().add(Ui.button("Logout", app::logout));
        app.setContent(root);
    }
}
