package ca.mcgill.ecse.grocerymanagementsystem.feature;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ItemController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.OrderProcessingController;

import ca.mcgill.ecse.grocerymanagementsystem.model.Order;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order.StateOrder;
import ca.mcgill.ecse.grocerymanagementsystem.model.Customer;
import ca.mcgill.ecse.grocerymanagementsystem.model.Employee;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OrderProcessingStepDefinitions extends StepDefinitions {
	private Order currentOrder;

	@Before
	public void before() {
		super.before();
		this.currentOrder = null;
	}

	@When("the user attempts to check out the order with ID {string}")
	public void the_user_attempts_to_check_out_the_order_with_id(String orderId) {

		currentOrder = orderByTestId.get(orderId);
		try {
			OrderProcessingController.checkOut(currentOrder.getOrderNumber());
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the user attempts to pay for the order with ID {string} {string} their points")
	public void the_user_attempts_to_pay_for_the_order_with_id_without_using_their_points(String orderId,
			String usingOrWithoutUsing) {
		currentOrder = orderByTestId.get(orderId);
		StepDefinitions.currentCustomer = currentOrder.getOrderPlacer();
		Boolean using = false;
		if (usingOrWithoutUsing.equals("using")) {
			using = true;
		}
		try {
			OrderProcessingController.payForOrder(currentOrder.getOrderNumber(), using);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}

	}

	@When("the manager attempts to assign the order with ID {string} to {string}")
	public void the_manager_attempts_to_assign_the_order_with_id_to(String orderId, String employeeUsername) {
		currentOrder = orderByTestId.get(orderId);
		try {
			OrderProcessingController.assignOrderToEmployee(currentOrder.getOrderNumber(), employeeUsername);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}

	}

	@When("the user attempts to indicate that assembly of the order with ID {string} is finished")
	public void the_user_attempts_to_indicate_that_assembly_of_the_order_with_id_is_finished(String orderId) {
		currentOrder = orderByTestId.get(orderId);

		try {
			OrderProcessingController.finishOrderAssembly(currentOrder.getOrderNumber());
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}

	}

	@When("the user attempts to cancel the order with ID {string}")
	public void the_user_attempts_to_cancel_the_order_with_id(String orderId) {
		currentOrder = orderByTestId.get(orderId);

		try {
			OrderProcessingController.cancelOrder(currentOrder.getOrderNumber());
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}

	}

	@When("the manager attempts to mark the order with ID {string} as delivered")
	public void the_manager_attempts_to_mark_the_order_with_id_as_delivered(String orderId) {
		currentOrder = orderByTestId.get(orderId);
		try {
			OrderProcessingController.deliverOrder(currentOrder.getOrderNumber());
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}

	}

	@Then("the order shall be {string}")
	public void the_order_shall_be(String expectedState) {
		assertNotNull(currentOrder, "Current order is null");

		StateOrder expectedEnum;

		switch (expectedState.trim().toLowerCase()) {
			case "under construction":
				expectedEnum = StateOrder.UnderConstruction;
				break;
			case "pending":
				expectedEnum = StateOrder.Pending;
				break;
			case "placed":
				expectedEnum = StateOrder.Placed;
				break;
			case "in preparation":
				expectedEnum = StateOrder.InPreparation;
				break;
			case "ready for delivery":
				expectedEnum = StateOrder.ReadyForDelivery;
				break;
			case "delivered":
				expectedEnum = StateOrder.Delivered;
				break;
			case "cancelled":
				expectedEnum = StateOrder.Cancelled;
				break;
			default:
				throw new IllegalArgumentException("Unrecognized order state: \"" + expectedState + "\"");
		}

		assertEquals(expectedEnum, currentOrder.getStateOrder(), "Order state mismatch");
	}

	@Then("the order's placer shall be {string}")
	public void the_order_s_placer_shall_be(String customerUsername) {
		assertNotNull(currentOrder);
		Customer customer = currentOrder.getOrderPlacer();
		assertNotNull(customer);
		assertEquals(customerUsername, customer.getUser().getUsername());
	}

	@Then("the order's assignee shall be {string}")
	public void the_order_s_assignee_shall_be(String employeeUsername) {
		assertNotNull(currentOrder);
		if (employeeUsername.equals("NULL")) {
			assertNull(currentOrder.getOrderAssignee());
		} else {
			Employee employee = currentOrder.getOrderAssignee();
			assertNotNull(employee);
			assertEquals(employeeUsername, employee.getUser().getUsername());
		}
	}

	@Then("the order's date placed shall be today")
	public void the_order_s_date_placed_shall_be_today() {
		assertNotNull(currentOrder);
		assertEquals(LocalDate.now(), currentOrder.getDatePlaced().toLocalDate());
	}

	@Then("the total cost of the order shall be {int} cents")
	public void the_total_cost_of_the_order_shall_be_cents(Integer expectedCost) {
		assertNotNull(currentOrder);
		assertEquals(expectedCost.intValue(), currentOrder.getTotalCost());
	}

	@Then("the final cost of the order, after considering points, shall be {int} cents")
	public void the_final_cost_of_the_order_after_considering_points_shall_be_cents(Integer expectedCost) {
		assertNotNull(currentOrder);
		assertEquals(expectedCost.intValue(), currentOrder.getTotalCost());
	}
}
