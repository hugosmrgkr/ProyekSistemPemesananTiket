package com.tiket.database;

import com.tiket.exception.DatabaseException;
import com.tiket.model.Jadwal;
import com.tiket.model.Kursi;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JadwalDAO {
    private Connection connection;

    public JadwalDAO() throws DatabaseException, SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void tambahJadwal(Jadwal jadwal) throws DatabaseException {
        String sql = "INSERT INTO jadwal (id_jadwal, lokasi_asal, lokasi_tujuan, waktu_berangkat, waktu_tiba, harga) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, jadwal.getIdJadwal());
            stmt.setString(2, jadwal.getLokasiAsal());
            stmt.setString(3, jadwal.getLokasiTujuan());
            stmt.setTimestamp(4, Timestamp.valueOf(jadwal.getWaktuBerangkat()));
            stmt.setTimestamp(5, Timestamp.valueOf(jadwal.getWaktuTiba()));
            stmt.setDouble(6, jadwal.getHarga());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menambah jadwal: " + e.getMessage(), e);
        }
    }

    public void updateJadwal(Jadwal jadwal) throws DatabaseException {
        String sql = "UPDATE jadwal SET lokasi_asal=?, lokasi_tujuan=?, waktu_berangkat=?, waktu_tiba=?, harga=? WHERE id_jadwal=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, jadwal.getLokasiAsal());
            stmt.setString(2, jadwal.getLokasiTujuan());
            stmt.setTimestamp(3, Timestamp.valueOf(jadwal.getWaktuBerangkat()));
            stmt.setTimestamp(4, Timestamp.valueOf(jadwal.getWaktuTiba()));
            stmt.setDouble(5, jadwal.getHarga());
            stmt.setString(6, jadwal.getIdJadwal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal update jadwal: " + e.getMessage(), e);
        }
    }

    public void hapusJadwal(String idJadwal) throws DatabaseException {
        String sql = "DELETE FROM jadwal WHERE id_jadwal=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idJadwal);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal hapus jadwal: " + e.getMessage(), e);
        }
    }

    public List<Jadwal> getAllJadwal() throws DatabaseException {
        List<Jadwal> jadwalList = new ArrayList<>();
        String sql = "SELECT * FROM jadwal";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Jadwal jadwal = new Jadwal();
                jadwal.setIdJadwal(rs.getString("id_jadwal"));
                jadwal.setLokasiAsal(rs.getString("lokasi_asal"));
                jadwal.setLokasiTujuan(rs.getString("lokasi_tujuan"));
                jadwal.setWaktuBerangkat(rs.getTimestamp("waktu_berangkat").toLocalDateTime());
                jadwal.setWaktuTiba(rs.getTimestamp("waktu_tiba").toLocalDateTime());
                jadwal.setHarga(rs.getDouble("harga"));
                
                jadwalList.add(jadwal);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil semua jadwal: " + e.getMessage(), e);
        }
        
        return jadwalList;
    }

    public Jadwal getJadwalById(String idJadwal) throws DatabaseException {
        String sql = "SELECT * FROM jadwal WHERE id_jadwal=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idJadwal);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Jadwal jadwal = new Jadwal();
                jadwal.setIdJadwal(rs.getString("id_jadwal"));
                jadwal.setLokasiAsal(rs.getString("lokasi_asal"));
                jadwal.setLokasiTujuan(rs.getString("lokasi_tujuan"));
                jadwal.setWaktuBerangkat(rs.getTimestamp("waktu_berangkat").toLocalDateTime());
                jadwal.setWaktuTiba(rs.getTimestamp("waktu_tiba").toLocalDateTime());
                jadwal.setHarga(rs.getDouble("harga"));
                
                return jadwal;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil jadwal: " + e.getMessage(), e);
        }
        
        return null;
    }

    public List<Jadwal> cariJadwal(String lokasiAsal, String lokasiTujuan) throws DatabaseException {
        List<Jadwal> jadwalList = new ArrayList<>();
        String sql = "SELECT * FROM jadwal WHERE lokasi_asal LIKE ? AND lokasi_tujuan LIKE ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + lokasiAsal + "%");
            stmt.setString(2, "%" + lokasiTujuan + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Jadwal jadwal = new Jadwal();
                jadwal.setIdJadwal(rs.getString("id_jadwal"));
                jadwal.setLokasiAsal(rs.getString("lokasi_asal"));
                jadwal.setLokasiTujuan(rs.getString("lokasi_tujuan"));
                jadwal.setWaktuBerangkat(rs.getTimestamp("waktu_berangkat").toLocalDateTime());
                jadwal.setWaktuTiba(rs.getTimestamp("waktu_tiba").toLocalDateTime());
                jadwal.setHarga(rs.getDouble("harga"));
                
                jadwalList.add(jadwal);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mencari jadwal: " + e.getMessage(), e);
        }
        
        return jadwalList;
    }
}