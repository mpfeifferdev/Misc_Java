// Name: Michael J. Pfeiffer
// NID: mi957047 

import java.util.ArrayList;
import java.util.HashSet;

class Coordinate {
	
	private Integer row, col;

	Coordinate(int row, int col) {
	
		this.row = row;
		this.col = col;
	}

	public int getRow() {

		return row;
	}

	public int getCol() {

		return col;
	}
	
	// random prime number used for new hash functions	
	@Override
	public int hashCode() {
	
		return row.hashCode() * 53 + col.hashCode(); 
	}

	@Override
	public boolean equals(Object o) {

		if(o instanceof Coordinate) {

			Coordinate c = (Coordinate)o;
			return (row == c.getRow()) && (col == c.getCol());
		}
		
		return false;
	}
	
	// could have also used coordinate.toString().hashCode()
	// but that greatly increases runtime 
	@Override
	public String toString() {

		return "" + col + row;
	}
}

public class SneakyKnights {

	// convert base-26 column to integer value
	private static int convertCol(String col) {

		int sum = 0;
	
		//Horner's rule, convert char to int and build polynomial
		for(int i = 0; i < col.length(); i++)
			sum = ((int)col.charAt(i) - (int)'a' + 1) + (26 * sum);

		return sum;
	}

	// converts and parses coordinate String into int values row, col, returns Coordinate object
	// O(k)
	private static Coordinate parseCoordinates(String coord) {
	
		int i;

		for(i = 0; Character.isLetter(coord.charAt(i)); i++) 
			;

		int col = convertCol(coord.substring(0, i));
		int row = Integer.parseInt(coord.substring(i, coord.length()));

		return new Coordinate(row, col);
	}

	// check whether a coordinate is within the bounds of boardSize
	// O(1)
	public static boolean isInBounds(int row, int col, int boardSize) {

		return  (row <= boardSize) && (row >= 1) && (col <= boardSize) && (col >= 1);
	}

	// takes a String of Knight coordinates and returns true if none of the knights can attack
	// each other; otherwise, false
	public static boolean allTheKnightsAreSafe(ArrayList<String> coordinateStrings, int boardSize) {

		HashSet<Coordinate> spotsNotAvailable = new HashSet<>();

		// array of all move adjustments possible for each knight
		int [][] movePositions = new int [][] { {2 , 1}, {2, -1}, {-2,  1}, {-2, -1},
                                                        {1, -2}, {1,  2}, {-1, -2}, {-1,  2} };
	
		for(String s : coordinateStrings) {

			Coordinate newCoord = parseCoordinates(s);
						
			spotsNotAvailable.add(newCoord);
						
			for(int i = 0; i < 8; i++) {
	
				int row = newCoord.getRow() + movePositions[i][0];
				int col = newCoord.getCol() + movePositions[i][1];

				// if move adjustment in bounds, check if move already in HashSet (hit)
				if(isInBounds(row, col, boardSize))
					if(spotsNotAvailable.contains(new Coordinate(row, col))) 
						return false;
			}
		}

		return true;
	}	

	public static double difficultyRating() {

		return 3.5;
	}

	public static double hoursSpent() {
	
		return 4.5;
	}
}
