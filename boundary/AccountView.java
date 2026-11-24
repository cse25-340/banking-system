package bank.boundary;

import bank.MainApp;
import bank.control.AccountController;
import bank.control.TransactionController;
import bank.entity.Account;
import bank.entity.Transaction;
import bank.entity.TransactionType;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Shows the customer's accounts and the transaction history of a chosen account. */
public class AccountView {

    private final MainApp app;
    private final AccountController accountController;
    private final TransactionController transactionController;

    private final TableView<Account> accountsTable = new TableView<>();
    private final TableView<Transaction> historyTable = new TableView<>();
    private final ComboBox<Account> historyBox = new ComboBox<>();

    public AccountView(MainApp app, AccountController accountController,
                       TransactionController transactionController) {
        this.app = app;
        this.accountController = accountController;
        this.transactionController = transactionController;
    }

    public void show() {
        TableColumn<Account, String> numberColumn = new TableColumn<>("Account number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        TableColumn<Account, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        TableColumn<Account, String> branchColumn = new TableColumn<>("Branch");
        branchColumn.setCellValueFactory(new PropertyValueFactory<>("branch"));
        TableColumn<Account, BigDecimal> balanceColumn = new TableColumn<>("Balance (BWP)");
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        accountsTable.getColumns().add(numberColumn);
        accountsTable.getColumns().add(typeColumn);
        accountsTable.getColumns().add(branchColumn);
        accountsTable.getColumns().add(balanceColumn);
        accountsTable.setPrefHeight(170);

        TableColumn<Transaction, LocalDateTime> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Transaction, TransactionType> kindColumn = new TableColumn<>("Type");
        kindColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Transaction, BigDecimal> amountColumn = new TableColumn<>("Amount (BWP)");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Transaction, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        historyTable.getColumns().add(dateColumn);
        historyTable.getColumns().add(kindColumn);
        historyTable.getColumns().add(amountColumn);
        historyTable.getColumns().add(descriptionColumn);
        historyTable.setPrefHeight(200);

        List<Account> accounts = accountController.getAccounts();
        showAccounts(accounts);
        historyBox.getItems().setAll(accounts);
        historyBox.setPromptText("Choose an account to see its history");
        historyBox.setOnAction(e -> loadHistory());

        VBox root = Ui.page(
                Ui.title("My accounts"),
                accountsTable,
                new Label("Transaction history"),
                historyBox,
                historyTable,
                Ui.button("Back to menu", app::showDashboard));
        app.setContent(root);
    }

    public void showAccounts(List<Account> list) {
        accountsTable.setItems(FXCollections.observableArrayList(list));
    }

    public void showHistory(List<Transaction> list) {
        historyTable.setItems(FXCollections.observableArrayList(list));
    }

    private void loadHistory() {
        Account selected = historyBox.getValue();
        if (selected != null) {
            showHistory(transactionController.getHistory(selected.getAccountNumber()));
        }
    }
}
