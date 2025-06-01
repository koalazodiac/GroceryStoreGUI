package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ItemController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DeleteItemPageController {

    @FXML
    private TextField nameInput;

    @FXML
    private Label feedbackLabel;

    @FXML
    private BorderPane rootPane;

    public void initialize() {
        feedbackLabel.setText("");
    }

    @FXML
    public void handleDeleteClicked() {
        String name = nameInput.getText();

        if (name == null || name.trim().isEmpty()) {
            showFeedback("Item name cannot be empty.", false);
            return;
        }

        try {
            ItemController.delete(name);
            showFeedback("Item \"" + name + "\" deleted successfully!", true);
            GMSView.refresh();
            nameInput.clear();
        } catch (GroceryStoreException e) {
            showFeedback(e.getMessage(), false);
        }
    }

    private void showFeedback(String message, boolean isSuccess) {
        feedbackLabel.setTextFill(isSuccess ? Color.GREEN : Color.RED);
        feedbackLabel.setText(message);
    }

    @FXML
    public void handleDoneClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/MainPage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Page");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
