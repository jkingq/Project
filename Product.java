package main;

/**
 * 
 * @author Alex Mercer and Phil Johnson
 *
 */
public abstract class Product implements ProductInterface {

	private int productID;

	private static int productIDCounter = 1;

	private String name;
	// (Various physical attributes)

	private Location location;

	private int stockLevel = 0; // (literally stock on shelf)

	private int reservedLevel = 0;

	private int pickedLevel = 0;


	public Product(String name, int stockLevel, Location location) {
		this.location = location;
		this.stockLevel = stockLevel;
		this.name = name;
		setProductID();
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID() {
		this.productID = productIDCounter;
		productIDCounter++;
	}
	
	public void setProductID(int id){
		this.productID = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getStockLevel() {
		return stockLevel;
	}

	public void setStockLevel(int stockLevel) {
		this.stockLevel = stockLevel;
	}

	public int getReservedLevel() {
		return reservedLevel;
	}

	public void setReservedLevel(int reservedLevel) {
		this.reservedLevel = reservedLevel;
	}

	public int getPickedLevel() {
		return pickedLevel;
	}

	public void setPickedLevel(int pickedLevel) {
		this.pickedLevel = pickedLevel;
	}

	public void reserve(int amount) {
		reservedLevel += amount;
		stockLevel -= amount;
	}

	public void pick(int amount) {
		pickedLevel += amount;
		reservedLevel -= amount;
	}

	public void dispatch(int amount) {

		reservedLevel += amount;
		pickedLevel -= amount;
	}

	public void receive(int amount) {
		stockLevel += amount;
	}
	
	public String toString(){
		String output = "";
		output += "ID - " + productID;
		output += "    Stock Level - " + stockLevel;
		output += "    ReservedLevel - " + reservedLevel;
		output += "    PickedLevel - " + pickedLevel;
		output += "    Name - " + name;
		
		return output;
	}

}
