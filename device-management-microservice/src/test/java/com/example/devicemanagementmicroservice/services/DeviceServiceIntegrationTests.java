package com.example.devicemanagementmicroservice.services;

import com.example.devicemanagementmicroservice.DeviceManagementMicroserviceTestConfig;
import com.example.devicemanagementmicroservice.dtos.DeviceDTO;
import com.example.devicemanagementmicroservice.dtos.DeviceDetailsDTO;
import com.example.devicemanagementmicroservice.dtos.UserReplicaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/test-sql/create.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/test-sql/delete.sql")
public class DeviceServiceIntegrationTests extends DeviceManagementMicroserviceTestConfig {

    @Autowired
    DeviceService deviceService;

    @Test
    public void testGetCorrect() {
        List<DeviceDTO> deviceDTOList = deviceService.findDevices();
        assertEquals("Test Insert Device", 1, deviceDTOList.size());
    }

    @Test
    public void testInsertCorrectWithGetById() {
        DeviceDetailsDTO d = new DeviceDetailsDTO("Computer", 300.5, 2021);
        UUID insertedID = deviceService.insert(d);

        UserReplicaDTO userReplicaDTO = null;
        if (d.getUserId() != null) {
            userReplicaDTO = new UserReplicaDTO(d.getUserId(), null);
        }

        DeviceDTO insertedDevice = new DeviceDTO(insertedID, d.getName(), d.getMaximumConsumption(), userReplicaDTO);
        DeviceDTO fetchedDevice = deviceService.findDeviceById(insertedID);

        assertEquals("Test Inserted Device", insertedDevice, fetchedDevice);
    }

    @Test
    public void testInsertCorrectWithGetAll() {
        DeviceDetailsDTO d = new DeviceDetailsDTO("Computer", 300.5, 2021);
        deviceService.insert(d);

        List<DeviceDTO> deviceDTOList = deviceService.findDevices();
        assertEquals("Test Inserted Devices", 2, deviceDTOList.size());
    }
}
