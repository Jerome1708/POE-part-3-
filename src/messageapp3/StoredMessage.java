/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package messageapp3;
/**
 *
 * @author lab_services_student
 */
public class StoredMessage {
   private String recipient;
    private String message;
    private String messageID;
    private String messageHash;
    
    // ===== CONSTRUCTOR =====
    public StoredMessage(String recipient, String message, String messageID, String messageHash) {
        this.recipient = recipient;
        this.message = message;
        this.messageID = messageID;
        this.messageHash = messageHash;
    }
    
    // ===== GETTERS =====
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    
    // ===== SETTERS =====
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public void setMessage(String message) { this.message = message; }
    public void setMessageID(String messageID) { this.messageID = messageID; }
    public void setMessageHash(String messageHash) { this.messageHash = messageHash; }  
}
