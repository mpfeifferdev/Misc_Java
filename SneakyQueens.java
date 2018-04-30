// Name: Michael J. Pfeiffer
// NID: mi957047 

import java.util.ArrayList;

public class SneakyQueens {

	// convert base-26 column to integer value
	private static int convertCol(String col) {

		int sum = 0;
	
		//Horner's rule, convert char to int and build polynomial
		for(int i = 0; i < col.length(); i++)
			sum = ((int)col.charAt(i) - (int)'a' + 1) + (26 * sum);

		return sum;
	}

	private static int [] parseCoordinates(String coord) {

		int i;
	
		for(i = 0; Character.isLetter(coord.charAt(i)); i++) 
			;

		int col = convertCol(coord.substring(0, i));
		int row = Integer.parseInt(coord.substring(i, coord.length()));

		return new int [] {col, row};
	}

	// given array list of queen coordinates, verify none can attack one another
	public static boolean allTheQueensAreSafe(ArrayList<String> coordinateStrings, int boardSize) {

		boolean middleTaken = false;

		int [] usedSum = new int [2 * boardSize + 1];   // sums of row + col
		int [] usedVert = new int [boardSize + 1];      // column bucket
		int [] usedHoriz = new int [boardSize + 1];     // row bucket
		int [] usedVertEdge = new int [boardSize + 1];  // points at (1, Y) 
		int [] usedHorizEdge = new int [boardSize + 1]; // points at (X, 1)
	
		for(String coordinate : coordinateStrings){
		
			int [] intCoords = parseCoordinates(coordinate);
			int col = intCoords[0], row = intCoords[1]; 			
			int sum = col + row; 

			// Given a point, the sum of the numeric row and column of that point
			// will give us the sum of all the diagonals points going from top left 
			// to bottom right.  E.g. (c, 4) -> (3, 4) = 7, and we can see (1,6), (2, 5), 
			// (3, 4), (4, 3), (5, 2), and (6, 1) are the only coords that are possible
			// with sum of 7. Also, each col/row must be unique.
			if(usedSum[sum] > 0 || usedVert[col] > 0 || usedHoriz[row] > 0)
				return false;			
			
			usedSum[sum]++;               
			usedVert[col]++;  
			usedHoriz[row]++;  

			// Now must handle diagonal from bottom left to top right. 
			// Instead of finding all diag points, find the left bottom most point
			// either on vertical or horizontal border.  E.g. (3, 4) -> Col < Row
			// max travel distance is 3 - X = 1 -> X = 2 -> Row (@ border) = Col - 2,
			// so store vertEdge[2] which corresponds to (1, 2).
			if(row < col) {

				int travel  = row - 1;
				
				if(usedHorizEdge[col - travel] > 0)
					return false;

				usedHorizEdge[col - travel]++;
			}

			else if(col < row) {

				int travel  = col - 1;

				if(usedVertEdge[row - travel] > 0)
					return false;
				
				usedVertEdge[row - travel]++;
			}
			// if row == col, then middle blocks become used
			else {

				if(middleTaken)
					return false;

				middleTaken = true;
			}
		}		
	
		return true;	
	}

	public static double difficultyRating() {

		return 3.2;
	}

	public static double hoursSpent() {
	
		return 4.5;
	}
}
