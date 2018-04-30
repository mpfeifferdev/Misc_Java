// Written by: Michael J. Pfeiffer
// NID:        mi957047
// Date:       4/21/17

import java.io.*;
import java.util.*;

public class RunLikeHell {

	public static int maxGain(int [] blocks) {
	
		if(blocks == null || blocks.length == 0)
			return 0;

		// space optimization since only need 2 previous values
		int [] maxArray = new int [2];
		int maxVal;	
	
		maxArray[0] = blocks[0];
		maxArray[1] = (blocks.length < 2) ? 0 : blocks[1];
	
		maxVal = maxArray[0];

		for(int i = 2; i < blocks.length; i++) {
		
			// maintain best option block (maxVal) until better option found
			maxVal = (maxArray[i % 2] > maxVal) ? maxArray[i % 2] : maxVal;

			// max value at current block is max of previous block
			// OR current block plus the best available option at least 2 blocks back	
			maxArray[i % 2] = Math.max(maxVal + blocks[i], maxArray[i % 2]); 
		}

		return Math.max(maxArray[0], maxArray[1]);
	}

	public static double difficultyRating() {

		return 1.5;
	}

	public static double hoursSpent() {

		return 2.75;
	}

}
