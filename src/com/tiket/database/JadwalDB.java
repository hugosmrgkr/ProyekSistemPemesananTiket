package com.tiket.database;

import com.tiket.model.Jadwal;
import com.tiket.exception.DatabaseException;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class JadwalDB {

    // Alias methods
    public List<Jadwal> getAll() throws DatabaseException { return getAllJadwal(); }
    public boolean create(Jadwal jadwal) throws DatabaseException { return insertJadwal(jadwal); }
    public boolean update(Jadwal jadwal) throws DatabaseException { return updateJadwal(jadwal); }
    public boolean delete(String idJadwal) throws DatabaseException { return deleteJadwal(idJadwal); }

    // -------------------------------------------------------
    // SEARCH
    // -------------------------------------------------------
    public List<Jadwal> search(String asal, String tujuan, String tanggal) throws DatabaseException {
        List<Jadwal> daftarJadwal = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM Jadwal WHERE 1=1");

        if (asal != null && !asal.isEmpty()) sql.append(" AND lokasiAsal LIKE ?");
        if (tujuan != null && !tujuan.isEmpty()) sql.append(" AND lokasiTujuan LIKE ?");
        if (tanggal != null && !tanggal.isEmpty()) sql.append(" AND DATE(waktuBerangkat) = ?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (asal != null && !asal.isEmpty()) ps.setString(index++, "%" + asal + "%");
            if (tujuan != null && !tujuan.isEmpty()) ps.setString(index++, "%" + tujuan + "%");
            if (tanggal != null && !tanggal.isEmpty()) ps.setString(index++, tanggal);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) daftarJadwal.add(extractJadwal(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Gagal mencari jadwal: " + e.getMessage(), e);
        }
        return daftarJadwal;
    }

    // -------------------------------------------------------
    // GET ALL
    // -------------------------------------------------------
    public List<Jadwal> getAllJadwal() throws DatabaseException {
        List<Jadwal> list = new ArrayList<>();
        String sql = "SELECT * FROM Jadwal";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(extractJadwal(rs));

        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil jadwal: " + e.getMessage(), e);
        }
        return list;
    }

    // -------------------------------------------------------
    // INSERT
    // -------------------------------------------------------
    public boolean insertJadwal(Jadwal jadwal) throws DatabaseException {
        String sql = "INSERT INTO Jadwal (idJadwal, idBus, lokasiAsal, lokasiTujuan, waktuBerangkat, waktuTiba, harga) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            LocalDateTime berangkat = LocalDateTime.parse(jadwal.getWaktuBerangkat(), format);
            LocalDateTime tiba = LocalDateTime.parse(jadwal.getWaktuTiba(), format);

            ps.setString(1, jadwal.getIdJadwal());
            ps.setString(2, jadwal.getIdBus());
            ps.setString(3, jadwal.getLokasiAsal());
            ps.setString(4, jadwal.getLokasiTujuan());
            ps.setTimestamp(5, Timestamp.valueOf(berangkat));
            ps.setTimestamp(6, Timestamp.valueOf(tiba));
            ps.setDouble(7, jadwal.getHarga());

            return ps.executeUpdate() > 0;

        } catch (DateTimeParseException e) {
            throw new DatabaseException(
                    "Format tanggal salah. Gunakan: dd-MM-yyyy HH:mm", e);
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menambahkan jadwal: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------
    public boolean updateJadwal(Jadwal jadwal) throws DatabaseException {
        String sql = "UPDATE Jadwal SET idBus=?, lokasiAsal=?, lokasiTujuan=?, waktuBerangkat=?, waktuTiba=?, harga=? "
                   + "WHERE idJadwal=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            LocalDateTime berangkat = LocalDateTime.parse(jadwal.getWaktuBerangkat(), format);
            LocalDateTime tiba = LocalDateTime.parse(jadwal.getWaktuTiba(), format);

            ps.setString(1, jadwal.getIdBus());
            ps.setString(2, jadwal.getLokasiAsal());
            ps.setString(3, jadwal.getLokasiTujuan());
            ps.setTimestamp(4, Timestamp.valueOf(berangkat));
            ps.setTimestamp(5, Timestamp.valueOf(tiba));
            ps.setDouble(6, jadwal.getHarga());
            ps.setString(7, jadwal.getIdJadwal());

            return ps.executeUpdate() > 0;

        } catch (DateTimeParseException e) {
            throw new DatabaseException("Format tanggal salah. Gunakan: dd-MM-yyyy HH:mm", e);
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengupdate jadwal: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------
    // DELETE
    // -------------------------------------------------------
    public boolean deleteJadwal(String idJadwal) throws DatabaseException {
        String sql = "DELETE FROM Jadwal WHERE idJadwal=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idJadwal);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Gagal menghapus jadwal: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------
    // Helper untuk mapping ResultSet → Object
    // -------------------------------------------------------
    private Jadwal extractJadwal(ResultSet rs) throws SQLException {
        return new Jadwal(
                rs.getString("idJadwal"),
                rs.getString("idBus"),
                rs.getString("lokasiAsal"),
                rs.getString("lokasiTujuan"),
                rs.getTimestamp("waktuBerangkat").toLocalDateTime(),
                rs.getTimestamp("waktuTiba").toLocalDateTime(),
                rs.getDouble("harga")
        );
    }
}
