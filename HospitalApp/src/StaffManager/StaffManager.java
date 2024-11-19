import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class StaffManager {

    String docFile = "HospitalApp\\src\\Database\\CSV\\DB\\DR_DB.csv";
    String staffFile = "HospitalApp\\src\\Database\\CSV\\DB\\STAFF_DB.csv";
    String loginFile = "HospitalApp\\src\\Database\\CSV\\DB\\LOGIN_DB.csv";

    Scanner sc = new Scanner(System.in);

    public void displayStaff() {
        // Load all staff data from the three different files
        List<String[]> allStaffList = loadAllStaffData();
        
        System.out.println("\n=======FILTER OPTIONS=======");
        System.out.println("1. Filter by role");
        System.out.println("2. Filter by gender");
        System.out.println("3. Filter by age");
        System.out.println("============================");
        System.out.print("Select a filter option: ");
        int filterOption = sc.nextInt();
        sc.nextLine(); // Consume the newline character
    
        switch (filterOption) {
            case 1:
                // Filter by role
                System.out.print("Enter role to filter (Doctor/Pharmacist/Administrator): ");
                String role = sc.nextLine();
                displayFilteredStaff(allStaffList, "role", role);
                break;
            case 2:
                // Filter by gender
                System.out.print("Enter gender to filter (Male/Female): ");
                String gender = sc.nextLine();
                displayFilteredStaff(allStaffList, "gender", gender);
                break;
            case 3:
                // Filter by age
                System.out.print("Enter minimum age to filter: ");
                int minAge = sc.nextInt();
                System.out.print("Enter maximum age to filter: ");
                int maxAge = sc.nextInt();
                sc.nextLine(); // Consume the newline character
                displayFilteredStaff(allStaffList, "age", minAge, maxAge);
                break;
            default:
                System.out.println("Invalid option selected.");
        }
    }
    
    // Helper method to load all staff data from the CSV files for Doctors, Pharmacists, and Administrators
    private List<String[]> loadAllStaffData() {
        List<String[]> allStaffList = new ArrayList<>();
        allStaffList.addAll(loadStaffData(docFile));
        allStaffList.addAll(loadStaffData(staffFile));
        return allStaffList;
    }

    // Method to display filtered staff based on role or gender
    private void displayFilteredStaff(List<String[]> staffList, String filterType, String filterValue) {
        System.out.println("Filtered Staff:");
        System.out.printf("%-15s %-25s %-20s %-10s %-5s%n", "Staff ID", "Name", "Role", "Gender", "Age");
        System.out.println("---------------------------------------------------------");
        for (String[] staff : staffList) {
            String role = staff[2];
            String gender = staff[3];
            if (filterType.equals("role") && role.equalsIgnoreCase(filterValue)) {
                System.out.printf("%-15s %-25s %-20s %-10s %-5s%n", staff[0], staff[1], role, gender, staff[4]);
            } else if (filterType.equals("gender") && gender.equalsIgnoreCase(filterValue)) {
                System.out.printf("%-15s %-25s %-20s %-10s %-5s%n", staff[0], staff[1], role, gender, staff[4]);
            }
        }
    }

    // Method to display filtered staff based on age
    private void displayFilteredStaff(List<String[]> staffList, String filterType, int minAge, int maxAge) {
        System.out.println("Filtered Staff by Age:");
        System.out.printf("%-15s %-25s %-20s %-10s %-5s%n", "Staff ID", "Name", "Role", "Gender", "Age");
        System.out.println("---------------------------------------------------------");
        for (String[] staff : staffList) {
            int age = Integer.parseInt(staff[4]);
            if (filterType.equals("age") && age >= minAge && age <= maxAge) {
                System.out.printf("%-15s %-25s %-20s %-10s %-5s%n", staff[0], staff[1], staff[2], staff[3], age);
            }
        }
    }

    public void addStaff(){ //add a new staff by entering staff id, name, position, gender, age

        String filePath = "";
        String requiredPrefix = "";
        System.out.print("Enter Role (Doctor/Pharmacist/Administrator): ");
        String role = sc.nextLine();

        while(true){
            if(role.equalsIgnoreCase("DOCTOR")){
                filePath = "HospitalApp\\src\\Database\\CSV\\DB\\DR_DB.csv";
                requiredPrefix = "D";
                break;
            }
            else if(role.equalsIgnoreCase("PHARMACIST") ){
                filePath = "HospitalApp\\src\\Database\\CSV\\DB\\STAFF_DB.csv";
                requiredPrefix = "PH";
                break;
            }
            else if(role.equalsIgnoreCase("ADMINISTRATOR")){
                filePath = "HospitalApp\\src\\Database\\CSV\\DB\\STAFF_DB.csv";
                requiredPrefix = "A";
                break;
            }
            else{
                System.out.println("Invalid role. Please try again");
                System.out.print("Enter Role (Doctor/Pharmacist/Administrator): ");
                role = sc.nextLine();
            }
        }

        String staffID;
        while (true) {
            System.out.println("Enter Staff ID: ");
            staffID = sc.nextLine();

            // Check if staffID starts with the required prefix
            if (staffID.toUpperCase().startsWith(requiredPrefix)) {
                while(staffIDExists(staffID.toUpperCase(), filePath)) {
                    System.out.println("Staff ID already exists. Please enter a unique ID.");
                    System.out.println("Enter Staff ID: ");
                    staffID = sc.nextLine();
                    if(!staffIDExists(staffID,filePath)){
                        break;
                    }
                }
                break; // Exit the loop if the ID is valid
            } else {
                System.out.println("Invalid Staff ID. For " + role + ", the ID should start with '" + requiredPrefix + "'. Please try again.");
            }
        }

        System.out.println("Enter Name: ");
        String staffName = sc.nextLine();
    
        System.out.println("Enter Gender: ");
        String gender = sc.nextLine();
        System.out.println("Enter Age: ");
        int age = sc.nextInt();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            
            bw.write(String.format("%s,%s,%s,%s,%d%n", staffID, staffName, role, gender, age));
            System.out.println("Staff member added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(loginFile, true))) {
            
            bw.write(String.format("%s,%s,%s,%s,%s%n", role.toUpperCase(), staffID, "password", "NULL", staffName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to check if a Staff ID already exists in the file
    private boolean staffIDExists(String staffID, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(staffID)) { // Check if first column matches the staff ID
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateStaff() {
        List<String[]> staffList = loadAllStaffData();
        System.out.println("======================CURRENT STAFF MEMBERS======================");
        System.out.printf("%-5s %-10s %-15s %-15s %-10s %-5s%n", "No.", "STAFF ID", "NAME", "ROLE", "GENDER", "AGE");

        // Display the list of current staff members
        for (int i = 0; i < staffList.size(); i++) {
            String[] staff = staffList.get(i);
            System.out.printf("%-5s %-10s %-15s %-15s %-10s %-5s%n", i + 1, staff[0], staff[1], staff[2], staff[3], staff[4]);
        }
        System.out.println("=================================================================");
        
        int choice = -1;
        boolean validChoice = false;
        
        // Handle invalid input for selecting a staff member
        while (!validChoice) {
            try {
                System.out.print("Select a staff member to update: ");
                choice = sc.nextInt();
                sc.nextLine(); // Consume the newline
                
                if (choice < 1 || choice > staffList.size()) {
                    System.out.println("Invalid choice. Please select a valid staff member.");
                } else {
                    validChoice = true; // Exit loop if valid choice is entered
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Clear the buffer
            }
        }

        String[] selectedStaff = staffList.get(choice - 1);
        
        // Handle updating name (optional)
        System.out.print("Enter new name (or press 'Enter' to keep " + selectedStaff[1] + "): ");
        String newName = sc.nextLine();
        if (!newName.isEmpty()) selectedStaff[1] = newName;

        // Handle gender input with validation (for Male, Female, Other)
        String newGender = selectedStaff[3];  // Default to current gender
        boolean validGender = false;
        while (!validGender) {
            try {
                System.out.print("Enter new gender (Male, Female, Other) (or press 'Enter' to keep " + selectedStaff[3] + "): ");
                newGender = sc.nextLine().trim();
                
                // If no input, keep the current gender
                if (newGender.isEmpty()) {
                    newGender = selectedStaff[3];
                    validGender = true; // Exit loop
                }
                // Validate gender input
                else if (newGender.equalsIgnoreCase("Male") || newGender.equalsIgnoreCase("Female") || newGender.equalsIgnoreCase("Other")) {
                    selectedStaff[3] = newGender; // Update gender
                    validGender = true; // Exit loop
                } else {
                    System.out.println("Invalid gender. Please enter 'Male', 'Female', or 'Other'.");
                }
            } catch (Exception e) {
                System.out.println("Error processing gender input. Please try again.");
            }
        }

        // Handle updating age (ensure valid integer input)
        int newAge = -1;
        boolean validAge = false;
        while (!validAge) {
            try {
                System.out.print("Enter new age (or '-1' to keep " + selectedStaff[4] + "): ");
                newAge = sc.nextInt();
                sc.nextLine(); // Consume the newline

                if (newAge == -1 || newAge > 0) {
                    selectedStaff[4] = (newAge == -1) ? selectedStaff[4] : String.valueOf(newAge); // Keep or update age
                    validAge = true; // Exit loop
                } else {
                    System.out.println("Please enter a valid age (positive integer or '-1').");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid age.");
                sc.nextLine(); // Clear the buffer
            }
        }

        // After updating the staff, save the changes to the appropriate file
        saveStaffData(staffList);  // Save all staff data, filtered by role

        // Update the authentication file based on the staff ID, name, and role
        updateAuthenticationFile(selectedStaff[0], selectedStaff[1], selectedStaff[2]);

        System.out.println("Staff member updated successfully.");
    }

    public void removeStaff() {
        List<String[]> staffList = loadAllStaffData();
        System.out.println("======================CURRENT STAFF MEMBERS======================");

        System.out.printf("%-5s %-10s %-15s %-15s %-10s %-5s%n", "No.", "STAFF ID", "NAME", "ROLE", "GENDER", "AGE");

        // Display the list of current staff members
        for (int i = 0; i < staffList.size(); i++) {
            String[] staff = staffList.get(i);
            System.out.printf("%-5s %-10s %-15s %-15s %-10s %-5s%n", i + 1, staff[0], staff[1], staff[2], staff[3], staff[4]);
        }
        System.out.println("=================================================================");
        
        int choice = -1;
        boolean validChoice = false;

        // Handle invalid input for selecting a staff member
        while (!validChoice) {
            try {
                System.out.print("Select a staff member to remove: ");
                choice = sc.nextInt();
                sc.nextLine(); // Consume the newline
                
                if (choice < 1 || choice > staffList.size()) {
                    System.out.println("Invalid choice. Please select a valid staff member.");
                } else {
                    validChoice = true; // Exit loop if valid choice is entered
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Clear the buffer
            }
        }

        // Get the staff ID (first column) of the selected staff member to remove from login_db
        String[] selectedStaff = staffList.get(choice - 1);
        String staffID = selectedStaff[0];  // Assuming staff ID is the first column in the staff CSV

        // Remove the selected staff member from the staff list
        staffList.remove(choice - 1);

        // Save the updated staff data into the correct file based on role
        saveStaffData(staffList);

        // Now update the login_db.csv to remove the corresponding authentication entry
        removeFromLoginDb(staffID);

        System.out.println("Staff member removed successfully.");
    }

    private void removeFromLoginDb(String staffID) {
        List<String[]> loginDb = loadStaffData(loginFile);  // Load the login_db data
        List<String[]> updatedLoginDb = new ArrayList<>();

        boolean staffRemoved = false;  // Flag to check if the staff ID was found and removed

        // Iterate through the loginDb to find and remove the matching staff ID
        for (String[] entry : loginDb) {
            String currentStaffId = entry[1].trim();  // Assuming staff ID is the 2nd field
            if (!currentStaffId.equals(staffID.trim())) {
                updatedLoginDb.add(entry);  // Add to the updated list if it's not the staff to be removed
            } else {
                staffRemoved = true;  // Set the flag when the staff ID is found
            }
        }

        // If the staff ID was found and removed, update the login_db.csv file
        if (staffRemoved) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(loginFile))) {
                bw.write("ROLE,ID,PW,SALT,NAME");  // Header
                bw.newLine();
                for (String[] entry : updatedLoginDb) {
                    bw.write(String.join(",", entry));
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No matching staff member found in login_db.");
        }
    }

    private List<String[]> loadStaffData(String filePath) {
        List<String[]> staffList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header line
            String line;
            while ((line = br.readLine()) != null) {
                staffList.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    private void saveStaffData(List<String[]> staffList) {
        // Separate staff based on role
        List<String[]> doctors = new ArrayList<>();
        List<String[]> pharmacistsAndAdmins = new ArrayList<>();
        
        // Filter staff based on role
        for (String[] staff : staffList) {
            String role = staff[2].toLowerCase();
            if (role.equals("doctor")) {
                doctors.add(staff);
            } else if (role.equals("pharmacist") || role.equals("administrator")) {
                pharmacistsAndAdmins.add(staff);
            }
        }
        
        // Save doctors to DR_DB.csv
        saveStaffDataToFile(doctors, docFile);
        
        // Save pharmacists and administrators to STAFF_DB.csv
        saveStaffDataToFile(pharmacistsAndAdmins, staffFile);
    }
    
    private void saveStaffDataToFile(List<String[]> staffList, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("STAFF_ID,NAME,ROLE,GENDER,AGE"); // Header
            bw.newLine();
            for (String[] staff : staffList) {
                bw.write(String.join(",", staff));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to update the authentication file (with staff roles, ID, password, salt, and name)
    private void updateAuthenticationFile(String staffID, String newName, String newRole) {
        List<String[]> authList = loadStaffData(loginFile);

        // Find the staff member in the authentication file based on their ID
        boolean staffFound = false;
        for (String[] auth : authList) {
            if (auth[1].equals(staffID)) {
                // Update the name and role
                auth[4] = newName; // Update the name
                auth[0] = newRole.toUpperCase(); // Update the role
                staffFound = true;
                break;
            }
        }

        // If the staff member was found and updated, save the file
        if (staffFound) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(loginFile))) {
                bw.write("ROLE,ID,PW,SALT,NAME"); // Header
                bw.newLine();
                for (String[] auth : authList) {
                    bw.write(String.join(",", auth));
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Staff member not found in authentication file.");
        }
    }
}
