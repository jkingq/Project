package main;

/**
 * 
 * @author Alex Mercer and Phil Johnson
 *
 */
public class OrderLine {
	private int productID;

	private int amount;

	public OrderLine(int productID, int amount) {
		super();
		this.productID = productID;
		this.amount = amount;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
