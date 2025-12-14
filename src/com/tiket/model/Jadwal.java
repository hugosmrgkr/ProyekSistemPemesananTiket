package com.tiket.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Jadwal {
    private String idJadwal;
    private String lokasiAsal;
    private String lokasiTujuan;
    private LocalDateTime waktuBerangkat;
    private LocalDateTime waktuTiba;
    private double harga;
    private List<Kursi> kursiList;

    public Jadwal() {
        this.kursiList = new ArrayList<>();
    }

    public Jadwal(String idJadwal, String lokasiAsal, String lokasiTujuan, 
                  LocalDateTime waktuBerangkat, LocalDateTime waktuTiba, double harga) {
        this.idJadwal = idJadwal;
        this.lokasiAsal = lokasiAsal;
        this.lokasiTujuan = lokasiTujuan;
        this.waktuBerangkat = waktuBerangkat;
        this.waktuTiba = waktuTiba;
        this.harga = harga;
        this.kursiList = new ArrayList<>();
    }

    public String getIdJadwal() { return idJadwal; }
    public void setIdJadwal(String idJadwal) { this.idJadwal = idJadwal; }

    public String getLokasiAsal() { return lokasiAsal; }
    public void setLokasiAsal(String lokasiAsal) { this.lokasiAsal = lokasiAsal; }

    public String getLokasiTujuan() { return lokasiTujuan; }
    public void setLokasiTujuan(String lokasiTujuan) { this.lokasiTujuan = lokasiTujuan; }

    public LocalDateTime getWaktuBerangkat() { return waktuBerangkat; }
    public void setWaktuBerangkat(LocalDateTime waktuBerangkat) { this.waktuBerangkat = waktuBerangkat; }

    public LocalDateTime getWaktuTiba() { return waktuTiba; }
    public void setWaktuTiba(LocalDateTime waktuTiba) { this.waktuTiba = waktuTiba; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public List<Kursi> getKursiList() { return kursiList; }
    public void setKursiList(List<Kursi> kursiList) { this.kursiList = kursiList; }

    public List<Kursi> getKursiTersedia() {
        List<Kursi> tersedia = new ArrayList<>();
        for (Kursi kursi : kursiList) {
            if (kursi.isTersedia()) {
                tersedia.add(kursi);
            }
        }
        return tersedia;
    }

    public void tambahKursi(Kursi kursi) {
        this.kursiList.add(kursi);
    }

    public String getInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("%s -> %s | %s | Rp %.2f", 
            lokasiAsal, lokasiTujuan, waktuBerangkat.format(formatter), harga);
    }
}