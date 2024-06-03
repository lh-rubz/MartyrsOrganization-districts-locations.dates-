package application;

import java.time.LocalDate;

public class DateBST {
	private DateNode root; // Root node of the binary search tree

	// Constructor to initialize an empty binary search tree
	public DateBST() {
		root = null; // Initially, the tree is empty
	}

	// Method to check if the binary search tree is empty
	public boolean isEmpty() {
		return root == null; // If root is null, the tree is empty
	}

	// Method to add a date to the binary search tree
	public void add(Date date) {
		root = add(root, date); // Call the private recursive add method
	}

	// Recursive method to add a date to the binary search tree
	private DateNode add(DateNode node, Date date) {
		if (node == null)
			node = new DateNode(date); // If the node is null, create a new node with the given date
		else {
			// Compare the date with the date of the current node to determine the insertion
			// position
			if (date.getDate().compareToIgnoreCase(node.getElement().getDate()) < 0)
				node.setLeft(add(node.getLeft(), date)); // Insert into the left subtree
			else
				node.setRight(add(node.getRight(), date)); // Insert into the right subtree
		}
		return node;
	}

	// Method to delete a date from the binary search tree
	public void delete(Date date) {
		if (isEmpty())
			System.out.println("Tree is empty"); // If the tree is empty, display a message
		else {
			if (!contains(date, root)) {
				System.out.println("Date not found"); // If the date is not found, display a message
			} else {
				root = delete(root, date); // Call the private recursive delete method
			}
		}
	}

	// Recursive method to delete a date from the binary search tree
	private DateNode delete(DateNode node, Date date) {
		if (node == null)
			return null;
		else if (date.getDate().compareTo(node.getElement().getDate()) < 0)
			node.setLeft(delete(node.getLeft(), date)); // Delete from the left subtree
		else if (date.getDate().compareTo(node.getElement().getDate()) > 0)
			node.setRight(delete(node.getRight(), date)); // Delete from the right subtree
		else {
			// If the node has two children
			if (node.getLeft() != null && node.getRight() != null) {
				// Find the minimum value in the right subtree
				DateNode minRightNode = findMinValue(node.getRight());
				// Replace the node's element with the minimum value
				node.setElement(minRightNode.getElement());
				// Delete the minimum value node from the right subtree
				node.setRight(delete(node.getRight(), minRightNode.getElement()));
			} else { // If the node has one or less child
				node = (node.getLeft() != null) ? node.getLeft() : node.getRight(); // Promote the child
			}
		}
		return node;
	}

	// Method to perform a pre-order traversal and return the values in a stack
	public Stack preOrder() {
		Stack stack = new Stack();
		preOrder(root, stack); // Calls the private preOrder method to perform the traversal
		return stack;
	}

	// Private helper method to perform a pre-order traversal recursively
	private void preOrder(DateNode node, Stack stack) {
		if (node != null) {
			stack.push(node.getElement()); // Visit the current node
			preOrder(node.getLeft(), stack); // Traverse the left subtree
			preOrder(node.getRight(), stack); // Traverse the right subtree
		}
	}

	// Method to perform a post-order traversal and return the values in a stack
	public Stack postOrder() {
		Stack stack = new Stack();
		postOrder(root, stack); // Calls the private postOrder method to perform the traversal
		return stack;
	}

	// Private helper method to perform a post-order traversal recursively
	private void postOrder(DateNode node, Stack stack) {
		if (node != null) {
			postOrder(node.getLeft(), stack); // Traverse the left subtree
			postOrder(node.getRight(), stack); // Traverse the right subtree
			stack.push(node.getElement()); // Visit the current node
		}
	}

	// Method to perform an in-order traversal and return the values in a stack
	public Stack inOrder() {
		Stack stack = new Stack();
		inOrder(root, stack); // Calls the private inOrder method to perform the traversal
		return stack;
	}

	// Private helper method to perform an in-order traversal recursively
	private void inOrder(DateNode node, Stack stack) {
		if (node != null) {
			inOrder(node.getRight(), stack); // Traverse the right subtree
			stack.push(node.getElement()); // Visit the current node
			inOrder(node.getLeft(), stack); // Traverse the left subtree
			// i started from the right then left because it's a stack and i want the first
			// element to pop to be the smallest here
		}
	}

	public Stack levelOrder() {
		Stack stack = new Stack();
		int height = height(root);
		for (int i = 1; i <= height; i++) {
			levelOrder(root, i, stack);
		}
		return stack;
	}

	private void levelOrder(DateNode node, int level, Stack stack) {
		if (node == null)
			return;
		if (level == 1)
			stack.push(node.getElement());
		else if (level > 1) {
			levelOrder(node.getLeft(), level - 1, stack);
			levelOrder(node.getRight(), level - 1, stack);
		}
	}

	// Method to get the height of the BST
	public int height() {
		return height(root); // Calls the private height method to calculate the height
	}

	// Private helper method to calculate the height recursively
	private int height(DateNode node) {
		if (node == null)
			return 0; // If the node is null, height is 0
		else {
			// Calculate the height of the left and right subtrees
			int leftHeight = height(node.getLeft());
			int rightHeight = height(node.getRight());
			// Return the maximum height + 1
			return Math.max(leftHeight, rightHeight) + 1;
		}
	}

	public Date getDate(String date) {
		return getDate(root, date);
	}

	private Date getDate(DateNode node, String date) {
		if (node == null)
			return null; // If the node is null, date doesn't exist in the tree
		else if (date.compareTo(node.getElement().getDate()) < 0)
			return getDate(node.getLeft(), date); // Search in the left subtree
		else if (date.compareTo(node.getElement().getDate()) > 0)
			return getDate(node.getRight(), date); // Search in the right subtree
		else
			return node.getElement(); // Return the location if found
	}

	// Other traversal methods (pre-order, post-order, level-order) follow similar
	// structure

	// Method to check if a date exists in the binary search tree
	public boolean contains(Date date) {
		return contains(date, root); // Call the private recursive contains method
	}

	// Recursive method to check if a date exists in the binary search tree
	private boolean contains(Date date, DateNode node) {
		if (node == null)
			return false; // If node is null, date doesn't exist in this subtree
		else if (date.getDate().compareToIgnoreCase(node.getElement().getDate()) < 0)
			return contains(date, node.getLeft()); // Search left subtree
		else if (date.getDate().compareToIgnoreCase(node.getElement().getDate()) > 0)
			return contains(date, node.getRight()); // Search right subtree
		else
			return true; // Date found
	}

	// Method to find the minimum date in the binary search tree
	public Date findMin() {
		return findMinValue(root).getElement(); // Call the private method to find the minimum value
	}

	// Recursive method to find the minimum date in the binary search tree
	private DateNode findMinValue(DateNode node) {
		if (node == null)
			return null; // If node is null, return null
		else if (node.getLeft() == null)
			return node; // If left child is null, this node has minimum value
		else
			return findMinValue(node.getLeft()); // Recur for left subtree
	}

	// Method to find the maximum date in the binary search tree
	public Date findMax() {
		return findMaxValue(root).getElement(); // Call the private method to find the maximum value
	}

	// Recursive method to find the maximum date in the binary search tree
	private DateNode findMaxValue(DateNode node) {
		if (node == null)
			return null; // If node is null, return null
		else if (node.getRight() == null)
			return node; // If right child is null, this node has maximum value
		else
			return findMaxValue(node.getRight()); // Recur for right subtree
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toStringInOrder(root, sb);
		return sb.toString();
	}

	private void toStringInOrder(DateNode node, StringBuilder sb) {
		if (node == null)
			return;

		// Traverse the left subtree
		toStringInOrder(node.getLeft(), sb);

		// Append the element
		sb.append(node.getElement().toString()).append("\n");

		// Traverse the right subtree
		toStringInOrder(node.getRight(), sb);
	}
}
