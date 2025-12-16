package Hospital_Patient_Record_System;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Main hospital system with CSV support and exception handling
 */
public class HospitalSystemWithCSV {
    private Map<String, Ward> wards;
    private List<Patient> allPatients;
    private BillingService billingService;

    public HospitalSystemWithCSV() {
        this.wards = new HashMap<>();
        this.allPatients = new ArrayList<>();
        this.billingService = new BillingService(0);
        initializeWards();
        try {
            loadPatients();
            loadWardRates();
        } catch (CSVFileException e) {
            System.out.println("⚠️  Warning: " + e.getMessage());
        }
    }

    private void initializeWards() {
        wards.put("ICU", new Ward("ICU", 5));
        wards.put("General", new Ward("General", 10));
        wards.put("Pediatric", new Ward("Pediatric", 8));
        wards.put("Emergency", new Ward("Emergency", 3));
        System.out.println("✓ Hospital initialized with 4 wards");
    }

    public void admitPatient(String patientId, String name, int age, String wardName)
            throws InvalidWardException, NoBedsAvailableException, InvalidPatientDataException {
        if (!wards.containsKey(wardName)) {
            throw new InvalidWardException(wardName);
        }
        
        Patient patient = new Patient(patientId, name, age, wardName, LocalDate.now());
        Ward ward = wards.get(wardName);
        ward.addPatient(patient);
        allPatients.add(patient);
        System.out.println("✓ Patient " + name + " admitted successfully!");
    }

    public void dischargePatient(String patientId)
            throws PatientNotFoundException, PatientAlreadyDischargedException {
        Patient patient = null;
        for (Patient p : allPatients) {
            if (p.getId().equals(patientId)) {
                patient = p;
                break;
            }
        }
        
        if (patient == null) {
            throw new PatientNotFoundException(patientId);
        }
        
        patient.discharge(LocalDate.now());
        Ward ward = wards.get(patient.getWard());
        ward.removePatient(patient);
        System.out.println("✓ Patient discharged successfully!");
    }

    private void loadPatients() throws CSVFileException {
        try (BufferedReader br = new BufferedReader(new FileReader("src/patients.csv"))) {
            String line;
            int lineNumber = 0;
            
            String header = br.readLine();
            if (header == null) {
                System.out.println("⚠️  patients.csv is empty, starting with no patients");
                return;
            }
            
            lineNumber++;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = line.split(",");
                    if (parts.length < 6) {
                        System.out.println("⚠️  Line " + lineNumber + ": Invalid format, skipping");
                        continue;
                    }
                    
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());
                    String ward = parts[3].trim();
                    LocalDate admitDate = LocalDate.parse(parts[4].trim());
                    String status = parts[5].trim();
                    
                    if (!wards.containsKey(ward)) {
                        throw new InvalidWardException("Ward '" + ward + "' not found", ward);
                    }
                    
                    Patient patient = new Patient(id, name, age, ward, admitDate);
                    
                    if ("discharged".equalsIgnoreCase(status)) {
                        patient.discharge(LocalDate.now());
                    }
                    
                    allPatients.add(patient);
                    
                    if ("admitted".equalsIgnoreCase(status)) {
                        wards.get(ward).addPatient(patient);
                    }
                    
                    System.out.println("✓ Loaded: " + name + " (" + id + ") - " + ward);
                } catch (InvalidWardException e) {
                    System.out.println("⚠️  Line " + lineNumber + ": " + e.getMessage());
                } catch (InvalidPatientDataException e) {
                    System.out.println("⚠️  Line " + lineNumber + ": Invalid data - " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("⚠️  Line " + lineNumber + ": Invalid age format, skipping");
                } catch (Exception e) {
                    System.out.println("⚠️  Line " + lineNumber + ": Error - " + e.getMessage());
                }
            }
            
            System.out.println("✓ Total patients loaded: " + allPatients.size());
        } catch (FileNotFoundException e) {
            System.out.println("⚠️  patients.csv not found, starting with empty system");
        } catch (IOException e) {
            throw new CSVFileException("Failed to read patients.csv", "patients.csv");
        }
    }

    private void loadWardRates() throws CSVFileException {
        try (BufferedReader br = new BufferedReader(new FileReader("src/rates.cfg"))) {
            String line;
            int lineNumber = 0;
            
            while ((line = br.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                try {
                    String[] parts = line.split("=");
                    if (parts.length != 2) {
                        System.out.println("⚠️  Line " + lineNumber + ": Invalid format, skipping");
                        continue;
                    }
                    
                    String wardName = parts[0].trim();
                    double rate = Double.parseDouble(parts[1].trim());
                    billingService.setWardRate(wardName, rate);
                    System.out.println("✓ Loaded rate: " + wardName + " = " + rate);
                } catch (NumberFormatException e) {
                    System.out.println("⚠️  Line " + lineNumber + ": Invalid rate format");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("⚠️  rates.cfg not found, using default rates");
        } catch (IOException e) {
            throw new CSVFileException("Failed to read rates.cfg", "rates.cfg");
        }
    }

    public void savePatients() {
        try (FileWriter fw = new FileWriter("src/patients.csv")) {
            fw.write("PatientID,Name,Age,Ward,AdmitDate,Status\n");
            for (Patient p : allPatients) {
                fw.write(String.format("%s,%s,%d,%s,%s,%s\n",
                    p.getId(), p.getName(), p.getAge(), p.getWard(), p.getAdmitDate(), p.getStatus()));
            }
            System.out.println("✓ Patients saved to CSV");
        } catch (IOException e) {
            System.out.println("❌ Error saving patients: " + e.getMessage());
        }
    }

    public void saveWardRates() {
        try (FileWriter fw = new FileWriter("src/rates.cfg")) {
            fw.write("# Hospital Ward Rates Configuration\n");
            fw.write("ICU=5000\n");
            fw.write("General=2000\n");
            fw.write("Pediatric=2500\n");
            fw.write("Emergency=8000\n");
            System.out.println("✓ Ward rates saved");
        } catch (IOException e) {
            System.out.println("❌ Error saving rates: " + e.getMessage());
        }
    }

    public void listPatients() {
        if (allPatients.isEmpty()) {
            System.out.println("❌ No patients in the system");
            return;
        }
        
        System.out.println("\n📋 ALL PATIENTS:");
        for (Patient p : allPatients) {
            p.displayInfo();
        }
    }

    public void showOccupancy() {
        System.out.println("\n🏥 WARD OCCUPANCY:");
        for (Ward ward : wards.values()) {
            ward.displayStatus();
        }
    }

    public void showWardAllocations() {
        System.out.println("\n🛏️  WARD ALLOCATIONS:");
        for (Ward ward : wards.values()) {
            System.out.println("\n" + ward.getName() + " Ward:");
            List<Patient> patients = ward.getPatients();
            if (patients.isEmpty()) {
                System.out.println("  (No patients)");
            } else {
                for (Patient p : patients) {
                    System.out.println("  - " + p.getName() + " (ID: " + p.getId() + ")");
                }
            }
        }
    }

    public void applyDiscount(double percentage) {
        billingService.applyDiscount(percentage);
    }

    public void calculateBilling() {
        billingService.generateBillingReport(allPatients);
    }

    public void calculateBillingForPatient(String patientId) {
        for (Patient p : allPatients) {
            if (p.getId().equals(patientId)) {
                billingService.calculatePatientBill(p);
                return;
            }
        }
        System.out.println("❌ Patient not found!");
    }

    public void saveBillingReport() {
        try (FileWriter fw = new FileWriter("src/billing_report.txt")) {
            fw.write("HOSPITAL BILLING REPORT\n");
            fw.write("=".repeat(50) + "\n\n");
            for (Patient p : allPatients) {
                fw.write(String.format("Patient: %s (ID: %s)\n", p.getName(), p.getId()));
                fw.write(String.format("Ward: %s | Days: %d\n", p.getWard(), p.getDaysAdmitted()));
                fw.write(String.format("Status: %s\n\n", p.getStatus()));
            }
            System.out.println("✓ Billing report saved to billing_report.txt");
        } catch (IOException e) {
            System.out.println("❌ Error saving billing report: " + e.getMessage());
        }
    }
}
