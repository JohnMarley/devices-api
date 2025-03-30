package com.example.devices.controller;

import com.example.devices.dto.DeviceDto;
import com.example.devices.dto.DevicesDto;
import com.example.devices.enums.State;
import com.example.devices.service.DeviceService;
import com.example.devices.validation.OnPatch;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }


    @PostMapping
    public ResponseEntity<DeviceDto> createDevice(@RequestBody @Valid DeviceDto deviceDto) {
        var createdDevice = deviceService.createDevice(deviceDto);
        return new ResponseEntity<>(createdDevice, HttpStatusCode.valueOf(201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable UUID id, @RequestBody @Valid DeviceDto deviceDto) {
        var updatedDevice = deviceService.updateDevice(id, deviceDto);
        return new ResponseEntity<>(updatedDevice, HttpStatusCode.valueOf(200));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DeviceDto> patchDevice(@PathVariable UUID id, @RequestBody @Validated(OnPatch.class) DeviceDto deviceDto) {
        var updatedDevice = deviceService.patchDevice(id, deviceDto);
        return new ResponseEntity<>(updatedDevice, HttpStatusCode.valueOf(200));
    }

    @GetMapping
    public ResponseEntity<DevicesDto> getAllDevices(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) State state) {
        return ResponseEntity.ok(deviceService.getDevicesByFilters(brand, state));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable UUID id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        deviceService.deleteDevice(id);
        return new ResponseEntity<>(HttpStatusCode.valueOf(204));
    }

}
