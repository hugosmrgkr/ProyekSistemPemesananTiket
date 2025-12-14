/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.exception;

public class TiketException extends Exception {
    public TiketException(String message) {
        super(message);
    }

    public TiketException(String message, Throwable cause) {
        super(message, cause);
    }
}