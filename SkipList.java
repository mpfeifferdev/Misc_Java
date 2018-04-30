// Written by: Michael J. Pfeiffer
// Date:       2/25/2017
// NID:        mi957047

import java.io.*;
import java.util.*;

class Node<T extends Comparable<T>> {

	private ArrayList<Node<T>> next;
	private T data;

	Node() {
		this(null, 0);
	}
		
	Node(int height) {

		this(null, height);
	}

	Node(T data, int height) {

		height = (height < 1) ? 1 : height; // min height is 1
		next = new ArrayList<>(height);
		this.data = data;
		
		for(int i = 0; i < height; i++)    // initialize next pointers to null
			next.add(null);
	}

	public T value() {

		return data;
	}

	public int height() {
	
		return next.size();
	}

	public Node<T> next(int level) {

		return (level < 0 || level > height() - 1) ? null : next.get(level);
	}

	public void setNext(int level, Node<T> node) {

		next.set(level, node);
	}
	
	public void grow() {

		next.add(null);
	}

	public void maybeGrow() {

		if(Math.random() < 0.5) 
			next.add(null);
	}

	public void trim(int height) {

		// if desired trim height is larger than current height
		if (height > height()) {

			System.out.println("Unable to trim to " + height + " from " + height() + ". Exiting");
			System.exit(2);
		}
		// if desired trim height is less than minimum
		else if(height < 1) {

			System.out.println("Unable to trim to " + height + ". Min height is 1. Exiting.");
			System.exit(3);
		}

		while(height() > height)
			next.remove(height() - 1);

		next.trimToSize();
	}
}	

public class SkipList<T extends Comparable<T>> {
	
	private Node<T> head;
	private int size;

	SkipList() {

		this(1);
	}

	SkipList(int height) {

		height = (height < 1) ? 1 : height;
		head = new Node<T>(height);
		size = 0;
	}
	
	public int size() {

		return size;
	}

	public int height() {

		return head.height();
	}

	public Node<T> head() {

		return head;
	}	

	public void insert(T data) {

		insert(data, Integer.MIN_VALUE);				
	}

	public void insert(T data, int height) {

		Node<T> newNode, tempNode;

		if(data == null)
			return;
		
		if(height > height()) {

			System.out.printf("Insert node height exceeds skip list height.  Exiting.");
			System.exit(10);
		}

		if(getMaxHeight(++size) > height())  
			growSkipList();


		if(height == Integer.MIN_VALUE)
			newNode = new Node<>(data, generateRandomHeight(max(height(), getMaxHeight(size))));
		else
			newNode = new Node<>(data, height);


		tempNode = head;

		for(int i = height() - 1; i >= 0; i--) {

			while(tempNode.next(i) != null && data.compareTo(tempNode.next(i).value()) > 0)
				tempNode = tempNode.next(i);

			if(i < newNode.height()) {

				newNode.setNext(i, tempNode.next(i));
				tempNode.setNext(i, newNode);
			}
		}
	}

	public void delete(T data) {

		int cVal = -1;
		Node <T> tempNode = head;
		Node<T> deleteMe = null;
		ArrayList<Node<T>> pointers = new ArrayList<>();

		if(data == null)
			return;	

		for(int i = height() - 1; i >= 0; i--) {

			while(tempNode.next(i) != null && (cVal = data.compareTo(tempNode.next(i).value())) > 0)
				tempNode = tempNode.next(i);

			if(tempNode.next(i) != null && cVal == 0) {
					 	
				pointers.add(tempNode);  // 0 is top level, size() - 1 is floor level
				deleteMe = tempNode.next(i); // most recent assignment will be the ONE
			}
		}
		
		if(pointers.size() > 0) {
			// assign links (extra top level links never get used)  
			for(int i = 0; i < deleteMe.height(); i++) {

				int index = pointers.size() - i - 1; // start bottom level
 				
				tempNode = pointers.get(index);	     
				tempNode.setNext(i, tempNode.next(i).next(i));
			}

			if(getMaxHeight(--size) < height())	
				trimSkipList();
		}
	}

	public boolean contains(T data) {

		return (get(data) == null) ? false : true;
	}

	public Node<T> get(T data) {

		int cVal = -1;
		Node <T> tempNode = head;

		if(data == null)
			return null;
	
		for(int i = height() - 1; i >= 0; i--) {

			while(tempNode.next(i) != null && (cVal = data.compareTo(tempNode.next(i).value())) > 0)
				tempNode = tempNode.next(i);

			if(tempNode.next(i) != null && cVal == 0)
				return tempNode.next(i);
		}

		return null;
	}

	public static double difficultyRating() {

		return 4.5;
	}

	public static double hoursSpent() {

		return 10.0;
	}

	private static int getMaxHeight(int n) {
			
		if(n < 2)
			return 1;

		double heightFormula = Math.log(n)/Math.log(2);
		// use episilon
		return (heightFormula - (int)heightFormula) < 1E-10 ? (int)heightFormula 
				: (int)heightFormula + 1;
	}

	private static int generateRandomHeight(int maxHeight) {

		int height;

		for(height = 1; height < maxHeight; height++)
			if(Math.random() < 0.5)
				break;
	
		return height;
	}

	private static int max(int a, int b) {

		return (a > b) ? a : b;
	}

	private void growSkipList() {

		head.grow();  // new level of head is now null

		Node<T> walkNode, assignNode;
		walkNode = assignNode = head;

		int activeLevel = height() - 2;  // previous top level

		while(walkNode.next(activeLevel) != null) {
			
			int recordedHeight = walkNode.next(activeLevel).height();					
			walkNode.next(activeLevel).maybeGrow();

			if(walkNode.next(activeLevel).height() > recordedHeight) {
				
				assignNode.setNext(activeLevel + 1, walkNode.next(activeLevel));
				assignNode = walkNode.next(activeLevel);
			}
			
			walkNode = walkNode.next(activeLevel);
		}	
	}

	private void trimSkipList() {

		int trimLevel = getMaxHeight(size);
		Node<T> currentNode, nextNode;
		
		currentNode = head;

		while(currentNode != null) {

			nextNode = currentNode.next(trimLevel);
			currentNode.trim(trimLevel);
			currentNode = nextNode;			
		}
	}
}
