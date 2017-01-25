package main;

import java.util.ArrayList;

/**
 * 
 * @author Alex Mercer and Phil Johnson
 *
 */
public class PurchaseOrder extends Order {

	private int supplierID;

	public PurchaseOrder(ArrayList<OrderLine> orderLines, int supplierID) {
		super(orderLines);
		this.supplierID = supplierID;
		// TODO Auto-generated constructor stub
	}
	
	public PurchaseOrder(int orderID, ArrayList<OrderLine> orderLines, int supplierID) {
		super(orderID, orderLines);
		this.supplierID = supplierID;
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Adds the stock described by the purchase order to the relevant stock levels.
	 * Marks the order as ARRIVED.
	 */
	@Override
	public void addStock() {
		setState(OrderStatus.ARRIVED);
		for(OrderLine o:getOrderLines()){
			Product p = InventorySystem.getProducts().findProduct(o.getProductID());
			if(p instanceof PorouswarableProduct){
				p.applyPorousWare(o.getAmount());
			}
			else{
				p.receive(o.getAmount());
			}
			DatabaseManager.updateToDB(p);
		}
		DatabaseManager.updateToDB(this);
		notifyAccounts();
		
	}

	@Override
	public void dispatch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyAccounts() {
		// TODO Auto-generated method stub

	}


	@Override
	public void pickOrder() {
		// TODO Auto-generated method stub

	}
	@Override
	public void reserveStock() {

	}

	public int getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(int supplierID) {
		this.supplierID = supplierID;
	}
	
	

}
