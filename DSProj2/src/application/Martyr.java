package application;

public class Martyr implements Comparable<Martyr>{
	private String name, Date, location, District;
	private int age;
	private char gender;
	

//every method here is O(1)
	public Martyr(String name, String date, int age, String location, String district, char gender) {
		setName(name);
		setDate(date);
		setAge(age);
		setLocation(location);
		setDistrict(district);
		setGender(gender);
		

	}
	@Override
	public int compareTo(Martyr other) {
		return this.name.compareTo(other.name);
	}

	@Override
	public String toString() {
		String []newDate= new String[3];
		newDate= getDate().split("/");
		//newDate[0]->year// newDate[1]->month//newDate[2]->day
		//and i want it month/year/day
		String updatedDate= newDate[1]+"/"+newDate[2]+"/"+newDate[0];
		return name + "," + updatedDate + "," + age + "," + location + "," + District + "," + gender + "\n";
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDistrict() {
		return District;
	}

	public void setDistrict(String district) {
		District = district;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		if (age > 0)
			this.age = age;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	

}
