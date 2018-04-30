// Wirtten by:	Michael J. Pfeiffer
// NID:		mi957047
// Date:	3/31/17


import java.util.*;
import java.io.*;


class BuildTopo {

	int count;
	Queue<Integer> pendingQ;
	StringBuilder string;
	int [] strEnd; // last strLen index for each recursive level
}

class EdgeLists {

	int n;
	int [] incoming;
	ArrayList<LinkedList<Integer>> inList;
	ArrayList<LinkedList<Integer>> outList;
}

public class TopolALLgical {


	public static HashSet<String> allTopologicalSorts(String filename) throws IOException {

		EdgeLists el;
		HashSet<String>	h = new HashSet<>();			

		if((el = initializeLists(filename)) == null)
			return h;

		BuildTopo topo = new BuildTopo();

		topo.count = 0;
		topo.pendingQ = new ArrayDeque<>();
		topo.string = new StringBuilder();
		topo.strEnd = new int [el.n];

		ArrayDeque<Integer> initState = new ArrayDeque<>();

		// fill first level queue with initial vectors with 0 incoming
		for(int i = 1; i <= el.n; i++) 
			if((el.incoming[i] = el.inList.get(i).size()) == 0)
				initState.add(i);
		
		allTopologicalSorts(el, topo, initState, h);

		return h;
	}

	public static void allTopologicalSorts(EdgeLists el, BuildTopo topo, 
					       ArrayDeque<Integer> incomingState, HashSet<String> h) {
		// base case, string is finished
		if(topo.count == el.n) {
			
			h.add(topo.string.toString());
			return;
		}

		// clone of zero incoming queue needed to avoid empty queue after recursion
		ArrayDeque<Integer> initState = incomingState.clone();
		
		while(!topo.pendingQ.isEmpty())
			initState.add(topo.pendingQ.remove());
		
		int initialQSize = initState.size();

		// to avoid adding visited vertices, only try each vertex once
		for(int i = 0; i < initialQSize; i++) {

			int node = initState.remove(); // try first

			changeState(el, topo, node);
			allTopologicalSorts(el, topo, initState, h);
			revertState(el, topo, node);	
	
			initState.add(node); // put first at back
		}
	}

	public static void changeState(EdgeLists el, BuildTopo topo, int node) {

		adjustIncoming(el, topo, node, false); // decrement incoming, add pending

		topo.strEnd[topo.count] = topo.string.length();
		
		topo.string.append(node);
		
		if(topo.count < el.n - 1)
			topo.string.append(' ');
		
		topo.count++;

	}

	public static void revertState(EdgeLists el, BuildTopo topo, int node) {

		topo.count--;	
		
		topo.string.replace(topo.strEnd[topo.count], topo.string.length(), "");

		adjustIncoming(el, topo, node, true); // increment incoming
	}

	public static void adjustIncoming(EdgeLists el, BuildTopo topo, int vertex, boolean increment) {
	
		int size = el.outList.get(vertex).size();
		int node;

		for(int i = 0; i < size; i++) {

			node = el.outList.get(vertex).get(i);

			if(increment)
				el.incoming[node]++;
			else  
				el.incoming[node]--;
				
			if(el.incoming[node] == 0)
				topo.pendingQ.add(node);	
		}
	} 

	public static EdgeLists initializeLists(String filename) throws IOException {

		EdgeLists el = new EdgeLists();
		Scanner in = new Scanner(new File(filename));
		
		if(in.hasNextInt())
			el.n = in.nextInt();
		else
			return null;

		if(el.n <= 0)
			return null;


		el.incoming = new int [el.n + 1];
		el.inList = new ArrayList<>(el.n + 1);
		el.outList = new ArrayList<>(el.n + 1);

		for(int i = 0; i <= el.n; i++) {

			el.inList.add(new LinkedList<Integer>());
			el.outList.add(new LinkedList<Integer>());
		}

		int vertex = 1, numberOfOutgoing, next;

		while(in.hasNextInt()) {

			numberOfOutgoing = in.nextInt(); // # outgoing from current vertex
			
			for(int i = 0; i < numberOfOutgoing; i++) {

				el.inList.get(next = in.nextInt()).add(vertex);
				el.outList.get(vertex).add(next);
			}
			
			vertex++;
		}

		return el;
	}

	public static double difficultyRating() {

		return 2.2;
	}
	
	public static double hoursSpent() {

		return 5.5;
	}
}
