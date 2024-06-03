package application;

import javafx.scene.control.ComboBox;

public class Location {
	private DateBST dates; // Binary search tree to manage dates
	private String locationName; // Name of the location

	/**
	 * Gets the earliest date in the date BST.
	 * @return the earliest date as a string.
	 */
	public String getEarliestDate() {
		return dates.findMin().getDate();
	}

	/**
	 * Gets the latest date in the date BST.
	 * @return the latest date as a string.
	 */
	public String getLatestDate() {
		return dates.findMax().getDate();
	}

	/**
	 * Gets the date with the maximum number of martyrs.
	 * @return the date with the most martyrs as a string.
	 */
	public String getMaxDate() {
		Stack datesS = dates.inOrder();
		Date maxdate = null;
		if (!datesS.isEmpty())
			maxdate = (Date) datesS.pop();
		while (!datesS.isEmpty()) {
			Date anotherDate = (Date) datesS.pop();
			if (maxdate.getMartyrs().getSize() < anotherDate.getMartyrs().getSize())
				maxdate = anotherDate;
		}
		return (maxdate == null) ? "No Dates" : maxdate.getDate();
	}

	/**
	 * Constructor to create a new Location with a given name.
	 * @param locationN the name of the location.
	 */
	public Location(String locationN) {
		locationName = locationN;
		dates = new DateBST();
	}

	/**
	 * Gets the date BST for this location.
	 * @return the date BST.
	 */
	public DateBST getDateBST() {
		return dates;
	}

	/**
	 * Sets the date BST for this location.
	 * @param dateBST the date BST to set.
	 */
	public void setDateBST(DateBST dateBST) {
		this.dates = dateBST;
	}

	/**
	 * Gets the name of this location.
	 * @return the location name.
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * Sets the name of this location.
	 * @param locationName the new location name.
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	@Override
	public String toString() {
		return "Location Name: " + locationName + "\n Dates in this Location:\n" + dates.toString();
	}

	/**
	 * Creates a ComboBox with the dates in this location.
	 * @return a ComboBox containing the dates.
	 */
	public ComboBox<String> getDatesCB() {
		Stack datesStack = dates.inOrder();
		ComboBox<String> datesCB = new ComboBox<String>();
		while (!datesStack.isEmpty()) {
			Date tempdate = (Date) datesStack.pop();
			datesCB.getItems().add(tempdate.getDate());
		}
		return datesCB;
	}

	/**
	 * Adds a martyr to this location.
	 * @param martyr the martyr to add.
	 */
	public boolean addMartyr(Martyr martyr) {
		Date tempDate = new Date(martyr.getDate());
		if (!dates.contains(tempDate)) {
			dates.add(tempDate);
		} else {
			tempDate = dates.getDate(tempDate.getDate());
		}
		if(tempDate.addMartyr(martyr)){
			return true;
		}
		return false;
	}

	/**
	 * Deletes a martyr from this location.
	 * @param martyr the martyr to delete.
	 * @return true if the martyr was deleted, false otherwise.
	 */
	public boolean deleteMartyr(Martyr martyr) {
		Date date = dates.getDate(martyr.getDate());
		if (date == null)
			return false;
		if (date.deleteMartyr(martyr))
			return true;
		return false;
	}

	/**
	 * Changes the name of this location and updates all martyrs with the new name.
	 * @param newName the new name for the location.
	 */
	public void changeName(String newName) {
		Stack datesS = dates.inOrder();
		while (!datesS.isEmpty()) {
			Date date = (Date) datesS.pop();
			for (int i = 0; i < date.getMartyrs().getSize(); i++) {
				Martyr martyr = date.getMartyrs().getElement(i);
				martyr.setLocation(newName);
			}
		}
	}
}
