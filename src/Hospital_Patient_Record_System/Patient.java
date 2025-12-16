package Hospital_Patient_Record_System;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Patient class representing a hospital patient
 */
public class Patient extends Person {
    private String ward;
    private LocalDate admitDate;
    private LocalDate dischargeDate;
    private String status;

    public Patient(String id, String name, int age, String ward, LocalDate admitDate)
            throws InvalidPatientDataException {
        super(id, name, age);
        
        if (age < 0 || age > 150) {
            throw new InvalidPatientDataException("Age must be between 0 and 150", "Age");
        }
        
        this.ward = ward;
        this.admitDate = admitDate;
        this.dischargeDate = null;
        this.status = "admitted";
    }

    public String getWard() {
        return ward;
    }

    public LocalDate getAdmitDate() {
        return admitDate;
    }

    public LocalDate getDischargeDate() {
        return dischargeDate;
    }

    public String getStatus() {
        return status;
    }

    public long getDaysAdmitted() {
        LocalDate endDate = (dischargeDate != null) ? dischargeDate : LocalDate.now();
        return ChronoUnit.DAYS.between(admitDate, endDate) + 1;
    }

    public void discharge(LocalDate date) throws PatientAlreadyDischargedException {
        if ("discharged".equalsIgnoreCase(status)) {
            throw new PatientAlreadyDischargedException(id, name);
        }
        
        this.dischargeDate = date;
        this.status = "discharged";
    }

    public void displayInfo() {
        System.out.printf(
            "ID: %s | Name: %s | Age: %d | Ward: %s | Status: %s | Days: %d%n",
            id, name, age, ward, status, getDaysAdmitted()
        );
    }
}
