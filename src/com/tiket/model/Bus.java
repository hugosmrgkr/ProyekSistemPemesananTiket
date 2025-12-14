/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

public class Bus extends Kendaraan {
    private String idBus;
    private String model;
    private int jumlahKursi;

    public Bus() {}

    public Bus(String idBus, String model, int jumlahKursi, String nomorPlat) {
        super(idBus, nomorPlat, jumlahKursi);
        this.idBus = idBus;
        this.model = model;
        this.jumlahKursi = jumlahKursi;
    }

    public String getIdBus() { return idBus; }
    public void setIdBus(String idBus) { this.idBus = idBus; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getJumlahKursi() { return jumlahKursi; }
    public void setJumlahKursi(int jumlahKursi) { this.jumlahKursi = jumlahKursi; }

    public String getInfoBus() {
        return "Bus " + model + " - " + jumlahKursi + " kursi";
    }
}