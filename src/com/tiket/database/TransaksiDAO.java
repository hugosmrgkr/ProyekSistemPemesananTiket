package com.tiket.database;

import com.tiket.exception.DatabaseException;
import com.tiket.model.Transaksi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaksiDAO {
    private Connection connection;

    public TransaksiDAO() throws DatabaseException, SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void tambahTransaksi(Transaksi transaksi) throws DatabaseException {
        String sql = "INSERT INTO transaksi (id_transaksi, jumlah, metode_pembayaran, status, bayar, refund) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, transaksi.getIdTransaksi());
            stmt.setDouble(2, transaksi.getJumlah());
            stmt.setString(3, transaksi.getMetodePembayaran());
            stmt.setString(4, transaksi.getStatus());
            stmt.setBoolean(5, transaksi.isBayar());
            stmt.setBoolean(6, transaksi.isRefund());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menambah transaksi: " + e.getMessage(), e);
        }
    }

    public void updateTransaksi(Transaksi transaksi) throws DatabaseException {
        String sql = "UPDATE transaksi SET jumlah=?, metode_pembayaran=?, status=?, bayar=?, refund=? WHERE id_transaksi=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, transaksi.getJumlah());
            stmt.setString(2, transaksi.getMetodePembayaran());
            stmt.setString(3, transaksi.getStatus());
            stmt.setBoolean(4, transaksi.isBayar());
            stmt.setBoolean(5, transaksi.isRefund());
            stmt.setString(6, transaksi.getIdTransaksi());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal update transaksi: " + e.getMessage(), e);
        }
    }

    public Transaksi getTransaksiById(String idTransaksi) throws DatabaseException {
        String sql = "SELECT * FROM transaksi WHERE id_transaksi=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idTransaksi);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Transaksi transaksi = new Transaksi();
                transaksi.setIdTransaksi(rs.getString("id_transaksi"));
                transaksi.setJumlah(rs.getDouble("jumlah"));
                transaksi.setMetodePembayaran(rs.getString("metode_pembayaran"));
                transaksi.setStatus(rs.getString("status"));
                transaksi.setBayar(rs.getBoolean("bayar"));
                transaksi.setRefund(rs.getBoolean("refund"));
                
                return transaksi;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil transaksi: " + e.getMessage(), e);
        }
        
        return null;
    }

    public List<Transaksi> getAllTransaksi() throws DatabaseException {
        List<Transaksi> transaksiList = new ArrayList<>();
        String sql = "SELECT * FROM transaksi";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Transaksi transaksi = new Transaksi();
                transaksi.setIdTransaksi(rs.getString("id_transaksi"));
                transaksi.setJumlah(rs.getDouble("jumlah"));
                transaksi.setMetodePembayaran(rs.getString("metode_pembayaran"));
                transaksi.setStatus(rs.getString("status"));
                transaksi.setBayar(rs.getBoolean("bayar"));
                transaksi.setRefund(rs.getBoolean("refund"));
                
                transaksiList.add(transaksi);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil semua transaksi: " + e.getMessage(), e);
        }
        
        return transaksiList;
    }

    // Tambahkan method ini di TransaksiDAO.java

public void linkTransaksiToTiket(String idTransaksi, String idTiket) throws DatabaseException {
    String sql = "INSERT INTO transaksi_tiket (transaksi_id, tiket_id) VALUES (?, ?)";
    
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, idTransaksi);
        stmt.setString(2, idTiket);
        stmt.executeUpdate();
    } catch (SQLException e) {
        throw new DatabaseException("Gagal link transaksi ke tiket: " + e.getMessage(), e);
    }
}
}