
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * The Pharmacist class represents a pharmacist in the hospital management application.
 * It extends the User class and includes attributes and methods specific to a pharmacist.
 * 
 * This class contains information about the pharmacist, such as their gender, age,
 * and a list of medications in their inventory. It also manages the loading of pharmacist
 * data from a CSV file.
 */

public class Pharmacist implements User{

    private String ID;
    private String name;
    private String gender;
    private String age;
    private ArrayList<Inventory> Inventory = new ArrayList<>();

    private String file = "HospitalApp\\src\\Database\\CSV\\DB\\STAFF_DB.csv";

    public String getGender(){
        return this.gender;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public String getAge(){
        return this.age;
    }
    public void setAge(String age){
        this.age = age;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getPharmacistID(){
        return this.ID;
    }
    public void setPharmacistID(String pharmacistID){
        this.ID = pharmacistID;
    }

    /**
     * Sets the list of inventory items managed by the pharmacist by filtering all inventory data.
     * 
     * This method loads all inventory items from the InventoryManager and adds to the list 
     * only those that are not already present to avoid duplicates.
     * 
     * @param inventory an ArrayList to store the pharmacist's inventory items
     */
    public void setInventory(ArrayList<Inventory> inventory){
		ArrayList<Inventory> allInventory = InventoryManager.loadInventory(new ArrayList<>());
	
		for (Inventory medication : allInventory) {
			if (!inventory.contains(medication)) {
				inventory.add(medication);
			}
		}
	}
    /**
     * Retrieves the list of inventory items managed by the pharmacist.
     * 
     * @return an ArrayList containing the pharmacist's inventory items
     */
    public ArrayList<Inventory> getInventory(){
        return Inventory;
    }
    
    /**
     * Constructs a Pharmacist object and loads the pharmacist's data from a CSV file 
     * based on the provided pharmacist ID
     * @param pharmacistID the unique identifier for the pharmacist
     */
    public Pharmacist(String pharmacistID){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if(values[0].equals(pharmacistID)){
					setPharmacistID(values[0]);
					setName(values[1]);
					setGender(values[3]);
					setAge(values[4]);
                    setInventory(this.Inventory);
				}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
