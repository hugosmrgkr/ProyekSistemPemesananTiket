/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

public class Kendaraan {
    private String id;
    private String nomorPlat;
    private int kapasitas;

    public Kendaraan() {}

    public Kendaraan(String id, String nomorPlat, int kapasitas) {
        this.id = id;
        this.nomorPlat = nomorPlat;
        this.kapasitas = kapasitas;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNomorPlat() { return nomorPlat; }
    public void setNomorPlat(String nomorPlat) { this.nomorPlat = nomorPlat; }

    public int getKapasitas() { return kapasitas; }
    public void setKapasitas(int kapasitas) { this.kapasitas = kapasitas; }

    public String getInfo() {
        return "ID: " + id + ", Plat: " + nomorPlat + ", Kapasitas: " + kapasitas;
    }
}