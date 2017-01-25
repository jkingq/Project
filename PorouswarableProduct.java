package main;

/**
 * Product that can have porousware applied to it.
 * @author Alex Mercer and Phil Johnson
 *
 */
public class PorouswarableProduct extends Product {
	
	private int porouswaredID;
	/**
	 * describes the percentage the stock level that the porouswared product should be maintained at compared to the total product stock level.
	 */
	private double percentagePorouswared = 15;	

	/**
	 * Creates a new product that can have porousware applied to it. 
	 * Since this type of product must have a porouswared counterpart this is also generated here.
	 * @param name The name of the product.
	 * @param stockLevel The current stock level of the product.
	 * @param location The location in the warehouse of the product.
	 */
	public PorouswarableProduct(String name, int stockLevel, Location location) {
		super(name, stockLevel, location);
		StandardProduct porouswared = new StandardProduct("Porousware "+ name, (int)(stockLevel * percentagePorouswared/100), 
				new Location(location.getRow(), location.getColumn() + 1));
		this.porouswaredID= porouswared.getProductID();
		InventorySystem.getProducts().add(porouswared);
		// TODO Auto-generated constructor stub
	}

	public PorouswarableProduct(String name, int stockLevel, Location location,
			int id, int reservedLevel, int pickedLevel, int porouswaredID) {
		super(name, stockLevel, location);
		this.setReservedLevel(reservedLevel);
		this.setPickedLevel(pickedLevel);
		this.setProductID(id);
		this.porouswaredID = porouswaredID;
	}

	@Override
	public void applyPorousWare(int amount) {
		int stockLevel = getStockLevel();
		Product porouswared = InventorySystem.getProducts().findProduct(porouswaredID);
		int porouswareStockLevel = porouswared.getStockLevel();
		int overallStock = stockLevel + porouswareStockLevel + amount; // overall stock is equal to total products
		int newPorouswareStockLevel = (int)(overallStock * percentagePorouswared/100); //Porouswared stock level is percentage porouswared of overall stock
		int amountToAddToPorousWare = newPorouswareStockLevel - porouswareStockLevel;
		if(amountToAddToPorousWare >= 0){ // this will be false when porouswared level is sufficiently high that new stock should not be porouswared
			porouswared.receive(Math.min(amountToAddToPorousWare,amount));
			receive(Math.max(amount - amountToAddToPorousWare,0));
		}
		else{
			receive(amount);
		}
	}

	public int getPorouswaredID() {
		return porouswaredID;
	}

	public void setPorouswaredID(int porouswaredID) {
		this.porouswaredID = porouswaredID;
	}
	
	
}
