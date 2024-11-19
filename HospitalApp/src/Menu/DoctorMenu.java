
import java.util.Scanner;


/**
 * The DoctorMenu class represents the menu interface for doctors in the hospital management application.
 * It provides options for doctors to manage patient medical records, appointments, and personal settings.
 */

public class DoctorMenu extends UserMenu {

    Scanner sc = new Scanner(System.in);
    
    /**
     * Displays the menu options available to the doctor.
     * This includes options for viewing and updating patient medical records,
     * managing appointments, and changing personal information.
     */
    public void displayMenu() {
        System.out.println("===== Doctor Menu =====");

        System.out.println("\n=== Medical Records ===");
    	System.out.println("1. View Patient Medical Records ");
    	System.out.println("2. Update Patient Medical Records ");

        System.out.println("\n=== Appointment Management ===");
    	System.out.println("3. View Personal Schedule ");
    	System.out.println("4. Set Availability for Appointments ");
    	System.out.println("5. Accept or Decline Appointment Requests ");
    	System.out.println("6. View Upcoming Appointments ");
    	System.out.println("7. Record Appointment Outcome");

        System.out.println("\n=== Security ===");        
        System.out.println("8. Change password");

        System.out.println("\n=== Logout ===");
    	System.out.println("9. Logout ");
    }

    /**
     * Handles the actions based on the user's menu selection.
     * This method continuously displays the menu and processes user input
     * until the user chooses to log out.
     *
     * @param id the unique identifier for the doctor
     */
    public void menuActions(String id) {
        DoctorController doctorController = new DoctorController(); // Use a relationship
        Doctor doctor = new Doctor(id);
        while(true){
            displayMenu();

            int option = sc.nextInt();

            switch(option) {
                case 1:
                    doctorController.viewPatientMedicalRecords(doctor);
                    break;
                case 2:
                    doctorController.updatePatientMedicalRecords(doctor);
                    break;
                case 3:
                    doctorController.viewPersonalSchedule(doctor);
                    break;
                case 4:
                    doctorController.setAvailability(doctor);
                    break;
                case 5:
                    doctorController.acceptOrDecline(doctor);
                    break;
                case 6:
                    doctorController.viewUpcomingAppointments(doctor);
                    break;
                case 7:
                    doctorController.recordAppointmentOutcome(doctor);
                    break;
                case 8:
                    changePassword(id);
                    break;
                case 9:
                    clearConsole();
                    break;
                default:
                    System.out.println("Invalid option. ");
                    break;
            }
            if (option == 9) {
                break;
            }
        }
    }
}
