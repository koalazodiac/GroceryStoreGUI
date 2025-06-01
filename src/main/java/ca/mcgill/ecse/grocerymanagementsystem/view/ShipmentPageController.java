package ca.mcgill.ecse.grocerymanagementsystem.view;

import javafx.fxml.FXML;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentProcessingController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;

public class ShipmentPageController {
    public TextField shipmentIDInput;
    @FXML
    private GridPane rootPane;

    public void handleCreateNewClicked() {
        try {
            ShipmentController.createShipment();
            // Success
            GMSView.refresh();
        } catch (GroceryStoreException e) {
            // Error
            ViewUtils.showErrorMessage(e.getMessage());
        }
    }

    public void handleDeleteClicked() {
        String name = shipmentIDInput.getText();
        if (name.equals("")) {
            ViewUtils.showErrorMessage("Enter a Shipment ID to delete");
            return;
        }
        try {
            ShipmentController.deleteShipment(Integer.parseInt(name));
            // Success! :D
            GMSView.refresh();
            shipmentIDInput.setText("");
        } catch (GroceryStoreException e) {
            // Error!
            ViewUtils.showErrorMessage(e.getMessage());
        }

    }

    public void handleReceiveClicked() {
        String name = shipmentIDInput.getText();
        if (name.equals("")) {
            ViewUtils.showErrorMessage("Enter a Shipment ID to receive");
            return;
        }
        try {
            ShipmentProcessingController.receiveShipment(Integer.parseInt(name));
            // Success! :D
            GMSView.refresh();
            shipmentIDInput.setText("");
        } catch (GroceryStoreException e) {
            // Error!
            ViewUtils.showErrorMessage(e.getMessage());
        }
    }

    public void handleManageClicked() {
        String name = shipmentIDInput.getText();
        if (name.equals("")) {
            ViewUtils.showErrorMessage("Enter a Shipment ID to manage");
            return;
        }
        if (!ShipmentController.existShipment(Integer.parseInt(name))) {
            ViewUtils.showErrorMessage("there is no shipment with number \"" + name + "\"");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ShipmentManagePage.fxml"));
            Parent root = loader.load();

            // Get controller of next page
            ShipmentManageController controller = loader.getController();

            // Pass the value
            controller.setShipmentID(name);

            // Show next scene
            Stage stage = (Stage) shipmentIDInput.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Shipment");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleViewListClicked(ActionEvent event) {
        TabPane tabPane = (TabPane) rootPane.getScene().lookup("#mainTabPane");
        for (Tab tab : tabPane.getTabs()) {
            if ("View All Shipments".equals(tab.getText())) {
                tabPane.getSelectionModel().select(tab);
                return;
            }
        }
    }

}
