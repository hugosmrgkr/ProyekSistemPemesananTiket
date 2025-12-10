/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.exception;

/**
 * Exception ketika tiket tidak ditemukan
 */
public class TiketNotFoundException extends Exception {
    public TiketNotFoundException(String message) {
        super(message);
    }
}
