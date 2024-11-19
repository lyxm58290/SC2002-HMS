
import java.util.Scanner;


/**
 * The PatientMenu class provides a menu interface specific to patients in the hospital management system.
 * This menu allows patients to view and update their medical records, manage appointments, and change their password.
 * Patients can also log out from this menu.
 */

public class PatientMenu extends UserMenu{

    Scanner sc = new Scanner(System.in);

    /**
     * Displays the menu options available to a patient. 
     * These options include viewing and updating personal information, managing appointments, 
     * viewing past appointment outcomes, and security settings.
     */
    public void displayMenu(){
        System.out.println("===== Patient Menu =====");
        
        System.out.println("\n=== Medical Records ===");
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        
        System.out.println("\n=== Appointment Management ===");
        System.out.println("3. View Available Appointment Slots");
        System.out.println("4. Schedule an Appointment");
        System.out.println("5. Reschedule an Appointment");
        System.out.println("6. Cancel an Appointment");
        System.out.println("7. View Scheduled Appointments");

        System.out.println("\n=== Past Appointments ===");
        System.out.println("8. View Past Appointment Outcome Records");

        System.out.println("\n=== Security ===");        
        System.out.println("9. Change password");

        System.out.println("\n=== Logout ===");
        System.out.println("10. Logout");
        
        System.out.println("=========================");
    };

    /**
     * Executes menu actions based on the patient's input.
     * Actions include viewing and updating records, scheduling and managing appointments, 
     * viewing past outcomes, and changing the password.
     *
     * @param id the unique identifier for the patient
     */
    public void menuActions(String id) {
        PatientController patientController = new PatientController();
        Patient patient = new Patient(id);
        while(true){
            displayMenu();

            int option = sc.nextInt();
            sc.nextLine();

            switch(option) {
                case 1:
                    patientController.viewMedicalRecord(patient);
                    break;
                case 2:
                    patientController.updateContactInfo(patient);
                    break;
                case 3:
                    patientController.viewAppointmentSlots(patient);
                    break;
                case 4:
                    patientController.scheduleAppointment(patient);
                    break;
                case 5:
                    patientController.rescheduleAppointment(patient);
                    break;
                case 6:
                    patientController.cancelAppointment(patient);
                    break;
                case 7:
                    patientController.printAppointmentsForID(patient);
                    break;
                case 8:
                    patientController.viewOutcomeRecord(patient); 
                    break;
                case 9:
                    changePassword(id);
                    break;
                case 10:
                    clearConsole();
                    break;
                default:
                    System.out.println("Invalid option. ");
                    break;
            }

            if (option == 10) {
                break;
            }
        }
    }
}
