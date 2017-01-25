package main;

/**
 * The interface for products
 * 
 * @author Alex Mercer and Phil Johnson
 *
 */

public interface ProductInterface {

	/**
	 * Adds required amount of product to reserved level
	 * 
	 * @param amount
	 *            the ordered amount of this product.
	 */
	void reserve(int amount);

	/**
	 * Moves stock from reserved level to picked level
	 * 
	 * @param amount
	 *            the ordered amount of this product.
	 */
	void pick(int amount);

	/**
	 * Dispatches the relevant
	 * 
	 * @param amount
	 *            the ordered amount of this product.
	 */
	void dispatch(int amount);

	/**
	 * Adds new stock to stock level
	 * 
	 * @param amount
	 *            the amount of this product received
	 */
	void receive(int amount);

	/**
	 * Calculates the proportion of received stock that should have porousware
	 * applied to it and adds the relevant proportion of stock to both standard and porouswared stock levels.
	 * 
	 * @param amount
	 *            the amount of unporouswared item that has been received.
	 */
	void applyPorousWare(int amount);

}
