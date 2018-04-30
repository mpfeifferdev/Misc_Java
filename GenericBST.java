// Sean Szumlanski
// COP 3503, Spring 2017

// Edited by: Michael J. Pfeiffer
// NID: mi957047


// ====================
// GenericBST: BST.java
// ====================
// Basic binary search tree (BST) implementation that supports insert() and
// delete() operations. This framework is provide for you to modify as part of
// Programming Assignment #2.


import java.io.*;
import java.util.*;

// BST node must be a comparable type
class Node<T extends Comparable<T>>
{
	T data;
	Node<T> left, right;

	Node(T data)
	{
		this.data = data;
	}
}

// Binary Search tree implemented with generics
public class GenericBST<T extends Comparable<T>>
{
	private Node<T> root;
	
	public void insert(T data)
	{
		root = insert(root, data);
	}

	// insert helper function (using Overloading)
	private Node<T> insert(Node<T> root, T data)
	{
		if (root == null)
		{
			return new Node<T>(data);
		}
		// if data comes before root.data per generic compare
		else if (data.compareTo(root.data) < 0)
		{
			root.left = insert(root.left, data);
		}
		// if data comes after root.data per generic compare
		else if (data.compareTo(root.data) > 0)
		{
			root.right = insert(root.right, data);
		}
		// if data == root.data per generic compare
		else
		{
			// Stylistically, I have this here to explicitly state that we are
			// disallowing insertion of duplicate values. This is unconventional
			// and a bit cheeky.
			;
		}

		return root;
	}

	public void delete(T data)
	{
		root = delete(root, data);
	}

	// delete helper function (using Overloading)
	private Node<T> delete(Node<T> root, T data)
	{
		if (root == null)
		{
			return null;
		}
		// if data < root.data per generic comparable
		else if (data.compareTo(root.data) < 0)
		{
			root.left = delete(root.left, data);
		}
		// if data > root.data per generic comparable
		else if (data.compareTo(root.data) > 0)
		{
			root.right = delete(root.right, data);
		}
		// else data == root.data, found what looking for
		else
		{
			// leaf node, node in caller gets null
			if (root.left == null && root.right == null)
			{
				return null;
			}
			// if has only one child, return child
			else if (root.right == null)
			{
				return root.left;
			}
			else if (root.left == null)
			{
				return root.right;
			}
			// else has two children, replace current node data with largest item
			// in left subtree, then remove largest from prev location
			else
			{
				root.data = findMax(root.left);
				root.left = delete(root.left, root.data);
			}
		}

		return root;
	}

	// This method assumes root is non-null, since this is only called by
	// delete() on the left subtree, and only when that subtree is non-empty.
	private T findMax(Node<T> root)
	{
		// max in BST is the right-most node
		while (root.right != null)
		{
			root = root.right;
		}

		return root.data;
	}

	// Returns true if the value is contained in the BST, false otherwise.
	public boolean contains(T data)
	{
		return contains(root, data);
	}

	// Overloaded method, find generic data in generic tree
	private boolean contains(Node<T> root, T data)
	{
		// not in tree
		if (root == null)
		{
			return false;
		}
		// data is smaller than current node data, check left subtree
		else if (data.compareTo(root.data) < 0)
		{
			return contains(root.left, data);
		}
		// data is larger than current node data, check right subtree
		else if (data.compareTo(root.data) > 0)
		{
			return contains(root.right, data);
		}
		// YAR! thar she blows!
		else
		{
			return true;
		}
	}

	public void inorder()
	{
		System.out.print("In-order Traversal:");
		inorder(root);
		System.out.println();
	}
	// print generic BST in inorder
	private void inorder(Node<T> root)
	{
		if (root == null)
			return;

		inorder(root.left);
		System.out.print(" " + root.data);
		inorder(root.right);
	}

	public void preorder()
	{
		System.out.print("Pre-order Traversal:");
		preorder(root);
		System.out.println();
	}
	// print generic BST in preorder
	private void preorder(Node<T> root)
	{
		if (root == null)
			return;

		System.out.print(" " + root.data);
		preorder(root.left);
		preorder(root.right);
	}

	public void postorder()
	{
		System.out.print("Post-order Traversal:");
		postorder(root);
		System.out.println();
	}
	// print generic BST in postorder
	private void postorder(Node<T> root)
	{
		if (root == null)
			return;

		postorder(root.left);
		postorder(root.right);
		System.out.print(" " + root.data);
	}

	public static double difficultyRating() {
		
		return 1.0;
	}

	public static double hoursSpent() {

		return 1.0;
	}
}
