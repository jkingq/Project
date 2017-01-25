package main;

/**
 * The address of a customer.
 * @author Alex Mercer and Phil Johnson
 *
 */
public class Address {

	private String line1;
	private String line2;
	private String postCode;
	private String city;
	private int longitude;
	private int lattitude;

	
	public Address(String line1, String line2, String postCode, String city,
			int longitude, int lattitude) {
		super();
		this.line1 = line1;
		this.line2 = line2;
		this.postCode = postCode;
		this.city = city;
		this.longitude = longitude;
		this.lattitude = lattitude;
	}
	
	/**
	 * Creates new address using only coordinates
	 * @param x The x position (used for TSP)
	 * @param y The y position (used for TSP)
	 */
	public Address(int x, int y){
		longitude = x;
		lattitude = y;
	}
	//Getters and Setters for longitude and latitude
	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public int getLattitude() {
		return lattitude;
	}

	public void setLattitude(int lattitude) {
		this.lattitude = lattitude;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	
	
	

}
