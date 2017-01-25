package main;

import java.util.ArrayList;

/**
 * 
 * @author Alex Mercer and Phil Johnson
 *
 */
public abstract class Order implements OrderInterface {
	private ArrayList<OrderLine> orderLines = new ArrayList<OrderLine>();

	/**
	 * All possible states of the order
	 * @author Alex Mercer and Phil Johnson
	 *
	 */
	public enum OrderStatus {
		PICKED, RESERVED, DISPATCHED, PENDING, ARRIVED
	}

	private OrderStatus state = OrderStatus.PENDING;
	private int orderID;
	private static int cOrderIDCounter = 1;
	private static int pOrderIDCounter = 1;

	public Order(ArrayList<OrderLine> orderLines) {
		this.orderLines = orderLines;
		setOrderID();
	}

	public Order(int orderID, ArrayList<OrderLine> orderLines) {
		this.orderLines = orderLines;
		this.orderID = orderID;
	}

	public ArrayList<OrderLine> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(ArrayList<OrderLine> orderLines) {
		this.orderLines = orderLines;
	}

	public OrderStatus getState() {
		return state;
	}

	public void setState(OrderStatus state) {
		this.state = state;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID() {
		if(this instanceof PurchaseOrder){
			this.orderID = pOrderIDCounter;
			pOrderIDCounter++;//Id auto increments
		}
		else if(this instanceof CustomerOrder){
			this.orderID = cOrderIDCounter;
			cOrderIDCounter++;//Id auto increments
		}
	}

	public static int getcOrderIDCounter() {
		return cOrderIDCounter;
	}

	public static void setcOrderIDCounter(int cOrderIDCounter) {
		Order.cOrderIDCounter = cOrderIDCounter;
	}

	public static int getpOrderIDCounter() {
		return pOrderIDCounter;
	}

	public static void setpOrderIDCounter(int pOrderIDCounter) {
		Order.pOrderIDCounter = pOrderIDCounter;
	}

	/**
	 * restores an order as pending. Only to be used if the order is in state reserved.
	 */
	public void cancelOrder() {
		if(state != OrderStatus.RESERVED){
			System.out.println("An error has occured with the canceling of this order");
			return;
		}
		state = OrderStatus.PENDING;
		for(OrderLine o:orderLines){
			Product product = InventorySystem.getProducts().findProduct(o.getProductID());
			product.reserve(- o.getAmount());	//by using negetive amount with reserve it is unreserved.
		}
	}
	
	

}
