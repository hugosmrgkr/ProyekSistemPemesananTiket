/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class Jadwal
 * Sesuai Class Diagram: Jadwal beroperasi dengan Bus, memiliki Kursi, dipesan oleh Tiket
 * Menerapkan konsep: ENCAPSULATION, COMPOSITION
 */
public class Jadwal {
    
    // ENCAPSULATION: private attributes
    private String idJadwal;
    private String lokasiAsal;
    private String lokasiTujuan;
    private LocalDateTime waktuBerangkat;  // DateTime untuk tanggal + jam
    private LocalDateTime waktuTiba;       // DateTime untuk tiba
    private double harga;
    
    // COMPOSITION: Jadwal memiliki relasi dengan Bus dan Kursi
    private List<Kursi> kursiList;  // 1..* (1 jadwal punya banyak kursi)
    
    // Helper attributes (tidak wajib di diagram, tapi berguna)
    private String idBus;
    private int kursiTersedia;
    
    // Constructor default
    public Jadwal() {}
    
    // Constructor dengan parameter
    public Jadwal(String idJadwal, String idBus, String lokasiAsal, String lokasiTujuan,
              LocalDateTime waktuBerangkat, LocalDateTime waktuTiba, double harga) {
        this.idJadwal = idJadwal;
        this.idBus = idBus;
        this.lokasiAsal = lokasiAsal;
        this.lokasiTujuan = lokasiTujuan;
        this.waktuBerangkat = waktuBerangkat;
        this.waktuTiba = waktuTiba;
        this.harga = harga;
    }
    
    // Getter & Setter (ENCAPSULATION)
    public String getIdJadwal() {
        return idJadwal;
    }
    
    public void setIdJadwal(String idJadwal) {
        this.idJadwal = idJadwal;
    }
    
    public String getIdBus() {
        return idBus;
    }
    
    public void setIdBus(String idBus) {
        this.idBus = idBus;
    }
    
    public String getLokasiAsal() {
        return lokasiAsal;
    }
    
    public void setLokasiAsal(String lokasiAsal) {
        this.lokasiAsal = lokasiAsal;
    }
    
    public String getLokasiTujuan() {
        return lokasiTujuan;
    }
    
    public void setLokasiTujuan(String lokasiTujuan) {
        this.lokasiTujuan = lokasiTujuan;
    }
    
    public LocalDateTime getWaktuBerangkat() {
        return waktuBerangkat;
    }
    
    public void setWaktuBerangkat(LocalDateTime waktuBerangkat) {
        this.waktuBerangkat = waktuBerangkat;
    }
    
    public LocalDateTime getWaktuTiba() {
        return waktuTiba;
    }
    
    public void setWaktuTiba(LocalDateTime waktuTiba) {
        this.waktuTiba = waktuTiba;
    }
    
    public double getHarga() {
        return harga;
    }
    
    public void setHarga(double harga) {
        this.harga = harga;
    }
    
    public List<Kursi> getKursiList() {
        return kursiList;
    }
    
    public void setKursiList(List<Kursi> kursiList) {
        this.kursiList = kursiList;
    }
    
    public int getKursiTersedia() {
        return kursiTersedia;
    }
    
    public void setKursiTersedia(int kursiTersedia) {
        this.kursiTersedia = kursiTersedia;
    }
    
    // COMPOSITION METHOD: Sesuai diagram - getKursiTersedia() dan tambahKursi()
    public List<Kursi> getKursiTersediaList() {
        // Return list kursi yang masih tersedia
        if (kursiList == null) return null;
        return kursiList.stream()
                .filter(Kursi::isTersedia)
                .collect(java.util.stream.Collectors.toList());
    }
    
    public void tambahKursi(Kursi kursi) {
        if (kursiList == null) {
            kursiList = new java.util.ArrayList<>();
        }
        kursiList.add(kursi);
    }
    
    // Method untuk mengurangi kursi tersedia
    public void kurangiKursi() {
        if (kursiTersedia > 0) {
            kursiTersedia--;
        }
    }
    
    // Method untuk menambah kursi tersedia (saat batal)
    public void tambahKursiTersedia() {
        kursiTersedia++;
    }
    
    @Override
    public String toString() {
        return lokasiAsal + " → " + lokasiTujuan + " | " + 
               waktuBerangkat + " | Rp " + harga;
    }
}