
import java.io.*;
import java.util.*;



/**
 * The PharmacistController class provides functionality to manage the pharmacist's operations 
 * within the hospital management application.
 * 
 * This includes viewing outcome records, managing inventory, dispensing medication, 
 * and submitting replenishment requests. The class integrates with InventoryManager and 
 * OutcomeManager for file-based operations and data management.
 */

public class PharmacistController {

    String invFile = "HospitalApp\\src\\Database\\CSV\\INVENTORY.csv";
    String outcomeFile = "HospitalApp\\src\\Database\\CSV\\OUTCOME.csv";

    Scanner sc = new Scanner(System.in);

    /**
     * Displays all outcome records related to medication orders.
     * 
     * This method delegates the operation to the OutcomeManager class.
     */ 
    public void viewAllOutcomeRecords() {
        OutcomeManager.viewAllOutcomeRecords();
    }
    
    /**
     * Displays the current medication inventory available to the pharmacist.
     * 
     * This method delegates the operation to the InventoryManager class.
     */
    public void viewInventory() {
        InventoryManager.viewMedInv();
    }

    /**
     * Handles the dispensing of medication to patients by:
     * - Loading inventory and outcome records from their respective CSV files.
     * - Displaying pending medication orders.
     * - Allowing the pharmacist to select an order to dispense.
     * - Checking inventory levels and updating records upon successful dispensing.
     * 
     * If the inventory is insufficient or the medication is unavailable, appropriate
     * messages are displayed to the pharmacist.
     * 
     * Updates are saved back to the CSV files after processing.
     */
    public void dispenseMedication(Pharmacist pharmacist) {
        List<String[]> outcomes = new ArrayList<>();
        List<String[]> pendingMedications = new ArrayList<>();
    
        // Read pending medication orders from the outcome file
        try (BufferedReader outcomeReader = new BufferedReader(new FileReader(outcomeFile))) {
            String line = outcomeReader.readLine();
            outcomes.add(line.split(",")); 
    
            while ((line = outcomeReader.readLine()) != null) {
                String[] details = line.split(",");
                String medStatus = details[7].trim();
    
                if (medStatus.equals("Pending")) {
                    pendingMedications.add(details);
                }
                outcomes.add(details);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    
        // Check if there are any pending medication orders
        if (pendingMedications.isEmpty()) {
            System.out.println("No pending medications found.");
            return;
        }
    
        // Display pending medication orders
        System.out.println("====================PENDING MEDICATION ORDERS====================");
        System.out.printf("%-5s %-10s %-15s %-10s %-10s%n", 
                            "No.", "Patient ID", "Medication", "Amount", "Status");
        System.out.println("=================================================================");
        for (int i = 0; i < pendingMedications.size(); i++) {
            String[] order = pendingMedications.get(i);
            System.out.printf("%-5s %-10s %-15s %-10s %-10s%n", 
                              i + 1, order[0], order[5], order[6], order[7]);
        }
        System.out.println("=================================================================");
    
        // Allow user to select a medication order
        System.out.print("Select which medication order to dispense: ");
        int choice = sc.nextInt();
        sc.nextLine();
    
        if (choice < 1 || choice > pendingMedications.size()) {
            System.out.println("Invalid choice.");
            return;
        }
    
        String[] selectedOrder = pendingMedications.get(choice - 1);
        String medName = selectedOrder[5].trim();
        int amountRequired = Integer.parseInt(selectedOrder[6].trim());
        ArrayList<Inventory> inventory = pharmacist.getInventory();
    
        // Find the medication in the inventory
        Inventory targetItem = null;
        for (Inventory item : inventory) {
            if (item.getMedName().equalsIgnoreCase(medName)) {
                targetItem = item;
                break;
            }
        }
    
        if (targetItem == null) {
            System.out.println("Apologies. We do not have this medicine.");
            return;
        }
    
        // Check stock and update if possible
        int stock = Integer.parseInt(targetItem.getStock());
        if (stock >= amountRequired) {
            targetItem.setStock(String.valueOf(stock - amountRequired));
            selectedOrder[7] = "Dispensed";
    
            System.out.printf("Dispensed %d units of %s for Patient ID: %s.%n", 
                              amountRequired, medName, selectedOrder[0]);
    
            // Update files after successful dispensing
            InventoryManager.updateInvOutcomefiles(outcomes, inventory, outcomeFile, invFile);
        } else {
            System.out.println("Insufficient stock to fulfill this order.");
        }
    }

    /**
    * Submits a request to replenish stock for a specific medication in the inventory.
    * 
    * This method prompts the pharmacist to select a medication and delegates 
    * the replenishment request to the InventoryManager.
    * 
    * @param pharmacist the Pharmacist object representing the current user
    */
    public void requestReplenishment(Pharmacist pharmacist) {
        String medicationName = InventoryManager.selectMedication(pharmacist.getInventory());

        InventoryManager.setReplenishment(medicationName, pharmacist.getInventory());
    }
}
