package ca.mcgill.ecse.grocerymanagementsystem.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse.grocerymanagementsystem.controller.GroceryStoreException;
import ca.mcgill.ecse.grocerymanagementsystem.controller.ShipmentController;
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.Shipment;
import ca.mcgill.ecse.grocerymanagementsystem.model.ShipmentItem;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ShipmentStepDefinitions extends StepDefinitions {
	private Map<String, Integer> shipmentIdToNumber;
	private Shipment currentShipment;

	@Before
	public void before() {
		super.before();
		this.shipmentIdToNumber = new HashMap<>();
		this.currentShipment = null;
	}

	@Given("the following shipments exist")
	public void the_following_shipments_exist(List<Map<String, String>> shipments) {
		for (Map<String, String> row : shipments) {
			String dateStr = row.containsKey("datePlaced") ? row.get("datePlaced") : row.get("dateOrdered");
			Date date = "NULL".equals(dateStr)
					? null
					: Date.valueOf(dateStr);
			Shipment s = new Shipment(date, getSystem());
			String id = row.get("id");
			int shipmentNumber = s.getShipmentNumber();
			this.shipmentIdToNumber.put(id, shipmentNumber);
		}
	}

	// newly added， not tested
	@Given("the following items are part of shipments")
	public void the_following_items_are_part_of_shipments(List<Map<String, String>> shipmentItems) {
		for (Map<String, String> row : shipmentItems) {
			String quantityStr = row.get("quantity");
			Integer quantity = "NULL".equals(quantityStr)
					? null
					: Integer.valueOf(quantityStr);
			Integer shipmentID = shipmentIdToNumber.get(row.get("shipment"));
			List<Shipment> lShipments = getSystem().getShipments();
			Shipment shipment = null;
			for (Shipment s : lShipments) {
				if (s.getShipmentNumber() == shipmentID) {
					shipment = s;
				}
			}
			String itemStr = row.get("item");
			Item item = "NULL".equals(itemStr)
					? null
					: Item.getWithName(itemStr);
			getSystem().addShipmentItem(quantity, shipment, item);
		}
	}

	@When("the manager attempts to create a new shipment")
	public void the_manager_attempts_to_create_a_new_shipment() {
		try {
			ShipmentController.createShipment();
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to delete the shipment with ID {string}")
	public void the_manager_attempts_to_delete_the_shipment_with_id(String id) {
		int shipmentNumber = shipmentIdToNumber.get(id);
		try {
			ShipmentController.deleteShipment(shipmentNumber);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to delete the non-existent shipment with shipment number {int}")
	public void the_manager_attempts_to_delete_the_non_existent_shipment_with_shipment_number(Integer shipmentNumber) {
		try {
			ShipmentController.deleteShipment(shipmentNumber);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to add item {string} to the shipment with ID {string}")
	public void the_manager_attempts_to_add_item_to_the_shipment_with_id(String item, String shipmentId) {
		Integer id = this.shipmentIdToNumber.get(shipmentId);
		try {
			ShipmentController.addItemToShipment(id, item);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}

	}

	@When("the user attempts to add item {string} to the non-existent shipment with number {int}")
	public void the_user_attempts_to_add_item_to_the_non_existent_shipment_with_number(String item,
			Integer shipmentNumber) {
		try {
			ShipmentController.addItemToShipment(shipmentNumber, item);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to set the quantity of item {string} in the shipment with ID {string} to {int}")
	public void the_manager_attempts_to_set_the_quantity_of_item_in_the_shipment_with_id_to(String item,
			String shipmentId, Integer quantity) {
		int id = this.shipmentIdToNumber.get(shipmentId);
		try {
			ShipmentController.updateQuantityInShipment(id, item, quantity);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@When("the manager attempts to set the quantity of item {string} in the non-existent shipment {int} to {int}")
	public void the_manager_attempts_to_set_the_quantity_of_item_in_the_non_existent_shipment_to(String item,
			Integer shipmentNumber, Integer quantity) {
		try {
			ShipmentController.updateQuantityInShipment(shipmentNumber, item, quantity);
		} catch (GroceryStoreException e) {
			StepDefinitions.error = e;
		}
	}

	@Then("a new shipment shall exist")
	public void a_new_shipment_shall_exist() {
		List<Shipment> newShipments = new ArrayList<>();
		for (Shipment s : getSystem().getShipments()) {
			boolean isNew = !shipmentIdToNumber.containsValue(s.getShipmentNumber());
			if (isNew) {
				newShipments.add(s);
			}
		}
		assertEquals(1, newShipments.size());
		this.currentShipment = newShipments.get(0);
	}

	@Then("no shipment shall exist with ID {string}")
	public void no_shipment_shall_exist_with_id(String id) {
		Boolean expected = false;
		Boolean result = false;
		int sID = this.shipmentIdToNumber.get(id);
		for (Shipment s : getSystem().getShipments()) {
			if (s.getShipmentNumber() == sID) {
				result = true;
			}
		}
		assertEquals(expected, result);
	}

	@Then("no shipment shall exist with number {int}")
	public void no_shipment_shall_exist_with_number(Integer shipmentNumber) {
		Boolean expected = false;
		Boolean result = false;
		for (Shipment s : getSystem().getShipments()) {
			if (s.getShipmentNumber() == shipmentNumber) {
				result = true;
			}
		}
		assertEquals(expected, result);
	}

	@Then("a shipment shall exist with ID {string}")
	public void a_shipment_shall_exist_with_id(String id) {
		Boolean expected = true;
		Boolean result = false;
		int sID = this.shipmentIdToNumber.get(id);
		for (Shipment s : getSystem().getShipments()) {
			if (s.getShipmentNumber() == sID) {
				result = true;
			}
		}
		assertEquals(expected, result);
	}

	@Then("no shipment shall exist with shipment number {int}")
	public void no_shipment_shall_exist_with_shipment_number(Integer shipmentNumber) {
		Boolean expected = false;
		Boolean result = false;
		for (Shipment s : getSystem().getShipments()) {
			if (s.getShipmentNumber() == shipmentNumber) {
				result = true;
			}
		}
		assertEquals(expected, result);
	}

	@Then("the newly-created shipment shall have {int} items")
	public void the_newly_created_shipment_shall_have_items(Integer n) {
		int actual = this.currentShipment.numberOfShipmentItems();
		assertEquals(n, actual);
	}

	@Then("the newly-created shipment shall not have been ordered yet")
	public void the_newly_created_shipment_shall_not_have_been_ordered_yet() {
		assertNull(this.currentShipment.getDateOrdered());
	}

	@Then("the total number of shipments shall be {int}")
	public void the_total_number_of_shipments_shall_be(Integer expected) {
		int actual = getSystem().numberOfShipments();
		assertEquals(expected, actual);
	}

	@Then("the shipment with ID {string} shall include {int} {string}")
	public void the_shipment_with_id_shall_include(String shipmentId, Integer quantity, String item) {
		Boolean expected = true;
		Boolean result = false;
		List<Shipment> s = getSystem().getShipments();
		for (Shipment shipment : s) {
			if (shipment.getShipmentNumber() == shipmentIdToNumber.get(shipmentId)) {
				for (ShipmentItem i : shipment.getShipmentItems()) {
					if (i.getItem().getName().equals(item)) {
						if (i.getQuantity() == (int) quantity) {
							result = true;
						}
					}
				}
			}
		}
		assertEquals(expected, result);
	}

	@Then("the shipment with ID {string} shall not include any items called {string}")
	public void the_shipment_with_id_shall_not_include_any_items_called(String shipmentId, String item) {
		Boolean expected = false;
		Boolean result = false;
		List<Shipment> s = getSystem().getShipments();
		for (Shipment shipment : s) {
			if (shipment.getShipmentNumber() == shipmentIdToNumber.get(shipmentId)) {
				for (ShipmentItem i : shipment.getShipmentItems()) {
					if (i.getItem().getName().equals(item)) {
						result = true;
					}
				}
			}
		}
		assertEquals(expected, result);
	}

	@Then("the shipment with ID {string} shall include {int} distinct item(s)")
	public void the_shipment_with_id_shall_include_distinct_items(String shipmentId, Integer n) {
		Boolean expected = true;
		Boolean result = false;
		List<Shipment> s = getSystem().getShipments();
		for (Shipment shipment : s) {
			if (shipment.getShipmentNumber() == shipmentIdToNumber.get(shipmentId)) {
				if (shipment.numberOfShipmentItems() == n) {
					result = true;
				}
			}
		}
		assertEquals(expected, result);
	}
}
