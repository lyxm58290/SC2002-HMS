

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The StartUp class is responsible for displaying the welcome message
 * to users when they launch the Hospital App. It enhances the user experience
 * by presenting a blinking effect and waiting for user interaction before proceeding.
 * 
 * This class includes utility methods for visual output and console management.
 */
public class StartUp {
    
    /**
     * Displays a welcome message to the user with a blinking effect.
     * The message alternates colors and waits for user input to proceed.
     * 
     * The method uses console manipulation to clear the screen and toggle
     * between two display styles at regular intervals. The user is prompted
     * to press a key and hit "Enter" to continue.
     *
     * @throws InterruptedException if the thread is interrupted during sleep
     * @throws IOException if an I/O error occurs while reading input
     */
    public static void displayWelcomeMessage() throws InterruptedException, IOException {
        final String BOLD = "\033[1m";
        final String GREEN = "\033[38;5;28m";
        final String RESET = "\033[0m";
        
        String message = "Welcome to the Hospital App";
        String divider = "===============================";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int colorToggle = 0;

        while (!reader.ready()) {
            clearConsole();
            if (colorToggle % 2 == 0) {
                System.out.println(BOLD + GREEN + divider + RESET);
                System.out.println(BOLD + GREEN + "| " + message + " |" + RESET);
                System.out.println(BOLD + GREEN + divider + RESET);
            } else {
                System.out.println(BOLD + divider + RESET);
                System.out.println(BOLD + "| " + message + " |" + RESET);
                System.out.println(BOLD + divider + RESET);
            }
            System.out.println("\nPress any key and Enter to continue.");
            Thread.sleep(500);
            colorToggle++;
        }
        
        reader.readLine();
        
        clearConsole();
        System.out.println(GREEN + "====== Hospital Management System ======" + RESET);
    }

    /**
     * Clears the console screen.
     */
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }
}