package com.example.devices.dto;

import com.example.devices.enums.State;
import com.example.devices.validation.OnPut;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDto {

    private UUID id;
    @NotBlank(message = "Name is required", groups = OnPut.class)
    private String name;
    @NotBlank(message = "Brand is required", groups = OnPut.class)
    private String brand;
    @NotBlank(message = "State is required", groups = OnPut.class)
    private State state;
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
