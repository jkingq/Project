package main;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * An order placed by a customer for one or many products.
 * @author Alex Mercer and Phil Johnson
 *
 */
public class CustomerOrder extends Order {

	private int customerID;
	//Constructor for CustomerOrder
	public CustomerOrder(int customerID, ArrayList<OrderLine> orderLines) {
		super(orderLines);
		setCustomerID(customerID); //Sets CustomerID to customerID
	}

	public CustomerOrder(int orderID, int customerID, ArrayList<OrderLine> orderLines) {
		super(orderID, orderLines);
		setCustomerID(customerID); //Sets CustomerID to customerID
	}
	

	/**
	 * Reserves the products from each product line on this order, removing them from stock and adding to reserved.
	 * Sets state to RESERVED
	 */
	@Override
	public void reserveStock() {
		setState(OrderStatus.RESERVED);
		Iterator<OrderLine> iterator = getOrderLines().iterator();
		while (iterator.hasNext()){ //iterates through whole list of the orderlines
			OrderLine line = iterator.next();
			Product product = InventorySystem.getProducts().findProduct(line.getProductID());//Finds the product referenced by the order line
			if(product == null || product.getStockLevel() < line.getAmount()){				//relocated to checkInStock method, can remove if things work
				notifyAccounts(); 				
				System.out.println("There has been a major error. THis should never happen");
				iterator.remove();
				continue;
			}
			product.reserve(line.getAmount());
			DatabaseManager.updateToDB(product);
		}
		DatabaseManager.updateToDB(this);
	}
	
	/**
	 * Checks if all the products in all the orderlines are in stock.
	 * @return true if all items in stock or false otherwise.
	 */
	public boolean checkInStock(){
		for(OrderLine o:getOrderLines()){
			Product product = InventorySystem.getProducts().findProduct(o.getProductID());
			if(product == null || product.getStockLevel() < o.getAmount()){//if product cannot be found or the product is not in stock
				notifyAccounts(); //tell someone that the order is bad and that we removed the bad order line 
				System.out.println("We don't have enough of that");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Picks the products from each product line on this order, removing them from reserved and adding to picked.
	 * Sets state to PICKED
	 */
	@Override
	public void pickOrder() {
		setState(OrderStatus.PICKED);
		for (OrderLine o : getOrderLines()) {
			Product product = InventorySystem.getProducts().findProduct(o.getProductID());
			product.pick(o.getAmount());
			DatabaseManager.updateToDB(product);
		}
		DatabaseManager.updateToDB(this);
	}


	/**
	 * Dispatches the products from each product line on this order, removing them from picked.
	 * Sets state to DISPATCHED
	 */
	@Override
	public void dispatch() {
		setState(OrderStatus.DISPATCHED);
		for (OrderLine o : getOrderLines()) {
			Product product = InventorySystem.getProducts().findProduct(o.getProductID());
			product.dispatch(o.getAmount());
			DatabaseManager.updateToDB(product);
		}
		notifyAccounts();
		DatabaseManager.updateToDB(this);
	}

	@Override
	public void notifyAccounts() {

	}


	@Override
	public void addStock() {
		System.out.println("Stock should not be added using a CustomerOrder. An unexpected error has occured.");
	}

	//Getter and Setter for customerID
	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

}
