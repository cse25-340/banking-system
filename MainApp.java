package bank;

import bank.boundary.AccountView;
import bank.boundary.DashboardView;
import bank.boundary.DepositView;
import bank.boundary.InterestView;
import bank.boundary.LoginView;
import bank.boundary.OpenAccountView;
import bank.boundary.RegisterCustomerView;
import bank.boundary.WithdrawView;
import bank.control.AccountController;
import bank.control.CustomerController;
import bank.control.InterestController;
import bank.control.LoginController;
import bank.control.TransactionController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/** Starts the JavaFX application and moves between the screens. */
public class MainApp extends Application {

    private Scene scene;

    private LoginController loginController;
    private CustomerController customerController;
    private AccountController accountController;
    private TransactionController transactionController;
    private InterestController interestController;

    @Override
    public void start(Stage stage) {
        loginController = new LoginController();
        customerController = new CustomerController();
        accountController = new AccountController(loginController);
        transactionController = new TransactionController();
        interestController = new InterestController();

        scene = new Scene(new BorderPane(), 760, 700);
        stage.setTitle("Banking System");
        stage.setScene(scene);
        stage.show();
        showLogin();
    }

    public void setContent(Parent root) {
        scene.setRoot(root);
    }

    public void showLogin() {
        new LoginView(this, loginController).show();
    }

    public void showRegister() {
        new RegisterCustomerView(this, customerController).show();
    }

    public void showDashboard() {
        new DashboardView(this, loginController).show();
    }

    public void showOpenAccount() {
        new OpenAccountView(this, accountController).show();
    }

    public void showAccounts() {
        new AccountView(this, accountController, transactionController).show();
    }

    public void showDeposit() {
        new DepositView(this, accountController, transactionController).show();
    }

    public void showWithdraw() {
        new WithdrawView(this, accountController, transactionController).show();
    }

    public void showInterest() {
        new InterestView(this, interestController).show();
    }

    public void logout() {
        loginController.logout();
        showLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
