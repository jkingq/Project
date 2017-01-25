package main;

import java.util.ArrayList;

/**
 * Database of all products
 * @author Alex Mercer and Phil Johnson
 *
 */
@SuppressWarnings("serial")
public class InventorySystem extends ArrayList<Product> {
	
	private static InventorySystem products = new InventorySystem();

	private InventorySystem() {

	}

	/**
	 * 
	 * @return singleton instance of the InventorySystem
	 */
	public static InventorySystem getProducts() {
		return products;
	}

	/**
	 * Finds the product with the given productID
	 * @param productId
	 * @return product with given ID
	 */
	public Product findProduct(int productId) {
		for (Product p : products) {
			if (productId == p.getProductID()) {
				return p;
			}
		}
		return null;
	}

}
