package ca.mcgill.ecse.grocerymanagementsystem.application;

import java.sql.Date;
import ca.mcgill.ecse.grocerymanagementsystem.controller.*;
import ca.mcgill.ecse.grocerymanagementsystem.model.*;

public class GroceryApplication {
	public static void main(String[] args) {
		GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
		system.delete();

		// === Shared setup ===
		User customerUser = new User("john_user", "password", "John", "1234567890", system);
		System.out.println("Listing out current users in the system");
		for (User user : system.getUsers()) {
			System.out.println("Name " + user.getName());
		}
		Customer customer = new Customer(customerUser, "123 Main St", 1000, system);
		System.out.println("Listing out current customers in the system");
		for (Customer systemCustomer : system.getCustomers()) {
			System.out.println("Name " + systemCustomer.getUser().getName());
		}

		User employeeUser = new User("jane_staff", "secure", "Jane", "0987654321", system);
		Employee employee = new Employee(employeeUser, system);
		System.out.println("Listing out current employees in the system");
		for (Employee systemEmployee : system.getEmployees()) {
			System.out.println("Name " + systemEmployee.getUser().getName());
		}

		Item apple = new Item("Apple", 0, 150, false, 1, system);
		Item banana = new Item("Banana", 0, 100, true, 1, system);

		System.out.println("Listing out current Items in the system");
		for (Item systemItem : system.getItems()) {
			System.out.println("Name " + systemItem.getName());
		}

		System.out.println("=== TESTING ALL CONTROLLERS ===");

		testItemController(system);
		testOrderController(system);
		testShipmentController(system);
		System.out.println("Testing perishable item");
		testOrderProcessingController(system, customer, employee, banana);

		System.out.println("=== ALL TESTS COMPLETE ===");
	}

	private static void testItemController(GroceryManagementSystem system) {
		System.out.println("---- Testing ItemController ----");
		try {
			ItemController.create("Milk", true, 3, 250);
			ItemController.updatePrice("Milk", 300);
			ItemController.updatePoints("Milk", 4);
			ItemController.delete("Milk");
			System.out.println("ItemController tests passed.");
		} catch (GroceryStoreException e) {
			System.err.println("ItemController error: " + e.getMessage());
		}
	}

	private static void testOrderController(GroceryManagementSystem system) {
		System.out.println("---- Testing OrderController ----");

		try {
			OrderController.createOrder("john_user", Order.DeliveryDeadline.SameDay);
			int orderNumber = system.getOrders().get(0).getOrderNumber();

			OrderController.addItemToOrder(orderNumber, "Apple");
			OrderController.updateQuantityInOrder(orderNumber, "Apple", 3);
			OrderController.updateQuantityInOrder(orderNumber, "Apple", 0);
			OrderController.deleteOrder(orderNumber);

			System.out.println("OrderController tests passed.");
		} catch (GroceryStoreException e) {
			System.err.println("OrderController error: " + e.getMessage());
		}
	}

	private static void testShipmentController(GroceryManagementSystem system) {
		System.out.println("---- Testing ShipmentController ----");
		try {
			ShipmentController.createShipment();
			int shipmentNumber = system.getShipments().get(0).getShipmentNumber();

			ShipmentController.addItemToShipment(shipmentNumber, "Banana");
			ShipmentController.updateQuantityInShipment(shipmentNumber, "Banana", 5);
			ShipmentController.updateQuantityInShipment(shipmentNumber, "Banana", 0);
			ShipmentController.deleteShipment(shipmentNumber);
			System.out.println("ShipmentController tests passed.");
		} catch (GroceryStoreException e) {
			System.err.println("ShipmentController error: " + e.getMessage());
		}
	}

	private static void testOrderProcessingController(GroceryManagementSystem system, Customer customer,
			Employee employee, Item item) {
		System.out.println("---- Testing OrderProcessingController ----");

		Date today = new Date(System.currentTimeMillis());
		Order order = new Order(null, Order.DeliveryDeadline.InThreeDays, system, customer);
		int orderNumber = order.getOrderNumber();
		new OrderItem(5, system, order, item);

		try {
			OrderProcessingController.checkOut(orderNumber);
			System.out.println("Checkout successful. State: " + order.getStateOrder());

			OrderProcessingController.payForOrder(orderNumber, true);
			System.out.println("Payment successful.");

			OrderProcessingController.assignOrderToEmployee(orderNumber, "jane_staff");
			System.out.println("Assigned to: " + order.getOrderAssignee().getUser().getUsername());

			OrderProcessingController.finishOrderAssembly(orderNumber);
			System.out.println("Assembly finished. State: " + order.getStateOrder());

			OrderProcessingController.deliverOrder(orderNumber);
			System.out.println("Delivery Attempted. State: " + order.getStateOrder());

			System.out.println("OrderProcessingController tests passed.");
		} catch (GroceryStoreException e) {
			System.err.println("OrderProcessingController error: " + e.getMessage());
		}
	}
}
