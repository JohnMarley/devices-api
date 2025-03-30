package com.example.devices.exception;

public class IllegalDeviceStateException extends RuntimeException {

    public IllegalDeviceStateException(String message) {
        super(message);
    }
}
