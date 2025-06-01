package ca.mcgill.ecse.grocerymanagementsystem.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ItemPageController {

    @FXML
private void handleDisplayAllItemsClicked(ActionEvent event) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/views/ViewAllItems.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("All Items");
        stage.show();
    } catch (Exception e) {
        e.printStackTrace(); 
    }
}


    @FXML
    private void handleDeleteItemClicked(ActionEvent event) {
        loadPage(event, "/views/DeleteItemPage.fxml", "Delete Item");
    }

    @FXML
    private void handleAddItemClicked(ActionEvent event) {
        loadPage(event, "/views/AddItemPage.fxml", "Add Item");
    }

    private void loadPage(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
private void handleBackClicked(ActionEvent event) {
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
