package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.TOShipment;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AllShipmentsController {
    @FXML
    private TableView<TOShipment> ShipmentsTable;
    @FXML
    private TableColumn<TOShipment, String> IDColumn;
    @FXML
    private TableColumn<TOShipment, String> DateColumn;

    @FXML
    private void initialize() {
        // Explain how the table should populate the cells given a TOFlight
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("shipmentNumber"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOrdered"));

        // Handle refresh events
        ShipmentsTable.addEventHandler(GMSView.REFRESH, e -> {
            List<TOShipment> shipments = ShipmentController.getAllShipments();
            ShipmentsTable.setItems(FXCollections.observableList(shipments));
        });
        GMSView.registerRefreshableNode(ShipmentsTable);
    }

}
