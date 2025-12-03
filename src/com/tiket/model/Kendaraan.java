/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

/**
 * Abstract class Kendaraan
 * Menerapkan konsep: INHERITANCE, POLYMORPHISM, ABSTRACT CLASS
 */
public abstract class Kendaraan {
    
    // ENCAPSULATION: protected attributes (bisa diakses child class)
    protected String id;
    protected String nomorPlat;
    protected int kapasitas;
    
    // Constructor default
    public Kendaraan() {}
    
    // Constructor dengan parameter
    public Kendaraan(String id, String nomorPlat, int kapasitas) {
        this.id = id;
        this.nomorPlat = nomorPlat;
        this.kapasitas = kapasitas;
    }
    
    // Getter & Setter (ENCAPSULATION)
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getNomorPlat() {
        return nomorPlat;
    }
    
    public void setNomorPlat(String nomorPlat) {
        this.nomorPlat = nomorPlat;
    }
    
    public int getKapasitas() {
        return kapasitas;
    }
    
    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }
    
    // ABSTRACT METHOD (harus di-override oleh child class)
    // POLYMORPHISM: setiap child class punya implementasi berbeda
    public abstract String getInfo();
    
    // CONCRETE METHOD (bisa langsung dipakai oleh child class)
    public boolean isFull(int kursiTerisi) {
        return kursiTerisi >= kapasitas;
    }
    
    @Override
    public String toString() {
        return "Kendaraan{" +
                "id='" + id + '\'' +
                ", nomorPlat='" + nomorPlat + '\'' +
                ", kapasitas=" + kapasitas +
                '}';
    }
}
