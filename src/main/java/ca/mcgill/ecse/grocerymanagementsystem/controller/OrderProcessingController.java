package ca.mcgill.ecse.grocerymanagementsystem.controller;

import java.sql.Date;
import java.util.*;
import ca.mcgill.ecse.grocerymanagementsystem.model.*;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.DeliveryDeadline;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.StateOrder;

public class OrderProcessingController {
	public static void checkOut(int orderNumber) {
		Order order = getOrderByNumber(orderNumber);
		if (!order.hasOrderItems()) {
			throw new GroceryStoreException("cannot check out an empty order");
		}
		if (order.getStateOrder() != StateOrder.UnderConstruction) {
			throw new GroceryStoreException("order has already been checked out");
		}
		order.checkout();
		// System.err.println("State after checkout: " + order.getStateOrder());
	}

	public static void payForOrder(int orderNumber, boolean usePoints) {
		Order order = getOrderByNumber(orderNumber);
		if (order.getStateOrder() == StateOrder.Cancelled){
			throw new GroceryStoreException("cannot pay for an order which has been cancelled");
		}
		if (order.getStateOrder() == StateOrder.UnderConstruction){
			throw new GroceryStoreException("cannot pay for an order which has not been checked out");
		}
		if (order.getStateOrder().ordinal() >= StateOrder.Placed.ordinal()){
			throw new GroceryStoreException("cannot pay for an order which has already been paid for");
		}
		//to update cost
		order.checkout();
		Customer customer = order.getOrderPlacer();

		
		

		if (order.getDatePlaced() != null) {
			throw new GroceryStoreException("order has already been paid");
		}

		if (order.getOrderItems().size() == 0) {
			throw new GroceryStoreException("cannot pay for an empty order");
		}
		// checks if insufficient stock item
		for (OrderItem orderItem : order.getOrderItems()) {
			int quantity = orderItem.getQuantity();
			Item item = orderItem.getItem();
			int newQuantity = item.getQuantityInInventory() - quantity;
			if (newQuantity < 0) {
				throw new GroceryStoreException("insufficient stock of item \"" + item.getName() + "\"");
			}
		}

		if (usePoints) {
			;
			order.payWithPoints(customer);
		}
		order.setPricePaid(order.getTotalCost());
		order.pay();
	}

	public static void assignOrderToEmployee(int orderNumber, String employeeUsername) {
		Order order = getOrderByNumber(orderNumber);
		StateOrder currentState = order.getStateOrder();
		if (currentState == StateOrder.Cancelled) {
			throw new GroceryStoreException("cannot assign employee to an order that has been cancelled");
		}
		if (currentState.ordinal() < StateOrder.Placed.ordinal()) {
			throw new GroceryStoreException("cannot assign employee to order that has not been placed");
		}
		if (currentState.ordinal() >= StateOrder.Placed.ReadyForDelivery.ordinal()) {
			throw new GroceryStoreException("cannot assign employee to an order that has already been prepared");
		}
		if (order.getDatePlaced() == null) {
			throw new GroceryStoreException("order has not been paid yet");
		}

		User user = getUserByName(employeeUsername);
		if (user == null) {
			throw new GroceryStoreException("there is no user with username \"" + employeeUsername + "\"");
		}
		Employee employee = getEmployeeByUsername(employeeUsername);

		if (employee == null) {
			throw new GroceryStoreException("\"" + employeeUsername + "\" is not an employee");
		}
		// System.err.println("State before assignment: " + order.getStateOrder());

		order.assignEmployee(employee);

	}

	public static void finishOrderAssembly(int orderNumber) {
		Order order = getOrderByNumber(orderNumber);
		StateOrder orderState = order.getStateOrder();
		if (orderState == StateOrder.Cancelled) {
			throw new GroceryStoreException("cannot finish assembling order because it was cancelled");
		}
		if (orderState == StateOrder.ReadyForDelivery || orderState == StateOrder.Delivered) {
			throw new GroceryStoreException("cannot finish assembling order that has already been assembled");
		}
		if (order.getOrderAssignee() == null) {
			throw new GroceryStoreException(
					"cannot finish assembling order because it has not been assigned to an employee");
		}

		if (order.canFinishAssembly()) {
			order.finishAssembly();
		} else {
			throw new GroceryStoreException(
					"cannot finish assembling an order with perishable items before the deadline");
		}
	}

	public static void deliverOrder(int orderNumber) {
		Order order = getOrderByNumber(orderNumber);

		if (order.getStateOrder().ordinal() < StateOrder.ReadyForDelivery.ordinal()
				|| order.getStateOrder() == StateOrder.Cancelled) {
			throw new GroceryStoreException("cannot mark an order as delivered if it is not ready for delivery");
		}
		if (!order.pastDeadline()) {
			throw new GroceryStoreException("cannot mark order as delivered before the delivery date");
		}

		order.deliver();
	}

	public static void cancelOrder(int orderNumber) {
		Order order = getOrderByNumber(orderNumber);
		StateOrder orderState = order.getStateOrder();
		if (orderState.equals(StateOrder.Cancelled)) {
			throw new GroceryStoreException("order was already cancelled");
		}
		if (order.getOrderAssignee() != null) {
			throw new GroceryStoreException("cannot cancel an order that has already been assigned to an employee");
		}

		order.cancel();
	}

	private static Order getOrderByNumber(int orderNumber) {
		GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
		List<Order> orders = system.getOrders();
		for (Order order : orders) {
			if (order.getOrderNumber() == orderNumber) {
				return order;
			}
		}
		return null;
	}

	private static Employee getEmployeeByUsername(String employeeUsername) {
		GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
		List<Employee> employees = system.getEmployees();
		for (Employee employee : employees) {
			if (employee.getUser().getUsername().equals(employeeUsername)) {
				return employee;
			}
		}
		return null;
	}

	private static User getUserByName(String employeeUsername) {
		GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
		List<User> users = system.getUsers();
		for (User user : users) {
			if (user.getUsername().equals(employeeUsername)) {
				return user;
			}
		}
		return null;
	}
}