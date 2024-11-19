
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class InventoryManager {
    static Scanner sc = new Scanner(System.in);
    static String invFile = "HospitalApp\\src\\Database\\CSV\\INVENTORY.csv";

    /**
     * Loads the inventory from the CSV file into an ArrayList.
     * 
     * @param inventoryList The list where the inventory will be loaded.
     * @return The updated inventory list containing all medication records.
     */
    public static ArrayList<Inventory> loadInventory(ArrayList<Inventory> inventoryList) {
        try (BufferedReader br = new BufferedReader(new FileReader(invFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Inventory med = new Inventory(values[0], values[1], values[2], values[3]);
                inventoryList.add(med);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inventoryList;
    }

    /**
     * Displays the current medication inventory and allows a pharmacist to dispense selected medications.
     */
    public static void viewMedInv() {
        System.out.println("\n");
        try (BufferedReader br = new BufferedReader(new FileReader(invFile))) {
            String line;
            br.readLine();
            System.out.println("=================CURRENT MEDICAL INVENTORY=================");
            System.out.printf("%-15s %-10s %-20s %-10s%n", "MED_NAME", "STOCK", "LOW_STOCK_THRESHOLD", "REQ");
            System.out.println("===========================================================");
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                System.out.printf("%-15s %-10s %-20s %-10s%n",
                        details[0], details[1], details[2], details[3]);
            }
            System.out.println("===========================================================");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n");
    }

    /**
     * Updates the outcome files and the inventory file after a medication is dispensed.
     * 
     * @param outcomes The list of outcome records to write to the outcome file.
     * @param inventoryMap A map containing the updated stock values of medications.
     * @param outcomeFilePath The path to the outcome file.
     * @param inventoryFilePath The path to the inventory file.
     */
    public static void updateInvOutcomefiles(List<String[]> outcomes,ArrayList<Inventory> inventory, 
                                String outcomeFilePath, String inventoryFilePath) {
        try (BufferedWriter outcomeWriter = new BufferedWriter(new FileWriter(outcomeFilePath))) {
            for (String[] record : outcomes) {
                outcomeWriter.write(String.join(",", record));
                outcomeWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String[]> originalInventoryData = new HashMap<>();

        try (BufferedReader inventoryReader = new BufferedReader(new FileReader(inventoryFilePath))) {
            String line = inventoryReader.readLine();

            while ((line = inventoryReader.readLine()) != null) {
                String[] details = line.split(",");
                originalInventoryData.put(details[0].trim(), details);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter inventoryWriter = new BufferedWriter(new FileWriter(inventoryFilePath))) {
            inventoryWriter.write("MED_NAME,STOCK,LOW_STOCK_THRESHOLD,REQ");
            inventoryWriter.newLine();
            
            for (Inventory item : inventory) {
                String medName = item.getMedName();
                Integer updatedStock = Integer.parseInt(item.getStock());

                String[] originalDetails = originalInventoryData.get(medName);
                if (originalDetails != null) {
                    String lowStockThreshold = originalDetails[2];
                    String req = originalDetails[3];
                    
                    inventoryWriter.write(medName + "," + updatedStock + "," + lowStockThreshold + "," + req);
                    inventoryWriter.newLine();
                } else {
                    System.out.println("Warning: Medication " + medName + " not found in original inventory.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Records updated successfully.");
    }
    
    /**
     * Updates the stock level of a selected medication.
     * Prompts the user to select a medication and enter a new stock level, and then updates the inventory file.
     */
    public void updateStockLevel() {
        List<String[]> inventoryList = new ArrayList<>();
        String medNameToUpdate;
        int newStockLevel;
    
        try (BufferedReader br = new BufferedReader(new FileReader(invFile))) {
            String line = br.readLine();
            System.out.println("===================CURRENT INVENTORY===================");
    
            while ((line = br.readLine()) != null) {
                inventoryList.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.printf("%-5s %-15s %-10s%n", "No.", "MEDICATION", "STOCK"); 
        System.out.println("=======================================================");
        for (int i = 0; i < inventoryList.size(); i++) { 
            String[] record = inventoryList.get(i);
            System.out.printf("%-5s %-15s %-10s%n", i + 1, record[0], record[1]);
        }
        System.out.println("=======================================================");

        System.out.print("Select a medication to update stock level: ");
        int choice = sc.nextInt();
    
        if (choice < 1 || choice > inventoryList.size()) {
            System.out.println("Invalid choice.");
            return;
        }
    
        medNameToUpdate = inventoryList.get(choice - 1)[0];
    
        System.out.print("Enter the new stock level for " + medNameToUpdate + ": ");
        newStockLevel = sc.nextInt();
    
        inventoryList.get(choice - 1)[1] = String.valueOf(newStockLevel);
    
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(invFile))) {
            bw.write("MED_NAME,STOCK,LOW_STOCK_THRESHOLD,REQ");
            bw.newLine();
            
            for (String[] record : inventoryList) {
                bw.write(String.join(",", record));
                bw.newLine();
            }
            System.out.println("Stock level updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n");
    }

    /**
     * Updates the low stock threshold of a selected medication.
     * Prompts the user to select a medication and enter a new low stock threshold, and then updates the inventory file.
     */
    public void updateLowStockLevel() {
        List<String[]> inventoryList = new ArrayList<>();
        String medNameToUpdate;

        try (BufferedReader br = new BufferedReader(new FileReader(invFile))) {
            String line = br.readLine();
    
            while ((line = br.readLine()) != null) {
                inventoryList.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("===================CURRENT INVENTORY===================");
        System.out.printf("%-5s %-15s %-20s%n", "No.", "MEDICATION", "LOW STOCK THRESHOLD");
        System.out.println("=======================================================");
        for (int i = 0; i < inventoryList.size(); i++) {
            String[] record = inventoryList.get(i);
            System.out.printf("%-5s %-15s %-20s%n", i + 1, record[0], record[2]);
        }
        System.out.println("=======================================================");
        System.out.print("Select which medication you want to update low stock level: ");
        int choice = sc.nextInt();

        if (choice < 1 || choice > inventoryList.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        medNameToUpdate = inventoryList.get(choice - 1)[0];
        System.out.print("Enter new low stock level for " + medNameToUpdate + ": ");
        int newLowStock = sc.nextInt();
        inventoryList.get(choice - 1)[2] = String.valueOf(newLowStock); 

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(invFile))) {
            bw.write("MED_NAME,STOCK,LOW_STOCK_THRESHOLD,REQ");
            bw.newLine();
            
            for (String[] record : inventoryList) {
                bw.write(String.join(",", record));
                bw.newLine();
            }
            System.out.println("Low stock level updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.print("\n");
    }

    /**
     * Writes updated inventory data to the inventory file.
     * 
     * @param inventoryMap A map containing the updated inventory data.
     * @param inventoryFilePath The path to the inventory file to write to.
     */
    public static void updateInventoryFile(Map<String, String[]> inventoryMap, String inventoryFilePath) {
        try (BufferedWriter inventoryWriter = new BufferedWriter(new FileWriter(inventoryFilePath))) {
            inventoryWriter.write("MED_NAME,STOCK,LOW_STOCK_THRESHOLD,REQ");
            inventoryWriter.newLine();

            for (String[] record : inventoryMap.values()) {
                inventoryWriter.write(String.join(",", record));
                inventoryWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new medication to the inventory.
     * Prompts the user to enter medication details (name, stock level, low stock threshold) and appends them to the inventory file.
     */
    public void addMed() {
        System.out.print("Enter medication name: ");
        String medName = sc.next();
        System.out.print("Enter initial stock level: ");
        int stock = sc.nextInt();
        System.out.print("Enter low stock threshold: ");
        int lowStockThreshold = sc.nextInt();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(invFile, true))) {
            bw.write(String.format("%s,%d,%d,0%n", medName.toUpperCase(), stock, lowStockThreshold));
            System.out.println("Medication added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a selected medication from the inventory.
     * Prompts the user to select a medication and removes it from the inventory file.
     */
    public void removeMed() {
        List<String[]> inventoryList = new ArrayList<>();
        String medNameToRemove;
    
        try (BufferedReader br = new BufferedReader(new FileReader(invFile))) {
            String line = br.readLine(); 
            while ((line = br.readLine()) != null) {
                inventoryList.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    
        System.out.println("\n====================CURRENT MEDICATION====================");
        System.out.printf("%-5s %-15s%n", "No.", "MEDICATION");
        System.out.println("==========================================================");
        for (int i = 0; i < inventoryList.size(); i++) {
            System.out.printf("%-5s %-15s%n", i + 1, inventoryList.get(i)[0]);
        }
        System.out.println("==========================================================");
    
        System.out.print("Select which medication you want to remove: ");
        int choice = sc.nextInt();
    
        if (choice < 1 || choice > inventoryList.size()) {
            System.out.println("Invalid choice.");
            return;
        }
    
        medNameToRemove = inventoryList.get(choice - 1)[0];
    
        inventoryList.remove(choice - 1);
    
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(invFile))) {
            bw.write("MED_NAME,STOCK,LOW_STOCK_THRESHOLD,REQ");
            bw.newLine();
            
            for (String[] record : inventoryList) {
                bw.write(String.join(",", record));
                bw.newLine();
            }
            System.out.println("Medication removed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows the pharmacist to select a medication from the inventory.
     * Displays a list of available medications and prompts the user to choose one.
     * 
     * @param inventory The list of medications in the inventory.
     * @return The name of the selected medication, or null if the selection is invalid.
     */
    public static String selectMedication(ArrayList<Inventory> inventory){
        if (inventory == null || inventory.isEmpty()) {
            System.out.println("Inventory is empty or not loaded properly.");
            return null;
        }
        System.out.println("=========================================INVENTORY LIST=========================================");
        System.out.printf("%-5s %-20s %-10s %-20s %-25s%n", "No.","Medication", "Stock", "Low Stock Threshold", "Current Replenishment Request");
        System.out.println("================================================================================================");
        int count = 1;
        for (Inventory med : inventory) {
            System.out.printf("%-5s %-20s %-10s %-20s %-25s%n", 
                            count++, med.getMedName(), med.getStock(), med.getLowStockThreshold(), med.getRequest());
        }
        System.out.println("================================================================================================");

        System.out.print("Select a medication: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice < 1 || choice >= inventory.size()) {
            System.out.println("Invalid choice.");
            return null;
        }

        Inventory selectedMedication = inventory.get(choice-1);
        String medicationName = selectedMedication.getMedName();
        return medicationName;
    }

    /**
     * Sets a replenishment request for a selected medication.
     * Prompts the user to enter a replenishment amount and updates the selected medication's request in the inventory file.
     * 
     * @param medicationName The name of the medication to request replenishment for.
     * @param inventory The list of medications in the inventory.
     */
    public static void setReplenishment(String medicationName, ArrayList<Inventory> inventory){
        System.out.print("Enter the amount to request for " + medicationName + ": ");
        int requestAmount = sc.nextInt();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(invFile))) {
            bw.write("MED_NAME,STOCK,LOW_STOCK_THRESHOLD,REQ");
            bw.newLine();
            for (Inventory med : inventory) {
                if(med.getMedName().equals(medicationName)){
                    med.setRequest(String.valueOf(requestAmount)); 
                }
                bw.write(String.join(",", med.getMedName(), med.getStock(), med.getLowStockThreshold(), med.getRequest()));
                bw.newLine();
            }
            System.out.println("Replenishment request submitted for " + medicationName + " with amount " + requestAmount + ".");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the stock of a specified medication in the inventory by adding the requested
     * replenishment amount and resets the request amount to zero. The updated inventory data 
     * is then written back to the inventory file.
     * 
     * @param medicationName The name of the medication to update.
     * @param inventory The list of Inventory objects representing the current medication inventory.
     * @throws IOException If an I/O error occurs while writing to the inventory file.
     */
    public static void updateReplenishment(String medicationName, ArrayList<Inventory> inventory){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(invFile))) {
            bw.write("MED_NAME,STOCK,LOW_STOCK_THRESHOLD,REQ");
            bw.newLine();
            for (Inventory med : inventory) {
                if(med.getMedName().equals(medicationName)){
                    int currentStock = Integer.parseInt(med.getStock());
                    int requestedAmount = Integer.parseInt(med.getRequest());
                    if (requestedAmount <= 0) {
                        System.out.println("No replenishment request available for this medication.");
                        return;
                    }
                    int newStock = currentStock + requestedAmount;
                    med.setStock(String.valueOf(newStock));
                    med.setRequest("0");
                    System.out.printf("Replenishment approved. New stock level for %s: %d%n", medicationName, newStock);
                }
                bw.write(String.join(",", med.getMedName(), med.getStock(), med.getLowStockThreshold(), med.getRequest()));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
