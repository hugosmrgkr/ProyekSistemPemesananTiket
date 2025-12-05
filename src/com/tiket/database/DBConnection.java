/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tiket.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // Konfigurasi koneksi database
    private static final String URL = "jdbc:mysql://localhost:3307/db_pemesanan_tiket"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "";

    /**
     * Mendapatkan objek koneksi ke database.
     * @return 
     * @throws SQLException jika terjadi error koneksi atau driver tidak ditemukan.
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver database tidak tersedia.", e);
        }
        return connection;
    }

    /**
     * Menutup objek koneksi.
     * @param connection Koneksi yang akan ditutup.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Mencatat (logging) error saat menutup koneksi, tapi tidak menghentikan program
                System.err.println("Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }
}
