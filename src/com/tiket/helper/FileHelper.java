/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.helper;

import com.tiket.model.Tiket;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Helper class untuk operasi File I/O
 * Menerapkan konsep: FILE I/O
 */
public class FileHelper {
    
    private static final String OUTPUT_DIR = "output";
    
    public FileHelper() {
        // Buat folder output jika belum ada
        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
    
    /**
     * Simpan struk tiket ke file
     * @param tiket object Tiket
     * @return true jika berhasil
     */
    public boolean saveStrukToFile(Tiket tiket) {
        if (tiket == null) {
            return false;
        }
        
        String filename = OUTPUT_DIR + "/struk_" + tiket.getIdTiket() + ".txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(tiket.cetakStruk());
            return true;
        } catch (IOException e) {
            System.err.println("Error menyimpan file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Simpan text custom ke file
     * @param filename nama file
     * @param content isi file
     * @return true jika berhasil
     */
    public boolean saveToFile(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
            return true;
        } catch (IOException e) {
            System.err.println("Error menyimpan file: " + e.getMessage());
            return false;
        }
    }
}