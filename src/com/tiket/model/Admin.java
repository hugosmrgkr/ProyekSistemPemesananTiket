/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

public class Admin {
    private String idAdmin;
    private String username;
    private String password;

    public Admin() {}

    public Admin(String idAdmin, String username, String password) {
        this.idAdmin = idAdmin;
        this.username = username;
        this.password = password;
    }

    public String getIdAdmin() { return idAdmin; }
    public void setIdAdmin(String idAdmin) { this.idAdmin = idAdmin; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public void buatJadwal(Jadwal jadwal) {
        // Logika untuk membuat jadwal
    }

    public void ubahJadwal(Jadwal jadwal) {
        // Logika untuk mengubah jadwal
    }

    public void hapusJadwal(String id) {
        // Logika untuk menghapus jadwal
    }
}