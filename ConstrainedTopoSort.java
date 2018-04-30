// Written by: Michael J. Pfeiffer
// NID: mi957047
// Date: 3/25/17

import java.io.*;
import java.util.*;

public class ConstrainedTopoSort {

	private int n;
	private ArrayList<LinkedList<Integer>> lists; // linked lists represent incoming lines
	private ArrayList<ArrayList<Integer>> outList; // two dimesional arrayList representing outgoing lines

	public ConstrainedTopoSort(String filename) throws IOException {

		Scanner in = new Scanner(new File(filename));
		n = in.nextInt();

		lists = new ArrayList<LinkedList<Integer>>(n + 1);
		outList = new ArrayList<ArrayList<Integer>>(n  + 1);

		for(int i = 0; i < n + 1; i++) {

			lists.add(new LinkedList<Integer>());
			outList.add(new ArrayList<Integer>()); 
		}
	
		int vertex = 0;

		// read input file and build lists
		while(in.hasNextLine()) {

			String s = in.nextLine();
			Scanner tokens = new Scanner(s);		
		
			if(tokens.hasNextInt())	
				tokens.nextInt(); // throw away first number

			int next;

			// build incoming/outgoing lists
			while(tokens.hasNextInt()) {
			
				lists.get(next = tokens.nextInt()).add(vertex);
				outList.get(vertex).add(next);
			}

			vertex++; // goto next vertex	
		}
	}

	// code modeled after code provided by Dr. Sean Szumlanski
	public boolean hasConstrainedTopoSort(int x, int y) {

		int count = 0;	
		int incoming [] = new int [n + 1];
		boolean xAdded = false, yAdded = false;

		if(x == y)
			return false;

		for(int i = 1; i < n + 1; i++)
			incoming[i] = lists.get(i).size();

		Queue<Integer> q = new ArrayDeque<Integer>();

		for(int i = 1; i < n + 1; i++) {	
			
			if(incoming[i] == 0) { 
				
				xAdded = (i == x) ? true : xAdded;
			
				// don't add y	
				if(i == y) 
					;
				else
					q.add(i);
			}
		}

		while(!q.isEmpty()) {

			int node = q.remove();

			count++;
			
			int sizeOut = outList.get(node).size();
	
			for(int i = 0; i < sizeOut; i++) {

				int next = outList.get(node).get(i);
				
				// don't add y
				if(--incoming[next] == 0 && next != y ) {	
					
					xAdded = (next == x) ? true : xAdded;
					q.add(next);
				}
			}

			// add y if x is in queue	
			if(xAdded && !yAdded && incoming[y] == 0) {
				
				yAdded = true;
				q.add(y);
			}
		}

		if(count != n)
			return false; // cycles or x is after y

		return true;
	}

	public static double difficultyRating() {
		return 1.5;
	}

	public static double hoursSpent() {
		return 2.5;
	}
}
