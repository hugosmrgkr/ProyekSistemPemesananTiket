package com.tiket.controller;

import com.tiket.database.JadwalDAO;
import com.tiket.exception.DatabaseException;
import com.tiket.exception.JadwalNotFoundException;
import com.tiket.model.Jadwal;

import java.util.List;

public class JadwalController {
    private JadwalDAO jadwalDAO;

    public JadwalController() throws DatabaseException {
        this.jadwalDAO = new JadwalDAO();
    }

    public void tambahJadwal(Jadwal jadwal) throws DatabaseException {
        if (jadwal == null) {
            throw new DatabaseException("Jadwal tidak boleh null");
        }
        jadwalDAO.tambahJadwal(jadwal);
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
        
        jadwalDAO.hapusJadwal(idJadwal);
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
}