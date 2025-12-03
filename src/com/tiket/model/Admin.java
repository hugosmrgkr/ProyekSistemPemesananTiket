/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

/**
 * Class Admin
 * Sesuai Class Diagram: Admin mengelola Jadwal
 * Menerapkan konsep: ENCAPSULATION
 */
public class Admin {
    
    // ENCAPSULATION: private attributes
    private String idAdmin;
    private String username;
    private String password; // Untuk login (opsional, bisa skip jika tidak ada fitur login)
    
    // Constructor default
    public Admin() {}
    
    // Constructor dengan parameter
    public Admin(String idAdmin, String username, String password) {
        this.idAdmin = idAdmin;
        this.username = username;
        this.password = password;
    }
    
    // Getter & Setter
    public String getIdAdmin() {
        return idAdmin;
    }
    
    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    // Method untuk mengelola jadwal (sesuai diagram)
    // Implementasi CRUD ada di JadwalDB, ini hanya method placeholder
    public void buatJadwal(Jadwal jadwal) {
        // Implementation di controller/service layer
    }
    
    public void ubahJadwal(Jadwal jadwal) {
        // Implementation di controller/service layer
    }
    
    public void hapusJadwal(String idJadwal) {
        // Implementation di controller/service layer
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "idAdmin='" + idAdmin + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
