package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.ItemController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.TOItem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ViewAllItemsPageController {
    @FXML private TableView<TOItem> itemTable;
    @FXML private TableColumn<TOItem, String> nameColumn;
    @FXML private TableColumn<TOItem, Boolean> perishableOrNotColumn;
    @FXML private TableColumn<TOItem, Integer> priceColumn;
    @FXML private TableColumn<TOItem, Integer> pointsColumn;
    @FXML private TableColumn<TOItem, Integer> quantityColumn;

    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        perishableOrNotColumn.setCellValueFactory(new PropertyValueFactory<>("isPerishable"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfPoints"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityInInventory"));

        itemTable.addEventHandler(GMSView.REFRESH, e -> {
            List<TOItem> items = ItemController.getAllItems();
            itemTable.setItems(FXCollections.observableList(items));
        });

        GMSView.registerRefreshableNode(itemTable);
        GMSView.refresh();
    }

    public void handleBackClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) itemTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

