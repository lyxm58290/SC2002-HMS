
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The OutcomeManager class handles the management of outcome records related to patient appointments.
 * It provides functionalities to:
 * - Load, view, and update outcome records from a CSV file.
 * - Create new outcome records based on an appointment and doctor's input.
 * - Add new outcome records to the CSV database.
 * - View outcome records based on patient ID, doctor ID, or all records.
 * - Update the outcome records with new details such as diagnosis, medication, and treatment plan.
 *
 * This class interacts with the CSV file storing the outcome data and provides methods for reading,
 * writing, and updating the outcome information in a user-friendly format.
 */

public class OutcomeManager {

    static String outcomeFile = "HospitalApp\\src\\Database\\CSV\\OUTCOME.csv";

    static Scanner sc = new Scanner(System.in);

    /**
     * Loads all the outcome records from the CSV file into an ArrayList.
     * 
     * @return An ArrayList of OutcomeRecord objects representing all outcome records.
     */
    public static ArrayList<OutcomeRecord> loadOutcomeRecord(){
        ArrayList<OutcomeRecord> allRecords = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(outcomeFile))) {
                String line;
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] detail = line.split(",");
                    String patientID = detail[0];
                    String doctorID = detail[1];
                    String dateOfAppointment = detail[2];
                    String diagnosis = detail[3];
                    String service = detail[4];
                    String med = detail[5];
                    String amount = detail[6];
                    String status = detail[7];
                    String treatmentPlan = detail[8];
                    allRecords.add(new OutcomeRecord(patientID, doctorID,dateOfAppointment,diagnosis,service,med,amount,status,treatmentPlan));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return allRecords;
    }

    /**
     * Creates a new outcome record for the given appointment and doctor ID.
     * 
     * @param selectedAppointment The selected appointment for which the outcome is to be created.
     * @param doctorID The ID of the doctor creating the outcome record.
     * @return A new OutcomeRecord object populated with the outcome details.
     */
    public static OutcomeRecord createNewOutcomeRecord(Appointment selectedAppointment, String doctorID){
        if(selectedAppointment == null){
            return null;
        }
        OutcomeRecord newOutcome = new OutcomeRecord();
        newOutcome.setPatientID(selectedAppointment.getPID());
        newOutcome.setDateOfAppointment(selectedAppointment.getDate() + "+" + selectedAppointment.getTime());
        newOutcome.setDoctorID(doctorID);

        System.out.print("Enter diagnosis: ");
        newOutcome.setDiagnosis(sc.nextLine());
        
        System.out.print("Enter treatment plan: ");
        newOutcome.setTreatmentPlan(sc.nextLine());

        System.out.print("Enter medication: ");
        newOutcome.setMed(sc.nextLine());

        System.out.print("How many of it would you like to prescribe: ");
        newOutcome.setAmount(sc.nextLine());
        
        System.out.print("Enter type of service provided: ");
        newOutcome.setService(sc.nextLine());

        return newOutcome;
    }

    /**
     * Appends a new outcome record to the outcome CSV file.
     * 
     * @param outcome The OutcomeRecord to be added to the file.
     */
    public static void addOutcomeRecord(OutcomeRecord outcome) {
        if(outcome == null){
            System.out.println("No accepted appointment.");
            return;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outcomeFile, true))) {
            String outcomeEntry = String.join(",", outcome.getPatientID(), outcome.getDoctorID(), outcome.getDateOfAppointment(), outcome.getDiagnosis(), outcome.getService(), outcome.getMed(), outcome.getAmount(), "Pending", outcome.getTreatmentPlan());
            bw.write(outcomeEntry);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Outcome recorded successfully.");
        System.out.println("\n");
    }

    /**
     * Displays the outcome records for a specific patient ID.
     * 
     * @param patientID The ID of the patient whose outcome records are to be displayed.
     */
    public static void viewOutcomeRecords(String patientID) { 
    
        try (BufferedReader br = new BufferedReader(new FileReader(outcomeFile))) {
            String line;
            br.readLine();
    
            boolean recordFound = false;
    
            System.out.println("=======================================YOUR OUTCOME RECORDS=======================================");
            System.out.printf("%-10s %-20s %-15s %-15s %-15s %-20s%n", "DR_ID", "DATE_TIME", "PAST_DIAG", "SERVICE", "MED_NAME", "TREATMENT_PLANS");
            System.out.println("==================================================================================================");
            
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(patientID)) {
                    System.out.printf("%-10s %-20s %-15s %-15s %-15s %-20s%n",
                            details[1], details[2], details[3], details[4], details[5], details[8]);
                    recordFound = true;
                }
            }
            System.out.println("==================================================================================================");
            if (!recordFound) {
                System.out.println("No past appointment records found for Patient ID: " + patientID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the outcome records for a specific doctor ID.
     *
     * @param doctorID The ID of the doctor whose patients' outcome records are to be displayed.
     */
    public static void viewOutcomeRecordsForDoc(String doctorID) {
    
        try (BufferedReader br = new BufferedReader(new FileReader(outcomeFile))) {
            String line;
            br.readLine();
            boolean recordFound = false;
    
            System.out.println("==================================DOCTOR'S PATIENT OUTCOME RECORDS==================================");
            System.out.printf("%-10s %-20s %-15s %-15s %-20s %-20s%n", "PA_ID", "DATE_TIME", "PAST_DIAG", "SERVICE", "MED_NAME", "TREATMENT_PLANS");
            System.out.println("====================================================================================================");
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[1].equals(doctorID)) {
                    System.out.printf("%-10s %-20s %-15s %-15s %-20s %-20s%n",
                            details[0], details[2], details[3], details[4], details[5], details[8]);
                    recordFound = true;
                }
            }
            System.out.println("====================================================================================================");
            if (!recordFound) {
                System.out.println("No past appointment records found for doctor ID: " + doctorID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays all outcome records for all patients and doctors.
     */
    public static void viewAllOutcomeRecords() {
    
        try (BufferedReader br = new BufferedReader(new FileReader(outcomeFile))) {
            String line;
            br.readLine(); 
    
            boolean recordFound = false;
    
            System.out.println("==========================================DOCTOR'S PATIENT OUTCOME RECORDS==========================================");
            System.out.printf("%-10s %-10s %-20s %-15s %-15s %-20s %-20s%n", "PA_ID", "DR_ID", "DATE_TIME", "PAST_DIAG", "SERVICE", "MED_NAME", "TREATMENT_PLANS");
            System.out.println("====================================================================================================================");
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                    System.out.printf("%-10s %-10s %-20s %-15s %-15s %-20s %-20s%n",
                            details[0], details[1], details[2], details[3], details[4], details[5], details[8]);
                    recordFound = true;
                }
            System.out.println("====================================================================================================================");
            if (!recordFound) {
                System.out.println("No past appointment records found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the medical outcome records for a specific doctor.
     * The doctor can select a patient and modify details such as diagnosis, medication, amount, and treatment plan.
     * 
     * @param doctorID The ID of the doctor updating the outcome records.
     */
    public static void updateMedicalRecords(String doctorID){
        List<String[]> allRecords = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(outcomeFile))) {
                String line;
                br.readLine();
                while ((line = br.readLine()) != null) {
                    allRecords.add(line.split(","));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Map<String, List<String[]>> patientRecordsMap = new HashMap<>();
            for (String[] record : allRecords) {
                if (record[1].trim().equalsIgnoreCase(doctorID)) {
                    String patientID = record[0].trim();
                    patientRecordsMap.computeIfAbsent(patientID, k -> new ArrayList<>()).add(record);
                }
            }
            if (patientRecordsMap.isEmpty()) {
                System.out.println("No outcome records found for this doctor.");
                return;
            }
            List<String> patientIDs = new ArrayList<>(patientRecordsMap.keySet());
            System.out.println("Select a patient to update outcome records:");
            System.out.println("===========================================");
            for (int i = 0; i < patientIDs.size(); i++) {
                System.out.println((i + 1) + ". Patient ID: " + patientIDs.get(i));
            }
            System.out.println("===========================================");
            System.out.print("Your choice: ");
        
            int patientChoice = sc.nextInt() - 1;
            if (patientChoice < 0 || patientChoice >= patientIDs.size()) {
                System.out.println("Invalid patient selection.");
                return;
            }
            String selectedPatientID = patientIDs.get(patientChoice);
        
            List<String[]> selectedPatientRecords = patientRecordsMap.get(selectedPatientID);
            System.out.println("\nSelect an outcome record to update for Patient ID: " + selectedPatientID);
            System.out.println("==================================OUTCOME RECORDS===================================");
            System.out.printf("%-5s %-20s %-15s %-20s %-10s %-15s%n", "No.","DATE_TIME", "DIAGNOSIS", "MEDICATION", "AMOUNT", "TREATMENT");
			System.out.println("====================================================================================");
            for (int i = 0; i < selectedPatientRecords.size(); i++) {
                String[] record = selectedPatientRecords.get(i);
                System.out.printf("%-5s %-20s %-15s %-20s %-10s %-15s%n", 
                                  i + 1, record[2].trim(), record[3].trim(), record[5].trim(), record[6].trim(), record[8].trim());
            }
            System.out.println("====================================================================================");
        
            System.out.print("Your choice: ");
            int recordChoice = sc.nextInt() - 1;
            
            if (recordChoice < 0 || recordChoice >= selectedPatientRecords.size()) {
                System.out.println("Invalid record selection.");
                return;
            }
        
            String[] selectedRecord = selectedPatientRecords.get(recordChoice);
            sc.nextLine();
        
            System.out.print("Enter new diagnosis (current: " + selectedRecord[3].trim() + "): ");
            selectedRecord[3] = sc.nextLine();
        
            System.out.print("Enter new medication (current: " + selectedRecord[5].trim() + "): ");
            selectedRecord[5] = sc.nextLine();
        
            System.out.print("Enter new amount (current: " + selectedRecord[6].trim() + "): ");
            selectedRecord[6] = sc.nextLine();
        
            System.out.print("Enter new treatment (current: " + selectedRecord[8].trim() + "): ");
            selectedRecord[8] = sc.nextLine();
        
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(outcomeFile))) {
                bw.write("PatientID,DoctorID,Date,Diagnosis,Service,Medication,Amount,Status,Treatment"); // Header line
                bw.newLine();
                for (String[] record : allRecords) {
                    bw.write(String.join(",", record));
                    bw.newLine();
                }
                System.out.println("Outcome record updated successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}