import java.util.*;
import java.io.*;

/**
 * The HospitalApp class serves as the entry point for the hospital management application.
 * It manages user authentication and presents a menu for users to interact with the system.
 * The application supports multiple user roles, including patients, doctors, pharmacists, and administrators.
 * 
 * This class utilizes a {@link Scanner} for user input and a {@link Login} object for handling user login processes.
 * It also uses {@link StartUp#displayWelcomeMessage()} to display a welcome message with a blinking effect at the start.
 * 
 * Upon starting, the application displays a welcome message and enters a loop where it continuously prompts
 * the user to log in and select options from the menu until the application is terminated.
 * 
 * @see Login
 * @see StartUp
 */

public class HospitalApp {

    static Scanner sc = new Scanner(System.in);
    static Login login = new Login();

    /**
     * The main method is the entry point of the HospitalApp application.
     * It initializes the application by displaying a welcome message and then enters a loop
     * that allows users to log in and interact with the system based on their role.
     * 
     * @param args command-line arguments (not used in this application)
     * @throws FileNotFoundException if the specified file is not found during the login process
     * @throws InterruptedException if the thread is interrupted while waiting
     * @throws IOException if an input or output exception occurs
     * @see StartUp#displayWelcomeMessage()
     */
    public static void main(String[] args) throws FileNotFoundException, InterruptedException, IOException {
        StartUp.displayWelcomeMessage();

        while (true) {
            login.start();

            String id = login.makeChoice();
            if(id != null){
                login.printMenu(id);
            }
            else{
                break;
            }
        }
    }
}
