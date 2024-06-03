package application;

public class DateNode {
    private DateNode left, right; // References to the left and right child nodes
    private Date element; // Element (date) stored in the node

    // Default constructor
    public DateNode() {
        // Initialize left and right child nodes to null, and element to null
        setRight(setLeft(null));
        setElement(null);
    }

    // Constructor with a date parameter
    public DateNode(Date date) {
        // Initialize left and right child nodes to null, and element to the given date
        setRight(setLeft(null));
        setElement(date);
    }

    // Getter for the right child node
    public DateNode getRight() {
        return right;
    }

    // Setter for the right child node
    public void setRight(DateNode right) {
        this.right = right;
    }

    // Getter for the left child node
    public DateNode getLeft() {
        return left;
    }

    // Setter for the left child node
    public DateNode setLeft(DateNode left) {
        this.left = left;
        return left; // Return the left child node for convenience
    }

    // Getter for the element (date)
    public Date getElement() {
        return element;
    }

    // Setter for the element (date)
    public void setElement(Date element) {
        this.element = element;
    }
}
