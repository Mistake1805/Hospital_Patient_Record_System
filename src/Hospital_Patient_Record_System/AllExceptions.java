package Hospital_Patient_Record_System;

/**
 * All 6 Custom Exceptions for Hospital System
 */

// 1) InvalidWardException
class InvalidWardException extends Exception {
    private String wardName;

    public InvalidWardException(String wardName) {
        super("❌ INVALID WARD: '" + wardName + "' does not exist in the hospital system.");
        this.wardName = wardName;
    }

    // ✅ ADD THIS
    public InvalidWardException(String message, String wardName) {
        super(message);
        this.wardName = wardName;
    }

    public String getWardName() { return wardName; }
}

// 2) PatientAlreadyDischargedException
class PatientAlreadyDischargedException extends Exception {
    private final String patientId;
    private final String patientName;

    public PatientAlreadyDischargedException(String patientId, String patientName) {
        super("❌ DISCHARGE ERROR: Patient '" + patientName + "' (ID: " + patientId + ") is already discharged.");
        this.patientId = patientId;
        this.patientName = patientName;
    }

    public String getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
}

// 3) NoBedsAvailableException
class NoBedsAvailableException extends Exception {
    private final String wardName;
    private final int totalBeds;
    private final int occupiedBeds;

    public NoBedsAvailableException(String wardName, int totalBeds, int occupiedBeds) {
        super("❌ ADMISSION ERROR: " + wardName + " ward is FULL! (" + occupiedBeds + "/" + totalBeds + " beds occupied)");
        this.wardName = wardName;
        this.totalBeds = totalBeds;
        this.occupiedBeds = occupiedBeds;
    }

    public String getWardName() { return wardName; }
    public int getTotalBeds() { return totalBeds; }
    public int getOccupiedBeds() { return occupiedBeds; }
    public int getAvailableBeds() { return totalBeds - occupiedBeds; }
}

// 4) PatientNotFoundException
class PatientNotFoundException extends Exception {
    private final String patientId;

    public PatientNotFoundException(String patientId) {
        super("❌ PATIENT NOT FOUND: Patient ID '" + patientId + "' does not exist in the system.");
        this.patientId = patientId;
    }

    public String getPatientId() { return patientId; }
}

// 5) InvalidPatientDataException  ✅ FIXED
class InvalidPatientDataException extends Exception {
    private final String fieldName;
    private final String fieldValue;

    public InvalidPatientDataException(String fieldName, String fieldValue) {
        super("❌ INVALID DATA: Field '" + fieldName + "' has invalid value: '" + fieldValue + "'");
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    // ✅ Different signature now (3 params), so NO duplicate constructor error
    public InvalidPatientDataException(String message, String fieldName, String fieldValue) {
        super(message);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() { return fieldName; }
    public String getFieldValue() { return fieldValue; }
}

// 6) CSVFileException
class CSVFileException extends Exception {
    private final String fileName;
    private final String operation;

    public CSVFileException(String fileName, String operation, Throwable cause) {
        super("❌ CSV FILE ERROR: Failed to " + operation + " file '" + fileName + "'", cause);
        this.fileName = fileName;
        this.operation = operation;
    }

    public CSVFileException(String message, String fileName) {
        super(message);
        this.fileName = fileName;
        this.operation = null;
    }

    public String getFileName() { return fileName; }
    public String getOperation() { return operation; }
}
