package com.example.devices.mapper;

import com.example.devices.dto.DeviceDto;
import com.example.devices.entity.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationTime", ignore = true)
    Device toEntity(DeviceDto dto);

    DeviceDto toDto(Device entity);
}
