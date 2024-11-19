import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The AppointmentManager class provides functionality for managing patient appointments,
 * including scheduling, loading, updating, and deleting appointments.
 */

public class AppointmentManager {

    DoctorSchedule docSchedule = new DoctorSchedule();

    static String apptFile = "HospitalApp\\src\\Database\\CSV\\APPOINTMENTS.csv";
    static Scanner sc = new Scanner(System.in);

    /**
     * Saves the list of appointments to a specified CSV file.
     * Each appointment is written as a line in the CSV file.
     * 
     * @param appointments the list of appointments to be saved
     * @param filePath the file path where appointments should be saved
     */
    public static void saveAppointmentsToFile(List<Appointment> appointments, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("Patient ID,Doctor ID,Date,Time,Status");
            bw.newLine();
            for (Appointment appointment : appointments) {
                bw.write(String.join(",", appointment.getPID(), appointment.getDID(), appointment.getDate(), appointment.getTime(), appointment.getStatus()));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads appointments from a CSV file and populates the provided list.
     * Each line in the file corresponds to an appointment with details (patient ID, doctor ID, date, time, status).
     * 
     * @param appointments the list to store the loaded appointments
     * @return the list of loaded appointments
     */
    public static ArrayList<Appointment> loadAppointments(ArrayList<Appointment> appointments) {
   
        try (BufferedReader br = new BufferedReader(new FileReader(apptFile))) {
            String currentLine;
            br.readLine();
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.trim().isEmpty()) {
                    continue;
                }
                String[] detailed = currentLine.split(",");
                if (detailed.length < 5) {
                    System.out.println("Skipping line due to insufficient data: " + currentLine);
                    continue;
                }
                String pID = detailed[0];
                String dID = detailed[1];
                String date = detailed[2];
                String time = detailed[3];
                String status = detailed[4];
                appointments.add(new Appointment(pID, dID, date, time, status));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return appointments;
    }

    /**
     * Updates the status of a specific appointment in the appointment list.
     * Searches for the appointment by patient ID and doctor ID, then updates its status.
     * 
     * @param appointment the appointment whose status needs to be updated
     * @return true if the status was successfully updated, false otherwise
     */
    public static boolean updateAppointmentStatus(Appointment appointment) {
        boolean updated = false;
        List<String[]> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(apptFile))) {
            String line = br.readLine(); 
            records.add(line.split(","));

            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(appointment.getPID()) && details[1].equals(appointment.getDID()) && details[2].equals(appointment.getDate()) && details[3].equals(appointment.getTime())) {
                    details[4] = "Completed";
                    updated = true;
                }
                records.add(details);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (updated) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(apptFile))) {
                for (String[] record : records) {
                    bw.write(String.join(",", record));
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return updated;
    }

    /**
     * Converts a time string (e.g., "EIGHT", "NINE") into its corresponding index for a time slot.
     * The time slots range from "EIGHT" to "FIVE", each with a corresponding index.
     * 
     * @param time the time string to convert (e.g., "EIGHT", "NINE")
     * @return the index of the time slot, or -1 if the time is invalid
     */
    public static int getTimeIndex(String time) {
        switch (time.toUpperCase()) {
            case "EIGHT": return 2;
            case "NINE": return 3;
            case "TEN": return 4;
            case "ELEVEN": return 5;
            case "TWELVE": return 6;
            case "ONE": return 7;
            case "TWO": return 8;
            case "THREE": return 9;
            case "FOUR": return 10;
            case "FIVE": return 11;
            default: return -1;
        }
    }

    /**
     * Converts a time string (e.g., "EIGHT") to its corresponding 24-hour format string (e.g., "0800").
     *
     * @param time the time string to convert
     * @return the corresponding 24-hour format string, or null if the time is invalid
     */
    public static String getTime(String time) {
        switch (time.toUpperCase()) {
            case "EIGHT": return "0800";
            case "NINE": return "0900";
            case "TEN": return "1000";
            case "ELEVEN": return "1100";
            case "TWELVE": return "1200";
            case "ONE": return "1300";
            case "TWO": return "1400";
            case "THREE": return "1500";
            case "FOUR": return "1600";
            case "FIVE": return "1700";
            default: return null;
        }
    }

    /**
     * Converts a 24-hour format string (e.g., "0800") back to its corresponding time string (e.g., "EIGHT").
     *
     * @param time the 24-hour format string to convert
     * @return the corresponding time string, or null if the time is invalid
     */
    public static String getTimeAlpha(String time){
        switch (time.toUpperCase()) {
            case "0800": return "EIGHT";
            case "0900": return "NINE";
            case "1000": return "TEN";
            case "1100": return "ELEVEN";
            case "1200": return "TWELVE";
            case "1300": return "ONE";
            case "1400": return "TWO";
            case "1500": return "THREE";
            case "1600": return "FOUR";
            case "1700": return "FIVE";
            default: return null;
        }
    } 

    /**
     * Adds a new appointment to the appointments list and updates the doctor's availability.
     * The appointment details are saved to the CSV file and the doctor's schedule is updated to reflect the appointment.
     * 
     * @param appointment the appointment to add
     * @param appointments the list of current appointments
     */
    public static void addAppointment(Appointment appointment, ArrayList<Appointment> appointments){
        DoctorSchedule.checkDocAvail(appointment.getDID(),appointment.getDate(),appointment.getTime());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(apptFile, true))) {
            bw.write(appointment.getPID() + "," + appointment.getDID() + "," + appointment.getDate() + "," + appointment.getTime() + "," + appointment.getStatus());
            bw.newLine();
            appointments.add(appointment);
            System.out.println("Appointment successfully scheduled and saved.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save appointment.");
            return;
        }
        DoctorSchedule.updateDocSchedule(appointment.getDID(), appointment.getDate(), appointment.getTime(), "N");
    } 

    /**
     * Changes the details of an existing appointment, including updating the date and time.
     * Ensures that the target date and time are available before updating the appointment.
     * 
     * @param chosenAppointment the appointment to change
     * @param appointments the list of current appointments
     */
    public static void changeAppointment(Appointment chosenAppointment, ArrayList<Appointment> appointments) {

        DoctorSchedule.viewDoctorSchedule(chosenAppointment.getDID());

        System.out.println("Enter your new date: (YYYY-MM-DD)");
        String targetDate = sc.next();

        System.out.println("Enter your new time: (HHMM, eg. 0800)");
        String targetTime = sc.next();
        Appointment oldAppt = new Appointment(chosenAppointment.getPID(), chosenAppointment.getDID(), 
        chosenAppointment.getDate(), chosenAppointment.getTime(), chosenAppointment.getStatus());
        DoctorSchedule.checkDocAvail(oldAppt.getDID(),targetDate,targetTime);
        for (Appointment appointment : appointments) {
            if (appointment.getPID().equals(oldAppt.getPID()) && 
                appointment.getDate().equals(oldAppt.getDate()) && 
                appointment.getTime().equals(oldAppt.getTime())) {
                appointment.setDate(targetDate);
                appointment.setTime(targetTime);
                appointment.setStatus("Pending");
                System.out.println("Appointment with pID " + oldAppt.getPID() + " rescheduled to " + targetDate + " at " + targetTime + ".");
                break;
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(apptFile))) {
            bw.write("Patient_ID,Doctor_ID,Date,Time,Status");
            bw.newLine();
            for (Appointment appointment : appointments) {
                bw.write(appointment.getPID() + "," +
                         appointment.getDID() + "," +
                         appointment.getDate() + "," +
                         appointment.getTime() + "," +
                         appointment.getStatus());
                bw.newLine();
            }
            System.out.println("Appointments updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        DoctorSchedule.updateDocSchedule(oldAppt.getDID(),oldAppt.getDate(),oldAppt.getTime(),"Y");
        DoctorSchedule.updateDocSchedule(oldAppt.getDID(),targetDate,targetTime,"N");
    }

    /**
     * Deletes an existing appointment from the appointments list and updates the doctor's availability.
     * The appointment is removed from the CSV file, and the doctor's schedule is updated to reflect the availability.
     * 
     * @param pID the patient ID associated with the appointment to delete
     * @param selectedAppointment the appointment to delete
     * @param appointments the list of current appointments
     */
    public static void deleteAppointment(String pID, Appointment selectedAppointment, ArrayList<Appointment> appointments) {                          
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(apptFile))) {
            bw.write("Patient_ID,Doctor_ID,Date,Time,Status");
            bw.newLine();
            for (Appointment appointment : appointments) {
                if (appointment.getPID().equals(pID) && 
                    appointment.getDate().equals(selectedAppointment.getDate()) && 
                    appointment.getTime().equals(selectedAppointment.getTime())){
                    continue;
                }
                else{
                    bw.write(appointment.getPID() + "," +
                         appointment.getDID() + "," +
                         appointment.getDate() + "," +
                         appointment.getTime() + "," +
                         appointment.getStatus());
                    bw.newLine();
                }
            }
            System.out.println("Appointment with pID " + pID + " removed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        DoctorSchedule.updateDocSchedule(selectedAppointment.getDID(), selectedAppointment.getDate(), selectedAppointment.getTime(), "Y");
    }

    /**
     * Prints a list of pending or confirmed appointments for a specific patient.
     * Filters appointments by the patient's ID and status (Pending or Confirmed).
     * 
     * @param patientID the patient ID to filter appointments by
     * @return a map of appointment indices and the corresponding appointments
     */
    public static Map<Integer, Appointment> printPendingConfirmedAppointments(String patientID) {
		boolean found = false;
		int startingIndex = 1;
		Map<Integer, Appointment> indexedAppointments = new HashMap<>();
	
		try (BufferedReader br = new BufferedReader(new FileReader(apptFile))) {
			String line; 
			System.out.println("=============================YOUR UPCOMING APPOINTMENTS=============================");
            System.out.printf("%-5s %-15s %-15s %-15s %-10s %-15s%n", "No.","PATIENT ID", "DOCTOR ID", "DATE", "TIME", "STATUS");
			System.out.println("====================================================================================");

			while ((line = br.readLine()) != null) {
				String[] details = line.split(",");
				String pID = details[0];
				String dID = details[1];
				String date = details[2];
				String time = details[3];
				String status = details[4];
	
				if (pID.equals(patientID) && (status.equals("Pending") || status.equals("Accepted"))) {
					Appointment appointment = new Appointment();
					appointment.setPID(pID);
					appointment.setDID(dID);
					appointment.setDate(date);
					appointment.setTime(time);
					appointment.setStatus(status);
	
					System.out.printf("%-5s %-15s %-15s %-15s %-10s %-15s%n", startingIndex, details[0], details[1], details[2], details[3], details[4]);
					indexedAppointments.put(startingIndex, appointment);
					found = true;
					startingIndex++;
				}
			}
			System.out.println("====================================================================================");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!found) {
			System.out.println("No appointments found for Patient ID: " + patientID);
			return null;
		}
        return indexedAppointments;
	}

    /**
     * Creates a new appointment for a patient with a specified doctor.
     * Prompts the user to enter appointment details, such as the date and time, and returns the created appointment.
     * 
     * @param patientID the ID of the patient requesting the appointment
     * @param doctorID the ID of the doctor for the appointment
     * @return the newly created appointment
     */
    public static Appointment createNewAppt(String patientID, String doctorID){
        System.out.println("Enter your date: (YYYY-MM-DD)");
        String Date = sc.next();

        System.out.println("Enter your time: (HHMM, eg. 0800)");
        String Time = sc.next();

        String Status = "Pending";
        Appointment appointment = new Appointment(patientID, doctorID, Date, Time, Status);
        return appointment;
    }
    
    /**
     * Allows a patient to select an appointment from a list of available appointments.
     * The patient can choose an appointment by its index, and the selected appointment is returned.
     * 
     * @param patientID the ID of the patient selecting the appointment
     * @return the selected appointment
     */
    public static Appointment selectAppointment(String patientID){
        Map<Integer, Appointment> indexedAppointments = new HashMap<>();
		indexedAppointments = printPendingConfirmedAppointments(patientID);
		System.out.println("Select which appointment:");
		int choice = sc.nextInt();
	
		Appointment selectedAppointment = indexedAppointments.get(choice);
		if (selectedAppointment == null) {
			System.out.println("Invalid selection.");
			return null;
		}
        else{
            return selectedAppointment;
        }
    }

    /**
     * Allows a doctor to select a completed appointment from a list of their completed appointments.
     * The doctor can choose an appointment by its index, and the selected appointment is returned.
     * 
     * @param doctorID the ID of the doctor selecting the completed appointment
     * @param docAppointments the list of completed appointments for the doctor
     * @return the selected completed appointment
     */
    public static Appointment selectCompletedAppointmentForDoc(String doctorID, ArrayList<Appointment> docAppointments){
        List<Appointment> completedAppointments = new ArrayList<>();
    
        for (Appointment appointment : docAppointments) {
            if (appointment.getDID().equals(doctorID) && appointment.getStatus().equals("Accepted")) {
                completedAppointments.add(appointment);
            }
        }
        
        if (completedAppointments.isEmpty()) {
            return null;
        }
        System.out.println("===========================ONGOING APPOINTMENTS===========================");
        System.out.printf("%-5s %-15s %-15s %10s%n", "No.", "PATIENT ID", "DATE", "TIME");
        System.out.println("==========================================================================");
        for (int i = 0; i < completedAppointments.size(); i++) {
            Appointment appointment = completedAppointments.get(i);
            System.out.printf("%-5s %-15s %-15s %10s%n", i + 1, appointment.getPID(), appointment.getDate(), appointment.getTime());
        }
        System.out.println("==========================================================================");

        System.out.print("Select an appointment to record an outcome: ");
        int choice = sc.nextInt() - 1;

        if (choice < 0 || choice >= completedAppointments.size()) {
            System.out.println("Invalid selection.");
            return null;
        }

        Appointment selectedAppointment = completedAppointments.get(choice);
        updateAppointmentStatus(selectedAppointment);
        return selectedAppointment;
    }

    /**
     * Allows a doctor to accept or decline pending appointments.
     * The doctor can view a list of pending appointments, select one, and choose to accept or decline it.
     * 
     * @param doctorID the ID of the doctor managing appointments
     * @param appointments the list of all appointments
     */
    public static void acceptOrDeclineAppointment(String doctorID, ArrayList<Appointment> appointments){
        List<Appointment> pendingAppointments = new ArrayList<>();
    
        for (Appointment appointment : appointments) {
            if (appointment.getDID().equals(doctorID) && appointment.getStatus().equals("Pending")) {
                pendingAppointments.add(appointment);
            }
        }
    
        if (pendingAppointments.isEmpty()) {
            System.out.println("No pending appointments to manage.");
            return;
        }

        System.out.println("===========================PENDING APPOINTMENTS===========================");
        System.out.printf("%-5s %-15s %-15s %10s%n", "No.", "PATIENT ID", "DATE", "TIME");
        System.out.println("==========================================================================");
        for (int i = 0; i < pendingAppointments.size(); i++) {
            Appointment appointment = pendingAppointments.get(i);
            System.out.printf("%-5s %-15s %-15s %10s%n", i + 1, appointment.getPID(), appointment.getDate(), appointment.getTime());
        }
        System.out.println("==========================================================================");
    
        System.out.println("Enter the number of the appointment to accept or decline:");
        int index = sc.nextInt() - 1;
    
        if (index >= 0 && index < pendingAppointments.size()) {
            Appointment selectedAppointment = pendingAppointments.get(index);
    
            System.out.println("Do you want to (1) accept or (2) decline this appointment?");
            int choice = sc.nextInt();
    
            switch (choice) {
                case 1:
                    selectedAppointment.setStatus("Accepted");
                    System.out.println("Appointment accepted.");
                    break;
                case 2:
                    selectedAppointment.setStatus("Decline");
                    System.out.println("Appointment declined.");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
    
            saveAppointmentsToFile(appointments, apptFile);
        } else {
            System.out.println("Invalid selection.");
        }
    }
    /**
     * Views the upcoming appointments for a specific doctor.
     * Displays the appointments scheduled for the doctor, including the details such as date, time, and patient ID.
     * 
     * @param appointments the list of all appointments
     * @param doctorID the ID of the doctor whose upcoming appointments are to be viewed
     */
    public static void viewUpcomingAppointments(ArrayList<Appointment> appointments, String doctorID){      
        System.out.println("=============================YOUR UPCOMING APPOINTMENTS=============================");
        System.out.printf("%-5s %-15s %-15s %-15s %-10s %-15s%n", "No.","PATIENT ID", "DOCTOR ID", "DATE", "TIME", "STATUS");
        System.out.println("====================================================================================");

        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getStatus().equals("Pending")){
                System.out.printf("%-5s %-15s %-15s %-15s %-10s %-15s%n", i+1, appointment.getPID(),appointment.getPID(), appointment.getDate(), appointment.getTime(), appointment.getStatus());
            }
        }

        System.out.println("====================================================================================");
    }
}
