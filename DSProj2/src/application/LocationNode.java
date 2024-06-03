package application;

public class LocationNode {
    private LocationNode left, right;
    private Location element;

    public LocationNode() {
        setRight(setLeft(null));
        setElement(null);
    }

    public LocationNode(Location location) {
        setRight(setLeft(null));
        setElement(location);
    }

    public LocationNode getRight() {
        return right;
    }

    public void setRight(LocationNode right) {
        this.right = right;
    }

    public LocationNode getLeft() {
        return left;
    }

    public LocationNode setLeft(LocationNode left) {
        this.left = left;
        return left;
    }

    public Location getElement() {
        return element;
    }

    public void setElement(Location element) {
        this.element = element;
    }
}
