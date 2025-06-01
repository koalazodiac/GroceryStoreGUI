package ca.mcgill.ecse.grocerymanagementsystem.controller;

import ca.mcgill.ecse.grocerymanagementsystem.model.*;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.DeliveryDeadline;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.StateOrder;

import java.util.List;
import java.util.stream.Collectors;


public class OrderController {
	public static void createOrder(String creatorUsername, DeliveryDeadline deadline) {
		validateCustomerUsername(creatorUsername);
		validateDeliveryDeadline(deadline);

		Customer creator = getCustomerByUsername(creatorUsername);
		Order order = new Order(null, deadline, GroceryManagementSystemController.getGroceryManagementSystem(),
				creator);
	}

	public static void deleteOrder(int orderNumber) {
		Order order = getOrderByNumber(orderNumber);
		if (order.getDatePlaced() != null) {
			throw new GroceryStoreException("cannot delete an order which has already been placed");

		}
		order.delete();

	}

	public static void addItemToOrder(int orderNumber, String itemName) {
		Order order = getOrderByNumber(orderNumber);
		if (order.getStateOrder() == StateOrder.Pending) {
			throw new GroceryStoreException("order has already been checked out");
		}
		if (order.getStateOrder() != StateOrder.UnderConstruction) {
			throw new GroceryStoreException("order has already been placed");
		}
		Item item = getItemByName(itemName);
		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem.getItem().equals(item)) {
				throw new GroceryStoreException("order already includes item \"" + itemName + "\"");

			}
		}
		if (item.getQuantityInInventory() == 0) {
			throw new GroceryStoreException("item \"" + item.getName() + "\" is out of stock");
		}
		new OrderItem(1, GroceryManagementSystemController.getGroceryManagementSystem(), order, item);
	}

	public static void updateQuantityInOrder(int orderNumber, String itemName, int newQuantity) {
		if (newQuantity < 0) {
			throw new GroceryStoreException("quantity must be non-negative");
		}
		if (newQuantity > 10) {
			throw new GroceryStoreException("quantity cannot exceed 10");
		}

		Order order = getOrderByNumber(orderNumber);

		if (order.getStateOrder() == StateOrder.Pending || order.getStateOrder() == StateOrder.Cancelled) {
			throw new GroceryStoreException("order has already been checked out");
		}
		if (order.getStateOrder().ordinal() >= StateOrder.Placed.ordinal()) {
			throw new GroceryStoreException("order has already been placed");
		}
		if (order.getDatePlaced() != null) {
			throw new GroceryStoreException("order has already been placed");

		}
		Item item = getItemByName(itemName);
		OrderItem targetOrderItem = null;
		for (OrderItem oi : order.getOrderItems()) {
			if (oi.getItem().equals(item)) {
				targetOrderItem = oi;
				break;
			}
		}
		if (targetOrderItem == null) {
			throw new GroceryStoreException("order does not include item \"" + itemName + "\"");
		}
		if (newQuantity == 0) {
			targetOrderItem.delete();
		} else {
			targetOrderItem.setQuantity(newQuantity);
		}
	}

	private static void validateCustomerUsername(String username) {
		if (username == null || username.isBlank() || username.equals("NULL")) {
			throw new GroceryStoreException("customer is required");
		}
	}

	private static void validateDeliveryDeadline(Order.DeliveryDeadline deadline) {
		if (deadline == null) {
			throw new GroceryStoreException("delivery deadline is required");
		}
	}

	private static Customer getCustomerByUsername(String username) {
		User user = User.getWithUsername(username);
		if (user == null) {
			throw new GroceryStoreException("there is no user with username \"" + username + "\"");
		}
		for (UserRole role : user.getRoles()) {
			if (role instanceof Customer) {
				return (Customer) role;
			}
		}
		throw new GroceryStoreException("\"" + username + "\" is not a customer");
	}

	private static Order getOrderByNumber(int orderNumber) {
		GroceryManagementSystem sys = GroceryManagementSystemController.getGroceryManagementSystem();
		for (Order order : sys.getOrders()) {
			if (order.getOrderNumber() == orderNumber) {
				return order;
			}
		}
		throw new GroceryStoreException("there is no order with number \"" + orderNumber + "\"");
	}

	private static Item getItemByName(String name) {
		Item item = Item.getWithName(name);
		if (item == null) {
			throw new GroceryStoreException("there is no item called \"" + name + "\"");

		}
		return item;
	}
	public static List<TOOrder> getAllOrders() {
		return GroceryManagementSystemController
				.getGroceryManagementSystem()
				.getOrders().stream()
				.map(TOConverter::convert)
				.collect(Collectors.toList());
	}
	public static boolean existOrder(int orderNumber) {
		GroceryManagementSystem sys = GroceryManagementSystemController.getGroceryManagementSystem();
		for (Order o : sys.getOrders()) {
			if (o.getOrderNumber() == orderNumber) {
				return true;
			}
		}
		return false;
	}
}
