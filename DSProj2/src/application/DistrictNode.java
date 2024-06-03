package application;

public class DistrictNode {
    private DistrictNode left, right;
    private District element;

    public DistrictNode() {
        setRight(setLeft(null));
        setElement(null);
    }

    public DistrictNode(District district) {
        setRight(setLeft(null));
        setElement(district);
    }

    public DistrictNode getRight() {
        return right;
    }

    public void setRight(DistrictNode right) {
        this.right = right;
    }

    public DistrictNode getLeft() {
        return left;
    }

    public DistrictNode setLeft(DistrictNode left) {
        this.left = left;
        return left;
    }

    public District getElement() {
        return element;
    }

    public void setElement(District element) {
        this.element = element;
    }
}
