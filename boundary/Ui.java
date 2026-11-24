package bank.boundary;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** Small helpers so every screen looks the same. Contains no business logic. */
final class Ui {

    static final double FIELD_WIDTH = 320;

    private Ui() {
    }

    static Label title(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        return label;
    }

    static Label message() {
        Label label = new Label();
        label.setWrapText(true);
        label.setMaxWidth(FIELD_WIDTH);
        return label;
    }

    static TextField field(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setMaxWidth(FIELD_WIDTH);
        return field;
    }

    static PasswordField passwordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setMaxWidth(FIELD_WIDTH);
        return field;
    }

    static Button button(String text, Runnable action) {
        Button button = new Button(text);
        button.setMinWidth(180);
        button.setOnAction(e -> action.run());
        return button;
    }

    static VBox page(Node... nodes) {
        VBox root = new VBox(12);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(nodes);
        return root;
    }

    static void limit(Region region, double width) {
        region.setMaxWidth(width);
    }

    static void showMessage(Label label, String text, boolean success) {
        label.setText(text);
        label.setStyle(success ? "-fx-text-fill: #1a7f37;" : "-fx-text-fill: #cf222e;");
    }
}
