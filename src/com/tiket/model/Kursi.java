/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

public class Kursi {
    private String idTiket;
    private int nomorKursi;
    private boolean tersedia;

    public Kursi() {
        this.tersedia = true;
    }

    public Kursi(String idTiket, int nomorKursi) {
        this.idTiket = idTiket;
        this.nomorKursi = nomorKursi;
        this.tersedia = true;
    }

    public String getIdTiket() { return idTiket; }
    public void setIdTiket(String idTiket) { this.idTiket = idTiket; }

    public int getNomorKursi() { return nomorKursi; }
    public void setNomorKursi(int nomorKursi) { this.nomorKursi = nomorKursi; }

    public boolean isTersedia() { return tersedia; }
    public void setTersedia(boolean tersedia) { this.tersedia = tersedia; }

    public void pesan() {
        this.tersedia = false;
    }

    public void batal() {
        this.tersedia = true;
    }
}