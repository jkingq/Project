package main;

import java.util.ArrayList;

import main.Order.OrderStatus;

/**
 * Contains all purchase orders.
 * @author Alex Mercer and Phil Johnson
 *
 */
@SuppressWarnings("serial")
public class PurchaseOrderSystem extends ArrayList<PurchaseOrder>{
	private static PurchaseOrderSystem orders = new PurchaseOrderSystem();

	private PurchaseOrderSystem() {

	}

	public static PurchaseOrderSystem getOrders() {
		return orders;
	}

	/**
	 * Marks received order as arrived and adds the contained stock to inventory system
	 * @param id The ID of the purchase order being received
	 */
	
	public PurchaseOrder findOrder(int id){
		for (PurchaseOrder o: orders){
			if(o.getOrderID() == id){
				return o;
			}
		}
		return null;
	}
	
	/**
	 * Handles the receiving of new stock in an order.
	 * @param id The order id of the received order
	 * @return Describes whether the order has been successfully received.
	 */
	public boolean receive(int id) {
		for (PurchaseOrder o: orders){
			if(o.getOrderID() == id){
				o.addStock();
				o.setState(OrderStatus.ARRIVED);
				return true;
			}
		}
		System.out.println("Failed to receive order. Order not found in system.");
		return false;
	}

}
