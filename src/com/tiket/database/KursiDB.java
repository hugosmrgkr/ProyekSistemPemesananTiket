/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tiket.database;

import com.tiket.model.Kursi;
import com.tiket.exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object untuk entitas Kursi.
 * Mengelola ketersediaan kursi berdasarkan Jadwal.
 */
public class KursiDB {
    
    // ============================================================
    // ALIAS METHODS untuk AppController (yang dipanggil controller)
    // ============================================================
    
    /**
     * Alias untuk getKursiByJadwalId() - dipanggil oleh AppController
     */
    public List<Kursi> getByJadwal(String idJadwal) throws DatabaseException {
        return getKursiByJadwalId(idJadwal);
    }
    
    /**
     * Alias untuk updateKetersediaanKursi() - dipanggil oleh AppController
     */
    public boolean updateStatus(String idKursi, boolean tersedia) throws DatabaseException {
        return updateKetersediaanKursi(idKursi, tersedia);
    }
    
    // ============================================================
    // ORIGINAL METHODS (dari code Anda)
    // ============================================================
    
    /**
     * Mengambil daftar kursi berdasarkan ID Jadwal.
     * @param idJadwal ID Jadwal.
     * @return Daftar objek Kursi.
     * @throws DatabaseException Jika terjadi error SQL.
     */
    public List<Kursi> getKursiByJadwalId(String idJadwal) throws DatabaseException {
        List<Kursi> daftarKursi = new ArrayList<>();
        
        String sql = "SELECT idKursi, idJadwal, nomorKursi, tersedia "
                   + "FROM Kursi WHERE idJadwal = ? ORDER BY nomorKursi";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, idJadwal);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Kursi kursi = new Kursi(
    rs.getString("idKursi"),
    rs.getString("idJadwal"),
    rs.getInt("nomorKursi"),
    rs.getBoolean("tersedia")
);
                    daftarKursi.add(kursi);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil kursi untuk Jadwal ID " 
                    + idJadwal + ": " + e.getMessage(), e);
        }
        
        return daftarKursi;
    }
    
    /**
     * Memperbarui status kursi (tersedia atau tidak).
     * @param idKursi ID Kursi.
     * @param tersedia Status baru.
     * @return true jika berhasil.
     * @throws DatabaseException Jika terjadi error SQL.
     */
    public boolean updateKetersediaanKursi(String idKursi, boolean tersedia) throws DatabaseException {
        String sql = "UPDATE Kursi SET tersedia = ? WHERE idKursi = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setBoolean(1, tersedia);
            ps.setString(2, idKursi);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Gagal memperbarui status Kursi ID " 
                    + idKursi + ": " + e.getMessage(), e);
        }
    }
}