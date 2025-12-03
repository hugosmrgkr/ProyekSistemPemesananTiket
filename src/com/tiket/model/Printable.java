/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

/**
 * Interface Printable
 * Menerapkan konsep: INTERFACE
 * 
 * Interface untuk class yang bisa dicetak/disimpan ke file
 */
public interface Printable {
    
    
     //Method untuk generate struk dalam bentuk String
     //@return String berisi format struk
     
    String cetakStruk();
    
    
//     Method untuk menyimpan struk ke file
//     @param path lokasi file
//     @return true jika berhasil, false jika gagal
     
    boolean simpanKeFile(String path);
}
