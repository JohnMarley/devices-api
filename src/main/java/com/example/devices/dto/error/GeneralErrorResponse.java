package com.example.devices.dto.error;

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
public class GeneralErrorResponse {

    private int status;
    private String statusMessage;
    private List<String> errors;
    @Builder.Default
    private Instant timeStamp = Instant.now();
}
