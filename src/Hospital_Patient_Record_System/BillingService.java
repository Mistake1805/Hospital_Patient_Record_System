package Hospital_Patient_Record_System;

/**
 * BillingService class for handling hospital billing
 */
public class BillingService {
    private double discountPercentage;
    private java.util.Map<String, Double> wardRates;

    public BillingService(double discountPercentage) {
        this.discountPercentage = discountPercentage;
        this.wardRates = new java.util.HashMap<>();
        initializeRates();
    }

    private void initializeRates() {
        wardRates.put("ICU", 5000.0);
        wardRates.put("General", 2000.0);
        wardRates.put("Pediatric", 2500.0);
        wardRates.put("Emergency", 8000.0);
    }

    public void setWardRate(String wardName, double rate) {
        wardRates.put(wardName, rate);
    }

    public void applyDiscount(double percentage) {
        this.discountPercentage = percentage;
        System.out.println("✓ Discount applied: " + percentage + "%");
    }

    public void calculatePatientBill(Patient patient) {
        if ("discharged".equalsIgnoreCase(patient.getStatus())) {
            double dailyRate = wardRates.getOrDefault(patient.getWard(), 0.0);
            double totalBill = dailyRate * patient.getDaysAdmitted();
            double discount = totalBill * (discountPercentage / 100.0);
            double finalBill = totalBill - discount;

            System.out.println("\n💰 BILLING STATEMENT");
            System.out.println("═══════════════════════════════════════");
            System.out.println("Patient: " + patient.getName() + " (ID: " + patient.getId() + ")");
            System.out.println("Ward: " + patient.getWard());
            System.out.println("Daily Rate: ₹" + dailyRate);
            System.out.println("Days Admitted: " + patient.getDaysAdmitted());
            System.out.println("Total Bill: ₹" + totalBill);
            System.out.println("Discount (" + discountPercentage + "%): -₹" + discount);
            System.out.println("Final Bill: ₹" + finalBill);
            System.out.println("═══════════════════════════════════════\n");
        } else {
            System.out.println("❌ Patient " + patient.getName() + " is still admitted. Cannot generate bill.");
        }
    }

    public void generateBillingReport(java.util.List<Patient> patients) {
        System.out.println("\n📊 BILLING REPORT FOR ALL PATIENTS");
        System.out.println("═══════════════════════════════════════");
        for (Patient p : patients) {
            if ("discharged".equalsIgnoreCase(p.getStatus())) {
                double dailyRate = wardRates.getOrDefault(p.getWard(), 0.0);
                double totalBill = dailyRate * p.getDaysAdmitted();
                double discount = totalBill * (discountPercentage / 100.0);
                double finalBill = totalBill - discount;
                System.out.printf("%-20s | Ward: %-15s | Days: %d | Bill: ₹%.2f%n",
                    p.getName(), p.getWard(), p.getDaysAdmitted(), finalBill);
            }
        }
        System.out.println("═══════════════════════════════════════\n");
    }
}
