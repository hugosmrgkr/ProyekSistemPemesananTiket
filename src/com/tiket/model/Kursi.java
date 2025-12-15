/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

public class Kursi {

    private String idKursi;
    private String idJadwal;
    private int nomorKursi;
    private boolean tersedia;

    // Constructor default
    public Kursi() {
        this.tersedia = true;
    }

    // Constructor 3 parameter
    public Kursi(String idKursi, String idJadwal, int nomorKursi) {
        this.idKursi = idKursi;
        this.idJadwal = idJadwal;
        this.nomorKursi = nomorKursi;
        this.tersedia = true;
    }

    // ✅ Constructor 4 parameter (WAJIB ADA)
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

    // Method bisnis
    public void pesan() {
        this.tersedia = false;
    }

    public void batal() {
        this.tersedia = true;
    }
}
