/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.messageapp;
import java.util.*;
import java.io.*;
import java.text.ParseException;
import messageapp3.StoredMessage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;  

/**
 * @author lab_services_student
 */
//add message from part 2
public class Message {

    
    // --- INSTANCE VARIABLES ---
    private final String messageID;
    private final String recipientCell;
    private final String messageContent;
    private final String messageHash;
    private final String flag; // "Sent", "Stored", "Disregard"
    private static int messageCounter = 0;

    // --- STATIC COLLECTIONS (PART 3) ---
    private static final List<String> sentMessages = new ArrayList<>();
    private static final List<String> disregardedMessages = new ArrayList<>();
    private static final List<StoredMessage> storedMessages = new ArrayList<>();
    private static final List<String> messageHashes = new ArrayList<>();
    private static final List<String> messageIDs = new ArrayList<>();
    private static final List<Message> allMessages = new ArrayList<>();

    // --- CONSTRUCTORS ---
    public Message(String recipientCell, String messageContent, String flag) {
        this.recipientCell = recipientCell;
        this.messageContent = messageContent;
        this.flag = flag;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();

        // Add to collections
        messageIDs.add(this.messageID);
        messageHashes.add(this.messageHash);
        boolean add = allMessages.add(this);

        // Store based on flag
        if (flag.equalsIgnoreCase("Sent")) {
            sentMessages.add(messageContent);
        } else if (flag.equalsIgnoreCase("Disregard")) {
            disregardedMessages.add(messageContent);
        } else if (flag.equalsIgnoreCase("Stored")) {
            storedMessages.add(new StoredMessage(recipientCell, messageContent, messageID, messageHash));
        }
    }

    // --- GETTERS ---
    public String getMessageID() { return messageID; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessageContent() { return messageContent; }
    public String getMessageHash() { return messageHash; }
    public String getFlag() { return flag; }
    public static List<Message> getAllMessages() { return new ArrayList<>(allMessages); }

    // --- MESSAGE METHODS ---
    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    private String createMessageHash() {
        if (messageID == null || messageID.length() < 2) {
            return "00:0:invalid";
        }
        String idPart = this.messageID.substring(0, 2);
        String numPart = String.valueOf(++messageCounter);
        String[] words = this.messageContent.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        return idPart + ":" + numPart + ":" + firstWord + lastWord;
    }

    public String processMessage(int action) {
        String result = "";
        switch (action) {
            case 1 -> {
                sentMessages.add(this.messageContent);
                result = "✓ Message successfully sent.";
            }
            case 2 -> {
                disregardedMessages.add(this.messageContent);
                result = "✓ Message disregarded.";
            }
            case 3 -> {
                storedMessages.add(new StoredMessage(this.recipientCell, this.messageContent, this.messageID, this.messageHash));
                result = "✓ Message successfully stored.";
            }
            default -> result = "✗ Invalid action.";
        }
        return result + "\n  Message ID: " + this.messageID + "\n  Hash: " + this.messageHash;
    }

    // ============================================================
    // PART 3 - STORED MESSAGES MANAGEMENT METHODS
    // ============================================================

    // 2a. Display sender and recipient of all stored messages
    public static void displayStoredMessages() {
        System.out.println("\n=== STORED MESSAGES (Sender & Recipient) ===");
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages found.");
            return;
        }
        System.out.println("Total: " + storedMessages.size());
        System.out.println("----------------------------------------");
        for (StoredMessage sm : storedMessages) {
            System.out.println("Recipient: " + sm.getRecipient());
            System.out.println("Message: " + sm.getMessage());
            System.out.println("----------------------------------------");
        }
    }

    // 2b. Display the longest stored message
    public static void displayLongestMessage() {
        System.out.println("\n=== LONGEST STORED MESSAGE ===");
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages found.");
            return;
        }
        StoredMessage longest = storedMessages.get(0);
        for (StoredMessage sm : storedMessages) {
            if (sm.getMessage().length() > longest.getMessage().length()) {
                longest = sm;
            }
        }
        System.out.println("Recipient: " + longest.getRecipient());
        System.out.println("Message: " + longest.getMessage());
        System.out.println("Length: " + longest.getMessage().length() + " characters");
        System.out.println("Hash: " + longest.getMessageHash());
    }

    // 2c. Search for a message ID and display recipient and message
    public static boolean searchByMessageID(String messageID) {
        System.out.println("\n=== SEARCH BY MESSAGE ID ===");
        for (StoredMessage sm : storedMessages) {
            if (sm.getMessageID().equals(messageID)) {
                System.out.println("✓ Message found!");
                System.out.println("  Recipient: " + sm.getRecipient());
                System.out.println("  Message: " + sm.getMessage());
                return true;
            }
        }
        System.out.println("✗ Message ID '" + messageID + "' not found.");
        return false;
    }

    // 2d. Search for all messages for a particular recipient
    public static boolean searchByRecipient(String recipient) {
        System.out.println("\n=== SEARCH BY RECIPIENT ===");
        boolean found = false;
        for (StoredMessage sm : storedMessages) {
            if (sm.getRecipient().equals(recipient)) {
                System.out.println("  Message: " + sm.getMessage());
                System.out.println("  ID: " + sm.getMessageID());
                System.out.println("  ---");
                found = true;
            }
        }
        if (!found) {
            System.out.println("✗ No messages found for recipient: " + recipient);
        }
        return found;
    }

    // 2e. Delete a message using the message hash
    public static boolean deleteByHash(String hash) {
        System.out.println("\n=== DELETE BY MESSAGE HASH ===");
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).getMessageHash().equals(hash)) {
                String deleted = storedMessages.get(i).getMessage();
                storedMessages.remove(i);
                System.out.println("✓ Message: \"" + deleted + "\" successfully deleted.");
                storeMessagesToJSON();
                return true;
            }
        }
        System.out.println("✗ Message with hash '" + hash + "' not found.");
        return false;
    }

    // 2f. Display a report with full details of all stored messages
    public static void displayFullReport() {
        System.out.println("\n=== FULL MESSAGE REPORT ===");
        System.out.println("Generated on: " + new Date());
        System.out.println("========================================");
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages to display.");
            return;
        }
        System.out.printf("%-20s | %-15s | %-40s | %-15s%n", "Message Hash", "Recipient", "Message", "Message ID");
        System.out.println("----------------------------------------------------------------------------------------");
        for (StoredMessage sm : storedMessages) {
            String msg = sm.getMessage();
            if (msg.length() > 37) {
                msg = msg.substring(0, 34) + "...";
            }
            System.out.printf("%-20s | %-15s | %-40s | %-15s%n", 
                sm.getMessageHash(), sm.getRecipient(), msg, sm.getMessageID());
        }
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("Total: " + storedMessages.size() + " messages");
    }

    // ============================================================
    // JSON HANDLING
    // ============================================================

    @SuppressWarnings("unchecked")
    public static void storeMessagesToJSON() {
        JSONArray jsonMessages = new JSONArray();
        for (StoredMessage sm : storedMessages) {
            JSONObject obj = new JSONObject();
            obj.put("MessageID", sm.getMessageID());
            obj.put("Hash", sm.getMessageHash());
            obj.put("Recipient", sm.getRecipient());
            obj.put("Content", sm.getMessage());
            jsonMessages.add(obj);
        }
        try (FileWriter file = new FileWriter("messages.json")) {
            file.write(jsonMessages.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.err.println("Error saving messages: " + e.getMessage());
        }
    }

    public static void loadMessagesFromJSON() throws org.json.simple.parser.ParseException {
        File file = new File("messages.json");
        if (!file.exists()) return;

        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(file)) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            for (Object obj : jsonArray) {
                JSONObject jsonObj = (JSONObject) obj;
                String id = (String) jsonObj.get("MessageID");
                String hash = (String) jsonObj.get("Hash");
                String recipient = (String) jsonObj.get("Recipient");
                String content = (String) jsonObj.get("Content");

                StoredMessage sm = new StoredMessage(recipient, content, id, hash);
                storedMessages.add(sm);
                messageIDs.add(id);
                messageHashes.add(hash);
            }
            System.out.println("Loaded " + storedMessages.size() + " messages from JSON.");
        } catch (IOException e) {
            // File might be empty - that's fine
        }
    }
// ============================================================
// RESET (for testing)
// ============================================================
public static void resetAll() {
    sentMessages.clear();
    storedMessages.clear();
    disregardedMessages.clear();
    messageIDs.clear();
    messageHashes.clear();
    allMessages.clear();
    messageCounter = 0;
}
// ============================================================
// TEST DATA
// ============================================================
public static void populateTestData() {
    System.out.println("\n=== POPULATING TEST DATA ===");

    // Test Data Message 1 - Sent
    new Message("+27834557896", "Did you get the cake?", "Sent");

    // Test Data Message 2 - Stored
    new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");

    // Test Data Message 3 - Disregard
    new Message("+27834484567", "Yohoooo, I am at your gate.", "Disregard");

    // Test Data Message 4 - Sent
    new Message("0838884567", "It is dinner time !", "Sent");

    // Test Data Message 5 - Stored
    new Message("+27838884567", "Ok, I am leaving without you.", "Stored");

    System.out.println("✓ Test data populated successfully!");
    System.out.println("  - Sent: " + sentMessages.size());
    System.out.println("  - Stored: " + storedMessages.size());
    System.out.println("  - Disregarded: " + disregardedMessages.size());
}
    // ============================================================
    // STATIC GETTERS FOR UI
    // ============================================================

    public static List<String> getSentMessages() { return new ArrayList<>(sentMessages); }
    public static List<String> getDisregardedMessages() { return new ArrayList<>(disregardedMessages); }
    public static List<StoredMessage> getStoredMessages() { return new ArrayList<>(storedMessages); }
    public static int getSentMessageCount() { return sentMessages.size(); }
    public static int getStoredMessageCount() { return storedMessages.size(); }
}