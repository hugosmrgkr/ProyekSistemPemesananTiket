package com.tiket.controller;

import com.tiket.database.JadwalDAO;
import com.tiket.database.KursiDAO;
import com.tiket.exception.DatabaseException;
import com.tiket.exception.JadwalNotFoundException;
import com.tiket.helper.Helper;
import com.tiket.model.Jadwal;
import com.tiket.model.Kursi;

import java.util.List;

public class JadwalController {
    private JadwalDAO jadwalDAO;
    private KursiDAO kursiDAO;

    public JadwalController() throws DatabaseException {
        this.jadwalDAO = new JadwalDAO();
        this.kursiDAO = new KursiDAO();
    }

    public void tambahJadwal(Jadwal jadwal) throws DatabaseException {
        if (jadwal == null) {
            throw new DatabaseException("Jadwal tidak boleh null");
        }
        
        // Tambah jadwal
        jadwalDAO.tambahJadwal(jadwal);
        
        // Tambahkan 40 kursi otomatis
        tambahKursiUntukJadwal(jadwal.getIdJadwal(), 40);
    }
    
    /**
     * Method untuk menambahkan kursi otomatis saat jadwal dibuat
     */
    private void tambahKursiUntukJadwal(String idJadwal, int jumlahKursi) throws DatabaseException {
        for (int i = 1; i <= jumlahKursi; i++) {
            String idKursi = Helper.generateKursiId(idJadwal, i);
            Kursi kursi = new Kursi(idKursi, i);
            kursi.setTersedia(true);
            
            try {
                kursiDAO.tambahKursi(kursi);
            } catch (DatabaseException e) {
                // Jika kursi sudah ada, skip
                if (!e.getMessage().contains("Duplicate entry")) {
                    throw e;
                }
            }
        }
    }

    public void updateJadwal(Jadwal jadwal) throws DatabaseException, JadwalNotFoundException {
        if (jadwal == null) {
            throw new DatabaseException("Jadwal tidak boleh null");
        }
        
        Jadwal existing = jadwalDAO.getJadwalById(jadwal.getIdJadwal());
        if (existing == null) {
            throw new JadwalNotFoundException("Jadwal dengan ID " + jadwal.getIdJadwal() + " tidak ditemukan");
        }
        
        jadwalDAO.updateJadwal(jadwal);
    }

    public void hapusJadwal(String idJadwal) throws DatabaseException, JadwalNotFoundException {
        Jadwal existing = jadwalDAO.getJadwalById(idJadwal);
        if (existing == null) {
            throw new JadwalNotFoundException("Jadwal dengan ID " + idJadwal + " tidak ditemukan");
        }
        
        // Hapus kursi terkait terlebih dahulu
        hapusKursiJadwal(idJadwal);
        
        jadwalDAO.hapusJadwal(idJadwal);
    }
    
    /**
     * Method untuk menghapus kursi saat jadwal dihapus
     */
    private void hapusKursiJadwal(String idJadwal) throws DatabaseException {
        try {
            List<Kursi> kursiList = kursiDAO.getAllKursiByJadwal(idJadwal);
            for (Kursi kursi : kursiList) {
                kursiDAO.hapusKursi(kursi.getIdTiket());
            }
        } catch (DatabaseException e) {
            // Jika gagal hapus kursi, lanjutkan
            System.err.println("Warning: Gagal hapus kursi untuk jadwal " + idJadwal);
        }
    }

    public List<Jadwal> getAllJadwal() throws DatabaseException {
        return jadwalDAO.getAllJadwal();
    }

    public Jadwal getJadwalById(String idJadwal) throws DatabaseException, JadwalNotFoundException {
        Jadwal jadwal = jadwalDAO.getJadwalById(idJadwal);
        if (jadwal == null) {
            throw new JadwalNotFoundException("Jadwal dengan ID " + idJadwal + " tidak ditemukan");
        }
        return jadwal;
    }

    public List<Jadwal> cariJadwal(String lokasiAsal, String lokasiTujuan) throws DatabaseException {
        if (lokasiAsal == null || lokasiTujuan == null) {
            throw new DatabaseException("Lokasi asal dan tujuan tidak boleh null");
        }
        return jadwalDAO.cariJadwal(lokasiAsal, lokasiTujuan);
    }
    
    /**
     * Method untuk mendapatkan jumlah kursi tersedia
     */
    public int getJumlahKursiTersedia(String idJadwal) throws DatabaseException {
        try {
            List<Kursi> kursiTersedia = kursiDAO.getKursiTersediaByJadwal(idJadwal);
            return kursiTersedia.size();
        } catch (DatabaseException e) {
            return 0;
        }
    }
}