/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.messageapp3;

import com.mycompany.messageapp.Message;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;
import messageapp3.StoredMessage;

/**
 * @author lab_services_student
 */
public class MessageTest {
    
    @Before
    public void setUp() {
        // Reset all data before each test
        try {
            java.lang.reflect.Field sentField = Message.class.getDeclaredField("sentMessages");
            sentField.setAccessible(true);
            ((List<String>) sentField.get(null)).clear();
            
            java.lang.reflect.Field storedField = Message.class.getDeclaredField("storedMessages");
            storedField.setAccessible(true);
            ((List<StoredMessage>) storedField.get(null)).clear();
            
            java.lang.reflect.Field disregardedField = Message.class.getDeclaredField("disregardedMessages");
            disregardedField.setAccessible(true);
            ((List<String>) disregardedField.get(null)).clear();
            
            java.lang.reflect.Field idsField = Message.class.getDeclaredField("messageIDs");
            idsField.setAccessible(true);
            ((List<String>) idsField.get(null)).clear();
            
            java.lang.reflect.Field hashesField = Message.class.getDeclaredField("messageHashes");
            hashesField.setAccessible(true);
            ((List<String>) hashesField.get(null)).clear();
            
            java.lang.reflect.Field allField = Message.class.getDeclaredField("allMessages");
allField.setAccessible(true);
((List<Message>) allField.get(null)).clear();
            
            java.lang.reflect.Field counterField = Message.class.getDeclaredField("messageCounter");
            counterField.setAccessible(true);
            counterField.setInt(null, 0);
            
        } catch (Exception e) {
            // Ignore
        }
        
    }
    
    // ============================================================
    // TEST: Sent Messages array correctly populated
    // ============================================================
    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        // Test Data: Message 1 and Message 4 (Sent messages)
        new Message("+27834557896", "Did you get the cake?", "Sent");
        new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
        new Message("+27834484567", "Yohoooo, I am at your gate.", "Disregard");
        new Message("0838884567", "It is dinner time !", "Sent");
        
        List<String> sentMessages = Message.getSentMessages();
        
        // Assert: Exactly 2 sent messages
        assertEquals("Should have exactly 2 sent messages", 2, sentMessages.size());
        
        // Assert: Both sent messages should be in the list
        assertTrue("Message 1 should be in sent messages", 
            sentMessages.contains("Did you get the cake?"));
        assertTrue("Message 4 should be in sent messages", 
            sentMessages.contains("It is dinner time !"));
        
        // Assert: Non-sent messages should NOT be in the list
        assertFalse("Stored message should NOT be in sent messages", 
            sentMessages.contains("Where are you? You are late! I have asked you to be on time."));
        assertFalse("Disregarded message should NOT be in sent messages", 
            sentMessages.contains("Yohoooo, I am at your gate."));
    }
    
    // ============================================================
    // TEST: Display the longest Message
    // ============================================================
    @Test
    public void testDisplayLongestMessage() {
        // Test Data: messages 1-4
        new Message("+27834557896", "Did you get the cake?", "Sent");
        new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
        new Message("+27834484567", "Yohoooo, I am at your gate.", "Disregard");
        new Message("0838884567", "It is dinner time !", "Sent");
        
        List<messageapp3.StoredMessage> storedMessages = Message.getStoredMessages();
        String longestActual = "";
        for (StoredMessage sm : storedMessages) {
            if (sm.getMessage().length() > longestActual.length()) {
                longestActual = sm.getMessage();
            }
        }
        
        String expected = "Where are you? You are late! I have asked you to be on time.";
        assertEquals("The longest message should be Message 2", expected, longestActual);
    }
    
    // ============================================================
    // TEST: Search for messageID
    // ============================================================
    @Test
    public void testSearchByMessageID() {
        // Create a STORED message
        Message msg = new Message("0838884567", "It is dinner time !", "Stored");
        String messageID = msg.getMessageID();
        
        // Search for the message by ID
        boolean found = Message.searchByMessageID(messageID);
        assertTrue("Message with ID should be found", found);
        
        // Search with invalid ID should return false
        boolean notFound = Message.searchByMessageID("0000000000");
        assertFalse("Search with invalid ID should return false", notFound);
    }
    
    // ============================================================
    // TEST: Search all messages for a particular recipient
    // ============================================================
    @Test
    public void testSearchByRecipient() {
        // Test Data: messages with recipient +27838884567
        new Message("+27834557896", "Did you get the cake?", "Sent");
        new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
        new Message("+27834484567", "Yohoooo, I am at your gate.", "Disregard");
        new Message("0838884567", "It is dinner time !", "Sent");
        new Message("+27838884567", "Ok, I am leaving without you.", "Stored");
        
        // Search for messages for recipient +27838884567
        boolean found = Message.searchByRecipient("+27838884567");
        assertTrue("Messages for +27838884567 should be found", found);
        
        // Search for non-existent recipient
        boolean notFound = Message.searchByRecipient("+27999999999");
        assertFalse("Search for non-existent recipient should return false", notFound);
    }
    
    // ============================================================
    // TEST: Delete a message using a message hash
    // ============================================================
    @Test
    public void testDeleteByHash() {
        // Create a stored message to delete
        Message msg = new Message("+27838884567", "Test message", "Stored");
        String hash = msg.getMessageHash();
        
        // Get count before deletion
        int beforeCount = Message.getStoredMessageCount();
        
        // Delete the message
        boolean deleted = Message.deleteByHash(hash);
        assertTrue("Message should be successfully deleted", deleted);
        
        // Count should decrease by 1
        int afterCount = Message.getStoredMessageCount();
        assertEquals("Count should decrease by 1", beforeCount - 1, afterCount);
        
        // Delete with invalid hash should return false
        boolean invalidDelete = Message.deleteByHash("invalid:hash");
        assertFalse("Delete with invalid hash should return false", invalidDelete);
    }
    
    // ============================================================
    // TEST: Display Report
    // ============================================================
    @Test
    public void testDisplayFullReport() {
        // Add some messages
        new Message("+27834557896", "Did you get the cake?", "Sent");
        new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
        new Message("+27834484567", "Yohoooo, I am at your gate.", "Disregard");
        new Message("0838884567", "It is dinner time !", "Sent");
        new Message("+27838884567", "Ok, I am leaving without you.", "Stored");
        
        List<messageapp3.StoredMessage> storedMessages = Message.getStoredMessages();
        
        // There should be stored messages
        assertFalse("There should be stored messages", storedMessages.isEmpty());
        
        // All messages should have required fields
       for (messageapp3.StoredMessage sm : storedMessages) {
            assertNotNull("Message hash should not be null", sm.getMessageHash());
            assertNotNull("Recipient should not be null", sm.getRecipient());
            assertNotNull("Message should not be null", sm.getMessage());
            assertNotNull("Message ID should not be null", sm.getMessageID());
        }
    }
    
    // ============================================================
    // BASIC GETTER TESTS
    // ============================================================
    
    @Test
    public void testGetMessageID() {
        Message msg = new Message("+27834557896", "Test message", "Sent");
        assertNotNull(msg.getMessageID());
        assertEquals(10, msg.getMessageID().length());
    }

    @Test
    public void testGetRecipientCell() {
        Message msg = new Message("+27834557896", "Test message", "Sent");
        assertEquals("+27834557896", msg.getRecipientCell());
    }

    @Test
    public void testGetMessageContent() {
        Message msg = new Message("+27834557896", "Test message", "Sent");
        assertEquals("Test message", msg.getMessageContent());
    }

    @Test
    public void testGetMessageHash() {
        Message msg = new Message("+27834557896", "Test message", "Sent");
        assertNotNull(msg.getMessageHash());
    }

    @Test
    public void testGetFlag() {
        Message msg = new Message("+27834557896", "Test message", "Sent");
        assertEquals("Sent", msg.getFlag());
    }
    
    @Test
public void testGetAllMessages() {
    new Message("+27834557896", "Test message 1", "Sent");
    new Message("+27834557896", "Test message 2", "Stored");
    
    List<Message> all = Message.getAllMessages();
    assertEquals(2, all.size());
}

    @Test
    public void testProcessMessage() {
        Message msg = new Message("+27834557896", "Test message", "Stored");
        String result = msg.processMessage(3);
        assertTrue(result.contains("stored"));
    }

    @Test
    public void testDisplayStoredMessages() {
        new Message("+27834557896", "Test message 1", "Stored");
        new Message("+27834557896", "Test message 2", "Stored");
        
        // Just ensure it runs without exception
        Message.displayStoredMessages();
        assertTrue(true);
    }

    @Test
    public void testStoreMessagesToJSON() {
        new Message("+27834557896", "Test message", "Stored");
        
        // Just ensure it runs without exception
        Message.storeMessagesToJSON();
        assertTrue(true);
    }

    @Test
    public void testLoadMessagesFromJSON() throws Exception {
        // Just ensure it runs without exception
        Message.loadMessagesFromJSON();
        assertTrue(true);
    }

  

    @Test
    public void testGetSentMessages() {
        new Message("+27834557896", "Sent message", "Sent");
        List<String> sent = Message.getSentMessages();
        assertEquals(1, sent.size());
        assertEquals("Sent message", sent.get(0));
    }

    @Test
    public void testGetDisregardedMessages() {
        new Message("+27834557896", "Disregarded message", "Disregard");
        List<String> disregarded = Message.getDisregardedMessages();
        assertEquals(1, disregarded.size());
        assertEquals("Disregarded message", disregarded.get(0));
    }

    @Test
    public void testGetStoredMessages() {
        new Message("+27834557896", "Stored message", "Stored");
        List<messageapp3.StoredMessage> stored = Message.getStoredMessages();
        assertEquals(1, stored.size());
        assertEquals("Stored message", stored.get(0).getMessage());
    }

    @Test
    public void testGetSentMessageCount() {
        int before = Message.getSentMessageCount();
        new Message("+27834557896", "Test sent", "Sent");
        assertEquals(before + 1, Message.getSentMessageCount());
    }

    @Test
    public void testGetStoredMessageCount() {
        int before = Message.getStoredMessageCount();
        new Message("+27834557896", "Test stored", "Stored");
        assertEquals(before + 1, Message.getStoredMessageCount());
    }
}