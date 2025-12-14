/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

import java.util.ArrayList;
import java.util.List;

public class Transaksi {
    private String idTransaksi;
    private double jumlah;
    private String metodePembayaran;
    private String status;
    private boolean bayar;
    private boolean refund;
    private List<Tiket> tiketList;

    public Transaksi() {
        this.tiketList = new ArrayList<>();
        this.bayar = false;
        this.refund = false;
        this.status = "PENDING";
    }

    public Transaksi(String idTransaksi, double jumlah, String metodePembayaran) {
        this.idTransaksi = idTransaksi;
        this.jumlah = jumlah;
        this.metodePembayaran = metodePembayaran;
        this.status = "PENDING";
        this.bayar = false;
        this.refund = false;
        this.tiketList = new ArrayList<>();
    }

    public String getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(String idTransaksi) { this.idTransaksi = idTransaksi; }

    public double getJumlah() { return jumlah; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }

    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isBayar() { return bayar; }
    public void setBayar(boolean bayar) { this.bayar = bayar; }

    public boolean isRefund() { return refund; }
    public void setRefund(boolean refund) { this.refund = refund; }

    public List<Tiket> getTiketList() { return tiketList; }
    public void setTiketList(List<Tiket> tiketList) { this.tiketList = tiketList; }

    public void mencakupTiket(Tiket tiket) {
        this.tiketList.add(tiket);
        this.jumlah += tiket.getHarga();
    }

    public boolean bayarTransaksi() {
        if (!bayar) {
            this.bayar = true;
            this.status = "LUNAS";
            return true;
        }
        return false;
    }

    public boolean refundTransaksi() {
        if (bayar && !refund) {
            this.refund = true;
            this.status = "REFUND";
            return true;
        }
        return false;
    }
}