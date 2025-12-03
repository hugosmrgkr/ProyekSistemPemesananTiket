/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class Tiket implements Printable
 * Sesuai Class Diagram: Tiket menggunakan Kursi, dibeli oleh Pelanggan, dicakup oleh Transaksi
 * Menerapkan konsep: ENCAPSULATION, INTERFACE
 */
public class Tiket implements Printable {
    
    // ENCAPSULATION: private attributes
    private String idTiket;
    private int nomorKursi;
    private double harga;
    private LocalDateTime waktuPemesanan;
    
    // Relasi: Tiket terhubung dengan Jadwal, Pelanggan, dan Kursi
    private String idJadwal;        // Foreign key ke Jadwal
    private String idPelanggan;     // Foreign key ke Pelanggan (opsional, bisa null)
    
    // Data pelanggan (embedded, tidak pakai foreign key)
    private String namaPelanggan;
    private String teleponPelanggan;
    private String emailPelanggan;
    
    // Attributes tambahan untuk informasi lengkap (dari JOIN table)
    private String lokasiAsal;
    private String lokasiTujuan;
    private String tanggalBerangkat;
    private String jamBerangkat;
    
    // Constructor default
    public Tiket() {
        this.waktuPemesanan = LocalDateTime.now();
    }
    
    // Constructor dengan parameter
    public Tiket(String idTiket, String idJadwal, String namaPelanggan, 
                 String teleponPelanggan, String emailPelanggan, int nomorKursi, double harga) {
        this.idTiket = idTiket;
        this.idJadwal = idJadwal;
        this.namaPelanggan = namaPelanggan;
        this.teleponPelanggan = teleponPelanggan;
        this.emailPelanggan = emailPelanggan;
        this.nomorKursi = nomorKursi;
        this.harga = harga;
        this.waktuPemesanan = LocalDateTime.now();
    }
    
    // Getter & Setter
    public String getIdTiket() {
        return idTiket;
    }
    
    public void setIdTiket(String idTiket) {
        this.idTiket = idTiket;
    }
    
    public String getIdJadwal() {
        return idJadwal;
    }
    
    public void setIdJadwal(String idJadwal) {
        this.idJadwal = idJadwal;
    }
    
    public String getIdPelanggan() {
        return idPelanggan;
    }
    
    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }
    
    public String getNamaPelanggan() {
        return namaPelanggan;
    }
    
    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }
    
    public String getTeleponPelanggan() {
        return teleponPelanggan;
    }
    
    public void setTeleponPelanggan(String teleponPelanggan) {
        this.teleponPelanggan = teleponPelanggan;
    }
    
    public String getEmailPelanggan() {
        return emailPelanggan;
    }
    
    public void setEmailPelanggan(String emailPelanggan) {
        this.emailPelanggan = emailPelanggan;
    }
    
    public int getNomorKursi() {
        return nomorKursi;
    }
    
    public void setNomorKursi(int nomorKursi) {
        this.nomorKursi = nomorKursi;
    }
    
    public double getHarga() {
        return harga;
    }
    
    public void setHarga(double harga) {
        this.harga = harga;
    }
    
    public LocalDateTime getWaktuPemesanan() {
        return waktuPemesanan;
    }
    
    public void setWaktuPemesanan(LocalDateTime waktuPemesanan) {
        this.waktuPemesanan = waktuPemesanan;
    }
    
    // Getter & Setter untuk info jadwal
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
    
    public String getTanggalBerangkat() {
        return tanggalBerangkat;
    }
    
    public void setTanggalBerangkat(String tanggalBerangkat) {
        this.tanggalBerangkat = tanggalBerangkat;
    }
    
    public String getJamBerangkat() {
        return jamBerangkat;
    }
    
    public void setJamBerangkat(String jamBerangkat) {
        this.jamBerangkat = jamBerangkat;
    }
    
    // IMPLEMENTS method dari interface Printable - cetakStruk()
    @Override
    public String cetakStruk() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        
        StringBuilder struk = new StringBuilder();
        struk.append("═══════════════════════════════════════════\n");
        struk.append("       STRUK PEMESANAN TIKET BUS\n");
        struk.append("═══════════════════════════════════════════\n");
        struk.append("ID Tiket      : ").append(idTiket).append("\n");
        struk.append("Tanggal Pesan : ").append(waktuPemesanan.format(formatter)).append("\n\n");
        
        struk.append("DETAIL PELANGGAN:\n");
        struk.append("Nama          : ").append(namaPelanggan).append("\n");
        struk.append("Telepon       : ").append(teleponPelanggan).append("\n");
        struk.append("Email         : ").append(emailPelanggan).append("\n\n");
        
        struk.append("DETAIL PERJALANAN:\n");
        struk.append("Asal          : ").append(lokasiAsal != null ? lokasiAsal : "-").append("\n");
        struk.append("Tujuan        : ").append(lokasiTujuan != null ? lokasiTujuan : "-").append("\n");
        struk.append("Tanggal       : ").append(tanggalBerangkat != null ? tanggalBerangkat : "-").append("\n");
        struk.append("Jam Berangkat : ").append(jamBerangkat != null ? jamBerangkat : "-").append("\n");
        struk.append("Nomor Kursi   : ").append(nomorKursi).append("\n\n");
        
        struk.append("PEMBAYARAN:\n");
        struk.append("Harga Tiket   : Rp ").append(String.format("%,.2f", harga)).append("\n");
        struk.append("Status        : LUNAS\n\n");
        
        struk.append("═══════════════════════════════════════════\n");
        struk.append("     Terima kasih atas kepercayaan Anda\n");
        struk.append("          Selamat Berperjalanan!\n");
        struk.append("═══════════════════════════════════════════\n");
        
        return struk.toString();
    }
    
    @Override
    public boolean simpanKeFile(String path) {
        // Implementasi akan dikerjakan di FileHelper
        // Method ini hanya placeholder
        return false;
    }
    
    @Override
    public String toString() {
        return "Tiket{" +
                "idTiket='" + idTiket + '\'' +
                ", namaPelanggan='" + namaPelanggan + '\'' +
                ", nomorKursi=" + nomorKursi +
                ", harga=" + harga +
                '}';
    }
}