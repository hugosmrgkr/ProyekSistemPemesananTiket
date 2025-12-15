package com.tiket.model;

import java.time.LocalDateTime;

public class Tiket {

    private String idTiket;
    private String idJadwal;
    private String idKursi;
    private int nomorKursi;
    private double harga;
    private LocalDateTime waktuStruk;

    // =========================
    // CONSTRUCTOR
    // =========================

    // Constructor kosong (wajib untuk DAO)
    public Tiket() {}

    // Constructor utama (dipakai controller)
    public Tiket(
            String idTiket,
            String idJadwal,
            String idKursi,
            int nomorKursi,
            double harga,
            LocalDateTime waktuStruk
    ) {
        this.idTiket = idTiket;
        this.idJadwal = idJadwal;
        this.idKursi = idKursi;
        this.nomorKursi = nomorKursi;
        this.harga = harga;
        this.waktuStruk = waktuStruk;
    }

    // =========================
    // GETTER & SETTER
    // =========================

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

    public String getIdKursi() {
        return idKursi;
    }

    public void setIdKursi(String idKursi) {
        this.idKursi = idKursi;
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

    public LocalDateTime getWaktuStruk() {
        return waktuStruk;
    }

    public void setWaktuStruk(LocalDateTime waktuStruk) {
        this.waktuStruk = waktuStruk;
    }

    // =========================
    // CETAK STRUK
    // =========================
    public String cetakStruk() {
        return
            "=========== STRUK TIKET ===========" +
            "\nID Tiket    : " + idTiket +
            "\nID Jadwal   : " + idJadwal +
            "\nID Kursi    : " + idKursi +
            "\nNomor Kursi: " + nomorKursi +
            "\nHarga       : Rp " + harga +
            "\nWaktu Cetak : " + waktuStruk +
            "\n==================================";
    }
}
