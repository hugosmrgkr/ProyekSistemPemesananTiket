/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

/**
 * Class Pelanggan
 * Sesuai Class Diagram: Pelanggan membeli Tiket
 * Menerapkan konsep: ENCAPSULATION
 */
public class Pelanggan {
    
    // ENCAPSULATION: private attributes
    private String idPelanggan;
    private String nama;
    private String email;
    private String telepon;
    
    // Constructor default
    public Pelanggan() {}
    
    // Constructor dengan parameter
    public Pelanggan(String idPelanggan, String nama, String email, String telepon) {
        this.idPelanggan = idPelanggan;
        this.nama = nama;
        this.email = email;
        this.telepon = telepon;
    }
    
    // Getter & Setter
    public String getIdPelanggan() {
        return idPelanggan;
    }
    
    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }
    
    public String getNama() {
        return nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelepon() {
        return telepon;
    }
    
    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }
    
    // Method untuk membeli tiket (sesuai diagram)
    // Return Tiket object setelah pembelian
    public Tiket buatPemesanan(Jadwal jadwal, int nomorKursi) {
        // Implementation di controller/service layer
        // Method ini hanya placeholder
        return null;
    }
    
    @Override
    public String toString() {
        return "Pelanggan{" +
                "idPelanggan='" + idPelanggan + '\'' +
                ", nama='" + nama + '\'' +
                ", email='" + email + '\'' +
                ", telepon='" + telepon + '\'' +
                '}';
    }
}