/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.messageapp;
/**
 *
 * @author lab_services_student
 */
//add login from part 1
public class Login {
   
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String cellNumber;
    
    // ===== SETTERS =====
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setCellNumber(String cellNumber) { this.cellNumber = cellNumber; }
    
    // ===== GETTERS =====
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCellNumber() { return cellNumber; }
    
    // ===== VALIDATION METHODS =====
    public boolean checkUserName(String username) {
        if (username == null || username.isEmpty()) return false;
        return username.contains("_") && username.length() <= 5;
    }
    
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasCapital && hasNumber && hasSpecial;
    }
    
    public boolean checkCellPhoneNumber(String cellNumber) {
        if (cellNumber == null || cellNumber.isEmpty()) return false;
        return cellNumber.matches("^(\\+27|27)[0-9]{9}$");
    }
    
    // ===== BUSINESS METHODS =====
    public String registerUser() {
        if (!checkUserName(username)) {
            return "✗ Username is not correctly formatted. Must contain '_' and be max 5 characters.";
        }
        if (!checkPasswordComplexity(password)) {
            return "✗ Password is not correctly formatted. Must contain 8+ chars, 1 capital, 1 number, 1 special.";
        }
        return "✓ The two conditions have been met and the user has successfully registered";
    }
    
    public boolean loginUser(String username, String password) {
        return this.username != null && this.username.equals(username) &&
               this.password != null && this.password.equals(password);
    }
    
    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "✓ Welcome " + firstName + " " + lastName + " it is great to see you.";
        } else {
            return "✗ Username or password is incorrect, please try again";
        }
    }
}

