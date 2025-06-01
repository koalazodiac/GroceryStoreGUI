package ca.mcgill.ecse.grocerymanagementsystem.feature;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryManagementSystemController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentController;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import ca.mcgill.ecse.grocerymanagementsystem.model.*;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.StateOrder;

import static org.junit.jupiter.api.Assertions.*;

public class OrderStepDefinitions extends StepDefinitions {

	private Order currentOrder;

	@Before
	public void before() {
		super.before();
		orderByTestId.clear();
	}

	@Given("the following orders exist in the system")
	public void the_following_orders_exist_in_the_system(List<Map<String, String>> orders) {
		GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();

		for (Map<String, String> row : orders) {
			// Extract and handle datePlaced (convert "NULL" to null)
			String dateStr = row.get("datePlaced");
			Date datePlaced = null;
			if (dateStr != null && !"NULL".equalsIgnoreCase(dateStr)) {
				switch (dateStr.toLowerCase()) {
					case "today":
						datePlaced = Date.valueOf(LocalDate.now());
						break;
					case "yesterday":
						datePlaced = Date.valueOf(LocalDate.now().minusDays(1));
						break;
					case "two days ago":
						datePlaced = Date.valueOf(LocalDate.now().minusDays(2));
						break;
					case "three days ago":
						datePlaced = Date.valueOf(LocalDate.now().minusDays(3));
						break;
					default:
						// Validate format
						if (!dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
							throw new IllegalArgumentException(
									"Invalid date format: " + dateStr
											+ ". Expected format: yyyy-MM-dd, or keywords like 'today', 'yesterday', 'two days ago', 'three days ago'.");
						}
						datePlaced = Date.valueOf(dateStr);
						break;
				}
			}
			// Extract deadline and convert it to enum
			Order.DeliveryDeadline deadline = Order.DeliveryDeadline.valueOf(row.get("deadline"));

			// Retrieve customer username and validate existence
			String customerUsername = row.get("customer");
			User user = User.getWithUsername(customerUsername);
			assertNotNull(user, "User " + customerUsername + " does not exist");

			// Retrieve the customer role
			Customer customer = user.getRoles().stream()
					.filter(role -> role instanceof Customer)
					.map(role -> (Customer) role)
					.findFirst()
					.orElse(null);
			assertNotNull(customer, "User " + customerUsername + " is not a customer");

			// Create the order (automatically assigns a unique order number)
			Order order = new Order(datePlaced, deadline, system, customer);
			String state = row.get("state");
			StateOrder orderStatus;
			switch (state.trim().toLowerCase()) {
				case "under construction":
					orderStatus = StateOrder.UnderConstruction;
					break;
				case "pending":
					orderStatus = StateOrder.Pending;
					break;
				case "placed":
					orderStatus = StateOrder.Placed;
					break;
				case "in preparation":
					orderStatus = StateOrder.InPreparation;
					break;
				case "ready for delivery":
					orderStatus = StateOrder.ReadyForDelivery;
					break;
				case "delivered":
					orderStatus = StateOrder.Delivered;
					break;
				case "cancelled":
					orderStatus = StateOrder.Cancelled;
					break;
				default:
					throw new IllegalArgumentException("Unrecognized order state: \"" + state + "\"");
			}
			order.setStatusOnlyForTesting(orderStatus);
			order.setOrderPlacer(customer);

			String employeeString = row.get("assignee");
			for (Employee employee : system.getEmployees()) {
				if (employee.getUser().getUsername().equals(employeeString)) {
					order.setOrderAssignee(employee);
				}
			}

			String dateString = row.get("datePlaced");
			Date dateDate;
			switch (dateString.trim().toLowerCase()) {
				case "today":
					dateDate = new Date(System.currentTimeMillis());
					break;
				case "yesterday":
					dateDate = new Date(System.currentTimeMillis() - 1L * 24 * 60 * 60 * 1000);
					break;
				case "two days ago":
					dateDate = new Date(System.currentTimeMillis() - 2L * 24 * 60 * 60 * 1000);
					break;
				case "three days ago":
					dateDate = new Date(System.currentTimeMillis() - 3L * 24 * 60 * 60 * 1000);
					break;
				case "null":
					dateDate = null;
					break;
				default:
					try {
						dateDate = java.sql.Date.valueOf(dateString.trim()); // handles yyyy-MM-dd
					} catch (IllegalArgumentException e) {
						throw new IllegalArgumentException("Unrecognized date: \"" + dateString + "\"");
					}
			}
			order.setDatePlaced(dateDate);

			// Map test ID to the actual order number
			String testId = row.get("id");
			orderByTestId.put(testId, order);
		}

	}

	@Given("the following items are part of orders")
	public void the_following_items_are_part_of_orders(List<Map<String, String>> orderItems) {
		// get the system
		GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();

		// traverse teh orderItem
		for (Map<String, String> row : orderItems) {
			String testOrderId = row.get("order");
			String itemName = row.get("item");
			int quantity = Integer.parseInt(row.get("quantity"));

			// Retrieve the order
			Order order = orderByTestId.get(testOrderId);
			assertNotNull(order, "Order with ID " + testOrderId + " not found");

			// Retrieve the item from the system
			Item item = system.getItems().stream()
					.filter(i -> i.getName().equals(itemName))
					.findFirst()
					.orElse(null);
			assertNotNull(item, "Item \"" + itemName + "\" not found");

			// Add the item to the order
			new OrderItem(quantity, system, order, item);
		}
	}

	@When("{string} attempts to create an order with deadline {string}")
	public void attempts_to_create_an_order_with_deadline(String name, String deadline) {
		try {
			// Capture order numbers before creating
			Set<Integer> existingOrderNumbers = new HashSet<>();
			for (Order order : getSystem().getOrders()) {
				existingOrderNumbers.add(order.getOrderNumber());
			}

			// Create new order
			Order.DeliveryDeadline deadline1 = deadline.equals("NULL") ? null
					: Order.DeliveryDeadline.valueOf(deadline);
			OrderController.createOrder(name, deadline1);

			// Find newly created order by comparing order numbers
			for (Order order : getSystem().getOrders()) {
				if (!existingOrderNumbers.contains(order.getOrderNumber())) {
					this.currentOrder = order;
					break;
				}
			}
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to delete the order with ID {string}")
	public void the_user_attempts_to_delete_the_order_with_id(String id) {
		try {
			Order order = orderByTestId.get(id);
			if (order == null)
				throw new GroceryStoreException("there is no order with ID \"" + id + "\"");

			OrderController.deleteOrder(order.getOrderNumber());
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to delete the non-existent order with order number {int}")
	public void the_user_attempts_to_delete_the_non_existent_order_with_order_number(Integer orderNumber) {
		try {
			OrderController.deleteOrder(orderNumber);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to add item {string} to the order with ID {string}")
	public void the_user_attempts_to_add_item_to_the_order_with_id(String item, String orderId) {
		Integer id = orderByTestId.containsKey(orderId) ? orderByTestId.get(orderId).getOrderNumber() : null;

		try {
			OrderController.addItemToOrder(id, item);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to add item {string} to the non-existent order with order number {int}")
	public void the_user_attempts_to_add_item_to_the_non_existent_order_with_order_number(String item,
			Integer orderNumber) {
		try {
			OrderController.addItemToOrder(orderNumber, item);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to set the quantity of item {string} in the order with ID {string} to {int}")
	public void the_user_attempts_to_set_the_quantity_of_item_in_the_order_with_id_to(String item, String orderId,
			int newQuantity) {
		Integer id = orderByTestId.containsKey(orderId) ? orderByTestId.get(orderId).getOrderNumber() : null;

		try {
			OrderController.updateQuantityInOrder(id, item, newQuantity);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to set the quantity of item {string} in the non-existent order {int} to {int}")
	public void the_user_attempts_to_set_the_quantity_of_item_in_the_nonexistent_order_to(String item, int orderNumber,
			int newQuantity) {
		try {
			OrderController.updateQuantityInOrder(orderNumber, item, newQuantity);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@Then("the total number of orders shall be {int}")
	public void the_total_number_of_orders_shall_be(Integer n) {
		int actual = getSystem().numberOfOrders();
		assertEquals(n, actual);
	}

	@Then("{string} shall have a new order")
	public void shall_have_a_new_order(String name) {
		// Retrieve the user from the system
		User user = User.getWithUsername(name);
		assertNotNull(user, "User " + name + " does not exist");

		// Retrieve the customer's Customer role
		Customer customer = user.getRoles().stream()
				.filter(role -> role instanceof Customer)
				.map(role -> (Customer) role)
				.findFirst()
				.orElse(null);
		assertNotNull(customer, "User " + name + " is not a customer");

		// Ensure the customer has at least one order
		assertTrue(customer.numberOfOrdersPlaced() > 0, "User " + name + " does not have a new order");

	}

	@Then("an order shall exist with ID {string}")
	public void an_order_shall_exist_with_id(String id) {
		Boolean expected = true;
		Boolean result = false;
		int OrderNumber = orderByTestId.containsKey(id) ? orderByTestId.get(id).getOrderNumber() : null;
		for (Order order : getSystem().getOrders()) {
			if (order.getOrderNumber() == OrderNumber) {
				result = true;
			}

		}
		assertEquals(expected, result);
	}

	@Then("no order shall exist with ID {string}")
	public void no_order_shall_exist_with_id(String id) {
		Boolean expected = false;
		Boolean result = false;
		int OrderNumber = orderByTestId.containsKey(id) ? orderByTestId.get(id).getOrderNumber() : null;
		for (Order order : getSystem().getOrders()) {
			if (order.getOrderNumber() == OrderNumber) {
				result = true;
			}

		}
		assertEquals(expected, result);
	}

	@Then("no order shall exist with order number {int}")
	public void no_order_shall_exist_with_order_number(Integer orderNumber) {
		Boolean expected = false;
		Boolean result = false;
		for (Order order : getSystem().getOrders()) {
			if (order.getOrderNumber() == orderNumber) {
				result = true;
			}
		}
		assertEquals(expected, result);
	}

	@Then("the newly-created order shall have deadline {string}")
	public void the_newly_created_order_shall_have_deadline(String deadline) {
		// Convert expected deadline to enum
		Order.DeliveryDeadline expected = Order.DeliveryDeadline.valueOf(deadline);

		// Use the currentOrder that was set in the
		// attempts_to_create_an_order_with_deadline method
		assertNotNull(this.currentOrder, "No current order exists");

		// Check if the deadline matches
		assertEquals(expected, this.currentOrder.getDeadline(),
				"The newly-created order does not have the expected deadline");
	}

	@Then("the newly-created order shall have {int} items")
	public void the_newly_created_order_shall_have_items(Integer n) {
		int actual = this.currentOrder.numberOfOrderItems();
		assertEquals(n, actual);
	}

	@Then("the newly-created order shall not have been placed")
	public void the_newly_created_order_shall_not_have_been_placed() {
		assertNull(this.currentOrder.getDatePlaced());
	}

	@Then("the order with ID {string} shall include {int} {string}")
	public void the_order_with_id_shall_include(String orderId, Integer quantity, String item) {
		boolean expected = true;
		boolean result = false;

		// Retrieve the order using the test ID
		Order order = orderByTestId.get(orderId);
		assertNotNull(order, "Order with ID " + orderId + " does not exist");

		// Iterate through the items in the order
		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem.getItem().getName().equals(item)) {
				if (orderItem.getQuantity() == quantity) {
					result = true;
					break;
				}
			}
		}

		// Check if the item with the expected quantity exists in the order
		assertEquals(expected, result);
	}

	@Then("the order with ID {string} shall not include any items called {string}")
	public void the_order_with_id_shall_not_include_any_items_called(String orderId, String item) {
		boolean expected = false;
		boolean result = false;

		// Retrieve the order using the test ID
		Order order = orderByTestId.get(orderId);
		assertNotNull(order, "Order with ID " + orderId + " does not exist");

		// Check if the order contains the specified item
		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem.getItem().getName().equals(item)) {
				result = true;
				break;
			}
		}

		// Assert that the item is NOT present in the order
		assertEquals(expected, result);
	}

	@Then("the order with ID {string} shall include {int} distinct item(s)")
	public void the_order_with_id_shall_include_distinct_items(String orderId, Integer n) {
		boolean result = false;

		// Retrieve the order using the test ID
		Order order = orderByTestId.get(orderId);
		assertNotNull(order, "Order with ID " + orderId + " does not exist");

		// Get the number of distinct items in the order
		int actualCount = order.numberOfOrderItems();

		if (actualCount == n) {
			result = true;
		}

		assertEquals(true, result);
	}
}
