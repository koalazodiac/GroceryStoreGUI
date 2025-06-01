package ca.mcgill.ecse.grocerymanagementsystem.controller;

import java.util.*;

import ca.mcgill.ecse.grocerymanagementsystem.model.Shipment;
import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;
import ca.mcgill.ecse.grocerymanagementsystem.model.ShipmentItem;

public class ShipmentController {
	public static void createShipment() {
		Shipment s = new Shipment(null, GroceryManagementSystemController.getGroceryManagementSystem());
	}

	public static void deleteShipment(int shipmentNumber) {
		GroceryManagementSystem system = GroceryManagementSystemController.getGroceryManagementSystem();
		List<Shipment> listShipment = system.getShipments();
		Boolean found = false;
		for (Shipment s : listShipment) {
			if (s.getShipmentNumber() == shipmentNumber) {
				found = true;
				if (s.getDateOrdered() != null) {
					throw new GroceryStoreException("cannot delete a shipment which has already been ordered");
				}
				s.setGroceryManagementSystem(new GroceryManagementSystem());
				system.removeShipment(s);
				break;
				// remove shipment item?
			}
		}
		if (!found) {
			throw new GroceryStoreException("there is no shipment with number \"" + shipmentNumber + "\"");
		}

	}

	public static void addItemToShipment(int shipmentNumber, String itemName) {
		List<Shipment> listShipment = GroceryManagementSystemController.getGroceryManagementSystem().getShipments();
		Item i = Item.getWithName(itemName);
		if (i == null) {
			throw new GroceryStoreException("there is no item called \"" + itemName + "\"");
		}
		boolean found = false;
		for (Shipment s : listShipment) {
			if (s.getShipmentNumber() == shipmentNumber) {
				found = true;
				if (s.getDateOrdered() != null) {
					throw new GroceryStoreException("shipment has already been ordered");
				}
				for (ShipmentItem j : s.getShipmentItems()) {
					if (j.getItem().equals(i)) {
						throw new GroceryStoreException("shipment already includes item \"" + itemName + "\"");
					}
				}
				ShipmentItem j = new ShipmentItem(1, GroceryManagementSystemController.getGroceryManagementSystem(), s,
						i);
			}
		}
		if (!found) {
			throw new GroceryStoreException("there is no shipment with number \"" + shipmentNumber + "\"");
		}
	}

	public static void updateQuantityInShipment(int shipmentNumber, String itemName, int newQuantity) {
		List<Shipment> listShipment = GroceryManagementSystemController.getGroceryManagementSystem().getShipments();
		boolean found = false;
		boolean founditem = false;
		Item item = Item.getWithName(itemName);
		if (item == null) {
			throw new GroceryStoreException("there is no item called \"" + itemName + "\"");
		}
		if (newQuantity < 0) {
			throw new GroceryStoreException("quantity must be non-negative");
		}
		for (Shipment s : listShipment) {
			if (s.getShipmentNumber() == shipmentNumber) {
				found = true;
				if (s.getDateOrdered() != null) {
					throw new GroceryStoreException("shipment has already been ordered");
				}
				for (ShipmentItem i : s.getShipmentItems()) {
					if (i.getItem().equals(item)) {
						founditem = true;
						i.setQuantity(newQuantity);
						if (newQuantity == 0) {
							i.setShipment(new Shipment(null, new GroceryManagementSystem()));
							s.removeShipmentItem(i);
							break;
						}
					}
				}
			}
		}
		if (!found) {
			throw new GroceryStoreException("there is no shipment with number \"" + shipmentNumber + "\"");
		}
		if (!founditem) {
			throw new GroceryStoreException("shipment does not include item \"" + itemName + "\"");
		}
	}

	public static List<TOShipment> getAllShipments() {
		List<TOShipment> shipments = new ArrayList<>();
		for (Shipment u : GroceryManagementSystemController.getGroceryManagementSystem().getShipments()) {
			shipments.add(TOConverter.convert(u));
		}
		return shipments;
	}

	public static boolean existShipment(int shipmentID){
		for (Shipment s: GroceryManagementSystemController.getGroceryManagementSystem().getShipments()){
			if (s.getShipmentNumber() == shipmentID){
				return true;
			}
		}
		return false;
	}

}
