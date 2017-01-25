package main;


/**
 * 
 * @author Alex Mercer and Phil Johnson
 *
 */
public interface OrderInterface {

	
	/**
	 * Used to update the system that order has been dispatched
	 */
	void dispatch();

	/**
	 * Notifies accounts
	 */
	void notifyAccounts();

	/**
	 * Used to update the system when an order has been picked
	 */
	void pickOrder();

	/**
	 * Used to update the system when a purchase order has arrived providing this item
	 */
	void addStock();

	/**
	 * Used to update the system when an order has been reserved when a worker begins attending it.
	 * @return 
	 */
	void reserveStock();

}
