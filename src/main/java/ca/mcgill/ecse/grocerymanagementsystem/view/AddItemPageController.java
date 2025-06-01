
package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ItemController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddItemPageController {

    @FXML
    private TextField nameInput;
    @FXML
    private TextField priceInput;
    @FXML
    private TextField pointsInput;
    @FXML
    private ChoiceBox<String> perishableChoiceBox;
    @FXML
    private Label feedbackLabel;

    public void initialize() {
        perishableChoiceBox.getItems().addAll("Yes", "No");
        perishableChoiceBox.setValue("Yes");
        feedbackLabel.setText("");
    }

    @FXML
    private void handleAddClicked() {
        String name = nameInput.getText();
        String priceStr = priceInput.getText();
        String pointsStr = pointsInput.getText();
        String perishableValue = perishableChoiceBox.getValue();

        try {
            if (name.isEmpty() || priceStr.isEmpty() || pointsStr.isEmpty() || perishableValue == null) {
                showFeedback("Please fill in all required fields.", false);
                return;
            }

            int price = Integer.parseInt(priceStr);
            int points = Integer.parseInt(pointsStr);
            boolean isPerishable = perishableValue.equals("Yes");

            ItemController.create(name, isPerishable, points, price);

            showFeedback("Item added successfully!", true);
            GMSView.refresh();
            clearInputs();
        } catch (NumberFormatException e) {
            showFeedback("Price and points must be valid integers.", false);
        } catch (GroceryStoreException e) {
            showFeedback(e.getMessage(), false);
        }
    }

    @FXML
    private void handleDoneClicked(ActionEvent event) {
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

    private void showFeedback(String message, boolean isSuccess) {
        feedbackLabel.setTextFill(isSuccess ? Color.GREEN : Color.RED);
        feedbackLabel.setText(message);
    }

    private void clearInputs() {
        nameInput.setText("");
        priceInput.setText("");
        pointsInput.setText("");
        perishableChoiceBox.setValue("Yes");
    }

    @FXML
    public void handleUpdateClicked() {
        String name = nameInput.getText();
        String priceStr = priceInput.getText();
        String pointsStr = pointsInput.getText();
        String perishableValue = perishableChoiceBox.getValue();

        try {
            if (name.isEmpty()) {
                showFeedback("Item name is required to update.", false);
                return;
            }

            boolean updated = false;

            if (!priceStr.isEmpty()) {
                int price = Integer.parseInt(priceStr);
                ItemController.updatePrice(name, price);
                updated = true;
            }

            if (!pointsStr.isEmpty()) {
                int points = Integer.parseInt(pointsStr);
                ItemController.updatePoints(name, points);
                updated = true;
            }

            if (updated) {
                showFeedback("Item updated successfully!", true);
                GMSView.refresh();
                clearInputs();
            } else {
                showFeedback("Please enter at least one attribute to update.", false);
            }

        } catch (NumberFormatException e) {
            showFeedback("Price and points must be valid integers.", false);
        } catch (GroceryStoreException e) {
            showFeedback(e.getMessage(), false);
        }
    }

}
