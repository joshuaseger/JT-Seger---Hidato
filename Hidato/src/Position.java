/**
 * Helper class for passing multidimensional array indices for Hidato class
 * @author JT Seger
 * 4/16/2015
 * 
 */
public class Position {

	int row;
	int column;
	
	/**
	 * Position Constructor  
	 * @param row int indicating # of row
	 * @param column int indicating # of column
	 */
	public Position(int row, int column){
		this.row = row;
		this.column= column;
	}
	
	/**
	 * Getter for this.row
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Setter for this.row
	 * @param row
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * Getter for this.column
	 * @return column
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * Setter for this.column
	 * @param column
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	
	/**
	 * Equals method for testing if two Positions are equal or not in values
	 */
	public boolean equals(Object x){
		if (!(x instanceof Position)) return false;
		if(x == this) return true;
		Position toTest = (Position)x;
		return Integer.compare(this.row, toTest.getRow()) == 0 && Integer.compare(this.column, toTest.getColumn()) == 0;
	}
	
	/**
	 * toString() method for displaying Positions 
	 */
	public String toString(){
		String result = "";
		result = result + "Positions Row: " + this.getRow() + " Column: " + this.getColumn();
		return result;
	}
	
	
	
}
