package ca.mcgill.ecse.grocerymanagementsystem.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.grocerymanagementsystem.model.GroceryManagementSystem;
import ca.mcgill.ecse.grocerymanagementsystem.model.Item;

public class ItemController {
	public static void create(String name, boolean isPerishable, int points, int price) {
		validateName(name);
		validatePoints(points);
		validatePrice(price);

		GroceryManagementSystem sys = GroceryManagementSystemController.getGroceryManagementSystem();

		if (Item.getWithName(name) != null){
			throw new GroceryStoreException("an item called \"" + name + "\" already exists");
		}

		Item item = new Item(name, 0, price, isPerishable, points, sys);
		sys.addItem(item);

	}

	public static void updatePrice(String name, int newPrice) {
		Item item = getItemByName(name);
		
		validatePrice(newPrice);
		item.setPrice(newPrice);
	}

	public static void updatePoints(String name, int newPoints) {
		Item item = getItemByName(name);

		validatePoints(newPoints);
		item.setNumberOfPoints(newPoints);
	}

	public static void delete(String name) {
		Item item = getItemByName(name);
		item.delete();
	}

	private static void validateName(String name){
		if (name == null || name.isBlank()) {
			throw new GroceryStoreException("name is required");
		}
	}

	private static void validatePoints(int points){
		if (points < 1 || points > 5){
			throw new GroceryStoreException("points must be between one and five");
		}
	}

	private static void validatePrice(int prices){
		if (prices <= 0){
			throw new GroceryStoreException("price must be positive");
		}
	}

	private static Item getItemByName(String name){
		Item item = Item.getWithName(name);
		if (item == null){
			throw new GroceryStoreException("there is no item called \"" + name + "\"");
		}
		return item;
	}

	public static List<TOItem> getAllItems(){
		List<TOItem> items = new ArrayList<>();
		for (Item u : GroceryManagementSystemController.getGroceryManagementSystem().getItems()){
			items.add(TOConverter.convert(u));

		}
		return items;
	}

	public static void update(String name, boolean isPerishable, int numberOfPoints, int price) throws GroceryStoreException {
        GroceryManagementSystem gms = GroceryManagementSystemController.getGroceryManagementSystem();
        List<Item> items = gms.getItems();

        Item itemToUpdate = null;

        for (Item item : items) {
            if (item.getName().equals(name)) {
                itemToUpdate = item;
                break;
            }
        }

        if (itemToUpdate == null) {
            throw new GroceryStoreException("Item with name '" + name + "' not found.");
        }

        itemToUpdate.setPrice(price);
        itemToUpdate.setNumberOfPoints(numberOfPoints);
        itemToUpdate.setIsPerishable(isPerishable);
    }


	
	
}


