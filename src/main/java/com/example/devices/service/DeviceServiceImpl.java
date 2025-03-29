package com.example.devices.service;

import com.example.devices.dto.DeviceDto;
import com.example.devices.entity.Device;
import com.example.devices.enums.State;
import com.example.devices.mapper.DeviceMapper;
import com.example.devices.repository.DeviceRepository;
import jakarta.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException("Device not found"));
        if (device.getState() == State.IN_USE) {
            if (!Objects.equals(device.getName(), deviceDto.getName())
                    ||
                    !Objects.equals(device.getBrand(), deviceDto.getBrand())) {
                throw new IllegalStateException("Cannot update name or brand while device is in use");
            }
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
                .orElseThrow(() -> new EntityNotFoundException("Device not found"));
        if (Objects.nonNull(deviceDto.getName())) {
            device.setName(deviceDto.getName());
        }
        if (Objects.nonNull(deviceDto.getBrand())) {
            device.setBrand(deviceDto.getBrand());
        }
        if (Objects.nonNull(deviceDto.getState())) {
            device.setState(deviceDto.getState());
        }
        return deviceMapper.toDto(deviceRepository.save(device));
    }

    @Override
    public DeviceDto getDeviceById(UUID id) {
        var device = deviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Device not found"));
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
                .orElseThrow(() -> new EntityNotFoundException("Device not found"));
        if (device.getState() == State.IN_USE) {
            throw new IllegalStateException("Cannot delete a device that is in use");
        }
        deviceRepository.deleteById(id);
    }
}
