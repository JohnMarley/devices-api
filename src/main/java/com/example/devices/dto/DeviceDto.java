package com.example.devices.dto;

import com.example.devices.enums.State;
import com.example.devices.validation.OnPut;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Schema(description = "Device object")
public class DeviceDto {

    @Schema(description = "id for user", example = "5def1285-6885-4002-b95c-a6b3fcc67956")
    private UUID id;
    @NotBlank(message = "Name is required", groups = OnPut.class)
    @Schema(description = "Device name", example = "5530")
    private String name;
    @NotBlank(message = "Brand is required", groups = OnPut.class)
    @Schema(description = "Device brand", example = "nokia")
    private String brand;
    @NotBlank(message = "State is required", groups = OnPut.class)
    @Schema(description = "Device state", example = "IN_USE", allowableValues = { "AVAILABLE", "IN_USE", "MAINTENANCE" })
    private State state;
    @Schema(description = "Device creationTime", example = "2025-03-29T20:40:24.177257Z")
    private Instant creationTime;

    @Override
    public String toString() {
        var objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotBlank(message = "Name is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name is required") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Brand is required") String getBrand() {
        return brand;
    }

    public void setBrand(@NotBlank(message = "Brand is required") String brand) {
        this.brand = brand;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }
}
