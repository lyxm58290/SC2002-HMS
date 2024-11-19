
/**
 * The UserMenu class serves as an abstract base class for user menus in the hospital management application.
 * It provides common functionalities for displaying menus, performing actions, clearing the console, and 
 * changing user passwords. This class is intended to be extended by role-specific menu classes.
 */

public abstract class UserMenu {

    /**
     * Displays the menu specific to the user role. 
     * This method should be overridden by subclasses to provide role-specific menu options for each user type.
     * For example, subclasses can provide options relevant to doctors, nurses, or patients.
     */
    public void displayMenu(){
        System.out.println("This ID has no assigned role.");
    };

    

    /**
     * Executes actions based on the user's input.
     * This method should be overridden by subclasses to handle actions relevant to each specific role.
     * 
     * @param id the unique identifier for the user
     */
    public void menuActions(String id){
        System.out.println("This ID has no assigned role.");
    };

    /**
     * Clears the console screen.
     */
    public void clearConsole() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }

    /**
     * Changes the password for the user identified by the given ID.
     * This method invokes the password-changing functionality from the ChangePassword class.
     *
     * @param ID the unique identifier for the user whose password will be changed
     */
    public void changePassword(String ID){
        ChangePassword.changePassword(ID); //Uses a relationship
    }
}
