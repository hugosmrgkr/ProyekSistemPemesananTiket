/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

/**
 * Class Kursi
 * Sesuai Class Diagram: Kursi memesan Jadwal
 * Menerapkan konsep: ENCAPSULATION
 */
public class Kursi {
    
    // ENCAPSULATION: private attributes
    private String idKursi;
    private int nomorKursi;
    private boolean tersedia;
    
    // Relasi: Kursi terhubung dengan Jadwal (1 kursi untuk 1 jadwal)
    private String idJadwal;  // Foreign key
    
    // Constructor default
    public Kursi() {
        this.tersedia = true; // Default kursi tersedia
    }
    
    // Constructor dengan parameter
    public Kursi(String idKursi, String idJadwal, int nomorKursi, boolean tersedia) {
        this.idKursi = idKursi;
        this.idJadwal = idJadwal;
        this.nomorKursi = nomorKursi;
        this.tersedia = tersedia;
    }
    
    // Getter & Setter
    public String getIdKursi() {
        return idKursi;
    }
    
    public void setIdKursi(String idKursi) {
        this.idKursi = idKursi;
    }
    
    public String getIdJadwal() {
        return idJadwal;
    }
    
    public void setIdJadwal(String idJadwal) {
        this.idJadwal = idJadwal;
    }
    
    public int getNomorKursi() {
        return nomorKursi;
    }
    
    public void setNomorKursi(int nomorKursi) {
        this.nomorKursi = nomorKursi;
    }
    
    public boolean isTersedia() {
        return tersedia;
    }
    
    public void setTersedia(boolean tersedia) {
        this.tersedia = tersedia;
    }
    
    // Method untuk booking kursi (sesuai diagram: pesan())
    public void pesan() {
        this.tersedia = false;
    }
    
    // Method untuk membatalkan booking (sesuai diagram: batal())
    public void batal() {
        this.tersedia = true;
    }
    
    @Override
    public String toString() {
        return "Kursi " + nomorKursi + " - " + (tersedia ? "Tersedia" : "Terisi");
    }
}