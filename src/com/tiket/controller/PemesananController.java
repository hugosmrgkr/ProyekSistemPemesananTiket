package com.tiket.controller;

import com.tiket.database.*;
import com.tiket.exception.*;
import com.tiket.model.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class PemesananController {
    private TiketDAO tiketDAO;
    private KursiDAO kursiDAO;
    private TransaksiDAO transaksiDAO;
    private PelangganDAO pelangganDAO;

    public PemesananController() throws DatabaseException {
        this.tiketDAO = new TiketDAO();
        this.kursiDAO = new KursiDAO();
        this.transaksiDAO = new TransaksiDAO();
        this.pelangganDAO = new PelangganDAO();
    }

    public Tiket pesanTiket(Jadwal jadwal, int nomorKursi, Pelanggan pelanggan) 
            throws DatabaseException, KursiTidakTersediaException {
        
        // Generate ID tiket
        String idTiket = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Cek apakah kursi tersedia
        Kursi kursi = new Kursi(idTiket, nomorKursi);
        kursiDAO.tambahKursi(kursi);
        
        // Buat tiket
        Tiket tiket = new Tiket(idTiket, nomorKursi, jadwal.getHarga(), LocalDateTime.now());
        tiketDAO.tambahTiket(tiket);
        
        // Update status kursi
        kursiDAO.updateStatusKursi(idTiket, false);
        
        // Simpan pelanggan jika belum ada
        Pelanggan existingPelanggan = pelangganDAO.getPelangganById(pelanggan.getIdPelanggan());
        if (existingPelanggan == null) {
            pelangganDAO.tambahPelanggan(pelanggan);
        }
        
        return tiket;
    }

    public Transaksi buatTransaksi(Tiket tiket, String metodePembayaran) throws DatabaseException {
        String idTransaksi = "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Transaksi transaksi = new Transaksi(idTransaksi, tiket.getHarga(), metodePembayaran);
        transaksi.mencakupTiket(tiket);
        
        transaksiDAO.tambahTransaksi(transaksi);
        
        return transaksi;
    }

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

    public String cetakStruk(String idTiket) throws DatabaseException {
        Tiket tiket = tiketDAO.getTiketById(idTiket);
        
        if (tiket == null) {
            return "Tiket tidak ditemukan";
        }
        
        String cetakId = "STR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        PencetakStruck pencetak = new PencetakStruck(cetakId, tiket);
        
        return pencetak.cetak();
    }
}