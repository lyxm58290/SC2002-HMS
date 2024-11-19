
/**
 * The User interface represents a general contract for user roles within the hospital management application.
 * It is intended to be implemented by specific user role classes (e.g., patients, doctors, pharmacists, and administrators).
 * 
 * Each implementing class is expected to define methods for retrieving the user's unique ID and name, as well as any role-specific functionality.
 */
public interface User {
    /**
     * The unique identifier for the user.
     */
    public String ID = "";

    /**
     * The name of the user.
     */
    public String name = "";
}