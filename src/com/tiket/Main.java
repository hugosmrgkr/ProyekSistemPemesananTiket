/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tiket;

import com.tiket.controller.MainController; // Asumsi Anda akan membuat kelas Controller
import com.tiket.database.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Kelas utama yang bertanggung jawab untuk memulai aplikasi.
 */
public class Main {
    
    public static void main(String[] args) {
        
        System.out.println("--- Memulai Sistem Pemesanan Tiket ---");
        
        // 1. UJI KONEKSI DATABASE
        if (checkDatabaseConnection()) {
            System.out.println("Koneksi database berhasil. Aplikasi siap dijalankan.");
            
            // Logika utama aplikasi dimulai dari sini (misalnya, menampilkan menu utama atau GUI)
            // MainController.startApplication(); 
            
        } else {
            System.err.println("Aplikasi dihentikan karena koneksi database gagal.");
        }
    }

    /**
     * Memeriksa koneksi database di awal aplikasi.
     * @return true jika koneksi berhasil, false jika gagal.
     */
    private static boolean checkDatabaseConnection() {
        System.out.print("Status Koneksi Database: ");
        Connection testConn = null;
        try {
            testConn = DBConnection.getConnection();
            System.out.println("BERHASIL");
            return true;
        } catch (SQLException e) {
            System.err.println("GAGAL");
            System.err.println("Detail Error: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeConnection(testConn);
        }
    }
}
