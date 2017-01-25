package main;

/**
 * The location in the warehouse of an item
 * @author Alex Mercer and Phil Johnson
 *
 */
public class Location {

	private int row = 0;
	private int column = 0;

	public Location(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	

}
