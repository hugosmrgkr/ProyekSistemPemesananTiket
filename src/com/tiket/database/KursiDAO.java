package com.tiket.database;

import com.tiket.exception.DatabaseException;
import com.tiket.model.Kursi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KursiDAO {
    private Connection connection;

    public KursiDAO() throws DatabaseException, SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void tambahKursi(Kursi kursi) throws DatabaseException {
        String sql = "INSERT INTO kursi (id_tiket, nomor_kursi, tersedia) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kursi.getIdTiket());
            stmt.setInt(2, kursi.getNomorKursi());
            stmt.setBoolean(3, kursi.isTersedia());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menambah kursi: " + e.getMessage(), e);
        }
    }

    public void updateStatusKursi(String idTiket, boolean tersedia) throws DatabaseException {
        String sql = "UPDATE kursi SET tersedia=? WHERE id_tiket=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, tersedia);
            stmt.setString(2, idTiket);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal update status kursi: " + e.getMessage(), e);
        }
    }

    public List<Kursi> getKursiTersedia() throws DatabaseException {
        List<Kursi> kursiList = new ArrayList<>();
        String sql = "SELECT * FROM kursi WHERE tersedia=true";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Kursi kursi = new Kursi();
                kursi.setIdTiket(rs.getString("id_tiket"));
                kursi.setNomorKursi(rs.getInt("nomor_kursi"));
                kursi.setTersedia(rs.getBoolean("tersedia"));
                
                kursiList.add(kursi);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil kursi tersedia: " + e.getMessage(), e);
        }
        
        return kursiList;
    }

    public Kursi getKursiById(String idTiket) throws DatabaseException {
        String sql = "SELECT * FROM kursi WHERE id_tiket=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idTiket);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Kursi kursi = new Kursi();
                kursi.setIdTiket(rs.getString("id_tiket"));
                kursi.setNomorKursi(rs.getInt("nomor_kursi"));
                kursi.setTersedia(rs.getBoolean("tersedia"));
                
                return kursi;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil kursi: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    // Method baru untuk mendapatkan kursi berdasarkan jadwal dan nomor
    public Kursi getKursiByJadwalAndNomor(String idJadwal, int nomorKursi) throws DatabaseException {
        // Format: KRS-JDW-XXX-NN
        String idKursi = "KRS-" + idJadwal + "-" + String.format("%02d", nomorKursi);
        return getKursiById(idKursi);
    }
    
    // Method baru untuk mendapatkan kursi tersedia berdasarkan jadwal
    public List<Kursi> getKursiTersediaByJadwal(String idJadwal) throws DatabaseException {
        List<Kursi> kursiList = new ArrayList<>();
        String sql = "SELECT * FROM kursi WHERE id_tiket LIKE ? AND tersedia=true";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "KRS-" + idJadwal + "-%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Kursi kursi = new Kursi();
                kursi.setIdTiket(rs.getString("id_tiket"));
                kursi.setNomorKursi(rs.getInt("nomor_kursi"));
                kursi.setTersedia(rs.getBoolean("tersedia"));
                
                kursiList.add(kursi);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil kursi tersedia: " + e.getMessage(), e);
        }
        
        return kursiList;
    }
    
    // Method baru untuk mendapatkan semua kursi berdasarkan jadwal
    public List<Kursi> getAllKursiByJadwal(String idJadwal) throws DatabaseException {
        List<Kursi> kursiList = new ArrayList<>();
        String sql = "SELECT * FROM kursi WHERE id_tiket LIKE ? ORDER BY nomor_kursi";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "KRS-" + idJadwal + "-%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Kursi kursi = new Kursi();
                kursi.setIdTiket(rs.getString("id_tiket"));
                kursi.setNomorKursi(rs.getInt("nomor_kursi"));
                kursi.setTersedia(rs.getBoolean("tersedia"));
                
                kursiList.add(kursi);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil semua kursi: " + e.getMessage(), e);
        }
        
        return kursiList;
    }

    // Tambahkan method ini di KursiDAO.java

public void hapusKursi(String idTiket) throws DatabaseException {
    String sql = "DELETE FROM kursi WHERE id_tiket=?";
    
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, idTiket);
        stmt.executeUpdate();
    } catch (SQLException e) {
        throw new DatabaseException("Gagal hapus kursi: " + e.getMessage(), e);
    }
}
}