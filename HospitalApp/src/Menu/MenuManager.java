
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The MenuManager class is responsible for managing user menus based on their roles.
 * It reads user information from a login file and instantiates the appropriate menu for each user.
 */

public class MenuManager {

    static String loginFile = "HospitalApp\\src\\Database\\CSV\\DB\\LOGIN_DB.csv";

    /**
     * Displays the menu for the user identified by the given ID.
     *
     * @param id the unique identifier for the user
     */
    public void displayMenu(String id) {
        UserMenu userMenu;
        String role = findRoleByID(id);
        switch(role) {
                case "PATIENT":
                    userMenu = new PatientMenu();
                    break;
                case "DOCTOR":
                    userMenu = new DoctorMenu();
                    break;
                case "PHARMACIST":
                    userMenu = new PharmacistMenu();
                    break;
                case "ADMINISTRATOR":
                    userMenu = new AdminMenu();
                    break;
                default:
                    System.out.println("Invalid Role. ");
                    return;
            }
        userMenu.menuActions(id);
    }

    /**
     * Finds the role of a user based on their ID.
     *
     * @param userID the unique identifier for the user
     * @return the role of the user, or null if not found
     */
    public static String findRoleByID(String userID) {

        try (BufferedReader br = new BufferedReader(new FileReader(loginFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[1].equals(userID)) {
                    return details[0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
