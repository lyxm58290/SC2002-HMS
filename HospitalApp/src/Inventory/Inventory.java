
import java.util.*;

/**
 * The Inventory class represents the inventory system for medical supplies in a healthcare facility.
 * It contains information about the stock levels of various medications, including:
 * - Medication name
 * - Current stock
 * - Low stock threshold
 * - Request status for additional stock
 *
 * The class provides getters and setters to access and modify these properties.
 * It is used by pharmacists and administrators to manage and update inventory levels.
 * The inventory data is stored in a CSV file and can be updated based on stock levels, threshold conditions, and requests.
 */

public class Inventory {

    private String medName;
    private String stock;
    private String lowStockThreshold;
    private String request;

    Scanner sc = new Scanner(System.in);
    String invFile = "HospitalApp\\src\\Database\\CSV\\INVENTORY.csv";

    /**
     * Sets the name of the medication.
     * 
     * @param medName The name of the medication.
     */
    public void setMedName(String medName){
        this.medName = medName;
    }

    /**
     * Retrieves the name of the medication.
     * 
     * @return The name of the medication.
     */
    public String getMedName(){
        return medName;
    }
    /**
     * Sets the current stock of the medication.
     * 
     * @param stock The current stock level of the medication.
     */

    public void setStock(String stock){
        this.stock = stock;
    }

    /**
     * Retrieves the current stock of the medication.
     * 
     * @return The current stock level of the medication.
     */
    public String getStock(){
        return stock;
    }

    /**
     * Sets the low stock threshold for the medication.
     * This threshold is used to trigger stock replenishment when the stock falls below this value.
     * 
     * @param lowStockThreshold The low stock threshold for the medication.
     */
    public void setLowStockThreshold(String lowStockThreshold){
        this.lowStockThreshold = lowStockThreshold;
    }

    /**
     * Retrieves the low stock threshold for the medication.
     * 
     * @return The low stock threshold for the medication.
     */
    public String getLowStockThreshold(){
        return lowStockThreshold;
    }

    /**
     * Sets the request status for additional stock of the medication.
     * 
     * @param request The request status, indicating whether additional stock is needed.
     */    
    public void setRequest(String request){
        this.request = request;
    }

    /**
     * Retrieves the request status for additional stock of the medication.
     * 
     * @return The request status for the medication.
     */
    public String getRequest(){
        return request;
    }

    /**
     * Constructor for the Inventory class.
     * Initializes the medication name, stock level, low stock threshold, and request status.
     * 
     * @param medName The name of the medication.
     * @param stock The current stock level of the medication.
     * @param lowStockThreshold The low stock threshold for the medication.
     * @param request The request status for the medication.
     */
    public Inventory(String medName, String stock, String lowStockThreshold, String request){
        this.medName = medName;
        this.stock = stock;
        this.lowStockThreshold = lowStockThreshold;
        this.request = request;
    }
}
