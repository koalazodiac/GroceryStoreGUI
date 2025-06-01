package ca.mcgill.ecse.grocerymanagementsystem.view;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.TOOrder;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ItemController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.DeliveryDeadline;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import ca.mcgill.ecse.grocerymanagementsystem.view.GMSView;
import ca.mcgill.ecse.grocerymanagementsystem.view.ViewUtils;

public class OrderPageController {

    @FXML
    private GridPane rootPane;

    @FXML
    private ChoiceBox<String> customerInput;
    @FXML
    private DatePicker deadlineInput;

    @FXML
    private ChoiceBox<Integer> orderInput;
    @FXML
    private ChoiceBox<String> itemInput;
    @FXML
    private TextField quantityInput;

    @FXML
    private ChoiceBox<Integer> actionOrderInput;

    @FXML
    public void initialize() {
        List<String> customers = UserController.getAllUsers()
                .stream().map(u -> u.getUsername()).collect(Collectors.toList());
        customerInput.setItems(FXCollections.observableList(customers));
        customerInput.addEventHandler(GMSView.REFRESH, e -> {
            List<String> cs = UserController.getAllUsers()
                    .stream().map(u -> u.getUsername()).collect(Collectors.toList());
            customerInput.setItems(FXCollections.observableList(cs));
        });
        GMSView.registerRefreshableNode(customerInput);

        List<String> items = ItemController.getAllItems()
                .stream()
                .map(i -> i.getName())
                .collect(Collectors.toList());
        itemInput.setItems(FXCollections.observableList(items));
        itemInput.addEventHandler(GMSView.REFRESH, e -> {
            List<String> its = ItemController.getAllItems()
                    .stream().map(i -> i.getName()).collect(Collectors.toList());
            itemInput.setItems(FXCollections.observableList(its));
        });
        GMSView.registerRefreshableNode(itemInput);

        orderInput.addEventHandler(GMSView.REFRESH, e -> reloadOrders());
        actionOrderInput.addEventHandler(GMSView.REFRESH, e -> reloadOrders());
        GMSView.registerRefreshableNode(orderInput);
        GMSView.registerRefreshableNode(actionOrderInput);
        reloadOrders();
    }

    private void reloadOrders() {
        List<Integer> ids = OrderController.getAllOrders()
                .stream().map(TOOrder::getOrderNumber).collect(Collectors.toList());
        orderInput.setItems(FXCollections.observableList(ids));
        actionOrderInput.setItems(FXCollections.observableList(ids));
    }

    @FXML
    private void handleCreateOrder(ActionEvent e) {
        try {
            String cust = customerInput.getValue();
            LocalDate ld = deadlineInput.getValue();
            DeliveryDeadline dd = null;

            if (ld != null) {
                LocalDate today = LocalDate.now();
                long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(today, ld);

                if (daysBetween == 0) {
                    dd = DeliveryDeadline.SameDay;
                } else if (daysBetween == 1) {
                    dd = DeliveryDeadline.InOneDay;
                } else if (daysBetween == 2) {
                    dd = DeliveryDeadline.InTwoDays;
                } else if (daysBetween == 3) {
                    dd = DeliveryDeadline.InThreeDays;
                } else {
                    throw new GroceryStoreException("Delivery deadline must be within 3 days");
                }
            }

            OrderController.createOrder(cust, dd);
            GMSView.refresh();
        } catch (GroceryStoreException ex) {
            ViewUtils.showErrorMessage(ex.getMessage());
        }
    }

    @FXML
    private void handleDeleteOrder(ActionEvent e) {
        try {
            Integer id = orderInput.getValue();
            if (id == null)
                throw new NumberFormatException();
            OrderController.deleteOrder(id);
            GMSView.refresh();
        } catch (NumberFormatException nfe) {
            ViewUtils.showErrorMessage("Please enter a valid order number");
        } catch (GroceryStoreException ex) {
            ViewUtils.showErrorMessage(ex.getMessage());
        }
    }

    @FXML
    private void handleAddItem(ActionEvent e) {
        try {
            Integer id = orderInput.getValue();
            if (id == null) {
                throw new GroceryStoreException("Please select an order");
            }
            String code = itemInput.getValue();
            if (code == null) {
                throw new GroceryStoreException("Please select an item");
            }
            int qty = Integer.parseInt(quantityInput.getText());
            if (qty <= 0) {
                throw new GroceryStoreException("Quantity must be greater than 0");
            }
            OrderController.addItemToOrder(id, code);
            GMSView.refresh();
        } catch (NumberFormatException nfe) {
            ViewUtils.showErrorMessage("Order number and quantity must be numbers");
        } catch (GroceryStoreException ex) {
            ViewUtils.showErrorMessage(ex.getMessage());
        }
    }

    @FXML
    private void handleRemoveItem(ActionEvent e) {
        try {
            Integer id = orderInput.getValue();
            if (id == null) {
                throw new GroceryStoreException("Please select an order");
            }
            String code = itemInput.getValue();
            if (code == null) {
                throw new GroceryStoreException("Please select an item");
            }
            int qty = Integer.parseInt(quantityInput.getText());
            if (qty <= 0) {
                throw new GroceryStoreException("Quantity must be greater than 0");
            }
            OrderController.updateQuantityInOrder(id, code, 0);
            GMSView.refresh();
        } catch (NumberFormatException nfe) {
            ViewUtils.showErrorMessage("Order number and quantity must be numbers");
        } catch (GroceryStoreException ex) {
            ViewUtils.showErrorMessage(ex.getMessage());
        }
    }

    @FXML
    private void handleCheckout(ActionEvent e) {
        try {
            Integer id = actionOrderInput.getValue();
            if (id == null)
                throw new NumberFormatException();
            OrderProcessingController.checkOut(id);
            GMSView.refresh();
        } catch (NumberFormatException nfe) {
            ViewUtils.showErrorMessage("Please enter a valid order number");
        } catch (GroceryStoreException ex) {
            ViewUtils.showErrorMessage(ex.getMessage());
        }
    }

    @FXML
    private void handlePay(ActionEvent e) {
        try {
            Integer id = actionOrderInput.getValue();
            if (id == null)
                throw new NumberFormatException();
            OrderProcessingController.payForOrder(id, false);
            GMSView.refresh();
        } catch (NumberFormatException nfe) {
            ViewUtils.showErrorMessage("Please enter a valid order number");
        } catch (GroceryStoreException ex) {
            ViewUtils.showErrorMessage(ex.getMessage());
        }
    }
}
