
import java.io.*;
import java.time.LocalDate;
import java.util.*;


/**
 * The DoctorController class manages the interactions and operations
 * related to doctors within the appointment management system. It provides
 * methods for viewing a doctor's personal schedule, upcoming appointments,
 * patient medical records, and managing appointment outcomes. Additionally,
 * it allows doctors to set their availability and accept or decline appointments.
 * 
 * This class acts as a bridge between the user interface and the underlying
 * data management systems, facilitating the retrieval and updating of
 * appointment and medical record information.
 */

public class DoctorController {
    private static final String doctorAvailFilePath = "HospitalApp\\src\\Database\\CSV\\DOCTORS_AVAILABILITY.csv";

    Scanner scanner = new Scanner(System.in);

    /**
     * Displays the personal schedule of the specified doctor.
     * 
     * @param doctor The doctor whose schedule is to be viewed.
     */
    public void viewPersonalSchedule(Doctor doctor){
        DoctorSchedule.viewDoctorSchedule(doctor.getDoctorID());
    }

    /**
     * Displays the upcoming accepted appointments for the specified doctor.
     * 
     * @param doctor The doctor whose upcoming appointments are to be viewed.
     */
    public void viewUpcomingAppointments(Doctor doctor) {   
        AppointmentManager.viewUpcomingAppointments(doctor.getDocAppointments(), doctor.getDoctorID());
    }

    /**
     * Views all medical records and outcome records for the specified doctor.
     * 
     * @param doctor The doctor whose patient medical records are to be viewed.
     */
    public void viewPatientMedicalRecords(Doctor doctor) {
        MedicalRecManager.viewAllMedicalRecordsForDoc(doctor.getDoctorID());
        OutcomeManager.viewOutcomeRecordsForDoc(doctor.getDoctorID());
        
    }

    /**
     * Updates the medical records for a specific patient associated with the specified doctor.
     * 
     * @param doctor The doctor updating the patient's medical records.
     */
    public void updatePatientMedicalRecords(Doctor doctor) {
        OutcomeManager.updateMedicalRecords(doctor.getDoctorID());
    }

    /**
     * Sets the availability of the specified doctor for the next seven days.
     * 
     * @param doctor The doctor whose availability is to be set.
     */
    public void setAvailability(Doctor doctor) {
        LocalDate today = LocalDate.now();
        List<LocalDate> nextSevenDays = new ArrayList<>();
    
        for (int i = 0; i < 7; i++) {
            nextSevenDays.add(today.plusDays(i));
        }
    
        System.out.println("Choose a date to set availability to unavailable (next 7 days):");
        System.out.println("===============================================================");
        System.out.printf("%-5s %-15s%n", "No.", "DATE");
        System.out.println("===============================================================");
        for (int i = 0; i < nextSevenDays.size(); i++) {
            System.out.printf("%-5s %-15s%n", i + 1, nextSevenDays.get(i));
        }
        System.out.println("=================================================================");

        int dateChoice = -1;
        while (dateChoice < 1 || dateChoice > 7) {
            System.out.print("Enter your choice (1-7): ");
            String input = scanner.nextLine();
            try {
                dateChoice = Integer.parseInt(input);
                if (dateChoice < 1 || dateChoice > 7) {
                    System.out.println("\nInvalid choice. Please select a number between 1 and 7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a valid number.");
            }
        }
    
        LocalDate selectedDate = nextSevenDays.get(dateChoice - 1);
        System.out.println("Selected date: " + selectedDate);
        
        if(!displayTimeSlots(selectedDate, doctor.getDoctorID())){
            return;
        }
    
        int timeSlotChoice = -1;
        while (timeSlotChoice < 1 || timeSlotChoice > 10) {
            System.out.print("Choose a time slot to set your availability to unavailable (1-10 for 8 AM to 6 PM): ");
            String timeSlotInput = scanner.nextLine();
            try {
                timeSlotChoice = Integer.parseInt(timeSlotInput);
                if (timeSlotChoice < 1 || timeSlotChoice > 10) {
                    System.out.println("Invalid choice. Please select a number between 1 and 10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    
        DoctorSchedule.updateAvailability(selectedDate, timeSlotChoice, doctor.getDoctorID());
    }   

    /**
     * Displays available time slots for a specific date for the specified doctor.
     * 
     * @param date The date for which to display available time slots.
     * @param doctorID The ID of the doctor whose time slots are being displayed.
     * @return true if time slots are found, false otherwise.
     */
    public boolean displayTimeSlots(LocalDate date, String doctorID) {
        try (BufferedReader br = new BufferedReader(new FileReader(doctorAvailFilePath))) {
            String line;
            boolean foundDate = false;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(doctorID) && values[1].equals(date.toString())) {
                    foundDate = true;
                    System.out.println("=============================TIME SLOTS FOR: " + date + "=============================");
                    System.out.printf("%-5s %-25s %-5s%n", "No.", "TIME", "AVAILABILITY");
                    System.out.println("===================================================================================");
                    for (int i = 2; i < values.length; i++) {
                        if (values[i].equals("Y")){
                            String timeSlot = getTimeSlot(i - 2); // Convert index to time slot
                            System.out.printf("%-5s %-25s %-5s%n", i - 1, timeSlot, values[i]);
                        }
                    }
                    System.out.println("===================================================================================");
                    return true;
                }
            }

            if (!foundDate) {
                System.out.println("No availability found for this date.");
                return false;
            }
        } catch (IOException e) {
            System.err.println("Error reading availability file: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Retrieves the string representation of a time slot based on its index.
     * 
     * @param index The index of the time slot.
     * @return The string representation of the time slot.
     */
    private String getTimeSlot(int index) {
        String[] timeSlots = {
            "8:00 AM - 9:00 AM",
            "9:00 AM - 10:00 AM",
            "10:00 AM - 11:00 AM",
            "11:00 AM - 12:00 PM",
            "12:00 PM - 1:00 PM",
            "1:00 PM - 2:00 PM",
            "2:00 PM - 3:00 PM",
            "3:00 PM - 4:00 PM",
            "4:00 PM - 5:00 PM",
            "5:00 PM - 6:00 PM"
        };
        return timeSlots[index];
    }

    /**
     * Accepts or declines appointments for the specified doctor.
     * 
     * @param doctor The doctor who will accept or decline appointments.
     */
    public void acceptOrDecline(Doctor doctor) {
        AppointmentManager.acceptOrDeclineAppointment(doctor.getDoctorID(), doctor.getDocAppointments());
    }
    
    /**
     * Records the outcome of a completed appointment for the specified doctor.
     * 
     * @param doctor The doctor who is recording the appointment outcome.
     */
    public void recordAppointmentOutcome(Doctor doctor) {
        String doctorID = doctor.getDoctorID();
        // Record the outcome
        OutcomeManager.addOutcomeRecord(OutcomeManager.createNewOutcomeRecord(AppointmentManager.selectCompletedAppointmentForDoc(doctorID, doctor.getDocAppointments()), doctorID));
    }
}
