package com.example.devices.controller;

import com.example.devices.dto.DeviceDto;
import com.example.devices.dto.DevicesDto;
import com.example.devices.enums.State;
import com.example.devices.exception.IllegalDeviceStateException;
import com.example.devices.service.DeviceService;
import com.example.devices.utils.SerializationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@WebMvcTest(DeviceController.class)
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private DeviceService deviceService;

    private static final String DEVICES = "/api/devices";
    private static final String DEVICES_BY_ID = DEVICES + "/{id}";
    private static final UUID RANDOM_UUID = UUID.randomUUID();
    private static final Instant TIME_STAMP = Instant.now();
    private DeviceDto deviceDto;

    @BeforeEach
    void beforeEach() {
        this.deviceDto = DeviceDto.builder()
                .name("5530")
                .brand("nokia")
                .state(State.AVAILABLE)
                .build();
    }


    @Test
    void createDevice_whenValidPayload_thenDeviceIsCreatedAnd201StatusTest() throws Exception {
        var deviceDto = this.deviceDto;
        var deviceDtoResponseExpected = SerializationUtil.deepCopy(deviceDto, DeviceDto.class);
        deviceDtoResponseExpected.setId(RANDOM_UUID);
        deviceDtoResponseExpected.setCreationTime(TIME_STAMP);
        Mockito.when(deviceService.createDevice(deviceDto))
                .thenReturn(deviceDtoResponseExpected);

        var response = mockMvc.perform(
                        MockMvcRequestBuilders.post(DEVICES)
                                .content(SerializationUtil.serializeObject(deviceDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var responseObject = SerializationUtil.deserializeJsonString(response, DeviceDto.class);
        Assertions.assertAll("Created device:",
                () -> Assertions.assertEquals(deviceDtoResponseExpected.getId(), responseObject.getId()),
                () -> Assertions.assertEquals(deviceDtoResponseExpected.getName(), responseObject.getName()),
                () -> Assertions.assertEquals(deviceDtoResponseExpected.getBrand(), responseObject.getBrand()),
                () -> Assertions.assertEquals(deviceDtoResponseExpected.getState(), responseObject.getState()),
                () -> Assertions.assertEquals(deviceDtoResponseExpected.getCreationTime(), responseObject.getCreationTime())
        );
    }

    @Test
    void getAllDevices_whenDevicesExist_thenReturnDeviceListAnd200StatusTest() throws Exception {
        this.deviceDto.setId(RANDOM_UUID);
        this.deviceDto.setCreationTime(TIME_STAMP);
        var devicesDto = DevicesDto.builder().devices(List.of(this.deviceDto)).build();
        Mockito.when(deviceService.getDevicesByFilters(null, null))
                .thenReturn(devicesDto);
        var responseBody = mockMvc.perform(MockMvcRequestBuilders.get(DEVICES))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        var responseActual = SerializationUtil.deserializeJsonString(responseBody, DevicesDto.class);
        Assertions.assertAll("success response",
                () -> Assertions.assertEquals(devicesDto.getDevices().getFirst().getId(), responseActual.getDevices().getFirst().getId()),
                () -> Assertions.assertEquals(devicesDto.getDevices().getFirst().getName(), responseActual.getDevices().getFirst().getName()),
                () -> Assertions.assertEquals(devicesDto.getDevices().getFirst().getBrand(), responseActual.getDevices().getFirst().getBrand()),
                () -> Assertions.assertEquals(devicesDto.getDevices().getFirst().getState(), responseActual.getDevices().getFirst().getState()),
                () -> Assertions.assertEquals(devicesDto.getDevices().getFirst().getCreationTime(), responseActual.getDevices().getFirst().getCreationTime())
        );
    }

    @Test
    void getDeviceById_whendeviceExist_thenReturnDeviceDtoAnd200StatusTest() throws Exception {
        this.deviceDto.setId(RANDOM_UUID);
        this.deviceDto.setCreationTime(TIME_STAMP);
        Mockito.when(deviceService.getDeviceById(RANDOM_UUID))
                .thenReturn(this.deviceDto);

        mockMvc.perform(MockMvcRequestBuilders.get(DEVICES_BY_ID, RANDOM_UUID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(RANDOM_UUID.toString()));
    }

    @Test
    void deleteDevice_whenExistedDeviceDeleted_thenEmptyResponseBodyAnd204ReturnedTest() throws Exception {
        Mockito.doNothing().when(deviceService).deleteDevice(RANDOM_UUID);

        mockMvc.perform(MockMvcRequestBuilders.delete(DEVICES_BY_ID, RANDOM_UUID))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void deleteDevice_whenExistedDeviceInUseDeleted_thenEmptyGeneralResponseAnd404ReturnedTest() throws Exception {
        Mockito.doThrow(new IllegalDeviceStateException("message")).when(deviceService).deleteDevice(RANDOM_UUID);

        mockMvc.perform(MockMvcRequestBuilders.delete(DEVICES_BY_ID, RANDOM_UUID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateDevice_whenExistedDeviceUpdate_thenUpdatedDeviceResponse200ReturnedTest() throws Exception {
        this.deviceDto.setId(RANDOM_UUID);
        this.deviceDto.setCreationTime(TIME_STAMP);
        this.deviceDto.setState(State.IN_USE);
        var deviceDtoResponseExpected = this.deviceDto;
        Mockito.when(deviceService.updateDevice(RANDOM_UUID, this.deviceDto)).thenReturn(this.deviceDto);

        var response = mockMvc.perform(
                        MockMvcRequestBuilders.put(DEVICES_BY_ID, RANDOM_UUID)
                                .content(SerializationUtil.serializeObject(this.deviceDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        var responseObject = SerializationUtil.deserializeJsonString(response, DeviceDto.class);
        Assertions.assertAll("Created device:",
                () -> Assertions.assertEquals(deviceDtoResponseExpected.getId(), responseObject.getId()),
                () -> Assertions.assertEquals(deviceDtoResponseExpected.getName(), responseObject.getName()),
                () -> Assertions.assertEquals(deviceDtoResponseExpected.getBrand(), responseObject.getBrand()),
                () -> Assertions.assertEquals(deviceDtoResponseExpected.getState(), responseObject.getState()),
                () -> Assertions.assertEquals(deviceDtoResponseExpected.getCreationTime(), responseObject.getCreationTime())
        );
    }
}