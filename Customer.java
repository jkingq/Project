package main;

/**
 * Customer information
 * @author Alex Mercer and Phil Johnson
 *
 */
public class Customer {
	private int id;

	private Address address;
	
	public Customer(int id, Address address) {
		this.id = id;
		this.address = address;
	}
	//Getters and Setters for id and address
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
