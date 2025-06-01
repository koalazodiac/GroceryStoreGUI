package ca.mcgill.ecse.grocerymanagementsystem.controller;

import java.sql.Date;
import java.util.Calendar;

import ca.mcgill.ecse.grocerymanagementsystem.model.*;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.DeliveryDeadline;

public class OrderProcessingControllerTest {

    public static void main(String[] args) {
        // Create system
        GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();

        // Create and register a customer
        User user1 = new User("john_user", "null", "John", "4123123321", system);
        Customer customer = new Customer(user1, "address", 1000, system);

        // Create and register an employee
        User user2 = new User("jane_staff", "null", "Jane", "4123123421", system);
        Employee employee = new Employee(user2, system);

        // Create an item
        Item banana = new Item("Banana", 50, 100, true, 1, system); // 100 cents, 1 point, perishable

        // Create an order
        Date today = new Date(System.currentTimeMillis());
        Order order = new Order(null, DeliveryDeadline.InThreeDays, system, customer);
        int orderNumber = order.getOrderNumber();
        
        // Add items to order
        new OrderItem(5, system, order, banana); // quantity = 5

        try {
            System.out.println("Checking out order...");
            OrderProcessingController.checkOut(orderNumber);
            System.out.println("Checkout successful. State: " + order.getStateOrder());

            System.out.println("Paying for order with points...");
            OrderProcessingController.payForOrder(orderNumber, true);
            System.out.println("Payment successful. Total cost: " + order.getTotalCost());

            System.out.println("Assigning order to employee...");
            OrderProcessingController.assignOrderToEmployee(orderNumber, "jane_staff");
            System.out
                    .println("Assignment successful. Assigned to: " + order.getOrderAssignee().getUser().getUsername());

            System.out.println("Finishing order assembly...");
            OrderProcessingController.finishOrderAssembly(orderNumber);
            System.out.println("Assembly finished. State: " + order.getStateOrder());

            System.out.println("Delivering order...");
            OrderProcessingController.deliverOrder(orderNumber);
            System.out.println("Order delivered. State: " + order.getStateOrder());

        } catch (GroceryStoreException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Reset system if needed
    }
}
