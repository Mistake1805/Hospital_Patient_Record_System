package Hospital_Patient_Record_System;

import java.util.ArrayList;
import java.util.List;

/**
 * Ward class representing a hospital ward
 */
public class Ward {
    private String name;
    private int totalBeds;
    private List<Patient> occupiedBeds;

    public Ward(String name, int totalBeds) {
        this.name = name;
        this.totalBeds = totalBeds;
        this.occupiedBeds = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getTotalBeds() {
        return totalBeds;
    }

    public List<Patient> getPatients() {
        return occupiedBeds;
    }

    public int getAvailableBeds() {
        return totalBeds - occupiedBeds.size();
    }

    public int getOccupancy() {
        return occupiedBeds.size();
    }

    public double getOccupancyPercentage() {
        return (occupiedBeds.size() * 100.0) / totalBeds;
    }

    public void addPatient(Patient patient) throws NoBedsAvailableException {
        if (occupiedBeds.size() >= totalBeds) {
            throw new NoBedsAvailableException(name, totalBeds, occupiedBeds.size());
        }
        
        occupiedBeds.add(patient);
        System.out.println("✓ Bed allocated in " + name + " ward");
    }

    public void removePatient(Patient patient) {
        occupiedBeds.remove(patient);
        System.out.println("✓ Bed released in " + name + " ward");
    }

    public void displayStatus() {
        System.out.printf("Ward: %s | Beds: %d/%d | Available: %d | Occupancy: %.1f%%%n",
            name, getOccupancy(), totalBeds, getAvailableBeds(), getOccupancyPercentage());
    }
}
