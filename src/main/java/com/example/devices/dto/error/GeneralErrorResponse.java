package com.example.devices.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "GeneralErrorResponse", description = "General error response")
public class GeneralErrorResponse {

    @Schema(description = "HTTP status code", example = "404")
    private int status;
    @Schema(description = "HTTP status message", example = "Not Found")
    private String statusMessage;
    @Schema(description = "Errors", example = "[invalid device state]")
    private List<String> errors;
    @Builder.Default
    @Schema(description = "timeStamp", example = "2025-03-29T20:40:24.177257Z")
    private Instant timeStamp = Instant.now();
}
