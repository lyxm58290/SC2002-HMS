
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The MedicalRecManager class is responsible for managing patient medical records.
 * It provides methods to view individual medical records and to view all medical records for a specific doctor.
 * This class interacts with the patient database stored in a CSV file to retrieve and display medical record information.
 */
public class MedicalRecManager {
    
    static String patientsFilePath = "HospitalApp\\src\\Database\\CSV\\DB\\PATIENT_DB.csv";

    /**
     * Views the medical records for a specific patient identified by their patient ID.
     * This method reads the patient database file and displays the medical record details of the patient
     * matching the given patient ID.
     *
     * @param patientID the unique identifier for the patient whose records are to be viewed
     */
    public static void viewMedicalRecords(String patientID) {
        try (BufferedReader br = new BufferedReader(new FileReader(patientsFilePath))) {
            String line;
            br.readLine();
    
            boolean recordFound = false;
    
            System.out.println("===============================================YOUR MEDICAL RECORDS===============================================");
            System.out.printf("%-10s %-15s %-15s %-10s %-15s %-25s %-10s %-10s%n", 
                              "PA_ID", "NAME", "DOB", "GENDER", "BLOOD_TYPE", "EMAIL", "PHONE_NO", "DR_ID");
            System.out.println("==================================================================================================================");
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(patientID)) {
                    System.out.printf("%-10s %-15s %-15s %-10s %-15s %-25s %-10s %-10s%n",
                            details[0], details[1], details[2], details[3], details[4], details[5], details[6], details[7]);
                    recordFound = true;
                }
            }
            System.out.println("===================================================================================================================");
            if (!recordFound) {
                System.out.println("No medical records found for Patient ID: " + patientID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Views all medical records for patients associated with a specific doctor identified by their doctor ID.
     * This method reads the patient database file and displays all medical records for patients
     * whose doctor ID matches the provided doctor ID.
     *
     * @param doctorID the unique identifier for the doctor whose patients' records are to be viewed
     */
    public static void viewAllMedicalRecordsForDoc(String doctorID) {
        try (BufferedReader br = new BufferedReader(new FileReader(patientsFilePath))) {
            String line;
            br.readLine();
    
            boolean recordFound = false;
    
            System.out.println("========================================= DOCTOR'S PATIENT MEDICAL RECORDS =========================================");
            System.out.printf("%-10s %-15s %-15s %-10s %-15s %-25s %-10s %-10s%n", 
                              "PA_ID", "NAME", "DOB", "GENDER", "BLOOD_TYPE", "EMAIL", "PHONE_NO", "DR_ID");
            System.out.println("====================================================================================================================");
    
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                
                if (details.length >= 8 && details[7].equalsIgnoreCase(doctorID)) { // Ensure DR_ID exists in details
                    System.out.printf("%-10s %-15s %-15s %-10s %-15s %-25s %-10s %-10s%n",
                                      details[0], details[1], details[2], details[3], details[4], details[5], details[6], details[7]);
                    recordFound = true;
                }
            }
    
            System.out.println("====================================================================================================================");
    
            if (!recordFound) {
                System.out.println("No patients found for this doctor ID: " + doctorID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}