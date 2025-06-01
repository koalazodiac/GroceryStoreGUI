package ca.mcgill.ecse.grocerymanagementsystem.controller;

import java.sql.Date;
import java.util.*;

import ca.mcgill.ecse.grocerymanagementsystem.model.Customer;
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.DeliveryDeadline;
import ca.mcgill.ecse.grocerymanagementsystem.model.OrderItem;
import ca.mcgill.ecse.grocerymanagementsystem.model.User;
import ca.mcgill.ecse.grocerymanagementsystem.model.UserRole;
import ca.mcgill.ecse.grocerymanagementsystem.model.ShipmentItem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Shipment;

public class ShipmentControllerTestMain {

    public static void main(String[] args) {
        // Reset the system
        GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
        system.delete();

        System.out.println("=== Testing ShipmentController ===");

        // Create an item to use in shipments
        Item bananas = new Item("Banana", 0, 100, false, 1, system);

        // Create shipment
        try {
            ShipmentController.createShipment();
            System.out.println("Shipment created successfully.");
        } catch (GroceryStoreException e) {
            System.err.println("Failed to create shipment: " + e.getMessage());
        }

        // Get the created shipment number
        int shipmentNumber = system.getShipment(0).getShipmentNumber();

        // Add item to shipment
        try {
            ShipmentController.addItemToShipment(shipmentNumber, "Banana");
            System.out.println("Added 'Banana' to shipment.");
        } catch (GroceryStoreException e) {
            System.err.println("Failed to add item: " + e.getMessage());
        }

        // Try adding same item again (should fail)
        try {
            ShipmentController.addItemToShipment(shipmentNumber, "Banana");
            System.err.println("ERROR: Duplicate item added.");
        } catch (GroceryStoreException e) {
            System.out.println("Duplicate item prevented: " + e.getMessage());
        }

        // Update quantity in shipment
        try {
            ShipmentController.updateQuantityInShipment(shipmentNumber, "Banana", 5);
            ShipmentItem item = system.getShipments().get(0).getShipmentItems().get(0);
            System.out.println("Updated quantity to: " + item.getQuantity());
        } catch (GroceryStoreException e) {
            System.err.println("Failed to update quantity: " + e.getMessage());
        }

        // Set quantity to 0 (should remove item)
        try {
            ShipmentController.updateQuantityInShipment(shipmentNumber, "Banana", 0);
            if (system.getShipments().get(0).getShipmentItems().isEmpty()) {
                System.out.println("Successfully removed item from shipment.");
            } else {
                System.err.println("ERROR: Item still exists after removal.");
            }
        } catch (GroceryStoreException e) {
            System.err.println("Failed to remove item: " + e.getMessage());
        }

        // Delete shipment
        try {
            ShipmentController.deleteShipment(shipmentNumber);
            if (system.getShipments().isEmpty()) {
                System.out.println("Shipment deleted successfully.");
            } else {
                System.err.println("ERROR: Shipment still exists after deletion.");
            }
        } catch (GroceryStoreException e) {
            System.err.println("Failed to delete shipment: " + e.getMessage());
        }

        // Try deleting nonexistent shipment
        try {
            ShipmentController.deleteShipment(shipmentNumber);
            System.err.println("ERROR: Deleted nonexistent shipment.");
        } catch (GroceryStoreException e) {
            System.out.println("Properly failed to delete nonexistent shipment: " + e.getMessage());
        }

        System.out.println("=== Test complete ===");
    }

    public static List<TOShipment> getAllShipments() {
        List<TOShipment> shipments = new ArrayList<>();
        for (Shipment s : GroceryManagementSystemController.getGroceryManagementSystem().getShipments()) {
            shipments.add(TOConverter.convert(s));

        }
        return shipments;
    }
}
