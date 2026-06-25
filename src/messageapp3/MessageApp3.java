/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package messageapp3;
import com.mycompany.messageapp.Login;
import com.mycompany.messageapp.Message;
import java.util.Scanner;
import java.util.List;
/**
 *
 * @author lab_services_student
 */
//adding main methods
public class MessageApp3 {
private static final Scanner scanner = new Scanner(System.in);
    private static final Login userLogin = new Login();
    private static boolean isLoggedIn = false;

    public static void main(String[] args) {
        try (scanner) {
            // Load existing messages
            Message.loadMessagesFromJSON();
            
            // Registration
            registerUser();
            
            // Login
            loginUser();
            
            if (isLoggedIn) {
                
                
                // Main menu
                mainMenu();
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
    
    // ===== REGISTRATION =====
    private static void registerUser() {
        System.out.println("==================================================");
        System.out.println("   WELCOME TO THE REGISTRATION SYSTEM");
        System.out.println("==================================================");
        
        System.out.print("\nEnter First Name: ");
        userLogin.setFirstName(scanner.nextLine().trim());
        
        System.out.print("Enter Last Name: ");
        userLogin.setLastName(scanner.nextLine().trim());
        
        // Username validation loop
        boolean validUsername = false;
        do {
            System.out.print("Enter Username (must contain _ and be max 5 chars): ");
            String username = scanner.nextLine().trim();
            if (!userLogin.checkUserName(username)) {
                System.out.println("✗ Username is not correctly formatted.");
            } else {
                userLogin.setUsername(username);
                validUsername = true;
                System.out.println("✓ Username successfully captured.");
            }
        } while (!validUsername);
        
        // Password validation loop
        boolean validPassword = false;
        do {
            System.out.print("Enter Password (8+ chars, 1 capital, 1 number, 1 special): ");
            String password = scanner.nextLine();
            if (!userLogin.checkPasswordComplexity(password)) {
                System.out.println("✗ Password is not correctly formatted.");
            } else {
                userLogin.setPassword(password);
                validPassword = true;
                System.out.println("✓ Password successfully captured.");
            }
        } while (!validPassword);
        
        // Cell phone validation loop
        boolean validCell = false;
        do {
            System.out.print("Enter Cell Phone Number (e.g., +27831234567): ");
            String cell = scanner.nextLine().trim();
            if (!userLogin.checkCellPhoneNumber(cell)) {
                System.out.println("✗ Cell number must include +27 and be 9 digits.");
            } else {
                userLogin.setCellNumber(cell);
                validCell = true;
                System.out.println("✓ Cell number successfully captured.");
            }
        } while (!validCell);
        
        System.out.println("\n==================================================");
        System.out.println("   REGISTRATION RESULT");
        System.out.println("==================================================");
        System.out.println(userLogin.registerUser());
    }
    
    // ===== LOGIN =====
    private static void loginUser() {
        System.out.println("\n==================================================");
        System.out.println("   WELCOME TO THE LOGIN SYSTEM");
        System.out.println("==================================================");
        
        int loginAttempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        do {
            System.out.print("Enter Username: ");
            String loginUsername = scanner.nextLine().trim();
            System.out.print("Enter Password: ");
            String loginPassword = scanner.nextLine();
            
            System.out.println(userLogin.returnLoginStatus(loginUsername, loginPassword));
            isLoggedIn = userLogin.loginUser(loginUsername, loginPassword);
            loginAttempts++;
            
            if (!isLoggedIn && loginAttempts >= MAX_ATTEMPTS) {
                System.out.println("✗ Maximum login attempts reached. Exiting...");
                System.exit(0);
            }
        } while (!isLoggedIn);
    }
    
    // ===== MAIN MENU =====
    private static void mainMenu() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n==================================================");
            System.out.println("   QUICKCHAT - MAIN MENU");
            System.out.println("==================================================");
            System.out.println("1. Send Messages");
            System.out.println("2. View Recently Sent Messages");
            System.out.println("3. Display Report");
            System.out.println("4. Stored Messages Management (Part 3)");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");
            
            int choice = getMenuChoice();
            
            switch (choice) {
                case 1 -> sendMessages();
                case 2 -> viewRecentlySent();
                case 3 -> Message.displayFullReport();
                case 4 -> storedMessagesMenu();
                case 5 -> {
                    System.out.println("Exiting application...");
                    Message.storeMessagesToJSON();
                    running = false;
                }
                default -> System.out.println("✗ Invalid option.");
            }
        }
    }
    
    private static int getMenuChoice() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.print("Please enter a number: ");
                    continue;
                }
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 5) return choice;
                System.out.print("Enter 1-5: ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }
    
    // ===== SEND MESSAGES =====
    private static void sendMessages() {
        System.out.println("\n=== SEND MESSAGES ===");
        System.out.print("How many messages do you want to enter? ");
        int numMessages = getPositiveInteger();
        
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");
            System.out.print("Enter Recipient (e.g., +27718693002): ");
            String recipient = scanner.nextLine().trim();
            
            if (recipient.isEmpty()) {
                System.out.println("✗ Recipient cannot be empty.");
                i--;
                continue;
            }
            
            System.out.print("Enter Message (max 250 chars): ");
            String content = scanner.nextLine().trim();
            
            if (content.isEmpty()) {
                System.out.println("✗ Message cannot be empty.");
                i--;
                continue;
            }
            
            if (content.length() > 250) {
                System.out.println("✗ Message exceeds 250 characters.");
                i--;
                continue;
            }
            
            // Choose action
            int action = getActionChoice();
            
            // Create and process message
            String flag = action == 1 ? "Sent" : (action == 2 ? "Disregard" : "Stored");
            Message msg = new Message(recipient, content, flag);
            System.out.println(msg.processMessage(action));
        }
        
        System.out.println("\n✓ Total sent: " + Message.getSentMessageCount());
        System.out.println("✓ Total stored: " + Message.getStoredMessageCount());
        Message.storeMessagesToJSON();
        System.out.println("✓ Messages saved.");
    }
    
    private static int getPositiveInteger() {
        while (true) {
            try {
                int number = Integer.parseInt(scanner.nextLine().trim());
                if (number > 0) return number;
                System.out.print("Enter a positive number: ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }
    
    private static int getActionChoice() {
        while (true) {
            try {
                System.out.println("\nChoose action:");
                System.out.println("  1) Send");
                System.out.println("  2) Discard");
                System.out.println("  3) Store");
                System.out.print("Enter choice (1-3): ");
                
                int action = Integer.parseInt(scanner.nextLine().trim());
                if (action >= 1 && action <= 3) return action;
                System.out.println("Please enter 1, 2, or 3.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number.");
            }
        }
    }
    
    // ===== VIEW RECENTLY SENT =====
    private static void viewRecentlySent() {
        System.out.println("\n=== RECENTLY SENT MESSAGES ===");
        List<String> sentMessages = Message.getSentMessages();
        if (sentMessages.isEmpty()) {
            System.out.println("No messages have been sent yet.");
        } else {
            System.out.println("Total sent: " + sentMessages.size());
            System.out.println("----------------------------------------");
            for (int i = 0; i < sentMessages.size(); i++) {
                System.out.println((i + 1) + ". " + sentMessages.get(i));
            }
        }
    }
    
    // ===== STORED MESSAGES MENU (PART 3) =====
    private static void storedMessagesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== STORED MESSAGES MANAGEMENT ===");
            System.out.println("a. Display sender and recipient of all stored messages");
            System.out.println("b. Display the longest stored message");
            System.out.println("c. Search for a message ID");
            System.out.println("d. Search for messages by recipient");
            System.out.println("e. Delete a message using message hash");
            System.out.println("f. Display full report");
            System.out.println("g. Back to Main Menu");
            System.out.print("Enter your choice (a-g): ");
            
            String choice = scanner.nextLine().trim().toLowerCase();
            
            switch (choice) {
                case "a" -> Message.displayStoredMessages();
                case "b" -> Message.displayLongestMessage();
                case "c" -> {
                    System.out.print("Enter Message ID: ");
                    String id = scanner.nextLine().trim();
                    if (!id.isEmpty()) Message.searchByMessageID(id);
                }
                case "d" -> {
                    System.out.print("Enter Recipient: ");
                    String recipient = scanner.nextLine().trim();
                    if (!recipient.isEmpty()) Message.searchByRecipient(recipient);
                }
                case "e" -> {
                    System.out.print("Enter Message Hash: ");
                    String hash = scanner.nextLine().trim();
                    if (!hash.isEmpty()) Message.deleteByHash(hash);
                }
                case "f" -> Message.displayFullReport();
                case "g" -> back = true;
                default -> System.out.println("✗ Invalid option. Enter a-g.");
            }
        }
    }
}
  
    


    

