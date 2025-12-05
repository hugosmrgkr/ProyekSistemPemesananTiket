/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.helper;

import com.tiket.model.Tiket;
import java.io.*;
import java.nio.file.*;

/**
 * Helper class untuk File I/O operations
 * Menerapkan konsep: FILE I/O, EXCEPTION HANDLING
 */
public class FileHelper {
    
    private static final String OUTPUT_DIR = "output/";
    
    /**
     * Simpan struk tiket ke file .txt
     * FILE I/O: Write ke file
     * 
     * @param tiket object Tiket yang akan disimpan
     * @return true jika berhasil, false jika gagal
     */
    public boolean saveStrukToFile(Tiket tiket) {
        try {
            // Buat folder output jika belum ada
            Path outputPath = Paths.get(OUTPUT_DIR);
            if (!Files.exists(outputPath)) {
                Files.createDirectories(outputPath);
            }
            
            // Generate filename
            String filename = OUTPUT_DIR + "struk_" + tiket.getIdTiket() + ".txt";
            
            // Get content dari method cetakStruk() (interface Printable)
            String content = tiket.cetakStruk();
            
            // FILE I/O: Write content ke file
            Files.write(Paths.get(filename), content.getBytes());
            
            System.out.println("✅ Struk berhasil disimpan: " + filename);
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Gagal menyimpan struk: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Baca struk dari file
     * FILE I/O: Read dari file
     * 
     * @param idTiket ID tiket yang akan dibaca
     * @return isi file struk, atau null jika gagal
     */
    public String readStrukFromFile(String idTiket) {
        try {
            String filename = OUTPUT_DIR + "struk_" + idTiket + ".txt";
            
            // FILE I/O: Read content dari file
            return new String(Files.readAllBytes(Paths.get(filename)));
            
        } catch (IOException e) {
            System.err.println("❌ Gagal membaca struk: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cek apakah file struk sudah ada
     * 
     * @param idTiket ID tiket
     * @return true jika file ada
     */
    public boolean isStrukExists(String idTiket) {
        String filename = OUTPUT_DIR + "struk_" + idTiket + ".txt";
        return Files.exists(Paths.get(filename));
    }
    
    /**
     * Hapus file struk
     * 
     * @param idTiket ID tiket
     * @return true jika berhasil dihapus
     */
    public boolean deleteStruk(String idTiket) {
        try {
            String filename = OUTPUT_DIR + "struk_" + idTiket + ".txt";
            return Files.deleteIfExists(Paths.get(filename));
        } catch (IOException e) {
            System.err.println("❌ Gagal menghapus struk: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Write log ke file (opsional - untuk debugging)
     * 
     * @param message pesan log
     */
    public void writeLog(String message) {
        try {
            String filename = "application.log";
            String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String logEntry = "[" + timestamp + "] " + message + "\n";
            
            // Append to file
            Files.write(
                Paths.get(filename), 
                logEntry.getBytes(), 
                StandardOpenOption.CREATE, 
                StandardOpenOption.APPEND
            );
            
        } catch (IOException e) {
            System.err.println("❌ Gagal menulis log: " + e.getMessage());
        }
    }
}