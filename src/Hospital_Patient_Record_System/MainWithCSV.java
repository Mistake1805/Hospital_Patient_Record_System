package Hospital_Patient_Record_System;

import java.util.Scanner;

/**
 * Main class with menu-driven interface and complete exception handling
 */
public class MainWithCSV {
    public static void main(String[] args) {
        HospitalSystemWithCSV hospital = new HospitalSystemWithCSV();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            displayMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        // Admit Patient
                        System.out.print("Enter Patient ID (e.g., P001): ");
                        String id = scanner.nextLine();
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Age: ");
                        int age = Integer.parseInt(scanner.nextLine());
                        System.out.println("Available Wards: ICU, General, Pediatric, Emergency");
                        System.out.print("Enter Ward: ");
                        String ward = scanner.nextLine();
                        
                        try {
                            hospital.admitPatient(id, name, age, ward);
                            hospital.savePatients();
                        } catch (InvalidWardException e) {
                            System.out.println(e.getMessage());
                            System.out.println("💡 Available wards: ICU, General, Pediatric, Emergency");
                        } catch (NoBedsAvailableException e) {
                            System.out.println(e.getMessage());
                            System.out.println("💡 Available beds: " + e.getAvailableBeds());
                            System.out.println("   Try another ward or come back later.");
                        } catch (InvalidPatientDataException e) {
                            System.out.println(e.getMessage());
                            System.out.println("💡 Field: " + e.getFieldName());
                        }
                        break;
                    
                    case 2:
                        // Discharge Patient
                        System.out.print("Enter Patient ID to discharge: ");
                        String dischargeId = scanner.nextLine();
                        
                        try {
                            hospital.dischargePatient(dischargeId);
                            hospital.savePatients();
                        } catch (PatientNotFoundException e) {
                            System.out.println(e.getMessage());
                            System.out.println("💡 Patient ID: " + e.getPatientId());
                        } catch (PatientAlreadyDischargedException e) {
                            System.out.println(e.getMessage());
                            System.out.println("💡 Patient: " + e.getPatientName() + " (" + e.getPatientId() + ")");
                        }
                        break;
                    
                    case 3:
                        // List All Patients
                        hospital.listPatients();
                        break;
                    
                    case 4:
                        // Show Ward Occupancy
                        hospital.showOccupancy();
                        break;
                    
                    case 5:
                        // Show Ward Allocations
                        hospital.showWardAllocations();
                        break;
                    
                    case 6:
                        // Apply Discount
                        System.out.print("Enter discount percentage (0-100): ");
                        double discount = Double.parseDouble(scanner.nextLine());
                        hospital.applyDiscount(discount);
                        break;
                    
                    case 7:
                        // Billing Report
                        hospital.calculateBilling();
                        hospital.saveBillingReport();
                        break;
                    
                    case 8:
                        // Billing for One Patient
                        System.out.print("Enter Patient ID for billing: ");
                        String billId = scanner.nextLine();
                        hospital.calculateBillingForPatient(billId);
                        break;
                    
                    case 9:
                        // Exit and Save
                        System.out.println("\n💾 Saving data...");
                        hospital.savePatients();
                        hospital.saveWardRates();
                        running = false;
                        System.out.println("✓ Goodbye!");
                        break;
                    
                    default:
                        System.out.println("❌ Invalid choice. Please try again.");
                }
                
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
            }
        }
        
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("\n" + "═".repeat(50));
        System.out.println(" HOSPITAL PATIENT RECORD SYSTEM (WITH EXCEPTIONS)");
        System.out.println("═".repeat(50));
        System.out.println("1. Admit Patient");
        System.out.println("2. Discharge Patient");
        System.out.println("3. List All Patients");
        System.out.println("4. Show Ward Occupancy");
        System.out.println("5. Show Ward Allocations");
        System.out.println("6. Apply Discount");
        System.out.println("7. Billing Report");
        System.out.println("8. Billing for One Patient");
        System.out.println("9. Exit (Save Data)");
        System.out.println("═".repeat(50));
        System.out.print("Choose an option (1-9): ");
    }
}
