package com.example.devices.mapper;

import com.example.devices.dto.DeviceDto;
import com.example.devices.entity.Device;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationTime", ignore = true)
    Device toEntity(DeviceDto dto);

    DeviceDto toDto(Device entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationTime", ignore = true)
    void updateDeviceFromDto(DeviceDto dto, @MappingTarget Device entity);
}
