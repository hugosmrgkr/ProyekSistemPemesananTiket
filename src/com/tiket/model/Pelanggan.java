/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

import java.util.ArrayList;
import java.util.List;

public class Pelanggan {
    private String idPelanggan;
    private String nama;
    private String email;
    private String telepon;
    private List<Tiket> tiketList;

    public Pelanggan() {
        this.tiketList = new ArrayList<>();
    }

    public Pelanggan(String idPelanggan, String nama, String email, String telepon) {
        this.idPelanggan = idPelanggan;
        this.nama = nama;
        this.email = email;
        this.telepon = telepon;
        this.tiketList = new ArrayList<>();
    }

    public String getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(String idPelanggan) { this.idPelanggan = idPelanggan; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }

    public List<Tiket> getTiketList() { return tiketList; }
    public void setTiketList(List<Tiket> tiketList) { this.tiketList = tiketList; }

    public void buatPemesanan(Tiket tiket) {
        this.tiketList.add(tiket);
    }
}