package com.tiket.controller;

import com.tiket.database.*;
import com.tiket.exception.*;
import com.tiket.helper.Helper;
import com.tiket.model.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PemesananController {

    private TiketDAO tiketDAO;
    private KursiDAO kursiDAO;
    private TransaksiDAO transaksiDAO;
    private PelangganDAO pelangganDAO;
    private JadwalDAO jadwalDAO;

    public PemesananController() throws DatabaseException, SQLException {
        this.tiketDAO = new TiketDAO();
        this.kursiDAO = new KursiDAO();
        this.transaksiDAO = new TransaksiDAO();
        this.pelangganDAO = new PelangganDAO();
        this.jadwalDAO = new JadwalDAO();
    }

    // =========================
    // CEK KURSI TERSEDIA
    // =========================
    public boolean isKursiTersedia(String idJadwal, int nomorKursi) throws DatabaseException {
        Kursi kursi = kursiDAO.getKursiByJadwalAndNomor(idJadwal, nomorKursi);
        return kursi == null || kursi.isTersedia();
    }

    public List<Kursi> getKursiTersediaByJadwal(String idJadwal) throws DatabaseException {
        return kursiDAO.getKursiTersediaByJadwal(idJadwal);
    }

    public List<Kursi> getAllKursiByJadwal(String idJadwal) throws DatabaseException {
        return kursiDAO.getAllKursiByJadwal(idJadwal);
    }

    // =========================
    // PESAN TIKET
    // =========================
    public Tiket pesanTiket(String idJadwal, int nomorKursi, Pelanggan pelanggan)
            throws DatabaseException, KursiTidakTersediaException {

        if (nomorKursi < 1 || nomorKursi > 40) {
            throw new KursiTidakTersediaException("Nomor kursi harus 1 - 40");
        }

        if (!isKursiTersedia(idJadwal, nomorKursi)) {
            throw new KursiTidakTersediaException(
                    "Kursi nomor " + nomorKursi + " sudah dipesan");
        }

        Jadwal jadwal = jadwalDAO.getJadwalById(idJadwal);
        if (jadwal == null) {
            throw new DatabaseException("Jadwal tidak ditemukan");
        }

        // ===== KURSI =====
        Kursi kursi = kursiDAO.getKursiByJadwalAndNomor(idJadwal, nomorKursi);

        if (kursi == null) {
            kursi = new Kursi();
            kursi.setIdJadwal(idJadwal);
            kursi.setNomorKursi(nomorKursi);
            kursi.setTersedia(false);
            kursiDAO.tambahKursi(kursi);
        } else {
            kursiDAO.updateStatusKursi(idJadwal, nomorKursi, false);
        }

            // ===== TIKET =====
            String idTiket = Helper.generateTiketId();
            String idKursi = kursi.getIdKursi(); // PENTING

            Tiket tiket = new Tiket(
                    idTiket,
                    idJadwal,
                    idKursi,
                    nomorKursi,
                    jadwal.getHarga(),
                    LocalDateTime.now()
            );

tiketDAO.tambahTiket(tiket);

        // ===== PELANGGAN =====
        Pelanggan existing = pelangganDAO.getPelangganById(pelanggan.getIdPelanggan());
        if (existing == null) {
            pelangganDAO.tambahPelanggan(pelanggan);
        }

        pelangganDAO.linkPelangganToTiket(pelanggan.getIdPelanggan(), idTiket);

        return tiket;
    }

    // =========================
    // BATALKAN TIKET
    // =========================
    public boolean batalkanTiket(String idTiket) throws DatabaseException {

        Tiket tiket = tiketDAO.getTiketById(idTiket);
        if (tiket == null) {
            throw new DatabaseException("Tiket tidak ditemukan");
        }

        kursiDAO.updateStatusKursi(
                tiket.getIdJadwal(),
                tiket.getNomorKursi(),
                true
        );

        return true;
    }

    // =========================
    // TRANSAKSI
    // =========================
    public Transaksi buatTransaksi(
            Tiket tiket,
            String metodePembayaran,
            Pelanggan pelanggan) throws DatabaseException {

        String idTransaksi = Helper.generateTransaksiId();

        Transaksi transaksi = new Transaksi(
                idTransaksi,
                tiket.getHarga(),
                metodePembayaran
        );

        transaksi.mencakupTiket(tiket);
        transaksiDAO.tambahTransaksi(transaksi);
        transaksiDAO.linkTransaksiToTiket(idTransaksi, tiket.getIdTiket());

        return transaksi;
    }

    public boolean bayarTransaksi(String idTransaksi)
            throws DatabaseException, TransaksiException {

        Transaksi transaksi = transaksiDAO.getTransaksiById(idTransaksi);

        if (transaksi == null) {
            throw new TransaksiException("Transaksi tidak ditemukan");
        }

        if (transaksi.isBayar()) {
            throw new TransaksiException("Transaksi sudah dibayar");
        }

        transaksi.bayarTransaksi();
        transaksiDAO.updateTransaksi(transaksi);
        return true;
    }

    // =========================
    // CETAK STRUK
    // =========================
    public String cetakStrukSederhana(String idTiket) throws DatabaseException {

        Tiket tiket = tiketDAO.getTiketById(idTiket);
        if (tiket == null) {
            return "Tiket tidak ditemukan";
        }

        String idCetak = Helper.generateStrukId();
        PencetakStruck pencetak = new PencetakStruck(idCetak, tiket);

        return pencetak.cetak();
    }
}
