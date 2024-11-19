
import java.io.*;
import java.util.*;


/**
 * The `AdminController` class is responsible for managing and controlling various administrative tasks 
 * within the hospital management application.
 * 
 * This includes handling appointments, outcomes, inventory management, and staff management. 
 * The `AdminController` class serves as an interface between the administrator and the underlying data management systems.
 * It allows the administrator to perform actions such as viewing and modifying medical inventory, approving replenishment requests,
 * and managing hospital staff.
 */

public class AdminController {

    String appointmentFile = "HospitalApp\\src\\Database\\CSV\\APPOINTMENTS.csv";
    String outcomeFile = "HospitalApp\\src\\Database\\CSV\\OUTCOME.csv";
    String inventoryFile = "HospitalApp\\src\\Database\\CSV\\INVENTORY.csv";

    Scanner sc = new Scanner(System.in);

    /**
     * Displays all completed appointments along with their corresponding outcome records.
     * 
     * The method loads completed appointments from the `APPOINTMENTS.csv` file and matches them with the corresponding 
     * outcome records from the `OUTCOME.csv` file. The information is displayed in a formatted manner, showing both 
     * appointment details and the corresponding outcome, including diagnosis, medication, treatment, and other relevant details.
     */
    public void displayAppointmentsAndOutcomes() {

        Map<String, String[]> completedOutcomesMap = new HashMap<>();

        try (BufferedReader outcomeReader = new BufferedReader(new FileReader(outcomeFile))) {
            String line = outcomeReader.readLine();
            while ((line = outcomeReader.readLine()) != null) {
                String[] details = line.split(",");
                String appointmentDateTime = details[2].trim(); 
                
                completedOutcomesMap.put(appointmentDateTime, details);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader appointmentReader = new BufferedReader(new FileReader(appointmentFile))) {
            String line = appointmentReader.readLine();

            while ((line = appointmentReader.readLine()) != null) {
                String[] appointmentDetails = line.split(",");
                String appointmentDateTime = appointmentDetails[2].trim() + "+" + appointmentDetails[3].trim();
                String appointmentStatus = appointmentDetails[4].trim(); 
                System.out.println("=============================================SCHEDULED APPOINTMENTS==============================================");
                System.out.printf("%-10s %-10s %-15s %-10s %-10s%n", "PATIENT ID", "DOCTOR ID", "DATE", "TIME", "STATUS");
                
                if (appointmentStatus.equalsIgnoreCase("Completed") && completedOutcomesMap.containsKey(appointmentDateTime)) {
                    System.out.printf("%-10s %-10s %-15s %-10s %-10s%n", 
                    appointmentDetails[0], appointmentDetails[1], appointmentDetails[2], appointmentDetails[3], appointmentDetails[4]);
                    String[] outcome = completedOutcomesMap.get(appointmentDateTime);
                    System.out.println("=================================================OUTCOME RECORDS=================================================");
                    System.out.printf("%-10s %-10s %-15s %-10s %-15s %-15s %-10s %-10s %-10s%n", "PATIENT ID", "DOCTOR ID", "DATE_TIME", "DIAGNOSIS", "SERVICE", "MEDS", "AMOUNT", "STATUS", "TREATMENT");
                    System.out.printf("%-10s %-10s %-15s %-10s %-15s %-15s %-10s %-10s %-10s%n\n", 
                    outcome[0], outcome[1], outcome[2], outcome[3], outcome[4], outcome[5], outcome[6], outcome[7], outcome[8]); 
                }else{
                    System.out.printf("%-10s %-10s %-15s %-10s %-10s%n", 
                    appointmentDetails[0], appointmentDetails[1], appointmentDetails[2], appointmentDetails[3], appointmentDetails[4]);
                    System.out.println("=================================================OUTCOME RECORDS=================================================");
                    System.out.printf("%-10s %-10s %-15s %-10s %-15s %-15s %-10s %-10s %-10s%n", "PATIENT ID", "DOCTOR ID", "DATE_TIME", "DIAGNOSIS", "SERVICE", "MEDS", "AMOUNT", "STATUS", "TREATMENT");
                    System.out.println("------------------------------------------------No Outcome Record------------------------------------------------\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manages the medical inventory by allowing the administrator to perform various actions on medications.
     * 
     * The administrator can view, add, remove, update stock levels, or manage low stock level alerts for medications.
     * This method presents a menu for the administrator to choose from and calls the corresponding method from the 
     * `InventoryManager` class to perform the selected operation.
     */
    public void manageMedInv(){
        InventoryManager invManager = new InventoryManager();

        System.out.println("\n");
        System.out.println("What would you like to do? ");
        System.out.println("===========================================");
        System.out.println("1. View medical inventory");
        System.out.println("2. Add new medication to stock");
        System.out.println("3. Remove a medication from stock");
        System.out.println("4. Update medication stock level");
        System.out.println("5. Update medication low stock level alert");
        System.out.println("===========================================");

        System.out.print("Your choice: ");
        int option = sc.nextInt();

        switch(option){
            case 1:
                invManager.viewMedInv();
                break;
            case 2:
                invManager.addMed();
                break;
            case 3:
                invManager.removeMed();
                break;
            case 4:
                invManager.updateStockLevel();
                break;
            case 5:
                invManager.updateLowStockLevel();
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    /**
     * Approves the replenishment request for a specific medication based on the administrator's inventory.
     * 
     * The administrator selects a medication from their inventory, and the system processes the replenishment request 
     * for that medication.
     *
     * @param admin the administrator approving the replenishment request
     */
    public void approveReplenishmentRequest(Admin admin) {
        String medicationName = InventoryManager.selectMedication(admin.getInventory());
        InventoryManager.updateReplenishment(medicationName, admin.getInventory());
    }

    /**
     * Manages hospital staff by allowing the administrator to perform various operations on staff members.
     * 
     * The administrator can display the staff list, add new staff members, update staff member details, or remove 
     * staff members from the system. The method displays a menu for the administrator to choose from and calls the 
     * corresponding method from the `StaffManager` class to perform the selected operation.
     */
    public void manageStaff(){
        StaffManager staffManager = new StaffManager();

        System.out.println("======STAFF MANAGEMENT======");
        System.out.println("1. Display staff list");
        System.out.println("2. Add staff member");
        System.out.println("3. Update staff member details");
        System.out.println("4. Remove staff member");
        System.out.println("============================");
        System.out.print("Select an option: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice){
            case 1: 
                staffManager.displayStaff();
                break;
            case 2:
                staffManager.addStaff();
                break;
            case 3:
                staffManager.updateStaff();
                break;
            case 4: 
                staffManager.removeStaff();
                break;
            default:
                System.out.println("Invalid option. Please select options 1-4 only.");
        
        }
    }
}
