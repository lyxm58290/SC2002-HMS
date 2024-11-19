
import java.io.*;
import java.util.*;

/**
 * The DoctorSelection class is responsible for enabling patients to choose a doctor.
 * It displays a list of available doctors from a CSV file, allows the patient to select
 * a preferred doctor by entering the doctor's ID, and updates the patient's record in the database.
 */
public class DoctorSelection {

    Scanner sc = new Scanner(System.in);

    public String chooseDoctor(String patientID) {
        String doctorFilePath = "HospitalApp\\src\\Database\\CSV\\DB\\DR_DB.csv";
        String patientFilePath = "HospitalApp\\src\\Database\\CSV\\DB\\PATIENT_DB.csv";
        String chosenDrID = "";

        /**
         * Allows the patient to choose a doctor by entering the doctor's ID.
         * This method reads the available doctors from a CSV file, displays their details,
         * and updates the patient's record with the selected doctor's ID.
         *
         * @param patientID the unique identifier of the patient selecting a doctor
         * @return the ID of the chosen doctor, or null if an invalid ID is entered
         */
        Map<String, String> doctors = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(doctorFilePath))) {
            String headerLine = br.readLine();
            if (headerLine != null) {
                String[] headers = headerLine.split(",");
                printHeaders(headers);

                String line;
                while ((line = br.readLine()) != null) {
                    String[] details = line.split(",");
                    printSpecificColumns(details, headers);
                    doctors.put(details[0], details[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            System.out.print("Enter the DR_ID of your preferred doctor: ");
            chosenDrID = sc.nextLine();
        
            if (doctors.containsKey(chosenDrID)) {
                System.out.println("You have selected the doctor with ID: " + chosenDrID);
                updatePatientDB(patientFilePath, chosenDrID, patientID);
                DoctorSchedule.initializeAvailability(chosenDrID);

                return chosenDrID;
            } else {
                System.out.println("Invalid DR_ID. Please try again.");

                return null;
            }
        }
    }

    /**
     * Prints the header of the doctor selection CSV file.
     *
     * @param headers an array of header values from the CSV file
     */
    private static void printHeaders(String[] headers) {
        System.out.printf("%-10s %-20s %-10s%n", "ID", "NAME", "GENDER");
        System.out.println("===============================================");
    }

    /**
     * Prints the specific columns (doctor ID, name, and gender) from the doctor's data.
     *
     * @param details an array containing details of a single doctor
     * @param headers an array of header values from the CSV file
     */
    private static void printSpecificColumns(String[] details, String[] headers) {
        System.out.printf("%-10s %-20s %-10s%n", details[0], details[1], details[3]);
    }

    /**
     * Updates the patient's record in the database with the chosen doctor's ID.
     *
     * @param patientFilePath the path to the patient's CSV file
     * @param chosenDrID the ID of the doctor selected by the patient
     * @param patientID the unique identifier of the patient
     */
    private static void updatePatientDB(String patientFilePath, String chosenDrID, String patientID) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(patientFilePath))) {
            String headerLine = br.readLine();
            lines.add(headerLine);

            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(patientID)) { 
                    details[7] = chosenDrID;
                }
                lines.add(String.join(",", details));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(patientFilePath, false))) {
            for (String updatedLine : lines) {
                bw.write(updatedLine);
                bw.newLine();
            }
            System.out.println("PATIENT_DB updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}