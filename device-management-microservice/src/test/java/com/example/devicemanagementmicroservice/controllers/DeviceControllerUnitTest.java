package com.example.devicemanagementmicroservice.controllers;

import com.example.devicemanagementmicroservice.DeviceManagementMicroserviceTestConfig;
import com.example.devicemanagementmicroservice.dtos.DeviceDetailsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeviceControllerUnitTest extends DeviceManagementMicroserviceTestConfig {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void insertDeviceTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DeviceDetailsDTO deviceDTO = new DeviceDetailsDTO("Computer", 300.5, 2021);

        mockMvc.perform(post("/device")
                        .content(objectMapper.writeValueAsString(deviceDTO))
                        .contentType("application/json"))
                .andExpect(status().isCreated());
    }

    @Test
    public void insertDeviceTestFailsDueToYearOfManufacture() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DeviceDetailsDTO deviceDTO = new DeviceDetailsDTO("Computer", 300.5, 2012);

        mockMvc.perform(post("/device")
                        .content(objectMapper.writeValueAsString(deviceDTO))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void insertDeviceTestFailsDueToNull() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DeviceDetailsDTO deviceDTO = new DeviceDetailsDTO("Computer", null, 2021);

        mockMvc.perform(post("/device")
                        .content(objectMapper.writeValueAsString(deviceDTO))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}
