package com.tiket.controller;

import com.tiket.database.*;
import com.tiket.exception.*;
import com.tiket.helper.Helper;
import com.tiket.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class PemesananController {
    private TiketDAO tiketDAO;
    private KursiDAO kursiDAO;
    private TransaksiDAO transaksiDAO;
    private PelangganDAO pelangganDAO;
    private JadwalDAO jadwalDAO;

    public PemesananController() throws DatabaseException {
        this.tiketDAO = new TiketDAO();
        this.kursiDAO = new KursiDAO();
        this.transaksiDAO = new TransaksiDAO();
        this.pelangganDAO = new PelangganDAO();
        this.jadwalDAO = new JadwalDAO();
    }

    /**
     * Cek ketersediaan kursi untuk jadwal tertentu
     */
    public boolean isKursiTersedia(String idJadwal, int nomorKursi) throws DatabaseException {
        Kursi kursi = kursiDAO.getKursiByJadwalAndNomor(idJadwal, nomorKursi);
        return kursi == null || kursi.isTersedia();
    }
    
    /**
     * Dapatkan daftar kursi tersedia untuk jadwal tertentu
     */
    public List<Kursi> getKursiTersediaByJadwal(String idJadwal) throws DatabaseException {
        return kursiDAO.getKursiTersediaByJadwal(idJadwal);
    }
    
    /**
     * Dapatkan semua kursi untuk jadwal tertentu (tersedia & tidak tersedia)
     */
    public List<Kursi> getAllKursiByJadwal(String idJadwal) throws DatabaseException {
        return kursiDAO.getAllKursiByJadwal(idJadwal);
    }

    /**
     * Pesan tiket dengan validasi kursi
     */
    public Tiket pesanTiket(String idJadwal, int nomorKursi, Pelanggan pelanggan) 
            throws DatabaseException, KursiTidakTersediaException {
        
        // Validasi nomor kursi
        if (nomorKursi < 1 || nomorKursi > 40) {
            throw new KursiTidakTersediaException("Nomor kursi harus antara 1-40");
        }
        
        // Cek apakah kursi tersedia
        if (!isKursiTersedia(idJadwal, nomorKursi)) {
            throw new KursiTidakTersediaException("Kursi nomor " + nomorKursi + " sudah dipesan");
        }
        
        // Dapatkan jadwal
        Jadwal jadwal = jadwalDAO.getJadwalById(idJadwal);
        if (jadwal == null) {
            throw new DatabaseException("Jadwal tidak ditemukan");
        }
        
        // Generate ID tiket
        String idTiket = Helper.generateTiketId();
        
        // Cek apakah kursi sudah ada di database
        String idKursi = Helper.generateKursiId(idJadwal, nomorKursi);
        Kursi kursiExisting = kursiDAO.getKursiById(idKursi);
        
        if (kursiExisting == null) {
            // Jika kursi belum ada, buat baru
            Kursi kursi = new Kursi(idKursi, nomorKursi);
            kursi.setTersedia(true);
            kursiDAO.tambahKursi(kursi);
        }
        
        // Buat tiket
        Tiket tiket = new Tiket(idTiket, nomorKursi, jadwal.getHarga(), LocalDateTime.now());
        tiketDAO.tambahTiket(tiket);
        
        // Update status kursi menjadi tidak tersedia
        kursiDAO.updateStatusKursi(idKursi, false);
        
        // Hubungkan tiket dengan jadwal
        tiketDAO.linkTiketToJadwal(idTiket, idJadwal);
        
        // Simpan pelanggan jika belum ada
        Pelanggan existingPelanggan = pelangganDAO.getPelangganById(pelanggan.getIdPelanggan());
        if (existingPelanggan == null) {
            pelangganDAO.tambahPelanggan(pelanggan);
        }
        
        // Hubungkan pelanggan dengan tiket
        pelangganDAO.linkPelangganToTiket(pelanggan.getIdPelanggan(), idTiket);
        
        return tiket;
    }

    /**
     * Batalkan pemesanan tiket
     */
    public boolean batalkanTiket(String idTiket) throws DatabaseException {
        Tiket tiket = tiketDAO.getTiketById(idTiket);
        
        if (tiket == null) {
            throw new DatabaseException("Tiket tidak ditemukan");
        }
        
        // Dapatkan jadwal dari tiket
        String idJadwal = tiketDAO.getJadwalIdByTiket(idTiket);
        if (idJadwal != null) {
            String idKursi = Helper.generateKursiId(idJadwal, tiket.getNomorKursi());
            
            // Update status kursi menjadi tersedia kembali
            kursiDAO.updateStatusKursi(idKursi, true);
        }
        
        return true;
    }

    /**
     * Buat transaksi dari tiket
     */
    public Transaksi buatTransaksi(Tiket tiket, String metodePembayaran, Pelanggan pelanggan) 
            throws DatabaseException {
        
        String idTransaksi = Helper.generateTransaksiId();
        
        Transaksi transaksi = new Transaksi(idTransaksi, tiket.getHarga(), metodePembayaran);
        transaksi.mencakupTiket(tiket);
        
        transaksiDAO.tambahTransaksi(transaksi);
        
        // Hubungkan transaksi dengan tiket
        transaksiDAO.linkTransaksiToTiket(idTransaksi, tiket.getIdTiket());
        
        return transaksi;
    }

    /**
     * Bayar transaksi
     */
    public boolean bayarTransaksi(String idTransaksi) throws DatabaseException, TransaksiException {
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

    /**
     * Cetak struk pemesanan
     */
    public String cetakStruk(String idTiket, String idJadwal, Pelanggan pelanggan) 
            throws DatabaseException {
        
        Tiket tiket = tiketDAO.getTiketById(idTiket);
        
        if (tiket == null) {
            return "Tiket tidak ditemukan";
        }
        
        Jadwal jadwal = jadwalDAO.getJadwalById(idJadwal);
        
        String cetakId = Helper.generateStrukId();
        
        // Buat struk yang lebih lengkap
        StringBuilder struk = new StringBuilder();
        struk.append("\n╔════════════════════════════════════════════════╗\n");
        struk.append("║          STRUK PEMESANAN TIKET BUS            ║\n");
        struk.append("╚════════════════════════════════════════════════╝\n");
        struk.append("\n");
        struk.append("ID Struk        : ").append(cetakId).append("\n");
        struk.append("Tanggal         : ").append(Helper.formatForDisplay(LocalDateTime.now())).append("\n");
        struk.append("------------------------------------------------\n");
        struk.append("INFORMASI PELANGGAN\n");
        struk.append("------------------------------------------------\n");
        struk.append("Nama            : ").append(pelanggan.getNama()).append("\n");
        struk.append("Email           : ").append(pelanggan.getEmail()).append("\n");
        struk.append("Telepon         : ").append(pelanggan.getTelepon()).append("\n");
        struk.append("\n");
        struk.append("------------------------------------------------\n");
        struk.append("INFORMASI TIKET\n");
        struk.append("------------------------------------------------\n");
        struk.append("ID Tiket        : ").append(tiket.getIdTiket()).append("\n");
        struk.append("Nomor Kursi     : ").append(tiket.getNomorKursi()).append("\n");
        
        if (jadwal != null) {
            struk.append("\n");
            struk.append("------------------------------------------------\n");
            struk.append("INFORMASI PERJALANAN\n");
            struk.append("------------------------------------------------\n");
            struk.append("ID Jadwal       : ").append(jadwal.getIdJadwal()).append("\n");
            struk.append("Rute            : ").append(jadwal.getLokasiAsal())
                .append(" → ").append(jadwal.getLokasiTujuan()).append("\n");
            struk.append("Keberangkatan   : ").append(Helper.formatForDisplay(jadwal.getWaktuBerangkat())).append("\n");
            struk.append("Tiba            : ").append(Helper.formatForDisplay(jadwal.getWaktuTiba())).append("\n");
            struk.append("Durasi          : ").append(Helper.getDurationText(
                jadwal.getWaktuBerangkat(), jadwal.getWaktuTiba())).append("\n");
        }
        
        struk.append("\n");
        struk.append("------------------------------------------------\n");
        struk.append("PEMBAYARAN\n");
        struk.append("------------------------------------------------\n");
        struk.append("Harga Tiket     : ").append(Helper.formatRupiahWithoutDecimal(tiket.getHarga())).append("\n");
        struk.append("Status          : LUNAS\n");
        struk.append("------------------------------------------------\n");
        struk.append("\n");
        struk.append("     Terima kasih atas kepercayaan Anda!\n");
        struk.append("      Selamat melakukan perjalanan :)\n");
        struk.append("\n");
        struk.append("════════════════════════════════════════════════\n");
        
        return struk.toString();
    }
    
    /**
     * Cetak struk sederhana (versi lama)
     */
    public String cetakStrukSederhana(String idTiket) throws DatabaseException {
        Tiket tiket = tiketDAO.getTiketById(idTiket);
        
        if (tiket == null) {
            return "Tiket tidak ditemukan";
        }
        
        String cetakId = Helper.generateStrukId();
        PencetakStruck pencetak = new PencetakStruck(cetakId, tiket);
        
        return pencetak.cetak();
    }
}