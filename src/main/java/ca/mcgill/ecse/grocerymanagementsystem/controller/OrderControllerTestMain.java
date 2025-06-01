package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.*;

public class OrderControllerTestMain {

    public static void main(String[] args) {
        // Reset system
        GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
        system.delete();

        System.out.println("=== Testing OrderController ===");

        // Create and register a customer
        User user1 = new User("john_doe", "null", "John", "4123123321", system);
        Customer customer = new Customer(user1, "address", 1000, system);

        // Create and register an item
        Item apple = new Item("Apple", 0, 100, false, 2, system); // $1.00, 2 pts

        // Create an order
        try {
            OrderController.createOrder("john_doe", Order.DeliveryDeadline.SameDay);
            System.out.println("Order created successfully.");
        } catch (GroceryStoreException e) {
            System.err.println("Failed to create order: " + e.getMessage());
        }

        // Get the order number
        int orderNumber = system.getOrders().get(0).getOrderNumber();

        // Add item to order
        try {
            OrderController.addItemToOrder(orderNumber, "Apple");
            System.out.println("Added 'Apple' to order.");
        } catch (GroceryStoreException e) {
            System.err.println("Failed to add item: " + e.getMessage());
        }

        // Try adding same item again (should fail)
        try {
            OrderController.addItemToOrder(orderNumber, "Apple");
            System.err.println("ERROR: Added duplicate item.");
        } catch (GroceryStoreException e) {
            System.out.println("Duplicate item prevented: " + e.getMessage());
        }

        // Update item quantity
        try {
            OrderController.updateQuantityInOrder(orderNumber, "Apple", 3);
            System.out.println("Updated 'Apple' quantity to 3.");
        } catch (GroceryStoreException e) {
            System.err.println("Failed to update quantity: " + e.getMessage());
        }

        // Set quantity to 0 (should remove item)
        try {
            OrderController.updateQuantityInOrder(orderNumber, "Apple", 0);
            System.out.println("Removed 'Apple' from order.");
        } catch (GroceryStoreException e) {
            System.err.println("Failed to remove item: " + e.getMessage());
        }

        // Try deleting the order
        try {
            OrderController.deleteOrder(orderNumber);
            System.out.println("Order deleted successfully.");
        } catch (GroceryStoreException e) {
            System.err.println("Failed to delete order: " + e.getMessage());
        }

        // Try deleting again (should fail)
        try {
            OrderController.deleteOrder(orderNumber);
            System.err.println("ERROR: Deleted order that no longer exists.");
        } catch (GroceryStoreException e) {
            System.out.println("Deletion of non-existent order prevented: " + e.getMessage());
        }

        System.out.println("=== Test complete ===");
    }
}
