package main;

/**
 * Non porouswarable product
 * This includes products that have had porousware applied
 * @author Alex Mercer and Phil Johnson
 *
 */
public class StandardProduct extends Product {

	public StandardProduct(String name, int stockLevel, Location location) {
		super(name, stockLevel, location);
		// TODO Auto-generated constructor stub
	}

	public StandardProduct(String name, int stockLevel, Location location,
			int id, int reservedLevel, int pickedLevel) {
		super(name, stockLevel, location);
		setReservedLevel(reservedLevel);
		setProductID(id);
		setPickedLevel(pickedLevel);
	}

	@Override
	public void applyPorousWare(int amount) {
		System.out.println("No no porousware thanks");

	}

}
