package application;

import java.time.LocalDate;

public class Date {
    // Private member variables
    private MartyrLinkedList martyrs; // List to store martyrs
    private String date; // Date for this instance
    private int totalAges, totalNumOfMartyrs;

    // Constructor
    public Date(String date) {
        this.date = date;
        martyrs = new MartyrLinkedList(); // Initialize the linked list of martyrs
        totalAges = 0;
        totalNumOfMartyrs = 0;
    }

    // Method to add a martyr to the list
    public boolean addMartyr(Martyr martyr) {
        if (!martyrs.martyrExists(martyr)) {
            martyrs.Add(martyr); // Add the martyr to the linked list
            if (martyr.getAge() != -1) {
                totalAges += martyr.getAge();
                totalNumOfMartyrs += 1;
            }
            return true;
        }
        return false;
    }
    // Method to delete a martyr from the list
    public boolean deleteMartyr(Martyr martyr) {
        int index = martyrs.FindMartyrIndex(martyr); // Find the index of the martyr
        if (index != -1) {
            martyrs.delete(index); // Delete the martyr from the linked list
            if (martyr.getAge() != -1) {
                totalNumOfMartyrs -= 1;
                totalAges -= martyr.getAge();
            }
            return true; // Return true indicating deletion was successful
        }
        return false; // Return false indicating martyr not found
    }

    // Getter for martyrs list
    public MartyrLinkedList getMartyrs() {
        return martyrs;
    }

    // Setter for martyrs list
    public void setMartyrs(MartyrLinkedList martyrs) {
        this.martyrs = martyrs;
    }

    public double getAverageAge() {
        return (double) totalAges / totalNumOfMartyrs;
    }

    public int getYoungest() {
        int youngestAge = -1; // Initialize with an invalid age
        for (int i = 0; i < martyrs.getSize(); i++) {
            int age = martyrs.getElement(i).getAge();
            if (age != -1 && (youngestAge == -1 || age < youngestAge)) {
                youngestAge = age;
            }
        }
        return youngestAge;
    }


    public int getOldest() {
        int age = martyrs.getElement(0).getAge();
        for (int i = 1; i < martyrs.getSize(); i++) {
            Martyr martyr = martyrs.getElement(i);
            if (martyr.getAge() > age) { // Corrected
                age = martyr.getAge();
            }
        }
        return age;
    }

    // Getter for date
    public String getDate() {
        return date;
    }

    // Setter for date
    public void setDate(String date) {
        this.date = date;
    }

    // Override toString method to represent object as a string
    @Override
    public String toString() {
        // Return date and list of martyrs as a string
        return date + ":\n" + martyrs.toString();
    }
}
