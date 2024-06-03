package application;

import javafx.scene.control.ComboBox;

public class District {
	private LocationBST locations; // Binary search tree to store locations in the district
	private String DistrictName; // Name of the district
	private int totalNumMartyrs;

	// Constructor
	public District(String DistrictN) {
		DistrictName = DistrictN; // Initialize the district name
		locations = new LocationBST(); // Initialize the binary search tree for locations
		setTotalNumMartyrs(0);
	}

	// Getter for locations
	public LocationBST getLocations() {
		return locations;
	}
//this method adds location
	public boolean addLocation(Location location) {
		if (!locations.contains(location)) {//avoids redundancy
			locations.add(location);
			return true;
		}
		return false;
	}

	public boolean deleteLocation(Location location) {
		if (locations.contains(location)) {
			locations.delete(location);
			return true;
		}
		return false;
	}

	// Setter for locations
	public void setLocations(LocationBST locations) {
		this.locations = locations;
	}

	// Getter for district name
	public String getDistrictName() {
		return DistrictName;
	}

	// Setter for district name
	public void setDistrictName(String districtName) {
		DistrictName = districtName;
	}

	// Override toString method to represent object as a string
	@Override
	public String toString() {
		// Return district name and list of locations as a string
		return DistrictName + ":\n" + locations.toString();
	}

	ComboBox<String> getLocationsCB() {
		ComboBox<String> locationscb = new ComboBox<String>();
		Stack locationsStack = locations.inOrder();
		while (!locationsStack.isEmpty()) {
			Location loc = (Location) locationsStack.pop();
			locationscb.getItems().add(loc.getLocationName());
		}
		return locationscb;
	}

	public boolean addMartyr(Martyr martyr, String LocationName) {
		Location location = locations.getLocation(LocationName);
		if (location == null) {
			location = new Location(LocationName);
			locations.add(location);
		}

		if(location.addMartyr(martyr)){

		setTotalNumMartyrs(getTotalNumMartyrs() + 1);
		return true;}
		return false;

	}

	public boolean DeleteMartyr(Martyr martyr, Location location) {
		location = locations.getLocation(location.getLocationName());
		if (location == null) {
			return false;
		}
		if (location.deleteMartyr(martyr)) {
			setTotalNumMartyrs(getTotalNumMartyrs() - 1);
			return true;
		}
		return false;

	}

	public int getTotalNumMartyrs() {
		return totalNumMartyrs;
	}

	private void setTotalNumMartyrs(int totalNumMartyrs) {
		this.totalNumMartyrs = totalNumMartyrs;
	}
}