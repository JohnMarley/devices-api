package com.example.devices.service;

import com.example.devices.dto.DeviceDto;
import com.example.devices.enums.State;

import java.util.List;
import java.util.UUID;

public interface DeviceService {

    DeviceDto createDevice(DeviceDto deviceDto);

    DeviceDto updateDevice(UUID id, DeviceDto deviceDto);

    DeviceDto patchDevice(UUID id, DeviceDto deviceDto);

    DeviceDto getDeviceById(UUID id);

    public List<DeviceDto> getDevicesByFilters(String brand, State state);

    void deleteDevice(UUID id);

}
