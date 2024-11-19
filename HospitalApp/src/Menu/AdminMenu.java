
import java.util.Scanner;


/**
 * The AdminMenu class represents the menu interface for administrators in the hospital management application.
 * It provides options for administrators to manage staff, appointments, medical inventory, and security settings.
 */

public class AdminMenu extends UserMenu{


    Scanner sc = new Scanner(System.in);

    /**
     * Displays the menu options available to the administrator.
     * This includes options for managing staff, viewing appointments,
     * managing medication inventory, and changing personal information.
     */
    public void displayMenu() {
        System.out.println("===== Administrator Menu =====");

        System.out.println("\n=== Staff Management ===");
    	System.out.println("1. View and Manage Hospital Staff ");

        System.out.println("\n=== Appointments ===");
    	System.out.println("2. View Appointments details ");

        System.out.println("\n=== Medical Inventory ===");
    	System.out.println("3. View and Manage Medication Inventory ");
    	System.out.println("4. Approve Replenishment Requests ");

        System.out.println("\n=== Security ===");        
        System.out.println("5. Change password");

        System.out.println("\n=== Logout ===");
    	System.out.println("6. Logout ");
    }
    
    /**
     * Handles the actions based on the user's menu selection.
     * This method continuously displays the menu and processes user input
     * until the user chooses to log out.
     *
     * @param id the unique identifier for the administrator
     */
    public void menuActions(String id) {	
        AdminController adminController = new AdminController(); // Use a relationship
        Admin admin = new Admin(id);
        while(true){
            displayMenu();
            int option = sc.nextInt();

            switch(option) {
                case 1:
                    adminController.manageStaff();
                    break;
                case 2:
                    adminController.displayAppointmentsAndOutcomes();
                    break;
                case 3:
                    adminController.manageMedInv();
                    break;
                case 4:
                    adminController.approveReplenishmentRequest(admin);
                    break;
                case 5:
                    changePassword(id);
                    break;
                case 6:
                    clearConsole();
                    break;
                default:
                    System.out.println("Invalid option. ");
                    break;
            }
            if (option == 6) {
                break;
            }
        }
    }
}
