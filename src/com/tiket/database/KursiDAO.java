/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tiket.database;

import com.tiket.exception.DatabaseException;
import com.tiket.model.Kursi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KursiDAO {
    private Connection connection;

    public KursiDAO() throws DatabaseException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void tambahKursi(String idJadwal, Kursi kursi) throws DatabaseException {
    String sql = "INSERT INTO kursi (id_jadwal, nomor_kursi, tersedia) VALUES (?, ?, ?)";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, idJadwal);
        stmt.setInt(2, kursi.getNomorKursi());
        stmt.setBoolean(3, true); // selalu true awal
        stmt.executeUpdate();
    } catch (SQLException e) {
        throw new DatabaseException("Gagal menambah kursi", e);
    }
}


    public void updateStatusKursi(
        String idJadwal,
        int nomorKursi,
        boolean tersedia
) throws DatabaseException {
         String sql = """
        UPDATE kursi
        SET tersedia=?
        WHERE id_jadwal=? AND nomor_kursi=?
    """;
        
         try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setBoolean(1, tersedia);
        stmt.setString(2, idJadwal);
        stmt.setInt(3, nomorKursi);
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
}