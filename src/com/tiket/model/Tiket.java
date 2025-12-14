/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

import java.time.LocalDateTime;

public class Tiket extends Kursi {
    private String idTiket;
    private int nomorKursi;
    private double harga;
    private LocalDateTime waktuStruck;

    public Tiket() {}

    public Tiket(String idTiket, int nomorKursi, double harga, LocalDateTime waktuStruck) {
        super(idTiket, nomorKursi);
        this.idTiket = idTiket;
        this.nomorKursi = nomorKursi;
        this.harga = harga;
        this.waktuStruck = waktuStruck;
    }

    @Override
    public String getIdTiket() { return idTiket; }
    @Override
    public void setIdTiket(String idTiket) { this.idTiket = idTiket; }

    @Override
    public int getNomorKursi() { return nomorKursi; }
    @Override
    public void setNomorKursi(int nomorKursi) { this.nomorKursi = nomorKursi; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public LocalDateTime getWaktuStruck() { return waktuStruck; }
    public void setWaktuStruck(LocalDateTime waktuStruck) { this.waktuStruck = waktuStruck; }

    public String cetakStruck() {
        return "Tiket #" + idTiket + " - Kursi " + nomorKursi + " - Rp " + harga;
    }
}