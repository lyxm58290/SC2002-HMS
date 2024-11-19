
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;


/**
 * The DoctorSchedule class manages the schedule and availability of doctors.
 * It allows viewing and updating a doctor's availability, checking available time slots,
 * and managing the availability file for the next 7 days.
 */

public class DoctorSchedule {

    private static final String AVAILABILITY_FILE_PATH = "HospitalApp\\src\\Database\\CSV\\DOCTORS_AVAILABILITY.csv";
    static Scanner sc = new Scanner(System.in);

    /**
     * Displays the upcoming 7-day schedule for a doctor, showing the available time slots.
     * @param doctorID The ID of the doctor whose schedule is to be viewed.
     */
    public static void viewDoctorSchedule(String doctorID) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
        System.out.println("=============================DOCTOR'S AVAILABILITY==============================");
        System.out.printf("%-15s %-15s %-25s%n", "DATE", "DAY", "AVAILABLE TIME SLOTS");
        System.out.println("================================================================================");
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            String formattedDate = date.format(dateFormatter);
            String day = dayOfWeek.toString();

            String availableTimeSlots = displayAvailableTimeSlots(date, doctorID);
            System.out.printf("%-15s %-15s %-25s%n", formattedDate, day, availableTimeSlots);
        }
        
        System.out.println("================================================================================");
    }

    /**
     * Helper method to display available time slots for a specific date and doctor.
     * @param date The date for which available time slots are to be displayed.
     * @param doctorID The ID of the doctor whose time slots are to be shown.
     * @return A string representation of the available time slots for the specified date and doctor.
     */   
    public static String displayAvailableTimeSlots(LocalDate date, String doctorID) {
        StringBuilder availableSlotsString = new StringBuilder();
        
        try (BufferedReader br = new BufferedReader(new FileReader(AVAILABILITY_FILE_PATH))) {
            String line;
            boolean foundDate = false;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(doctorID) && values[1].equals(date.toString())) {
                    foundDate = true;
                    List<String> availableSlots = new ArrayList<>();
                    boolean isAvailable = false;
                    int startHour = 8;
                    int endHour = 18;

                    for (int i = 2; i < values.length; i++) {
                        if (values[i].equals("Y")) {
                            if (!isAvailable) {
                                startHour = 8 + (i - 2);
                                isAvailable = true;
                            }
                        } else {
                            if (isAvailable) {
                                int endHourSlot = 8 + (i - 2);
                                availableSlots.add(formatTimeSlot(startHour, endHourSlot));
                                isAvailable = false;
                            }
                        }
                    }

                    if (isAvailable) {
                        availableSlots.add(formatTimeSlot(startHour, endHour));
                    }
                    if (availableSlots.isEmpty()) {
                        availableSlotsString.append("No available time slots.");
                    } else {
                        availableSlotsString.append(String.join(", ", availableSlots));
                    }
                    break;
                }
            }

            if (!foundDate) {
                availableSlotsString.append("No availability found for this date.");
            }
        } catch (IOException e) {
            System.err.println("Error reading availability file: " + e.getMessage());
            availableSlotsString.append("Error loading availability.");
        }

        return availableSlotsString.toString();
    }
    
    /**
     * Formats the start and end hour into a readable time slot.
     * @param startHour The start hour of the time slot.
     * @param endHour The end hour of the time slot.
     * @return A string representing the time range.
     */
    private static String formatTimeSlot(int startHour, int endHour) {
        String start = formatHour(startHour);
        String end = formatHour(endHour);
        return start + " - " + end;
    }

    /**
     * Converts an hour into a formatted string with AM/PM notation.
     * @param hour The hour to be converted.
     * @return A formatted time string.
     */
    private static String formatHour(int hour) {
        if (hour == 12) {
            return "12:00 PM";
        } else if (hour > 12) {
            return (hour - 12) + ":00 PM";
        } else {
            return hour + ":00 AM";
        }
    }

    /**
     * Updates the doctor's schedule for a specific date and time slot.
     * @param dID The ID of the doctor whose schedule is to be updated.
     * @param date The date of the appointment.
     * @param time The time of the appointment.
     * @param avail The availability status ("Y" for available, "N" for unavailable).
     */
    public static void updateDocSchedule(String dID,String date, String time, String avail){
        List<String[]> updatedRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(AVAILABILITY_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(dID) && details[1].equals(date)) {
                    int newTimeColumn = AppointmentManager.getTimeIndex(AppointmentManager.getTimeAlpha(time));
                    if (newTimeColumn != -1) {
                        details[newTimeColumn] = avail;
                    } else if (newTimeColumn == -1) {
                        System.out.println("The specified time slot could not be found for the new date.");
                    }
                }
                updatedRecords.add(details);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(AVAILABILITY_FILE_PATH))) {
            for (String[] record : updatedRecords) {
                bw.write(String.join(",", record));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the doctor is available for a specific date and time slot.
     * Prompts the user to enter a valid date and time if unavailable.
     * @param dID The ID of the doctor.
     * @param date The date to check availability.
     * @param time The time to check availability.
     * @return True if the doctor is available, false otherwise.
     */
    public static boolean checkDocAvail(String dID, String date, String time){
        while (true) {
            if (docAvailDates(AVAILABILITY_FILE_PATH,dID, date)) {
                System.out.println("The date " + date + " is available for scheduling.");
                break;
            } else {
                System.out.println("The date " + date + " is unavailable. Please enter a different date.");
                System.out.println("Enter your date: (YYYY-MM-DD)");
                date = sc.nextLine();
            }
        }
        
        while (true) {
            if (docAvailTimes(AVAILABILITY_FILE_PATH,dID, date, time)) {
                System.out.println("You have selected the timing : " + time);
                return true;
                 // Exit the loop once a valid ID is chosen
            } else {
                System.out.println("Timing not avail. Please try again.");
                System.out.println("Enter your time: (HHMM, eg. 0800)");
                time = sc.nextLine();
            }
        }
    }

    /**
     * Checks if the doctor has availability for a specific date in the CSV file.
     * @param filePath The path to the CSV file.
     * @param drID The ID of the doctor.
     * @param targetDate The date to check availability.
     * @return True if the doctor has availability for the specified date.
     */
    public static boolean docAvailDates(String filePath, String drID, String targetDate) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(drID) && details[1].equals(targetDate)) {
                        return true;
                }
            }
        }catch (IOException e) {
                e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if a doctor has availability for a specific time slot on a given date.
     * @param filePath The path to the CSV file.
     * @param drID The ID of the doctor.
     * @param targetDate The date to check.
     * @param targetTime The time slot to check.
     * @return True if the doctor is available for the specified time slot.
     */
    public static boolean docAvailTimes(String filePath, String drID, String targetDate, String targetTime) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(drID) && details[1].equals(targetDate)) {
                    int timeIndex = getTimeIndex(getTimeAlpha(targetTime));
                    if (timeIndex != -1 && timeIndex < details.length && details[timeIndex].equals("Y")) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Initializes the availability schedule for a doctor for the next 7 days, if not already initialized.
     * Adds the doctor's availability for each day (from 8 AM to 5 PM) in a CSV file.
     * @param doctorID The ID of the doctor whose availability is to be initialized.
     */
    public static void initializeAvailability(String doctorID) {
        if (checkAvailabilityExists(doctorID)) {
            return;
        }
        LocalDate today = LocalDate.now();
        List<String> updatedContent = new ArrayList<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader(AVAILABILITY_FILE_PATH))) {
            String line = br.readLine();
            if (line != null) {
                updatedContent.add(line);
            }
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String recordDoctorID = values[0];
                LocalDate recordDate = LocalDate.parse(values[1]);
    
                if (!recordDoctorID.equals(doctorID) || recordDate.isBefore(today) || recordDate.isAfter(today.plusDays(6))) {
                    updatedContent.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading availability file: " + e.getMessage());
        }
    
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            StringBuilder availabilityLine = new StringBuilder(doctorID + "," + date + ",");
            for (int hour = 8; hour <= 17; hour++) { 
                availabilityLine.append("Y,");
            }
            updatedContent.add(availabilityLine.substring(0, availabilityLine.length() - 1)); // Remove trailing comma
        }
    
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(AVAILABILITY_FILE_PATH))) {
            for (String line : updatedContent) {
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Availability initialized for the next 7 days.");
        } catch (IOException e) {
            System.err.println("Error writing to availability file: " + e.getMessage());
        }
    }

    /**
     * Checks if availability data exists for a specific doctor for the next 7 days.
     * @param doctorID The ID of the doctor whose availability is being checked.
     * @return True if the doctor has availability data for the next 7 days; otherwise, false.
     */
    private static boolean checkAvailabilityExists(String doctorID) {
        LocalDate today = LocalDate.now();
        Set<String> existingDates = new HashSet<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader(AVAILABILITY_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(doctorID)) {
                    existingDates.add(values[1]); 
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading availability file: " + e.getMessage());
        }
    
        for (int i = 0; i < 7; i++) {
            LocalDate dateToCheck = today.plusDays(i);
            if (!existingDates.contains(dateToCheck.toString())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates the availability of a doctor for a specific date and time slot.
     * Toggles the availability between "Y" (available) and "N" (not available) for the selected time slot.
     * @param date The date for which the availability is to be updated.
     * @param timeSlot The time slot to update (8 AM - 5 PM).
     * @param doctorID The ID of the doctor whose availability is being updated.
     */
    public static void updateAvailability(LocalDate date, int timeSlot, String doctorID) {
        StringBuilder updatedContent = new StringBuilder();
        boolean found = false;
    
        try (BufferedReader br = new BufferedReader(new FileReader(AVAILABILITY_FILE_PATH))) {
            String line;
    
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(doctorID) && values[1].equals(date.toString())) {
                    found = true;
                    int index = timeSlot + 1; 
                    values[index] = values[index].equals("Y") ? "N" : "Y";
                }
                updatedContent.append(String.join(",", values)).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading availability file: " + e.getMessage());
        }
    
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(AVAILABILITY_FILE_PATH))) {
            bw.write(updatedContent.toString());
            System.out.println("Availability updated successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to availability file: " + e.getMessage());
        }
    
        if (!found) {
            System.out.println("No existing availability found for the selected date.");
        }
    }

    /**
     * Converts a time string (e.g., "EIGHT", "NINE") to its corresponding column index in the availability CSV file.
     * @param time The time string to be converted (e.g., "EIGHT", "NINE").
     * @return The column index corresponding to the time, or -1 if the time is invalid.
     */
    public static int getTimeIndex(String time) {
        switch (time.toUpperCase()) {
            case "EIGHT": return 2;
            case "NINE": return 3;
            case "TEN": return 4;
            case "ELEVEN": return 5;
            case "TWELVE": return 6;
            case "ONE": return 7;
            case "TWO": return 8;
            case "THREE": return 9;
            case "FOUR": return 10;
            case "FIVE": return 11;
            default: return -1;
        }
    }

    /**
     * Converts a time string (e.g., "EIGHT", "NINE") to the corresponding time in 24-hour format (e.g., "0800", "0900").
     * @param time The time string to convert.
     * @return The corresponding 24-hour formatted time string, or null if the time is invalid.
     */
    public static String getTime(String time) {
        switch (time.toUpperCase()) {
            case "EIGHT": return "0800";
            case "NINE": return "0900";
            case "TEN": return "1000";
            case "ELEVEN": return "1100";
            case "TWELVE": return "1200";
            case "ONE": return "1300";
            case "TWO": return "1400";
            case "THREE": return "1500";
            case "FOUR": return "1600";
            case "FIVE": return "1700";
            default: return null; 
        }
    }

    /**
     * Converts a 24-hour formatted time string (e.g., "0800", "0900") to the corresponding time string (e.g., "EIGHT", "NINE").
     * @param time The 24-hour formatted time string to convert.
     * @return The corresponding time string (e.g., "EIGHT", "NINE"), or null if the time is invalid.
     */
    public static String getTimeAlpha(String time){
        switch (time.toUpperCase()) {
            case "0800": return "EIGHT";
            case "0900": return "NINE";
            case "1000": return "TEN";
            case "1100": return "ELEVEN";
            case "1200": return "TWELVE";
            case "1300": return "ONE";
            case "1400": return "TWO";
            case "1500": return "THREE";
            case "1600": return "FOUR";
            case "1700": return "FIVE";
            default: return null; 
        }
    } 
}