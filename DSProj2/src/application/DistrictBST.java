package application;

// Class representing a Binary Search Tree (BST) for District objects
public class DistrictBST {
	private DistrictNode root; // Root node of the BST

	// Constructor to initialize an empty BST
	public DistrictBST() {
		root = null; // Initially, the root is set to null indicating an empty tree
	}

	// Method to check if the BST is empty
	public boolean isEmpty() {
		return root == null; // Returns true if the root is null, indicating an empty tree
	}

	// Method to add a district to the BST
	public void add(District district) {
		root = add(root, district); // Calls the private add method to recursively add the district
	}

	// Private helper method to add a district recursively
	private DistrictNode add(DistrictNode node, District district) {
		if (node == null)
			node = new DistrictNode(district); // If the current node is null, create a new node with the district
		else {
			// If the district name is less than the current node's district name, go left
			if (district.getDistrictName().compareToIgnoreCase(node.getElement().getDistrictName()) < 0)
				node.setLeft(add(node.getLeft(), district)); // Recursively add to the left subtree
			else
				node.setRight(add(node.getRight(), district)); // Otherwise, add to the right subtree
		}
		return node; // Return the modified node
	}

	// Method to delete a district from the BST
	public void delete(District district) {
		if (isEmpty())
			System.out.println("Tree is empty"); // If the tree is empty, print a message
		else {
			if (!contains(district, root)) {
				System.out.println("District not found"); // If the district is not found, print a message
			} else {
				root = delete(root, district); // Otherwise, call the private delete method
			}
		}
	}

	// Private helper method to delete a district recursively
	private DistrictNode delete(DistrictNode node, District district) {
		if (node == null)
			return null;
		else if (district.getDistrictName().compareTo(node.getElement().getDistrictName()) < 0)
			node.setLeft(delete(node.getLeft(), district)); // Recursively delete from the left subtree
		else if (district.getDistrictName().compareTo(node.getElement().getDistrictName()) > 0)
			node.setRight(delete(node.getRight(), district)); // Recursively delete from the right subtree
		else {
			// If the node has two children
			if (node.getLeft() != null && node.getRight() != null) {
				DistrictNode minRightNode = findMinValue(node.getRight()); // Find the minimum node in the right subtree
				node.setElement(minRightNode.getElement()); // Replace the node's element with the minimum right node's
															// element
				node.setRight(delete(node.getRight(), minRightNode.getElement())); // Delete the minimum right node
			} else { // If the node has one or less child
				node = (node.getLeft() != null) ? node.getLeft() : node.getRight(); // Set the node to its non-null
																					// child
			}
		}
		return node; // Return the modified node
	}

	// Method to perform an in-order traversal and return the values in a stack
	public Stack inOrder() {
		Stack stack = new Stack();
		inOrder(root, stack); // Calls the private inOrder method to perform the traversal
		return stack;
	}

	// Private helper method to perform an in-order traversal recursively
	private void inOrder(DistrictNode node, Stack stack) {
		if (node != null) {
			inOrder(node.getRight(), stack); // Traverse the left subtree
			stack.push(node.getElement()); // Visit the current node
			inOrder(node.getLeft(), stack); // Traverse the right subtree
		}
	}

	// Method to perform a pre-order traversal and return the values in a stack
	public Stack preOrder() {
		Stack stack = new Stack();
		preOrder(root, stack); // Calls the private preOrder method to perform the traversal
		return stack;
	}

	// Private helper method to perform a pre-order traversal recursively
	private void preOrder(DistrictNode node, Stack stack) {
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
	private void postOrder(DistrictNode node, Stack stack) {
		if (node != null) {
			postOrder(node.getLeft(), stack); // Traverse the left subtree
			postOrder(node.getRight(), stack); // Traverse the right subtree
			stack.push(node.getElement()); // Visit the current node
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

	private void levelOrder(DistrictNode node, int level, Stack stack) {
		if (node == null)
			return;
		if (level == 1)
			stack.push(node.getElement());
		else if (level > 1) {
			levelOrder(node.getLeft(), level - 1, stack);
			levelOrder(node.getRight(), level - 1, stack);
		}
	}

	// Other methods like preOrder, postOrder, levelOrder, contains, findMin,
	// findMax have similar structures
	// They follow the same recursive approach to traverse, search, and find minimum
	// and maximum values

	// Method to get the height of the BST
	public int height() {
		return height(root); // Calls the private height method to calculate the height
	}

	// Private helper method to calculate the height recursively
	private int height(DistrictNode node) {
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

	// Other methods like levelOrder, contains, findMin, findMax have similar
	// structures

	// Method to check if the BST contains a district
	public boolean contains(District district) {
		return contains(district, root); // Calls the private contains method to check if the district exists
	}

	// Private helper method to check if the BST contains a district recursively
	private boolean contains(District district, DistrictNode node) {
		if (node == null)
			return false; // If the node is null, district doesn't exist
		else if (district.getDistrictName().compareToIgnoreCase(node.getElement().getDistrictName()) < 0)
			return contains(district, node.getLeft()); // Recursively search in the left subtree
		else if (district.getDistrictName().compareToIgnoreCase(node.getElement().getDistrictName()) > 0)
			return contains(district, node.getRight()); // Recursively search in the right subtree
		else
			return true; // If the district is found, return true
	}

	// Other methods like findMin, findMax have similar structures

	// Method to find the minimum district in the BST
	public District findMin() {
		return findMinValue(root).getElement(); // Calls the private findMinValue method to find the minimum value
	}

	// Private helper method to find the minimum district recursively
	private DistrictNode findMinValue(DistrictNode node) {
		if (node == null)
			return null; // If the node is null, return null
		else if (node.getLeft() == null)
			return node; // If the left child is null, the current node is the minimum
		else
			return findMinValue(node.getLeft()); // Otherwise, recursively search in the left subtree
	}

	public District getDistrict(String districtname) {
		return getDistrict(root, districtname);
	}

	private District getDistrict(DistrictNode node, String district) {
		if (node == null)
			return null; // If the node is null, district doesn't exist
		else if (district.compareToIgnoreCase(node.getElement().getDistrictName()) < 0)
			return getDistrict(node.getLeft(), district); // Recursively search in the left subtree
		else if (district.compareToIgnoreCase(node.getElement().getDistrictName()) > 0)
			return getDistrict(node.getRight(), district); // Recursively search in the right subtree
		else
			return node.getElement(); // If the district is found, return true
	}

	// Other methods like findMax have similar structures

	// Method to find the maximum district in the BST
	public District findMax() {
		return findMaxValue(root).getElement(); // Calls the private findMaxValue method to find the maximum value
	}

	// Private helper method to find the maximum district recursively
	private DistrictNode findMaxValue(DistrictNode node) {
		if (node == null)
			return null; // If the node is null, return null
		else if (node.getRight() == null)
			return node; // If the right child is null, the current node is the maximum
		else
			return findMaxValue(node.getRight()); // Otherwise, recursively search in the right subtree
	}

	// Method to clear the BST
	public void clear() {
		root = clear(root);
	}

	// Recursive helper method to clear the tree starting from a given node
	private DistrictNode clear(DistrictNode node) {
		if (node != null) {
			// Recursively clear the left subtree
			node.setLeft(clear(node.getLeft()));
			// Recursively clear the right subtree
			node.setRight(clear(node.getRight()));
			// Set the current node to null to delete it
			node = null;
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toStringInOrder(root, sb);
		return sb.toString();
	}

	private void toStringInOrder(DistrictNode node, StringBuilder sb) {
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
