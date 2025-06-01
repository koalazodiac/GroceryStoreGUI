package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.controller.TOUser;
import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ViewAllUsersPageController {
    @FXML private TableView<TOUser> userTable;
    @FXML private TableColumn<TOUser, String> usernameColumn;
    @FXML private TableColumn<TOUser, String> nameColumn;
    @FXML private TableColumn<TOUser, String> phoneColumn;
    @FXML private TableColumn<TOUser, String> roleColumn;
    @FXML private TableColumn<TOUser, String> addressColumn;

public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        userTable.addEventHandler(GMSView.REFRESH, e -> {
            List<TOUser> users = UserController.getAllUsers(); 
            userTable.setItems(FXCollections.observableList(users));
        });

        GMSView.registerRefreshableNode(userTable);

        GMSView.refresh();
    }

    public void handleBackClicked() {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainPage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) userTable.getScene().getWindow(); 
        stage.setScene(new Scene(root));
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
