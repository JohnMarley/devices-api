package com.example.devices.service;

import com.example.devices.dto.DeviceDto;
import com.example.devices.entity.Device;
import com.example.devices.enums.State;
import com.example.devices.exception.DeviceNotFoundException;
import com.example.devices.exception.ErrorMessages;
import com.example.devices.exception.IllegalDeviceStateException;
import com.example.devices.mapper.DeviceMapper;
import com.example.devices.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    public DeviceServiceImpl(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    @Transactional
    @Override
    public DeviceDto createDevice(DeviceDto deviceDto) {
        var device = deviceMapper.toEntity(deviceDto);
        var savedDevice = deviceRepository.save(device);
        return deviceMapper.toDto(savedDevice);
    }

    @Transactional
    @Override
    public DeviceDto updateDevice(UUID id, DeviceDto deviceDto) {
        var device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(ErrorMessages.DEVICE_NOT_FOUND_MESSAGE));
        if (Objects.equals(device.getState(), State.IN_USE) && Objects.equals(deviceDto.getState(), State.IN_USE)) {
            throw new IllegalDeviceStateException(ErrorMessages.CANNOT_UPDATE_DEVICE_IN_USE_NAME_AND_BRAND_MESSAGE);
        }
        var updatedDevice = deviceMapper.toEntity(deviceDto);
        updatedDevice.setId(id);
        updatedDevice.setCreationTime(device.getCreationTime());
        return deviceMapper.toDto(deviceRepository.save(updatedDevice));
    }

    @Transactional
    @Override
    public DeviceDto patchDevice(UUID id, DeviceDto deviceDto) {
        var device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(ErrorMessages.DEVICE_NOT_FOUND_MESSAGE));
        // Validate the state transition before updating
        validateDeviceState(device, deviceDto);

        deviceMapper.updateDeviceFromDto(deviceDto, device);
        return deviceMapper.toDto(deviceRepository.save(device));
    }

    @Override
    public DeviceDto getDeviceById(UUID id) {
        var device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(ErrorMessages.DEVICE_NOT_FOUND_MESSAGE));
        return deviceMapper.toDto(device);
    }

    @Override
    public List<DeviceDto> getDevicesByFilters(String brand, State state) {
        List<Device> devices = deviceRepository.findByBrandAndState(brand, state);
        return devices.stream()
                .map(deviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteDevice(UUID id) {
        var device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(ErrorMessages.DEVICE_NOT_FOUND_MESSAGE));
        if (device.getState() == State.IN_USE) {
            throw new IllegalDeviceStateException(ErrorMessages.CANNOT_DELETE_DEVICE_IN_USE_MESSAGE);
        }
        deviceRepository.deleteById(id);
    }

    private void validateDeviceState(Device device, DeviceDto deviceDto) {
        if (Objects.equals(device.getState(), State.IN_USE)) {
            // Throw an exception if the state is attempted to be changed to IN_USE or state is not provided
            if (Objects.isNull(deviceDto.getState())
                    ||
                    Objects.equals(deviceDto.getState(), State.IN_USE)) {
                throw new IllegalDeviceStateException(ErrorMessages.CANNOT_UPDATE_DEVICE_IN_USE_NAME_AND_BRAND_MESSAGE);
            }
        }
    }
}
