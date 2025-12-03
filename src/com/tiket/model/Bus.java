/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

/**
 * Class Bus extends Kendaraan
 * Menerapkan konsep: INHERITANCE, POLYMORPHISM, ENCAPSULATION
 */
public class Bus extends Kendaraan {
    
    // ENCAPSULATION: private attribute
    private String model;
    
    // Constructor default
    public Bus() {
        super(); // Call parent constructor
    }
    
    // Constructor dengan parameter
    public Bus(String id, String nomorPlat, int kapasitas, String model) {
        super(id, nomorPlat, kapasitas); // Call parent constructor
        this.model = model;
    }
    
    // Getter & Setter
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    // POLYMORPHISM: Override abstract method dari parent
    @Override
    public String getInfo() {
        return "Bus " + model + " (" + nomorPlat + ") - Kapasitas: " + kapasitas + " kursi";
    }
    
    // POLYMORPHISM: Method Overloading (method dengan nama sama, parameter beda)
    public String getInfo(boolean includeId) {
        if (includeId) {
            return "[" + id + "] " + getInfo();
        }
        return getInfo();
    }
    
    @Override
    public String toString() {
        return "Bus{" +
                "id='" + id + '\'' +
                ", nomorPlat='" + nomorPlat + '\'' +
                ", model='" + model + '\'' +
                ", kapasitas=" + kapasitas +
                '}';
    }
}
