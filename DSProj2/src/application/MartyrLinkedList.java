package application;

public class MartyrLinkedList {
	private MartyrNode Front, Back;
	private int size;

	// Get the first martyr in the list
	public Martyr getFirst() {
		return Front != null ? Front.getElement() : null;
	}

	// Check if a martyr with similar attributes already exists in the list
	public boolean martyrExists(Martyr newMartyr) {
		MartyrNode current = Front;
		while (current != null) {
			Martyr existingMartyr = current.getElement();
			// Check if all attributes are similar
			if (existingMartyr.getName().equals(newMartyr.getName())
					&& existingMartyr.getDate().equals(newMartyr.getDate())
					&& existingMartyr.getAge() == newMartyr.getAge()
					&& existingMartyr.getLocation().equals(newMartyr.getLocation())
					&& existingMartyr.getDistrict().equals(newMartyr.getDistrict())
					&& existingMartyr.getGender() == newMartyr.getGender()) {
				return true; // Similar martyr exists in the list
			}
			current = current.getNext();
		}
		return false; // No similar martyr found in the list
	}

	// Get the last martyr in the list
	public Martyr GetLast() {
		return Back != null ? Back.getElement() : null;
	}

	// Find the index of a martyr by name
	int FindMartyrIndex(Martyr martyr) {
		if (size == 0)
			return -1;
		MartyrNode current = Front;
		int i = 0;
		while (current != null) {
			Martyr tempMartyr = current.getElement();
			if (tempMartyr.getAge() == martyr.getAge() && tempMartyr.getDate() == martyr.getDate()
					&& martyr.getDistrict() == tempMartyr.getDistrict() && martyr.getGender() == tempMartyr.getGender()
					&& tempMartyr.getLocation() == martyr.getLocation() && martyr.getName() == tempMartyr.getName()) {
				return i;
			}
			i++;
			current = current.getNext();
		}
		return -1;
	}

	// Get the size of the list
	public int getSize() {
		return size;
	}

	// Constructor
	public MartyrLinkedList() {
		Front = Back = null;
		size = 0;
	}

	// Add a martyr to the beginning of the list
	void addFirst(Martyr element) {
		if (size == 0) {
			Front = Back = new MartyrNode(element);
		} else {
			MartyrNode newNode = new MartyrNode(element);
			newNode.setNext(Front);
			Front = newNode;
		}
		size++;
	}

	// Add a martyr to the end of the list
	void addLast(Martyr element) {
		if (size == 0) {
			Front = Back = new MartyrNode(element);
		} else {
			Back.setNext(new MartyrNode(element));
			Back = Back.getNext();
		}
		size++;
	}

	// Add a martyr at a specific index in the list
	void addElement(int index, Martyr element) {
		if (index == 0)
			addFirst(element);
		else if (index >= size - 1)
			addLast(element);
		else {
			MartyrNode current = Front;
			for (int i = 0; i < index; i++) {
				current = current.getNext();
			}
			MartyrNode temp = current.getNext();
			MartyrNode newNode = new MartyrNode(element);
			newNode.setNext(temp);
			current.setNext(newNode);
			size++;
		}
	}

	// Get the martyr at a specific index in the list
	Martyr getElement(int index) {
		if (index == 0)
			return getFirst();
		else if (index == size - 1)
			return GetLast();
		else if (index < size && index >= 1) {
			MartyrNode current = Front.getNext();
			for (int i = 1; i <= index - 1; i++) {
				current = current.getNext();
			}
			return current.getElement();
		}
		return null;
	}

	// Check if the list is empty
	public boolean isEmpty() {
		return size == 0;
	}

	// Add a martyr to the list in sorted order by age
	public void addMartyr(Martyr data) {
		MartyrNode newNode = new MartyrNode(data);
		if (isEmpty() || data.getAge() <= Front.getElement().getAge()) {
			addFirst(data);
			return;
		}
		MartyrNode current = Front;
		while (current.getNext() != null && data.getAge() > current.getNext().getElement().getAge()) {
			current = current.getNext();
		}
		newNode.setNext(current.getNext());
		current.setNext(newNode);
		size++;
	}

	// Add a martyr to the end of the list
	void Add(Martyr element) {
		if (size == 0)
			addFirst(element);
		else {
			addElement(size, element);
		}
	}

	// Delete the first martyr in the list
	void deleteFirst() {
		if (size == 1) {
			Front = Back = null;
		} else if (size > 1) {
			Front = Front.getNext();
		}
		size--;
	}

	// Delete a martyr at a specific index in the list
	void delete(int index) {
		if (size == 1)
			deleteFirst();
		else if (index == size - 1)
			deleteLast();
		else if (index < size) {
			MartyrNode current = Front;
			for (int i = 0; i < index; i++) {
				current = current.getNext();
			}
			current.setNext(current.getNext().getNext());
			size--;
		}
	}

	// Delete the last martyr in the list
	void deleteLast() {
		if (size == 1) {
			Front = Back = null;
		} else if (size > 1) {
			MartyrNode current = Front;
			for (int i = 0; i < size - 1; i++) {
				current = current.getNext();
			}
			Back = current;
		}
		size--;
	}

	// Print all martyrs in the list
	public void print() {
		print(Front);
	}

	// Helper method to recursively print martyrs starting from a given node
	private void print(MartyrNode element) {
		if (element.getElement() != null) {
			System.out.println(element.getElement());
			if (element.getNext() != null)
				print(element.getNext());
		}
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		MartyrNode current = Front;
		while (current != null) {
			stringBuilder.append(current.getElement().toString()).append("\n");
			current = current.getNext();
		}
		return stringBuilder.toString();
	}
}
