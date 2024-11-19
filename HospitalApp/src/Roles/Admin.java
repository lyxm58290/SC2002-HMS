import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Represents an administrator in the hospital management application.
 * 
 * The `Admin` class is responsible for handling administrator-specific data such as:
 * - ID, name, gender, and age
 * - Managing and loading the medical inventory
 * 
 * Administrators can view and modify data loaded from the hospital's staff database (`STAFF_DB.csv`).
 * This class implements the `User` interface.
 */


public class Admin implements User{

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
    public String getAdminID(){
        return this.ID;
    }
    public void setAdminID(String adminID){
        this.ID = adminID;
    }

    /**
     * Populates the administrator's inventory with a complete list of medications.
     * 
     * Ensures that the administrator's inventory is updated with all available medications
     * from the database, avoiding duplicate entries.
     * 
     * @param inventory the list of inventory items to be updated
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
     * Retrieves the administrator's inventory of medications.
     *
     * @return the list of medications managed by the administrator
     */
    public ArrayList<Inventory> getInventory(){
        return Inventory;
    }
    
    /**
     * Retrieves the administrator's inventory of medications.
     *
     * @return the list of medications managed by the administrator
     */
    public Admin(String adminID){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if(values[0].equals(adminID)){
					setAdminID(values[0]);
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


