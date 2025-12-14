package com.tiket.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3307/db_pemesanan_tiket?useSSL=false&serverTimezone=UTC";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    private static DatabaseConnection instance;
    private Connection connection;

    // ================= CONSTRUCTOR =================
    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    DB_URL, DB_USERNAME, DB_PASSWORD
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver tidak ditemukan", e);
        }
    }

    // ================= SINGLETON =================
    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.connection == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // ================= GET CONNECTION =================
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    DB_URL, DB_USERNAME, DB_PASSWORD
            );
        }
        return connection;
    }

    // ================= CLOSE CONNECTION =================
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Gagal menutup koneksi DB: " + e.getMessage());
        }
    }

    // ================= METHOD TEST =================
    public static void main(String[] args) {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            Connection conn = db.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ KONEKSI DATABASE BERHASIL!");
                System.out.println("🔗 DB URL  : " + DB_URL);
                System.out.println("👤 User   : " + DB_USERNAME);
            } else {
                System.out.println("❌ KONEKSI DATABASE GAGAL!");
            }

        } catch (SQLException e) {
            System.out.println("❌ ERROR SAAT KONEKSI DATABASE");
            e.printStackTrace();
        }
    }
}
