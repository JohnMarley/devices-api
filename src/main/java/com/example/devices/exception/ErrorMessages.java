package com.example.devices.exception;

public final class ErrorMessages {

    private ErrorMessages() {}

    public static final String GENERAL_INTERNAL_SERVER_ERROR_MESSAGE = "Something went wrong. Please try again later.";
    public static final String CANNOT_DELETE_DEVICE_IN_USE_MESSAGE = "Cannot delete a device that is in use";
    public static final String CANNOT_UPDATE_DEVICE_IN_USE_NAME_AND_BRAND_MESSAGE = "Cannot update name or brand while device is in use";
    public static final String DEVICE_NOT_FOUND_MESSAGE = "Device not found";
}
