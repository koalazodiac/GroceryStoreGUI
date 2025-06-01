package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.*;

public class ItemControllerTestMain {

    public static void main(String[] args) {
        // Reset system
        GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
        system.delete();

        System.out.println("=== Testing ItemController ===");

        // Test: Create item
        try {
            ItemController.create("Milk", true, 3, 250);
            System.out.println("Created item 'Milk' successfully.");
        } catch (GroceryStoreException e) {
            System.err.println("Failed to create 'Milk': " + e.getMessage());
        }

        // Test: Duplicate item
        try {
            ItemController.create("Milk", false, 2, 100);
            System.err.println("ERROR: Duplicate 'Milk' was created.");
        } catch (GroceryStoreException e) {
            System.out.println("Duplicate check passed: " + e.getMessage());
        }

        // Test: Invalid price
        try {
            ItemController.create("Bread", true, 2, 0);
            System.err.println("ERROR: Created item with invalid price.");
        } catch (GroceryStoreException e) {
            System.out.println("Invalid price check passed: " + e.getMessage());
        }

        // Test: Invalid points
        try {
            ItemController.create("Bread", true, 6, 100);
            System.err.println("ERROR: Created item with invalid points.");
        } catch (GroceryStoreException e) {
            System.out.println("Invalid points check passed: " + e.getMessage());
        }

        // Test: Update price
        try {
            ItemController.updatePrice("Milk", 300);
            Item milk = Item.getWithName("Milk");
            System.out.println("Updated price of 'Milk' to: " + milk.getPrice());
        } catch (GroceryStoreException e) {
            System.err.println("Failed to update price: " + e.getMessage());
        }

        // Test: Update points
        try {
            ItemController.updatePoints("Milk", 5);
            Item milk = Item.getWithName("Milk");
            System.out.println("Updated points of 'Milk' to: " + milk.getNumberOfPoints());
        } catch (GroceryStoreException e) {
            System.err.println("Failed to update points: " + e.getMessage());
        }

        // Test: Delete item
        try {
            ItemController.delete("Milk");
            if (Item.getWithName("Milk") == null) {
                System.out.println("Successfully deleted 'Milk'.");
            } else {
                System.err.println("ERROR: 'Milk' still exists after deletion.");
            }
        } catch (GroceryStoreException e) {
            System.err.println("Failed to delete 'Milk': " + e.getMessage());
        }

        // Test: Delete non-existent item
        try {
            ItemController.delete("FakeItem");
            System.err.println("ERROR: Deleted a non-existent item.");
        } catch (GroceryStoreException e) {
            System.out.println("Properly failed to delete nonexistent item: " + e.getMessage());
        }

        System.out.println("=== Test complete ===");
    }
}
