
import java.io.*;
import java.util.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * The ChangePassword class provides functionality for changing a user's password
 * in the application, including password validation and hashing.
 */

public class ChangePassword {

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;
    static Scanner sc = new Scanner(System.in);

    /**
     * Default constructor for the ChangePassword class.
     */
    public ChangePassword(){}

    /**
     * Changes the password for a user identified by the given user ID.
     * It verifies the old password, validates the new password, and updates
     * the password in the database file.
     *
     * @param id the user ID for which the password needs to be changed
     */
    public static void changePassword(String id) {
        String LoginFile = "HospitalApp\\src\\Database\\CSV\\DB\\LOGIN_DB.csv";
        List<String> lines = new ArrayList<>();

        String oldHashedPassword = "";
        String oldSalt = "";
        boolean userFound = false;
        try (BufferedReader br = new BufferedReader(new FileReader(LoginFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
                String[] details = line.split(",");
                String userID = details[1];
                
                if (userID.equals(id)) {
                    oldHashedPassword = details[2];
                    oldSalt = details[3];
                    userFound = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!userFound) {
            System.out.println("UserID not found.");
            return;
        }

        System.out.print("Enter the old password: ");
        String oldPasswordInput = sc.nextLine();

        if (oldPasswordInput.equals("password")) {
            System.out.println("This is your first time changing the password.");
        } else {
            try {
                if (!verifyPassword(oldPasswordInput, oldSalt, oldHashedPassword)) {
                    System.out.println("Old password does not match. Access denied.");
                    return;
                }
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
                return;
            }
        }

        String newPassword = "";
        String confirmationPassword = "";

        while (true) {
            System.out.print("Enter new password: ");
            newPassword = sc.nextLine();

            if (!isValidPassword(newPassword)) {
                System.out.println("Password must be at least 8 characters long, contain letters, digits, and special characters.");
                continue;
            }

            System.out.print("Enter confirmation password: ");
            confirmationPassword = sc.nextLine();

            if (!newPassword.equals(confirmationPassword)) {
                System.out.println("Passwords do not match. Try again.");
                continue;
            }
            else{
                break;
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            String[] details = lines.get(i).split(",");
            String userID = details[1];

            if (userID.equals(id)) {
                try {
                    String newSalt = generateSalt();
                    String hashedPassword = hashPassword(newPassword, newSalt);

                    details[2] = hashedPassword;
                    details[3] = newSalt;

                    lines.set(i, String.join(",", details));
                    System.out.println("Password updated for userID: " + id);
                    break;
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LoginFile, false))) {
            for (String updatedLine : lines) {
                bw.write(updatedLine);
                bw.newLine();
            }
            System.out.println("File updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hashes a password using PBKDF2 with HMAC SHA-256.
     *
     * @param password the password to be hashed
     * @param salt the salt to be used in the hashing process
     * @return the Base64-encoded hashed password
     * @throws NoSuchAlgorithmException if the specified algorithm is not available
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Generates a random salt for use in password hashing.
     *
     * @return a Base64-encoded random salt
     */
    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new Random().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Verifies a password against a stored hash using the provided salt.
     *
     * @param password the password to verify
     * @param salt the salt used during the password hashing process
     * @param storedHash the stored hash to compare against
     * @return true if the password matches the stored hash, false otherwise
     * @throws NoSuchAlgorithmException if the specified algorithm is not available
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    public static boolean verifyPassword(String password, String salt, String storedHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String newHash = hashPassword(password, salt);
        return newHash.equals(storedHash);
    }

    /**
     * Validates the strength of a password based on specific criteria.
     * The password must be at least 8 characters long and contain letters,
     * digits, and special characters.
     *
     * @param password the password to validate
     * @return true if the password meets the strength criteria, false otherwise
     */
    public static boolean isValidPassword(String password) {
        String pattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(pattern);
    }
}
