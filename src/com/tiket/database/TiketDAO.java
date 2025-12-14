package com.tiket.database;

import com.tiket.exception.DatabaseException;
import com.tiket.model.Tiket;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TiketDAO {
    private Connection connection;

    public TiketDAO() throws DatabaseException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void tambahTiket(Tiket tiket) throws DatabaseException {
        String sql = "INSERT INTO tiket (id_tiket, nomor_kursi, harga, waktu_struck) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tiket.getIdTiket());
            stmt.setInt(2, tiket.getNomorKursi());
            stmt.setDouble(3, tiket.getHarga());
            stmt.setTimestamp(4, Timestamp.valueOf(tiket.getWaktuStruck()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menambah tiket: " + e.getMessage(), e);
        }
    }

    public Tiket getTiketById(String idTiket) throws DatabaseException {
        String sql = "SELECT * FROM tiket WHERE id_tiket=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idTiket);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Tiket tiket = new Tiket();
                tiket.setIdTiket(rs.getString("id_tiket"));
                tiket.setNomorKursi(rs.getInt("nomor_kursi"));
                tiket.setHarga(rs.getDouble("harga"));
                tiket.setWaktuStruck(rs.getTimestamp("waktu_struck").toLocalDateTime());
                
                return tiket;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil tiket: " + e.getMessage(), e);
        }
        
        return null;
    }

    public List<Tiket> getAllTiket() throws DatabaseException {
        List<Tiket> tiketList = new ArrayList<>();
        String sql = "SELECT * FROM tiket";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Tiket tiket = new Tiket();
                tiket.setIdTiket(rs.getString("id_tiket"));
                tiket.setNomorKursi(rs.getInt("nomor_kursi"));
                tiket.setHarga(rs.getDouble("harga"));
                tiket.setWaktuStruck(rs.getTimestamp("waktu_struck").toLocalDateTime());
                
                tiketList.add(tiket);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil semua tiket: " + e.getMessage(), e);
        }
        
        return tiketList;
    }
}