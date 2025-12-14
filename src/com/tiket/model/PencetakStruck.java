/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

public class PencetakStruck {
    private String cetakId;
    private Tiket tiket;

    public PencetakStruck() {}

    public PencetakStruck(String cetakId, Tiket tiket) {
        this.cetakId = cetakId;
        this.tiket = tiket;
    }

    public String getCetakId() { return cetakId; }
    public void setCetakId(String cetakId) { this.cetakId = cetakId; }

    public Tiket getTiket() { return tiket; }
    public void setTiket(Tiket tiket) { this.tiket = tiket; }

    public String cetak() {
        if (tiket == null) {
            return "Tiket tidak tersedia";
        }
        
        StringBuilder struck = new StringBuilder();
        struck.append("\n========== STRUCK PEMESANAN ==========\n");
        struck.append("ID Cetak    : ").append(cetakId).append("\n");
        struck.append("ID Tiket    : ").append(tiket.getIdTiket()).append("\n");
        struck.append("Nomor Kursi : ").append(tiket.getNomorKursi()).append("\n");
        struck.append("Harga       : Rp ").append(String.format("%.2f", tiket.getHarga())).append("\n");
        struck.append("Waktu       : ").append(tiket.getWaktuStruck()).append("\n");
        struck.append("======================================\n");
        
        return struck.toString();
    }
}