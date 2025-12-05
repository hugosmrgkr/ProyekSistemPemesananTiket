package com.tiket.database;

import com.tiket.model.Tiket;
import com.tiket.exception.DatabaseException;
import com.tiket.exception.TiketNotFoundException;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * Data Access Object untuk entitas Tiket.
 */
public class TiketDB {
    
    /**
     * Method untuk AppController (alias untuk saveTiket)
     */
    public boolean create(Tiket tiket) throws DatabaseException {
        return saveTiket(tiket);
    }
    
    /**
     * Simpan tiket ke database
     */
    public boolean saveTiket(Tiket tiket) throws DatabaseException {
        String sql = "INSERT INTO Tiket (" +
                "idTiket, idJadwal, idPelanggan, namaPelanggan, teleponPelanggan, emailPelanggan, " +
                "nomorKursi, harga, waktuPemesanan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, tiket.getIdTiket());
                ps.setString(2, tiket.getIdJadwal());
                ps.setString(3, tiket.getIdPelanggan());
                ps.setString(4, tiket.getNamaPelanggan());
                ps.setString(5, tiket.getTeleponPelanggan());
                ps.setString(6, tiket.getEmailPelanggan());
                ps.setInt(7, tiket.getNomorKursi());
                ps.setDouble(8, tiket.getHarga());
                ps.setTimestamp(9, Timestamp.valueOf(tiket.getWaktuPemesanan()));
                
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            try { 
                if (conn != null) conn.rollback(); 
            } catch (SQLException ex) {}
            throw new DatabaseException("Gagal menyimpan tiket: " + e.getMessage(), e);
        } finally {
            try { 
                if (conn != null) conn.setAutoCommit(true); 
            } catch (SQLException ignore) {}
            DBConnection.closeConnection(conn);
        }
    }
    
    /**
     * Ambil tiket berdasarkan ID
     */
    public Tiket getTiketById(String idTiket) 
            throws DatabaseException, TiketNotFoundException {
        String sql = "SELECT * FROM Tiket WHERE idTiket = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, idTiket);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new TiketNotFoundException("Tiket dengan ID " + idTiket + " tidak ditemukan.");
                }
                
                Tiket t = new Tiket();
                t.setIdTiket(rs.getString("idTiket"));
                t.setIdJadwal(rs.getString("idJadwal"));
                t.setIdPelanggan(rs.getString("idPelanggan"));
                t.setNamaPelanggan(rs.getString("namaPelanggan"));
                t.setTeleponPelanggan(rs.getString("teleponPelanggan"));
                t.setEmailPelanggan(rs.getString("emailPelanggan"));
                t.setNomorKursi(rs.getInt("nomorKursi"));
                t.setHarga(rs.getDouble("harga"));
                
                Timestamp ts = rs.getTimestamp("waktuPemesanan");
                t.setWaktuPemesanan(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());
                
                return t;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil tiket: " + e.getMessage(), e);
        }
    }
}