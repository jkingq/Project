package main;

import java.util.ArrayList;

/**
 * Database of all customers.
 * @author Alex Mercer and Phil Johnson
 *
 */
@SuppressWarnings("serial")
public class CustomerSystem extends ArrayList<Customer> {
	
	/**
	 * singleton instance of the CustomerSystem
	 */
	private static CustomerSystem customers = new CustomerSystem();

	private CustomerSystem() {

	}
	/**
	 * Returns the single instance of customerSystem 
	 * @return List of customers
	 */
	public static CustomerSystem getCustomers() {
		return customers;
	}
	
	/**
	 * Finds the customer with a given customerID
	 * @param customerID The ID of the customer that should be found
	 * @return The customer with customerID provided
	 */
	public Customer findCustomer(int customerID) {
		for (Customer c : customers) {
			if (customerID == c.getId()) {
				return c;
			}
		}
		System.out.println("THE CUSTOMER ISNT THERE");//Explains that a customer is not found
		return null;
	}

}
