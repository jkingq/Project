package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import main.Order.OrderStatus;

public class DatabaseManager {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/nb_gardens_warehouse?autoReconnect=true&useSSL=false";
	static final String USER = "WarehouseUser";
	static final String PASS = "warehouse";
	

	private static Connection connection = null;
	private static Statement statement = null;
	private static boolean isDBok = true;
	
	/**
	 * Attempts to open database connection. If this fails the database cannot be used until program is restarted.
	 */
	public static void testDatabase(){
		try{
			Class.forName(JDBC_DRIVER);			//find driver
			connection = DriverManager.getConnection(DB_URL,USER,PASS);		//open connection
			statement = connection.createStatement();
		}
		catch(Exception e){
			isDBok = false;
			System.out.println("Connection to Database failed. No data can be imported");
			try {
			    if (statement != null)
			     connection.close();
			  } catch (SQLException se) { }
			  try {
			    if (connection != null)
			    	connection.close();
			  } catch (SQLException se) {
			    se.printStackTrace();
			  }
		}
	}
	
	/**
	 * Opens the connection with the database. 
	 * @throws ClassNotFoundException Driver cannot be found.
	 * @throws SQLException Database cannot be found.
	 */
	private static void openConnection() throws ClassNotFoundException, SQLException{
		if(!isDBok){
			throw new ClassNotFoundException();
		}
		Class.forName(JDBC_DRIVER);			//find driver
		connection = DriverManager.getConnection(DB_URL,USER,PASS);		//open connection
		statement = connection.createStatement();
	}
	
	/**
	 * Closes connection to the database.
	 */
	private static void closeConnection(){
		try {
		    if (statement != null)
		     connection.close();
		  } catch (SQLException se) { }
		  try {
		    if (connection != null)
		    	connection.close();
		  } catch (SQLException se) {
		    se.printStackTrace();
		  }
	}	
	
	/**
	 * overwrites the list of customer orders with the orders on the database.
	 * @return Information on success of task.
	 */
	public static String readCOrdersFromDB(){
		try {
			openConnection();
		} catch (ClassNotFoundException e1) {
			return "Database inaccessible"; 
		} catch (SQLException e1) {
			return "Database inaccessible"; 
		}
		String output = "";
		OrderSystem.getOrders().clear();
		try{
			String sql = "Select * from customerorders where not state = 'DISPATCHED'";
			ResultSet orders = statement.executeQuery(sql);		//find orders in warehouse (not dispatched or arrived)
			ArrayList<Integer> orderIDs = new ArrayList<Integer>();
			ArrayList<Integer> customerIDs = new ArrayList<Integer>();
			ArrayList<OrderStatus> states = new ArrayList<Order.OrderStatus>();
			while (orders.next()){
				orderIDs.add(orders.getInt("orderID"));
				customerIDs.add(orders.getInt("customers_customerID"));
				OrderStatus state = OrderStatus.valueOf(orders.getString("State").toUpperCase());
				states.add(state);
			}
			for(int x = 0; x < orderIDs.size(); x++){								//for each of these orders
				ArrayList<OrderLine> orderLines = new ArrayList<OrderLine>();
				sql = "Select * from customerorderline col"
						+ " join customerorders co on co.orderID = col.customerorders_orderid"
						+ " where co.orderID = " + orderIDs.get(x);
				ResultSet lines = statement.executeQuery(sql);		//find the matching orderLines
				while(lines.next()){
					OrderLine orderLine = new OrderLine(lines.getInt("Products_productID"), lines.getInt("Amount"));
					orderLines.add(orderLine);						//Add these orderlines to list of orderlines
				}
				CustomerOrder order = new CustomerOrder(orderIDs.get(x), customerIDs.get(x), orderLines);
				order.setState(states.get(x));
				OrderSystem.getOrders().add(order);
			}
		}
		catch(SQLException e){
			System.out.println("An unknown error has occured with the SQL commands");
			return "An unknown error has occured with the SQL commands";
		}
		closeConnection();
		return output;
	}

	/**
	 * overwrites the list of purchase orders with the orders on the database.
	 * @return Information on success of task.
	 */
	public static String readPOrdersFromDB(){
		try {
			openConnection();
		} catch (ClassNotFoundException e1) {
			return "Database inaccessible"; 
		} catch (SQLException e1) {
			return "Database inaccessible"; 
		}
		String output = "";
		PurchaseOrderSystem.getOrders().clear();
		try {
			String sql = "Select * from purchaseorders where not state = 'ARRIVED'";
			ResultSet orders = statement.executeQuery(sql);		//find orders in warehouse (not dispatched or arrived)
			ArrayList<Integer> orderIDs = new ArrayList<Integer>();
			ArrayList<Integer> supplierIDs = new ArrayList<Integer>();
			ArrayList<OrderStatus> states = new ArrayList<Order.OrderStatus>();
			while (orders.next()){
				orderIDs.add(orders.getInt("purchaseOrderID"));
				supplierIDs.add(orders.getInt("suppliers_supplierID"));
				OrderStatus state = OrderStatus.valueOf(orders.getString("State"));
				states.add(state);
			}
			for(int x = 0; x < orderIDs.size(); x++){								//for each of these orders
				ArrayList<OrderLine> orderLines = new ArrayList<OrderLine>();
				sql = "Select * from purchaseorderline col"
						+ " join purchaseorders co on co.purchaseOrderID = col.purchaseorders_purchaseorderid"
						+ " where co.purchaseorderID = " + orderIDs.get(x);
				ResultSet lines = statement.executeQuery(sql);		//find the matching orderLines
				while(lines.next()){
					OrderLine orderLine = new OrderLine(lines.getInt("Products_productID"), lines.getInt("Amount"));
					orderLines.add(orderLine);						//Add these orderlines to list of orderlines
				}
				PurchaseOrder order = new PurchaseOrder(orderIDs.get(x), orderLines, supplierIDs.get(x));
				order.setState(states.get(x));
				PurchaseOrderSystem.getOrders().add(order);
			}
		}
		 catch (SQLException e) {
				System.out.println("An unknown error has occured with the SQL commands");
				return "An unknown error has occured with the SQL commands";
		}
		closeConnection();
		return output;
	}

/**
	 * overwrites the list of products with the products on the database.
	 * @return Information on success of task.
 */
	public static String readProductsFromDB(){
		try {
			openConnection();
		} catch (ClassNotFoundException e1) {
			return "Database inaccessible"; 
		} catch (SQLException e1) {
			return "Database inaccessible"; 
		}
		String output = "";
		InventorySystem.getProducts().clear();
		try {
			String sql = "Select * from products";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				int id = rs.getInt("productID");
				String name = rs.getString("name");
				int stockLevel = rs.getInt("stockLevel");
				int reservedLevel = rs.getInt("reservedLevel");
				int pickedLevel = rs.getInt("pickedLevel");
				int row = rs.getInt("storedrow");
				int column = rs.getInt("storedcolumn");
				int porousID = rs.getInt("porouswaredID");
				if(rs.wasNull()){
					porousID = -1;
				}
				Product p;
				if(porousID >= 0){
					p = new PorouswarableProduct(name, stockLevel, new Location(row,column), id, reservedLevel, pickedLevel, porousID);
				}
				else{
					p = new StandardProduct(name, stockLevel, new Location(row,column), id, reservedLevel, pickedLevel);
				}
				InventorySystem.getProducts().add(p);
			}
			
		} catch (SQLException e) {
			System.out.println("An unknown error has occured with the SQL commands");
			return "An unknown error has occured with the SQL commands";
			
		}
		closeConnection();
		return output;
	}

	/**
	 * Updates individual order in database
	 * @param co the customer order to be updated
	 * @return output describing success of process
	 */
	public static String updateToDB(CustomerOrder co){
		try {
			openConnection();
		} catch (ClassNotFoundException e1) {
			return "Database inaccessible"; 
		} catch (SQLException e1) {
			return "Database inaccessible"; 
		}
		String output = "";
		try {
			String sql = "SELECT * FROM CustomerOrders where orderID = " + co.getOrderID()
					+ " and Customers_customerID = " + co.getCustomerID();	//ensures both id and customerid match database
			ResultSet rs = statement.executeQuery(sql);						//These should not change and so this should always be fine
			if(rs.next()){
				statement = connection.createStatement();			//Warehouse workers should only need to update the order status
				sql = "Update customerorders set state = '" + co.getState() + "' where orderID = " + co.getOrderID();
				statement.executeUpdate(sql);
				output = "Order successfully updated";
					
			}
			else{
				System.out.println("Error, customer order with these ids not found");
				output = "Error, customer order with these ids not found";
					//Could put more detail here about which orders are not found in the system.
			}
		} catch (SQLException e) {
			System.out.println("An unknown error has occured with the SQL commands");
			return "An unknown error has occured with the SQL commands";
		}
		closeConnection();
		return output;
	}
	
	/**
	 * Updates individual order in database
	 * @param co the customer order to be updated
	 * @return output describing success of process
	 */
	public static String updateToDB(PurchaseOrder po){
		try {
			openConnection();
		} catch (ClassNotFoundException e1) {
			return "Database inaccessible"; 
		} catch (SQLException e1) {
			return "Database inaccessible"; 
		}
		String output = "";
		try {
			String sql = "SELECT * FROM PurchaseOrders where purchaseOrderID = " + po.getOrderID()
					+ " and Suppliers_supplierID = " + po.getSupplierID();	//ensures both id and supplierid match database
			ResultSet rs = statement.executeQuery(sql);						//These should not change and so this should always be fine
			if(rs.next()){
				statement = connection.createStatement();			//Warehouse workers should only need to update the order status
				sql = "Update purchaseorders set state = '" + po.getState() + "' where purchaseorderID = " + po.getOrderID();
				statement.executeUpdate(sql);
				output = "Order successfully updated";
					
			}
			else{
				System.out.println("Error, customer order with these ids not found");
				output = "Error, customer order with these ids not found";
					//Could put more detail here about which orders are not found in the system.
			}
		}catch (SQLException e) {
			System.out.println("An unknown error has occured with the SQL commands");
			return "An unknown error has occured with the SQL commands";
		}
		closeConnection();
		return output;
	}
	
	

	/**
	 * Updates individual product in database
	 * @param p the customer product to be updated
	 * @return output describing success of process
	 */
	public static String updateToDB(Product p){
		try {
			openConnection();
		} catch (ClassNotFoundException e1) {
			return "Database inaccessible"; 
		} catch (SQLException e1) {
			return "Database inaccessible"; 
		}
		String output = "";
		try {
			String sql = "SELECT * FROM Products where productID = " + p.getProductID();	//ensures both id present in db
			ResultSet rs = statement.executeQuery(sql);						
			if(rs.next()){
				statement = connection.createStatement();	//Warehouse workers should only need to stock levels and optionally location
				sql = "Update purchaseorders set pickedLevel = " + p.getPickedLevel()
						+ ", stockLevel  = " + p.getStockLevel()
						+ ", reservedLevel = " + p.getReservedLevel()
						+ " where productID = " + p.getProductID();
				statement.executeUpdate(sql);
				output = "Order successfully updated";
					
			}
			else{
				System.out.println("Error, customer order with these ids not found");
				output = "Error, customer order with these ids not found";
					//Could put more detail here about which orders are not found in the system.
			}
		} catch (SQLException e) {
			System.out.println("An unknown error has occured with the SQL commands");
			return "An unknown error has occured with the SQL commands";
		}
		closeConnection();		//should always be in finally.
		return output;
	}

	
}
