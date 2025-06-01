package ca.mcgill.ecse.grocerymanagementsystem.feature;

import java.util.HashMap;
import java.util.Map;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryManagementSystemController;
import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.model.Customer;
import ca.mcgill.ecse.grocerymanagementsystem.model.Employee;
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Order;

public class StepDefinitions {
	/**
	 * Set this field in <code>@When</code> steps if an error was raised.
	 */
	static GroceryStoreException error;
	protected static Customer currentCustomer;
	protected static Employee currentEmployee;

	// Track orders by test alias
	protected static Map<String, Order> orderByTestId = new HashMap<>();

	protected void before() {
		GroceryManagementSystemController.resetSystem();
		orderByTestId.clear();
		error = null;
	}

	protected GroceryManagementSystem getSystem() {
		return GroceryManagementSystemController.getGroceryManagementSystem();
	}
}
