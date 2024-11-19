
import java.io.*;
import java.util.*;


/**
 * The PatientController class is responsible for managing patient-related operations
 * such as updating contact information, scheduling appointments, and viewing medical records.
 * It interacts with the patient database and the appointment manager to handle various
 * patient tasks.
 */
public class PatientController {

    String patientFilePath = "HospitalApp\\src\\Database\\CSV\\DB\\PATIENT_DB.csv";
    String appointmentFile = "HospitalApp\\src\\Database\\CSV\\APPOINTMENTS.csv";

    Scanner sc = new Scanner(System.in);
    
    /**
     * Updates the contact information (phone number or email address) of the specified patient.
     *
     * @param patient the patient whose contact information is to be updated
     */
    public void updateContactInfo(Patient patient) {
        System.out.println("What would you like to update?");
        System.out.println("==============================");
        System.out.println("1. Phone number ");
        System.out.println("2. Email address ");
        int choice = sc.nextInt();
        
        switch(choice) {
            case 1:
                System.out.println("Please enter your new phone number: ");
                String newNum = sc.next();
                patient.setPhoneNo(newNum);
                updatePatient(patient.getID(), newNum, patient.getEmail());
                break;
            case 2:
                System.out.println("Please enter your new email address: ");
                String newEmail = sc.next();
                patient.setEmail(newEmail);
                updatePatient(patient.getID(), patient.getPhoneNo(), newEmail);
                break;
        }
    }

    /**
     * Updates the patient's information (phone number and email address) in the database.
     *
     * @param ID the unique identifier for the patient
     * @param phoneNo the new phone number for the patient
     * @param email the new email address for the patient
     */
    public void updatePatient(String ID, String phoneNo, String email) {
        List<String> updatedContent = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(patientFilePath))) {
            String line;
            String header = br.readLine();
            updatedContent.add(header);
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(ID)) {
                    values[5] = email;
                    values[6] = phoneNo;
                }
                updatedContent.add(String.join(",", values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(patientFilePath))) {
            for (String line : updatedContent) {
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Patient information updated.");
        } catch (IOException e) {
            System.err.println("Error updating patient: " + e.getMessage());
        }
    }

    /**
     * Views available appointment slots for the assigned doctor of the specified patient.
     * If the patient does not have an assigned doctor, an appropriate message is displayed.
     *
     * @param patient the patient whose appointment slots are to be viewed
     */
    public void viewAppointmentSlots(Patient patient) {
        String doctorID = patient.getAssignedDoctorID();
        if (doctorID.equals("NULL") || doctorID.isEmpty() || doctorID == null) {
            System.out.println("You have not been assigned a doctor");
        } else {
            DoctorSchedule.viewDoctorSchedule(patient.getAssignedDoctorID());
        }
    }

    /**
     * Schedules a new appointment for the specified patient with their assigned doctor.
     * If the patient does not have an assigned doctor, an appropriate message is displayed.
     *
     * @param patient the patient for whom the appointment is to be scheduled
     */
    public void scheduleAppointment(Patient patient) {
        String doctorID = patient.getAssignedDoctorID();
        if (doctorID.equals("NULL") || doctorID.isEmpty() || doctorID == null) {
            System.out.println("You have not been assigned a doctor");
            return;
        }
        DoctorSchedule.viewDoctorSchedule(patient.getAssignedDoctorID());
        AppointmentManager.addAppointment(AppointmentManager.createNewAppt(patient.getID(), doctorID),patient.getPatientAppointments());
    }

    /**
     * Reschedules an existing appointment for the specified patient.
     * The patient can select an existing appointment to reschedule.
     *
     * @param patient the patient whose appointment is to be rescheduled
     */
    public void rescheduleAppointment(Patient patient) {
        AppointmentManager.changeAppointment(AppointmentManager.selectAppointment(patient.getID()), patient.getPatientAppointments());
    }
	
    /**
    * Cancels an existing appointment for the specified patient.
    * The patient can select an existing appointment to cancel.
    *
    * @param patient the patient whose appointment is to be canceled
    */
    public void cancelAppointment(Patient patient) {
        AppointmentManager.deleteAppointment(patient.getID(), AppointmentManager.selectAppointment(patient.getID()), patient.getPatientAppointments());
    }

    /**
     * Prints all pending and confirmed appointments for the specified patient.
     *
     * @param patient the patient whose appointments are to be printed
     */
    public void printAppointmentsForID(Patient patient) {
        AppointmentManager.printPendingConfirmedAppointments(patient.getID());
    }
    
    /**
     * Views the outcome records for the specified patient.
     *
     * @param patient the patient whose outcome records are to be viewed
     */
    public void viewOutcomeRecord(Patient patient) {
        OutcomeManager.viewOutcomeRecords(patient.getID());
    }

    /**
     * Views the medical records for the specified patient.
     *
     * @param patient the patient whose medical records are to be viewed
     */
    public void viewMedicalRecord(Patient patient) {
        MedicalRecManager.viewMedicalRecords(patient.getID());
    }
}
