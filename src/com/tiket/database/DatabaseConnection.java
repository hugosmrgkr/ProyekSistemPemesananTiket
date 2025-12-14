package com.tiket.database;

import com.tiket.exception.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3307/db_pemesanan_tiket?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";      // nanti kita ganti ke tiket_user
    private static final String PASSWORD = "";      // sesuai kondisi sekarang

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws DatabaseException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseException("Koneksi database gagal: " + e.getMessage(), e);
        }
    }

    public static DatabaseConnection getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws DatabaseException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mendapatkan koneksi database", e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================== METHOD TEST (INI YANG KITA JALANKAN) ==================
    public static void testConnection() {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            if (db.getConnection() != null) {
                System.out.println("✅ KONEKSI DATABASE BERHASIL");
            }
        } catch (DatabaseException e) {
            System.out.println("❌ KONEKSI DATABASE GAGAL");
            System.out.println(e.getMessage());
        }
    }

    // ================== MAIN KHUSUS TEST ==================
    public static void main(String[] args) {
        System.out.println("=== TEST KONEKSI DATABASE ===");
        testConnection();
    }
}
