package com.tiket.database;

import com.tiket.exception.DatabaseException;
import com.tiket.model.Tiket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TiketDAO {
    private Connection connection;

    public TiketDAO() throws DatabaseException, SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // =========================
    // TAMBAH TIKET
    // =========================
    public void tambahTiket(Tiket tiket) throws DatabaseException {
        String sql = """
            INSERT INTO tiket 
            (id_tiket, id_jadwal, id_kursi, nomor_kursi, harga, waktu_struk)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tiket.getIdTiket());
            stmt.setString(2, tiket.getIdJadwal());
            stmt.setString(3, tiket.getIdKursi());   // ✅ FIX UTAMA
            stmt.setInt(4, tiket.getNomorKursi());
            stmt.setDouble(5, tiket.getHarga());
            stmt.setTimestamp(6, Timestamp.valueOf(tiket.getWaktuStruk()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menambah tiket: " + e.getMessage(), e);
        }
    }

    // =========================
    // GET TIKET BY ID
    // =========================
    public Tiket getTiketById(String idTiket) throws DatabaseException {
        String sql = "SELECT * FROM tiket WHERE id_tiket=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idTiket);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Tiket tiket = new Tiket();
                tiket.setIdTiket(rs.getString("id_tiket"));
                tiket.setIdJadwal(rs.getString("id_jadwal"));
                tiket.setIdKursi(rs.getString("id_kursi")); // ✅
                tiket.setNomorKursi(rs.getInt("nomor_kursi"));
                tiket.setHarga(rs.getDouble("harga"));
                tiket.setWaktuStruk(
                        rs.getTimestamp("waktu_struk").toLocalDateTime()
                );
                return tiket;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil tiket: " + e.getMessage(), e);
        }

        return null;
    }

    // =========================
    // GET SEMUA TIKET
    // =========================
    public List<Tiket> getAllTiket() throws DatabaseException {
        List<Tiket> tiketList = new ArrayList<>();
        String sql = "SELECT * FROM tiket";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Tiket tiket = new Tiket();
                tiket.setIdTiket(rs.getString("id_tiket"));
                tiket.setIdJadwal(rs.getString("id_jadwal"));
                tiket.setIdKursi(rs.getString("id_kursi")); // ✅
                tiket.setNomorKursi(rs.getInt("nomor_kursi"));
                tiket.setHarga(rs.getDouble("harga"));
                tiket.setWaktuStruk(
                        rs.getTimestamp("waktu_struk").toLocalDateTime()
                );
                tiketList.add(tiket);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil semua tiket: " + e.getMessage(), e);
        }

        return tiketList;
    }
}
