package ca.mcgill.ecse.grocerymanagementsystem.view;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.UserController;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class UserAccountManagementPageController {
    public ChoiceBox<String> accountTypeChoiceBox;
    public TextField usernameInput;
    public PasswordField passwordInput;
    public TextField nameInput;
    public TextField phoneNumberInput;
    public TextField addressInput;
    public Label feedbackLabel;
    public Label addressLabel;

    public void initialize() {
        accountTypeChoiceBox.getItems().addAll("Customer", "Employee");
        accountTypeChoiceBox.setValue("Customer");

        accountTypeChoiceBox.setOnAction(e -> updateFormFields());
        updateFormFields();
    }

    @FXML
    private void updateFormFields() {
        boolean isCustomer = "Customer".equals(accountTypeChoiceBox.getValue());
        addressInput.setVisible(isCustomer);
        addressInput.setManaged(isCustomer);

        addressLabel.setVisible(isCustomer);
        addressLabel.setManaged(isCustomer);
    }

    @FXML
    private void showFeedback(String message, boolean isSuccess) {
        feedbackLabel.setTextFill(isSuccess ? Color.GREEN : Color.RED);
        feedbackLabel.setText(message);
    }
    @FXML
    private void clearInputs(){
        usernameInput.setText("");
        addressInput.setText("");
        nameInput.setText("");
        passwordInput.setText("");
        phoneNumberInput.setText("");
    }
    
    public void handleRegisterClicked(){
        String userType = accountTypeChoiceBox.getValue();
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        String name = nameInput.getText();
        String phoneNumber = phoneNumberInput.getText();
        String address = addressInput.getText();
        
        try{
            if ("Customer".equals(userType)){
                UserController.registerNewCustomer(username, password, address);
                if (!address.isEmpty()){
                    UserController.updateAddress(username, address);
                }
            }
            else{
                UserController.registerNewEmployee(username);
                
            }
            if (!password.isEmpty()) {
                UserController.updatePassword(username, password);
            }
            if (!name.isEmpty()){
                UserController.updateName(username, name);
            }
            if (!phoneNumber.isEmpty()){
            UserController.updatePhoneNumber(username, phoneNumber);
            }
            

            showFeedback(userType + " registered successfully!", true);
            GMSView.refresh();
            clearInputs();
        }
        catch (GroceryStoreException e) {
            showFeedback(e.getMessage(), false);
        }
    }
    public void handleUpdateClicked(){
        String userType = accountTypeChoiceBox.getValue();
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        String name = nameInput.getText();
        String phoneNumber = phoneNumberInput.getText();
        String address = addressInput.getText();

        try {
            if (!password.isEmpty()) {
                UserController.updatePassword(username, password);
            }
            if (!name.isEmpty()){
                UserController.updateName(username, name);
            }
            if (!phoneNumber.isEmpty()){
                UserController.updatePhoneNumber(username, phoneNumber);
            }
            if (!address.isEmpty() && "Customer".equals(userType)){
                UserController.updateAddress(username, address);
            }

            showFeedback(userType + " updated successfully!", true);
            GMSView.refresh();
            clearInputs();
        } catch (GroceryStoreException e) {
            showFeedback(e.getMessage(), false);
        }   
    }
    public void handleDeleteClicked(){
        String userType = accountTypeChoiceBox.getValue();
        String username = usernameInput.getText();

        try {
            if ("Customer".equals(userType)){
                UserController.deleteCustomer(username);
            }
            else{
                UserController.deleteEmployee(username);
            }
            showFeedback(userType + " deleted successfully!", true);
            GMSView.refresh();
            clearInputs();
        } catch (GroceryStoreException e) {
            showFeedback(e.getMessage(), false);
        }
    
    }

}
