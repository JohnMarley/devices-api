package com.example.devices.exception;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.List;

class GlobalExceptionHandlerTest {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void resourceNotFoundException_whenExceptionThrown_thenGeneralResponse404Returned() {
        var response = globalExceptionHandler.resourceNotFoundException(new DeviceNotFoundException("message"));
        SoftAssertions.assertSoftly(softAssertion -> {
            softAssertion.assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.NOT_FOUND);
            var responseBody = response.getBody();
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            softAssertion.assertThat(responseBody.getStatus())
                    .isEqualTo(HttpStatus.NOT_FOUND.value());
            softAssertion.assertThat(responseBody.getStatusMessage())
                    .isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
            softAssertion.assertThat(responseBody.getErrors())
                    .containsExactly("message");
            softAssertion.assertThat(responseBody.getTimeStamp())
                    .isBefore(Instant.now());
        });
    }

    @Test
    void testRequestDataValidationException() {
        var bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.getFieldErrors())
                .thenReturn(List.of(
                        new FieldError("deviceDto", "name", "error1"),
                        new FieldError("deviceDto", "brand", "error2")));
        var methodArgException = new MethodArgumentNotValidException(null, bindingResult);
        var response = globalExceptionHandler.requestDataValidationException(methodArgException);
        org.assertj.core.api.Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        var responseBody = response.getBody();
        org.assertj.core.api.Assertions.assertThat(responseBody)
                .isNotNull();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(responseBody.getStatus())
                    .isEqualTo(HttpStatus.BAD_REQUEST.value());
            softAssertions.assertThat(responseBody.getStatusMessage())
                    .isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
            softAssertions.assertThat(responseBody.getErrors())
                    .containsExactly("error1", "error2");
            softAssertions.assertThat(responseBody.getTimeStamp())
                    .isBefore(Instant.now());
        });
    }

    @Test
    void testIllegalDeviceStateException() {
        var response = globalExceptionHandler.illegalDeviceStateException(new IllegalDeviceStateException("message"));
        SoftAssertions.assertSoftly(softAssertion -> {
            softAssertion.assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST);
            var responseBody = response.getBody();
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            softAssertion.assertThat(responseBody.getStatus())
                    .isEqualTo(HttpStatus.BAD_REQUEST.value());
            softAssertion.assertThat(responseBody.getStatusMessage())
                    .isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
            softAssertion.assertThat(responseBody.getErrors())
                    .containsExactly("message");
            softAssertion.assertThat(responseBody.getTimeStamp())
                    .isBefore(Instant.now());
        });
    }
}