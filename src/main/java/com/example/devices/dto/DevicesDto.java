package com.example.devices.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "List of Devices")
public class DevicesDto {

    @Schema(description = "List of Device objects")
    private List<DeviceDto> devices;
}
