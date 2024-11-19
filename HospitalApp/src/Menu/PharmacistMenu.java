
import java.util.Scanner;

/**
 * The PharmacistMenu class represents the menu interface for pharmacists in the hospital management application.
 * It allows pharmacists to perform tasks such as viewing appointment outcome records, managing medication inventory,
 * submitting replenishment requests, changing passwords, and logging out.
 * 
 * This class interacts with the PharmacistController to execute various operations.
 */

public class PharmacistMenu extends UserMenu{

    Scanner sc = new Scanner(System.in);

    /**
     * Displays the menu options available to the pharmacist.
     * 
     * The menu is divided into several sections:
     * - Appointment Records: Options to view outcome records.
     * - Medical Inventory: Options to update prescription statuses, view inventory, and submit replenishment requests.
     * - Security: Option to change the pharmacist's password.
     * - Logout: Option to log out of the system.
     */
    public void displayMenu() {
        System.out.println("===== Pharmacist Menu =====");


        System.out.println("\n=== Appointment Records ===");
    	System.out.println("1. View Appointment Outcome Record ");

        System.out.println("\n=== Medical Inventory ===");
    	System.out.println("2. Update Prescription Status ");
    	System.out.println("3. View Medication Inventory ");
    	System.out.println("4. Submit Replenishment Request ");
        
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
     * @param id the unique identifier for the pharmacist
     */
    public void menuActions(String id) {	
        PharmacistController pharmacistController = new PharmacistController(); //uses a relationship
        Pharmacist pharmacist = new Pharmacist(id);
        while(true){
            displayMenu();
            int option = sc.nextInt();

            switch(option) {
                case 1:
                    pharmacistController.viewAllOutcomeRecords();
                    break;
                case 2:
                    pharmacistController.dispenseMedication(pharmacist);
                    break;
                case 3:
                    pharmacistController.viewInventory();
                    break;
                case 4:
                    pharmacistController.requestReplenishment(pharmacist);
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
