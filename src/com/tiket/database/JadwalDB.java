/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.database;

import com.tiket.model.Jadwal;
import com.tiket.exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO untuk entitas Jadwal.
 * Menangani operasi CRUD pada tabel Jadwal.
 */
public class JadwalDB {

    public List<Jadwal> getAllJadwal() throws DatabaseException {
        List<Jadwal> daftarJadwal = new ArrayList<>();

        String sql = "SELECT * FROM Jadwal";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Jadwal j = new Jadwal(
                        rs.getString("idJadwal"),
                        rs.getString("idBus"),   // sesuai model Jadwal
                        rs.getString("lokasiAsal"),
                        rs.getString("lokasiTujuan"),
                        rs.getTimestamp("waktuBerangkat").toLocalDateTime(),
                        rs.getTimestamp("waktuTiba").toLocalDateTime(),
                        rs.getDouble("harga")
                );

                daftarJadwal.add(j);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil jadwal: " + e.getMessage(), e);
        }

        return daftarJadwal;
    }


    public boolean insertJadwal(Jadwal jadwal) throws DatabaseException {

        String sql = "INSERT INTO Jadwal "
                + "(idJadwal, idBus, lokasiAsal, lokasiTujuan, waktuBerangkat, waktuTiba, harga) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, jadwal.getIdJadwal());
            ps.setString(2, jadwal.getIdBus());
            ps.setString(3, jadwal.getLokasiAsal());
            ps.setString(4, jadwal.getLokasiTujuan());
            ps.setTimestamp(5, Timestamp.valueOf(jadwal.getWaktuBerangkat()));
            ps.setTimestamp(6, Timestamp.valueOf(jadwal.getWaktuTiba()));
            ps.setDouble(7, jadwal.getHarga());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Gagal menambahkan jadwal: " + e.getMessage(), e);
        }
    }

    public boolean updateJadwal(Jadwal jadwal) throws DatabaseException {

        String sql = "UPDATE Jadwal SET "
                + "idBus = ?, lokasiAsal = ?, lokasiTujuan = ?, "
                + "waktuBerangkat = ?, waktuTiba = ?, harga = ? "
                + "WHERE idJadwal = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, jadwal.getIdBus());
            ps.setString(2, jadwal.getLokasiAsal());
            ps.setString(3, jadwal.getLokasiTujuan());
            ps.setTimestamp(4, Timestamp.valueOf(jadwal.getWaktuBerangkat()));
            ps.setTimestamp(5, Timestamp.valueOf(jadwal.getWaktuTiba()));
            ps.setDouble(6, jadwal.getHarga());
            ps.setString(7, jadwal.getIdJadwal());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengupdate jadwal: " + e.getMessage(), e);
        }
    }

    public boolean deleteJadwal(String idJadwal) throws DatabaseException {

        String sql = "DELETE FROM Jadwal WHERE idJadwal = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idJadwal);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Gagal menghapus jadwal: " + e.getMessage(), e);
        }
    }
}
