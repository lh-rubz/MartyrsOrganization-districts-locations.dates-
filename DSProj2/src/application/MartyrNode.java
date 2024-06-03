package application;

public class MartyrNode {
	private Martyr element;
	private MartyrNode next;

	public MartyrNode(Martyr element) {
		this(element, null);
	}

	public MartyrNode(Martyr element2, MartyrNode next) {

		this.setElement(element2);
		this.setNext(next);
	}

	public Martyr getElement() {
		return element;
	}

	public void setElement(Martyr element) {
		if (element != null)
			this.element = element;
	}

	public MartyrNode getNext() {
		return next;
	}

	public void setNext(MartyrNode next) {
		this.next = next;
	}
}