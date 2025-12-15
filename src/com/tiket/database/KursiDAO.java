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

    // ==============================
    // TAMBAH KURSI
    // ==============================
    public void tambahKursi(Kursi kursi) throws DatabaseException {
        String sql = """
            INSERT INTO kursi (id_kursi, id_jadwal, nomor_kursi, tersedia)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kursi.getIdKursi());
            stmt.setString(2, kursi.getIdJadwal());
            stmt.setInt(3, kursi.getNomorKursi());
            stmt.setBoolean(4, kursi.isTersedia());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menambah kursi", e);
        }
    }

    // ==============================
    // UPDATE STATUS KURSI (BY ID_KURSI)
    // ==============================
    public void updateStatusKursi(String idKursi, boolean tersedia) throws DatabaseException {
        String sql = "UPDATE kursi SET tersedia=? WHERE id_kursi=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, tersedia);
            stmt.setString(2, idKursi);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal update status kursi", e);
        }
    }

    // ==============================
    // AMBIL KURSI BY ID
    // ==============================
    public Kursi getKursiById(String idKursi) throws DatabaseException {
        String sql = "SELECT * FROM kursi WHERE id_kursi=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idKursi);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil kursi", e);
        }
        return null;
    }

    // ==============================
    // AMBIL KURSI BY JADWAL + NOMOR
    // ==============================
    public Kursi getKursiByJadwalAndNomor(String idJadwal, int nomorKursi) throws DatabaseException {
        String sql = """
            SELECT * FROM kursi
            WHERE id_jadwal=? AND nomor_kursi=?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idJadwal);
            stmt.setInt(2, nomorKursi);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil kursi by jadwal & nomor", e);
        }
        return null;
    }

    // ==============================
    // UPDATE STATUS KURSI (BY JADWAL + NOMOR)
    // ==============================
    public void updateStatusKursi(String idJadwal, int nomorKursi, boolean tersedia)
            throws DatabaseException {

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
            throw new DatabaseException("Gagal update status kursi", e);
        }
    }

    // ==============================
    // KURSI TERSEDIA BY JADWAL
    // ==============================
    public List<Kursi> getKursiTersediaByJadwal(String idJadwal) throws DatabaseException {
        String sql = """
            SELECT * FROM kursi
            WHERE id_jadwal=? AND tersedia=true
            ORDER BY nomor_kursi
        """;

        List<Kursi> list = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idJadwal);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil kursi tersedia", e);
        }
        return list;
    }

    // ==============================
    // SEMUA KURSI BY JADWAL
    // ==============================
    public List<Kursi> getAllKursiByJadwal(String idJadwal) throws DatabaseException {
        String sql = """
            SELECT * FROM kursi
            WHERE id_jadwal=?
            ORDER BY nomor_kursi
        """;

        List<Kursi> list = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idJadwal);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mengambil semua kursi", e);
        }
        return list;
    }

    // ==============================
    // HAPUS KURSI
    // ==============================
    public void hapusKursi(String idKursi) throws DatabaseException {
        String sql = "DELETE FROM kursi WHERE id_kursi=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idKursi);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal hapus kursi", e);
        }
    }

    // ==============================
    // MAPPING RESULTSET
    // ==============================
    private Kursi mapResultSet(ResultSet rs) throws SQLException {
        Kursi kursi = new Kursi();
        kursi.setIdKursi(rs.getString("id_kursi"));
        kursi.setIdJadwal(rs.getString("id_jadwal"));
        kursi.setNomorKursi(rs.getInt("nomor_kursi"));
        kursi.setTersedia(rs.getBoolean("tersedia"));
        return kursi;
    }
}
