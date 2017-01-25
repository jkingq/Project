package main;

import java.util.ArrayList;

import main.Order.OrderStatus;

/**
 * The Database containing all Customer Orders
 * @author Alex Mercer and Phil Johnson
 *
 */
@SuppressWarnings("serial")
public class OrderSystem extends ArrayList<CustomerOrder> {

	private static OrderSystem orders = new OrderSystem();

	private OrderSystem() {

	}

	public static OrderSystem getOrders() {
		return orders;
	}

	/**
	 * outputs information about order with given index.
	 * @param index position of required order in order list.
	 */
	public void viewOrder(int index) {
		CustomerOrder order = orders.get(index);
		System.out.println("Order " + order.getOrderID() + ": "
				+ order.getCustomerID());
		for (OrderLine o : order.getOrderLines()) {
			System.out.println("Product " + o.getProductID() + ": "
					+ o.getAmount());
		}
	}

	/**
	 * Outputs ID and state of all customer orders.
	 */
	public void viewAllOrders() {
		for (Order o : orders) {
			System.out.println(o.getOrderID() + " " + o.getState());
		}
	}

	/**
	 * Goes through the list of customer orders and finds the first order that is pending and hence not already being attended.
	 * @return The order that must be fulfilled
	 */
			
	public CustomerOrder findFirstAvailableOrder() {
		for(CustomerOrder o: orders){
			if(o.getState() == OrderStatus.PENDING){
				if(o.checkInStock()){		//checks all items in stock before returning the order
					return o;		
				}
			}
		}
		System.out.println("No orders available to process");
		return null;
	}
	
	/**
	 * Dispatches all orders that are picked and ready to be dispatched.
	 * @return List of dispatched orders with optimised route.
	 */
	public ArrayList<CustomerOrder> dispatchOrders(){
		viewAllOrders();
		
		ArrayList<CustomerOrder> dispatchedOrders = new ArrayList<CustomerOrder>();
		
		for(CustomerOrder o:orders){
			if(o.getState() == OrderStatus.PICKED){
				dispatchedOrders.add(o);//Adds each picked order to dispatched orders list
				o.dispatch();//Runs the order dispatch method
			}
		}
		dispatchedOrders = nearestNeighbour(dispatchedOrders);
		
		return dispatchedOrders;
	}
	
	/**
	 * Finds the nearest neighbour solution for the list of customerorders
	 */
	public ArrayList<CustomerOrder> nearestNeighbour(ArrayList<CustomerOrder> dispatchedOrders){
		ArrayList<CustomerOrder> rearrangedOrders = new ArrayList<CustomerOrder>();
		CustomerOrder bestOrder = null;
		int startLat = 0;
		int startLon = 0;
		while(dispatchedOrders.size()>0){
			double bestDistance = Double.MAX_VALUE;
			double distance = 0;
			for(CustomerOrder o: dispatchedOrders){
				int difLat = CustomerSystem.getCustomers().findCustomer(o.getCustomerID()).getAddress().getLattitude() - startLat;
				int difLon = CustomerSystem.getCustomers().findCustomer(o.getCustomerID()).getAddress().getLongitude() - startLon;
				//differences between start long and lat and the current objects.
				
				distance = Math.sqrt((difLat*difLat) + (difLon*difLon));//find distance by Pythag
				if(distance < bestDistance){ // finds order with the lowest distance
					bestDistance = distance;
					bestOrder = o;
				}
			} //Updates the start long and lat to the new starting point
			startLat = CustomerSystem.getCustomers().findCustomer(bestOrder.getCustomerID()).getAddress().getLattitude();
			startLon = CustomerSystem.getCustomers().findCustomer(bestOrder.getCustomerID()).getAddress().getLongitude();
			dispatchedOrders.remove(bestOrder);// removes order from the list when chosen
			rearrangedOrders.add(bestOrder);
			System.out.print(bestOrder.getOrderID() + "  ");
		}
		System.out.println();
		return rearrangedOrders;
	}
	
	/**
	 * A simulated annealing method for solving the travelling salesman problem.
	 * Not as consistent as nearest neighbour in current form.
	 * @param dispatchedOrders unordered list of orders
	 * @return ordered list of orders.
	 */
	public ArrayList<CustomerOrder> solveBySA(ArrayList<CustomerOrder> dispatchedOrders){
		int limit = 500000;
		double t = 1.0;
		double totalDist = getDistance(dispatchedOrders);
		double bestDist = totalDist;
		System.out.println(totalDist);
		ArrayList<CustomerOrder> bestOrder = new ArrayList<CustomerOrder>();
		for(CustomerOrder o:dispatchedOrders){
			bestOrder.add(o);
		}
		int changes = 0;
		for(int x = 0; x < limit; x++){
			t = 1- (double)x/limit;
			int pos = (int)(Math.random() * (dispatchedOrders.size()-1));
			int moveSize = (int)(Math.max(Math.random()*t*40,1));
			int newPos = Math.min(pos+moveSize,dispatchedOrders.size()-1);
			CustomerOrder tempOrder = dispatchedOrders.get(pos);
			dispatchedOrders.set(pos, dispatchedOrders.get(newPos));		//need to vary move size for it to work better
			dispatchedOrders.set(newPos, tempOrder);						//does not effectively cover area
			
			totalDist = getDistance(dispatchedOrders);
			
			double chance = 1;
			if(totalDist>bestDist){
				chance = 1-Math.pow(Math.E, -((t)*(1/((totalDist - bestDist)*10))));
				//chance = 0;
				//chance = (1/((totalDist - bestDist)*(totalDist - bestDist)))*t;
				if(t<0.05){
					chance = 0;
				}
			}
			if(Math.random() < chance){
				System.out.println(totalDist);
				changes ++;
				bestDist = totalDist;
				bestOrder.clear();
				for(CustomerOrder o:dispatchedOrders){
					bestOrder.add(o);
				}
				//System.out.println(bestDist);
			}
			
		}
		System.out.println(changes);
		return bestOrder;
	}
	
	public double getDistance(ArrayList<CustomerOrder> orders){
		double totalDist = 0;
		for(int i = 0; i < orders.size() ; i ++){
			int x1 = 0;
			int y1 = 0;
			if(i>0){
				x1 = CustomerSystem.getCustomers().findCustomer(orders.get(i-1).getCustomerID()).getAddress().getLattitude();
				y1 = CustomerSystem.getCustomers().findCustomer(orders.get(i-1).getCustomerID()).getAddress().getLongitude();
			}
			int x2 = CustomerSystem.getCustomers().findCustomer(orders.get(i).getCustomerID()).getAddress().getLattitude();
			int y2 = CustomerSystem.getCustomers().findCustomer(orders.get(i).getCustomerID()).getAddress().getLongitude();
			double size = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
			totalDist += size;
		}
		return totalDist;
	}
	
	
}
