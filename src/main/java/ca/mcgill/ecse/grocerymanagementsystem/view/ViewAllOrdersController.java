package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.util.List;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.TOOrder;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewAllOrdersController {

    @FXML private TableView<TOOrder> ordersTable;
    @FXML private TableColumn<TOOrder,Integer> orderNumColumn;
    @FXML private TableColumn<TOOrder,String>  customerColumn;
    @FXML private TableColumn<TOOrder,Double>  totalColumn;
    @FXML private TableColumn<TOOrder,String>  deadlineColumn;
    @FXML private TableColumn<TOOrder,String>  statusColumn;
    @FXML private TableColumn<TOOrder,String>  assigneeColumn;

    @FXML
    public void initialize() {

        orderNumColumn .setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        customerColumn .setCellValueFactory(new PropertyValueFactory<>("customerName"));
        totalColumn    .setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        deadlineColumn .setCellValueFactory(new PropertyValueFactory<>("deadline"));
        statusColumn   .setCellValueFactory(new PropertyValueFactory<>("status"));
        assigneeColumn .setCellValueFactory(new PropertyValueFactory<>("assignee"));

        ordersTable.addEventHandler(GMSView.REFRESH, e -> {
            List<TOOrder> orders = OrderController.getAllOrders();
            ordersTable.setItems(FXCollections.observableList(orders));
        });
        GMSView.registerRefreshableNode(ordersTable);
    }
}
