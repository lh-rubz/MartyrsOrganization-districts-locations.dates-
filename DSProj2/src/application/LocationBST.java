package application;

public class LocationBST {
	private LocationNode root;

	public LocationBST() {
		root = null;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public void add(Location location) {
		root = add(root, location);
	}

	public Location getLocation(String locationName) {
		return getLocation(root, locationName);
	}

	private Location getLocation(LocationNode node, String locationName) {
		if (node == null)
			return null; // If the node is null, location doesn't exist in the tree
		else if (locationName.compareToIgnoreCase(node.getElement().getLocationName()) < 0)
			return getLocation(node.getLeft(), locationName); // Search in the left subtree
		else if (locationName.compareToIgnoreCase(node.getElement().getLocationName()) > 0)
			return getLocation(node.getRight(), locationName); // Search in the right subtree
		else
			return node.getElement(); // Return the location if found
	}

	private LocationNode add(LocationNode node, Location location) {
		if (node == null)
			node = new LocationNode(location);
		else {
			if (location.getLocationName().compareToIgnoreCase(node.getElement().getLocationName()) < 0)
				node.setLeft(add(node.getLeft(), location));
			else
				node.setRight(add(node.getRight(), location));
		}
		return node;
	}

	public void delete(Location location) {
		if (isEmpty())
			System.out.println("Tree is empty");
		else {
			if (!contains(location, root)) {
				System.out.println("Location not found");
			} else {
				root = delete(root, location);
			}
		}
	}

	private LocationNode delete(LocationNode node, Location location) {
		if (node == null)
			return null;
		else if (location.getLocationName().compareTo(node.getElement().getLocationName()) < 0)
			node.setLeft(delete(node.getLeft(), location));
		else if (location.getLocationName().compareTo(node.getElement().getLocationName()) > 0)
			node.setRight(delete(node.getRight(), location));
		else {
			// has two children
			if (node.getLeft() != null && node.getRight() != null) {
				LocationNode minRightNode = findMinValue(node.getRight());
				node.setElement(minRightNode.getElement());
				node.setRight(delete(node.getRight(), minRightNode.getElement()));
			} else {// one or less child
				node = (node.getLeft() != null) ? node.getLeft() : node.getRight();

			}
		}
		return node;
	}

	// Method to perform an in-order traversal and return the values in a stack
	public Stack inOrder() {
		Stack stack = new Stack();
		inOrder(root, stack); // Calls the private inOrder method to perform the traversal
		return stack;
	}

	// Private helper method to perform an in-order traversal recursively
	private void inOrder(LocationNode node, Stack stack) {
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
	private void preOrder(LocationNode node, Stack stack) {
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
	private void postOrder(LocationNode node, Stack stack) {
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

	private void levelOrder(LocationNode node, int level, Stack stack) {
		if (node == null)
			return;
		if (level == 1)
			stack.push(node.getElement());
		else if (level > 1) {
			levelOrder(node.getRight(), level - 1, stack);//i reversed them beacuse i'm using them in a stack
			levelOrder(node.getLeft(), level - 1, stack);
		}
	}

	public int height() {
		return height(root);
	}

	private int height(LocationNode node) {
		if (node == null)
			return 0;
		else {
			int leftHeight = height(node.getLeft());
			int rightHeight = height(node.getRight());
			return Math.max(leftHeight, rightHeight) + 1;
		}
	}

	public boolean contains(Location location) {
		return contains(location, root);
	}

	private boolean contains(Location location, LocationNode node) {
		if (node == null)
			return false;
		else if (location.getLocationName().compareToIgnoreCase(node.getElement().getLocationName()) < 0)
			return contains(location, node.getLeft());
		else if (location.getLocationName().compareToIgnoreCase(node.getElement().getLocationName()) > 0)
			return contains(location, node.getRight());
		else
			return true;
	}

	public Location findMin() {
		return findMinValue(root).getElement();
	}

	private LocationNode findMinValue(LocationNode node) {
		if (node == null)
			return null;
		else if (node.getLeft() == null)
			return node;
		else
			return findMinValue(node.getLeft());
	}

	public Location findMax() {
		return findMaxValue(root).getElement();
	}

	private LocationNode findMaxValue(LocationNode node) {
		if (node == null)
			return null;
		else if (node.getRight() == null)
			return node;
		else
			return findMaxValue(node.getRight());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toStringInOrder(root, sb);
		return sb.toString();
	}

	private void toStringInOrder(LocationNode node, StringBuilder sb) {
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
