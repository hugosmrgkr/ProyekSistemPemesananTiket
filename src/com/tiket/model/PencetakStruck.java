package com.tiket.model;

import java.time.LocalDateTime;

public class PencetakStruck {

    private String idCetak;
    private Tiket tiket;
    private LocalDateTime waktuCetak;

    // Constructor kosong
    public PencetakStruck() {
        this.waktuCetak = LocalDateTime.now();
    }

    // Constructor utama
    public PencetakStruck(String idCetak, Tiket tiket) {
        this.idCetak = idCetak;
        this.tiket = tiket;
        this.waktuCetak = LocalDateTime.now();
    }

    // Getter & Setter
    public String getIdCetak() {
        return idCetak;
    }

    public void setIdCetak(String idCetak) {
        this.idCetak = idCetak;
    }

    public Tiket getTiket() {
        return tiket;
    }

    public void setTiket(Tiket tiket) {
        this.tiket = tiket;
    }

    public LocalDateTime getWaktuCetak() {
        return waktuCetak;
    }

    public void setWaktuCetak(LocalDateTime waktuCetak) {
        this.waktuCetak = waktuCetak;
    }

    // =========================
    // CETAK STRUK (FINAL)
    // =========================
    public String cetak() {
        if (tiket == null) {
            return "Tiket tidak tersedia";
        }

        StringBuilder struk = new StringBuilder();
        struk.append("\n══════════════════════════════════════\n");
        struk.append("          STRUK PEMESANAN TIKET        \n");
        struk.append("══════════════════════════════════════\n");
        struk.append("ID Cetak      : ").append(idCetak).append("\n");
        struk.append("ID Tiket      : ").append(tiket.getIdTiket()).append("\n");
        struk.append("Nomor Kursi   : ").append(tiket.getNomorKursi()).append("\n");

        if (tiket.getIdJadwal() != null) {
            struk.append("ID Jadwal     : ").append(tiket.getIdJadwal()).append("\n");
        }

        struk.append("Harga         : Rp ")
             .append(String.format("%.2f", tiket.getHarga())).append("\n");
        struk.append("Waktu Cetak   : ").append(waktuCetak).append("\n");
        struk.append("══════════════════════════════════════\n");

        return struk.toString();
    }
}
