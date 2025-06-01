package ca.mcgill.ecse.grocerymanagementsystem.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.TOShipment;
import ca.mcgill.ecse.grocerymanagementsystem.controller.TOShipmentItem;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

public class ShipmentManageController {
    @FXML
    private Label shipmentIDLabel;
    public TextField itemNameInput;
    public TextField itemQuantityInput;
    public ChoiceBox<String> shipmentItemInput;

    public void initialize() {
        shipmentItemInput.addEventHandler(GMSView.REFRESH, e -> {
            List<String> names = new ArrayList<>();
            for (TOShipment shipment : ShipmentController.getAllShipments()) {
                if (shipment.getShipmentNumber() == Integer.parseInt(shipmentIDLabel.getText())) {
                    for (TOShipmentItem item : shipment.getTOShipmentItems()) {
                        names.add(item.getTOItem().getName());
                    }
                }
                shipmentItemInput.setItems(FXCollections.observableList(names));
                shipmentItemInput.setValue(null);
            }
        });
        GMSView.registerRefreshableNode(shipmentItemInput);
    }

    public void setShipmentID(String shipmentID) {
        shipmentIDLabel.setText(shipmentID);
    }

    public void handleAddItemClicked() {
        String name = itemNameInput.getText();
        int ID = Integer.parseInt(shipmentIDLabel.getText());
        try {
            ShipmentController.addItemToShipment(ID, name);
            // Success! :D
            GMSView.refresh();
            itemNameInput.setText("");
            itemQuantityInput.setText("");
            shipmentItemInput.setValue(null);

        } catch (GroceryStoreException e) {
            // Error!
            ViewUtils.showErrorMessage(e.getMessage());
        }

    }

    public void handleRemoveItemClicked() {
        String name = itemNameInput.getText();
        int ID = Integer.parseInt(shipmentIDLabel.getText());
        try {
            ShipmentController.updateQuantityInShipment(ID, name, 0);
            // Success! :D
            GMSView.refresh();
            itemNameInput.setText("");
            itemQuantityInput.setText("");
            shipmentItemInput.setValue(null);

        } catch (GroceryStoreException e) {
            // Error!
            ViewUtils.showErrorMessage(e.getMessage());
        }

    }

    public void handleUpdateAmountClicked() {
        String name = shipmentItemInput.getValue();
        int ID = Integer.parseInt(shipmentIDLabel.getText());
        int quantity = Integer.parseInt(itemQuantityInput.getText());
        try {
            ShipmentController.updateQuantityInShipment(ID, name, quantity);
            // Success! :D
            GMSView.refresh();
            itemNameInput.setText("");
            itemQuantityInput.setText("");
            shipmentItemInput.setValue(null);

        } catch (GroceryStoreException e) {
            // Error!
            ViewUtils.showErrorMessage(e.getMessage());
        }

    }

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
