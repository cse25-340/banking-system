package bank.boundary;

import bank.MainApp;
import bank.control.InterestController;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** The screen where the bank administrator starts the monthly interest payment. */
public class InterestView {

    private final MainApp app;
    private final InterestController controller;

    private final Label resultLabel = Ui.message();

    public InterestView(MainApp app, InterestController controller) {
        this.app = app;
        this.controller = controller;
    }

    public void show() {
        VBox root = Ui.page(
                Ui.title("Pay monthly interest"),
                new Label("Savings pay 0.05% and Investment accounts pay 5% of the balance."),
                Ui.button("Pay interest to all accounts", this::handlePay),
                resultLabel,
                Ui.button("Back to menu", app::showDashboard));
        app.setContent(root);
    }

    private void handlePay() {
        try {
            showInterestResult(controller.payMonthlyInterest());
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    public void showInterestResult(int count) {
        Ui.showMessage(resultLabel, "Interest was paid to " + count + " account(s).", true);
    }

    public void showError(String msg) {
        Ui.showMessage(resultLabel, msg, false);
    }
}
