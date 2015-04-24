import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


/**
 * Class Hidato models a game of Hidato by creating a board from a .txt file
 *  and solving it, displaying all possible solutions in it's toString().  
 * @author JT Seger
 * 4/16/2015
 * NOTE that the game.txt file has been modified to allow for multiple solutions
 * to exist.  47 and 49 values have been removed.
 */

public class Hidato {
	
private int columns, rows, recursionCount, numberOfSolutions;
private String[][] board;
private ArrayList<String[][]> solvedBoards;
private HashMap<Integer, Position> valuesOnBoard;
private Position end, startPosition;

/**
 * Constructor for class Hidato.  Works by asking user for .txt file input,
 * then constructs a Hidato board based on file's specified rows 
 * and columns.  Throws FileNotFoundException if invalid file name is provided.
 */
public Hidato()
{	System.out.print("Please enter board .txt file: ");
	Scanner userInput = new Scanner(System.in);
	String file = userInput.nextLine();
	userInput.close();
	try {
		 Scanner input = new Scanner(new File(file));
		 String row = input.next();
		 String column = input.next();
		this.rows = Integer.parseInt(row);
		this.columns = Integer.parseInt(column);
		input.nextLine();
		this.board = createBoard(this.rows, this.columns, input);
		
	} catch (FileNotFoundException e) {
		System.out.println("Invalid file name");
		e.printStackTrace();
	}
} 

/**
 * Creates Hidato board by initializing a String[][] of size specified by 
 * parameters.  As it constructs the board, it records the starting position 
 * and ending position, and all numbers already on board.
 * @param rows String specifying number of rows in Hidato game
 * @param columns String specifying number of columns in Hidato game
 * @param input Scanner for reading in board
 * @return String[][] of constructed Hidato board
 */
private String[][] createBoard(int rows, int columns, Scanner input) {
		int highestValue = 0;
		this.valuesOnBoard = new HashMap<Integer, Position>();
		String[][]board = new String[rows][columns];
		int number = 0;
		for(int i=0;i<rows;i++){
			Scanner line = new Scanner(input.nextLine());
			for(int j=0;j<columns;j++){
			
				String valueToAdd = line.next();
				if (!valueToAdd.equals("--") && !valueToAdd.equals("**")){
				
				 number = Integer.parseInt(valueToAdd);
				Position numberPosition = new Position(i, j);
				if(number == 1)
					this.startPosition = numberPosition;
				
				if (number > highestValue){
					this.end = numberPosition;
					highestValue = number;                    
				}
				this.valuesOnBoard.put(number, numberPosition);
				}
				board[i][j] = valueToAdd;
			}
			line.close();
		}
		return board;
}

/**
 * Recursive caller method for solve() which solves a Hidato board through backtracking.
 * @return Boolean if board was solved or not
 */
	private Boolean solve(){
		this.solvedBoards = new ArrayList<String[][]>();
		int targetValue = 1;
		this.numberOfSolutions = 0;
		this.recursionCount = 0;
		return solve(targetValue, this.startPosition);	
	}
	

	
/**
 * Recursive backtracking algorithm to solve Hidato board
 * @param targetValue is the value which must be inserted next on board
 * @param currentPosition is the current Position of Hidato board.
 * @return Base case true if currentPosition.equals(this.end)
 *  if it can set a value or neighbor value is targetValue, call recursion on that position. otherwise return false.
 *  All solved boards are copied to this.solvedBoards ArrayList.
 */
private Boolean solve(int targetValue, Position currentPosition) {
	this.recursionCount++;
	targetValue++;
	//BASE CASE
	if(currentPosition.equals(this.end)){
		this.numberOfSolutions++; 									//increment number of solutions
		String[][] solvedBoard = this.copyStringMatrix(this.board); //Copy current board
		this.solvedBoards.add(solvedBoard);						  	//Add instance of solved board to list of solved boards
		return false; 			//Note we return false here because we want to keep searching for other solutions.
	}
	
	ArrayList<Position> currentNeighbors = getNeighborPositions(currentPosition);
	if(this.valuesOnBoard.containsKey(targetValue)){
		Position targetPosition = this.valuesOnBoard.get(targetValue);
		if(currentNeighbors.contains(targetPosition)){
		if(solve(targetValue, targetPosition))
			return true;		
		else {return false;}		
		}
		else {return false;}
		
	}
	else{
		for(Position neighbor : currentNeighbors){
			if(!isNumber(neighbor)){
				setValue(neighbor, targetValue);
			if(solve(targetValue, neighbor))
				return true;	
			else 
				setValue(neighbor, 0);	
			}
		}
		return false;
	}		
}

/**
 * Helper method for ridding a list of neighbors of all "**" values		
 * @param neighbors list of Positions to clean
 * @return a cleaned ArrayList of Positions
 */
private ArrayList<Position> cleanNeighbors(ArrayList<Position> neighbors){
	ArrayList<Position> result = new ArrayList<Position>();
	for(Position position : neighbors){
		if(isNumber(position)){
			result.add(position);
		}
		else{
		String value = retrieveValue(position);
		if(value.equals("--")){
			result.add(position);
			}
		}
	}
	return result;
}

/**
 * Sets a given Hidato board Position to an Integer value converted into a String.  
 * If the value is a 0, then it sets the positions value to "--"
 * @param position to set value
 * @param value is the value to set
 */
private void setValue(Position position, Integer value){
	if(value.equals(0))	this.board[position.getRow()][position.getColumn()] = "--";
	else this.board[position.getRow()][position.getColumn()] = "" + value;
}

/**
 * Finds the neighbors of a given position on Hidato board
 * @param position is the position ti find neighbors surrounding
 * @return ArrayList of Positions, cleaned for any "**" values
 */
private ArrayList<Position> getNeighborPositions(Position position){
	ArrayList<Position> neighbors = new ArrayList<Position>();
	int row = position.getRow();
	int column = position.getColumn();
	if (row != 0 && column != 0){
		neighbors.add(new Position(row-1, column-1));
	}
	if (row!=0){
		neighbors.add(new Position(row-1, column));
	}
	if (row!=0 && column!=this.columns-1){
		neighbors.add(new Position(row-1, column+1));
	}
	if (column!=0){
		neighbors.add(new Position(row, column-1));
	}
	if(column!=this.columns-1){
		neighbors.add(new Position(row, column +1));
	}
	if(row != this.rows-1 && column != 0){
		neighbors.add(new Position(row+1, column-1));
	}
	if(row != this.rows - 1){
		neighbors.add(new Position(row +1, column));
	}
	if(row != this.rows-1 && column != this.columns -1){
		neighbors.add(new Position(row +1, column +1));
	}
		return cleanNeighbors(neighbors);
}

/**
 * Retrieves the String value for a given Hidato board position
 * @param position
 * @return
 */
	private String retrieveValue(Position position){
		return this.board[position.getRow()][position.getColumn()];	
	}
	
	/**
	 * Checks if a certain position on Hidato board is a number or not
	 * @param position
	 * @return true if value at position does not equal "--" or "**", false otherwise.
	 */
	private Boolean isNumber(Position position){
		String value = this.board[position.getRow()][position.getColumn()];
		if(value.equals("--") || value.equals("**")){
			return false;
		}
		return true;
	}

	/**
	 * Displays number of recursive calls in solving a Hidato board
	 * @return String displaying number of recursive calls
	 */
	private String numRecursiveCalls(){
		String toReturn = "Called Recursion: " + this.recursionCount + " times.";
		return toReturn;
	}
	
	/**
	 * Hidato toString which displays all solved boards, number of solutions,
	 *  number of recursive calls, and number of backtracks.  
	 */
	public String toString(){
		if(this.numberOfSolutions > 0){
		String toReturn = "Number of solutions: " + this.numberOfSolutions + "\n";
		for(String[][] board : this.solvedBoards){
		 toReturn = toReturn + "\n";
		for(String[] row : board){
		for (String value : row){
			if (value.length()==1) value = " " + value;
			 toReturn = toReturn + value + ", ";
		}
		  toReturn = toReturn + "\n";
		}
		}
		return toReturn;
	}
		else
		return "No solutions exist for this Hidato game";	
	
	}
	
	/**
	 * Returns a copy of a multidimensional String array.  Helper method in Hidato toString()
	 * @param input String[][] to copy
	 * @return copy of input
	 */
	private String[][] copyStringMatrix(String[][] input) {
	    if (input == null)
	        return null;
	    String[][] result = new String[input.length][];
	    for (int r = 0; r < input.length; r++) {
	        result[r] = input[r].clone();
	    }
	    return result;
	}
	
	/**
	 * Creates a new game of Hidato by asking for user txt file, creating board, solving board, and then displaying results.
	 * @param args
	 */
	public static void main(String[] args){
		Hidato test = new Hidato();
		test.solve();
		System.out.println(test);
		System.out.println(test.numRecursiveCalls());
	}
}
