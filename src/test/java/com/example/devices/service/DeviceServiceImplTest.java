package com.example.devices.service;

import com.example.devices.dto.DeviceDto;
import com.example.devices.entity.Device;
import com.example.devices.enums.State;
import com.example.devices.exception.DeviceNotFoundException;
import com.example.devices.exception.IllegalDeviceStateException;
import com.example.devices.mapper.DeviceMapper;
import com.example.devices.mapper.DeviceMapperImpl;
import com.example.devices.repository.DeviceRepository;
import com.example.devices.utils.SerializationUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class DeviceServiceImplTest {

    @Mock
    private DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper = new DeviceMapperImpl();
    private DeviceServiceImpl deviceServiceImpl;
    private DeviceDto deviceDto;
    private Device device;
    private UUID randomUUID;
    private Instant timeStamp;

    @BeforeEach
    public void beforeEach() {
        deviceServiceImpl = new DeviceServiceImpl(deviceRepository, deviceMapper);
        this.deviceDto = DeviceDto.builder()
                .name("5530")
                .brand("nokia")
                .state(State.AVAILABLE)
                .build();
        this.device = deviceMapper.toEntity(this.deviceDto);
        this.randomUUID = UUID.randomUUID();
        this.timeStamp = Instant.now();
        this.device.setId(randomUUID);
        this.device.setCreationTime(timeStamp);
    }

    @Test
    void createDevice_whenValidPayloadProvided_thenNewDeviceCreatedTest() {
        Mockito.when(deviceRepository.save(Mockito.any(Device.class))).thenReturn(this.device);
        var deviceDto = deviceServiceImpl.createDevice(this.deviceDto);
        this.deviceDto.setId(randomUUID);
        this.deviceDto.setCreationTime(timeStamp);
        Assertions.assertThat(deviceDto)
                .isEqualTo(this.deviceDto);
    }

    @Test
    void updateDevice_whenExistingDeviceUpdated_thenUpdatedDeviceReturnedTest() {
        Mockito.when(deviceRepository.findById(this.randomUUID)).thenReturn(Optional.of(this.device));
        Mockito.when(deviceRepository.save(Mockito.any(Device.class))).thenReturn(this.device);

        var result = deviceServiceImpl.updateDevice(this.randomUUID, this.deviceDto);
        this.deviceDto.setId(this.randomUUID);
        this.deviceDto.setCreationTime(this.timeStamp);
        Assertions.assertThat(result)
                .isEqualTo(this.deviceDto);
    }

    @Test
    void updateDevice_whenExistingDeviceInUseNameBrandUpdated_thenExceptionThrownTest() {
        this.device.setState(State.IN_USE);
        this.deviceDto.setState(State.IN_USE);
        Mockito.when(deviceRepository.findById(this.randomUUID)).thenReturn(Optional.of(this.device));

        org.junit.jupiter.api.Assertions.assertThrows(IllegalDeviceStateException.class,
                () -> deviceServiceImpl.updateDevice(this.randomUUID, this.deviceDto));
    }

    @Test
    void patchDevice_whenExistingDeviceNameUpdated_thenUpdatedDeviceReturnedTest() {
        var deviceDto = SerializationUtil.deepCopy(this.deviceDto, DeviceDto.class);
        deviceDto.setState(null);
        deviceDto.setBrand(null);
        Mockito.when(deviceRepository.findById(this.randomUUID)).thenReturn(Optional.of(this.device));
        Mockito.when(deviceRepository.save(Mockito.any(Device.class))).thenReturn(this.device);

        var result = deviceServiceImpl.patchDevice(this.randomUUID, deviceDto);
        this.deviceDto.setId(this.randomUUID);
        this.deviceDto.setCreationTime(this.timeStamp);
        Assertions.assertThat(result)
                .isEqualTo(this.deviceDto);
    }

    @Test
    void updateDevice_whenExistingDeviceInUseNameUpdated_thenExceptionThrownTest() {
        this.device.setState(State.IN_USE);
        this.deviceDto.setState(null);
        Mockito.when(deviceRepository.findById(this.randomUUID)).thenReturn(Optional.of(this.device));

        org.junit.jupiter.api.Assertions.assertThrows(IllegalDeviceStateException.class,
                () -> deviceServiceImpl.patchDevice(this.randomUUID, this.deviceDto));
    }

    @Test
    void getDeviceById_whenDeviceExists_thenDeviceReturned() {
        Mockito.when(deviceRepository.findById(this.randomUUID)).thenReturn(Optional.ofNullable(this.device));
        var result = deviceServiceImpl.getDeviceById(this.randomUUID);
        Assertions.assertThat(result)
                .isEqualTo(deviceMapper.toDto(this.device));
    }

    @Test
    void getDevice_whenDeviceNotExist_thenExceptionThrownTest() {
        org.junit.jupiter.api.Assertions.assertThrows(DeviceNotFoundException.class,
                () -> deviceServiceImpl.getDeviceById(this.randomUUID));
    }

    @Test
    void getDevicesByFilters_whenDeviceExists_thenDeviceListReturned() {
        Mockito.when(deviceRepository.findByBrandAndState(null, null)).thenReturn((List.of(this.device)));
        var result = deviceServiceImpl.getDevicesByFilters(null, null);
        Assertions.assertThat(result.getDevices())
                .containsOnly(deviceMapper.toDto(this.device));
    }

    @Test
    void deleteDevice_whenDeleteExistingDevice_thenEmptyResponse204Returned() {
        Mockito.when(deviceRepository.findById(this.randomUUID)).thenReturn(Optional.ofNullable(this.device));
        Mockito.doNothing().when(deviceRepository).deleteById(this.randomUUID);
        deviceServiceImpl.deleteDevice(this.randomUUID);
        Mockito.verify(deviceRepository, Mockito.times(1)).deleteById(this.randomUUID);
    }

    @Test
    void deleteDevice_whenDeviceInUseStatus_thenExceptionThrownTest() {
        this.device.setState(State.IN_USE);
        Mockito.when(deviceRepository.findById(this.randomUUID)).thenReturn(Optional.ofNullable(this.device));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalDeviceStateException.class,
                () -> deviceServiceImpl.deleteDevice(this.randomUUID));
    }
}