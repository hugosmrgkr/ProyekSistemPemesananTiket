/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

/**
 * Class Transaksi
 * Sesuai Class Diagram: Transaksi mencakup Tiket (1..*)
 * Menerapkan konsep: ENCAPSULATION
 */
public class Transaksi {
    
    // ENCAPSULATION: private attributes
    private String idTransaksi;
    private double jumlah;            // Total harga
    private String metodePembayaran;  // Cash, Transfer, dll
    private String status;             // LUNAS, PENDING
    
    // Relasi: Transaksi mencakup 1..* Tiket
    private String idTiket;  // Foreign key (simplified: 1 transaksi untuk 1 tiket)
    
    // Constructor default
    public Transaksi() {
        this.status = "PENDING";
    }
    
    // Constructor dengan parameter
    public Transaksi(String idTransaksi, String idTiket, double jumlah, String metodePembayaran) {
        this.idTransaksi = idTransaksi;
        this.idTiket = idTiket;
        this.jumlah = jumlah;
        this.metodePembayaran = metodePembayaran;
        this.status = "PENDING";
    }
    
    // Getter & Setter
    public String getIdTransaksi() {
        return idTransaksi;
    }
    
    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }
    
    public String getIdTiket() {
        return idTiket;
    }
    
    public void setIdTiket(String idTiket) {
        this.idTiket = idTiket;
    }
    
    public double getJumlah() {
        return jumlah;
    }
    
    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }
    
    public String getMetodePembayaran() {
        return metodePembayaran;
    }
    
    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    // Method sesuai diagram: bayar() dan refund()
    public boolean bayar() {
        // Set status ke LUNAS
        this.status = "LUNAS";
        return true;
    }
    
    public boolean refund() {
        // Set status ke REFUNDED
        this.status = "REFUNDED";
        return true;
    }
    
    @Override
    public String toString() {
        return "Transaksi{" +
                "idTransaksi='" + idTransaksi + '\'' +
                ", jumlah=" + jumlah +
                ", metodePembayaran='" + metodePembayaran + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
