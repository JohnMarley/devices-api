package com.example.devices.controller;

import com.example.devices.dto.DeviceDto;
import com.example.devices.dto.DevicesDto;
import com.example.devices.dto.error.GeneralErrorResponse;
import com.example.devices.enums.State;
import com.example.devices.service.DeviceService;
import com.example.devices.validation.OnPatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Devices", description = "Endpoints for managing devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Operation(
            summary = "Create a new device",
            description = "Adds a new device to the system and returns the created device details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Device successfully created",
                    content = @Content(schema = @Schema(implementation = DeviceDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<DeviceDto> createDevice(@RequestBody @Valid DeviceDto deviceDto) {
        var createdDevice = deviceService.createDevice(deviceDto);
        return new ResponseEntity<>(createdDevice, HttpStatusCode.valueOf(201));
    }

    @Operation(
            summary = "Update an existing device",
            description = "Updates the details of an existing device based on its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device successfully updated",
                    content = @Content(schema = @Schema(implementation = DeviceDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable UUID id, @RequestBody @Valid DeviceDto deviceDto) {
        var updatedDevice = deviceService.updateDevice(id, deviceDto);
        return new ResponseEntity<>(updatedDevice, HttpStatusCode.valueOf(200));
    }

    @Operation(
            summary = "Partially update a device",
            description = "Updates specific fields of an existing device based on its ID. " +
                    "Only the provided fields will be updated, leaving others unchanged."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device successfully updated",
                    content = @Content(schema = @Schema(implementation = DeviceDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<DeviceDto> patchDevice(@PathVariable UUID id, @RequestBody @Validated(OnPatch.class) DeviceDto deviceDto) {
        var updatedDevice = deviceService.patchDevice(id, deviceDto);
        return new ResponseEntity<>(updatedDevice, HttpStatusCode.valueOf(200));
    }

    @Operation(
            summary = "Retrieve a list of devices",
            description = "Fetches all devices, optionally filtering by brand and/or state."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of devices retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DevicesDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<DevicesDto> getAllDevices(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) State state) {
        return ResponseEntity.ok(deviceService.getDevicesByFilters(brand, state));
    }

    @Operation(
            summary = "Get a device by ID",
            description = "Retrieves details of a specific device using its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device found successfully",
                    content = @Content(schema = @Schema(implementation = DeviceDto.class))),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid device ID format",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable UUID id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    @Operation(
            summary = "Delete a device by ID",
            description = "Deletes a specific device using its unique identifier. The device must not be in use."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Device deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid device ID format",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Device is in use and cannot be deleted",
                    content = @Content(schema = @Schema(implementation = GeneralErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        deviceService.deleteDevice(id);
        return new ResponseEntity<>(HttpStatusCode.valueOf(204));
    }

}
