
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;



/**
 * The Login class manages the login process for users of the Hospital App.
 * 
 * This includes:
 * - Authenticating user credentials.
 * - Retrieving forgotten user IDs based on user-provided information.
 * - Delegating post-login functionality to relevant menu classes.
 * 
 * The class prioritizes secure password handling and user-friendly prompts,
 * ensuring accessibility and robustness.
 */

public class Login {

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    String loginFile = "HospitalApp\\src\\Database\\CSV\\DB\\LOGIN_DB.csv";

    Scanner sc = new Scanner(System.in);

     /**
     * Starts the login process by displaying the initial menu options.
     * Prompts the user for their choice to log in, retrieve a user ID, or exit.
     *
     * @throws FileNotFoundException if the login file is not found
     */
    public void start() throws FileNotFoundException{
        System.out.println("=====================================");
        System.out.println("Please enter your choice to continue.");
        System.out.println("\t1. Login");
        System.out.println("\t2. Forget UserID");
        System.out.println("\t3. Exit");
        System.out.print("Your choice (1-3): ");
    }

    /**
     * Handles the user's menu choice and directs them to the appropriate action.
     * Options include logging in, retrieving a user ID, or exiting the system.
     *
     * @return the user ID if login is successful, otherwise null
     */
    public String makeChoice(){
        String id = null;

        while(true){
            int choice = -1;
            try {
                choice = sc.nextInt(); 
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number (1-3).");
                sc.nextLine();
                System.out.print("Your choice (1-3): ");
                continue;
            }

            if(choice == 1){
                id = login();
                return id;
            }
            else if(choice == 2){
                if(!retrieveUserID(sc)){
                    return null;
                }
                System.out.println("Do you want to log in to your account? (Y/N)");
                String option = sc.nextLine();
                
                if(option.toLowerCase().equals("y")){
                    id = login();
                    return id;
                }
                else{
                    System.out.println("Exiting system. Thank you.");
                    return id;
                }
            }
            else if(choice == 3){
                System.out.println("Thank you for using the hospital app.");
                return id;
            }
            else{
                System.out.println("Invalid choice Please select 1-3.");
            }
        }
    }
    
    /**
     * Handles user login by verifying credentials against the stored data.
     *
     * @return the user ID if login is successful, otherwise null
     */
    public String login() {
        System.out.print("Enter your User ID: ");
        String userID = sc.nextLine().toUpperCase();
        System.out.print("Enter your password: ");
        String password = sc.nextLine();
        
        try (BufferedReader br = new BufferedReader(new FileReader(loginFile))) {
            String line;
            boolean userFound = false;
            String storedHash = "";
            String storedSalt = "";
            String userPassword = "";

            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[1].equals(userID)) {
                    userFound = true;
                    userPassword = details[2];
                    storedHash = details[2];
                    storedSalt = details[3];
                    break;
                }
            }

            if (!userFound) {
                System.out.println("User ID not found.");
                return null;
            }

            if(password.equals(userPassword)){
                System.out.println("Login Successful. Remember to change your password");
                return userID;
            }
            else if (verifyPassword(password, storedSalt, storedHash)) {
                System.out.println("Login successful!");
                return userID;
            } else {
                System.out.println("Invalid password. Access denied.");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Verifies the provided password against the stored hash using PBKDF2 with HMAC SHA-256.
     *
     * @param password   the password to be verified
     * @param salt       the salt used during the password hashing process
     * @param storedHash the stored hash to compare against
     * @return true if the password matches the stored hash, false otherwise
     * @throws NoSuchAlgorithmException if the specified algorithm is not available
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    public static boolean verifyPassword(String password, String salt, String storedHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        String newHash = Base64.getEncoder().encodeToString(hash);
        return newHash.equals(storedHash);
    }

    /**
     * Retrieves the user ID based on the provided full name.
     * Prompts the user to enter their full name and searches for a matching user ID in the login file.
     *
     * @param sc the Scanner object used to read user input
     * @return true if the user ID is found, otherwise false
     */
    public boolean retrieveUserID(Scanner sc) {
        System.out.print("Enter your full name: ");
        String fullName = sc.nextLine();
        
        boolean userFound = false;
    
        try (BufferedReader readUserID = new BufferedReader(new FileReader(loginFile))) {
            String line;
            while ((line = readUserID.readLine()) != null) {
                String[] details = line.split(",");
                String userId = details[1].trim();
                String name = details[4].trim();

                if (name.replace(" ", "").equalsIgnoreCase(fullName.trim()) || name.equalsIgnoreCase(fullName.trim())) {
                    System.out.println("UserID for " + name + ": " + userId);
                    userFound = true;
                    break;
                }
            }
            if (!userFound) {
                System.out.println("No UserID found for the name: " + fullName);
                return userFound;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userFound;
    }

    /**
     * Prints the menu for the application based on the provided user ID.
     * If the ID is valid, it delegates the display to the MenuManager; otherwise, it shows an error message.
     *
     * @param ID the user ID used to display the appropriate menu
     */
    public void printMenu(String ID){
        MenuManager menuManager = new MenuManager();
        if(ID != null){
            menuManager.displayMenu(ID);
        }
        else{
            System.out.println("Invalid ID");
        }
    }
}
