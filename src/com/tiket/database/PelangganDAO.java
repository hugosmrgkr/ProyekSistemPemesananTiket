package com.tiket.database;

import com.tiket.exception.DatabaseException;
import com.tiket.model.Pelanggan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PelangganDAO {
    private Connection connection;

    public PelangganDAO() throws DatabaseException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void tambahPelanggan(Pelanggan pelanggan) throws DatabaseException {
        String sql = "INSERT INTO pelanggan (id_pelanggan, nama, email, telepon) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pelanggan.getIdPelanggan());
            stmt.setString(2, pelanggan.getNama());
            stmt.setString(3, pelanggan.getEmail());
            stmt.setString(4, pelanggan.getTelepon());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menambah pelanggan: " + e.getMessage(), e);
        }
    }

    public Pelanggan getPelangganById(String idPelanggan) throws DatabaseException {
        String sql = "SELECT * FROM pelanggan WHERE id_pelanggan=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idPelanggan);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Pelanggan pelanggan = new Pelanggan();
                pelanggan.setIdPelanggan(rs.getString("id_pelanggan"));
                pelanggan.setNama(rs.getString("nama"));
                pelanggan.setEmail(rs.getString("email"));
                pelanggan.setTelepon(rs.getString("telepon"));
                
                return pelanggan;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil pelanggan: " + e.getMessage(), e);
        }
        
        return null;
    }
}