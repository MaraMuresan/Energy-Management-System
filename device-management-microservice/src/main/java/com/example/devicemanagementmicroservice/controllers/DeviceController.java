package com.example.devicemanagementmicroservice.controllers;

import com.example.devicemanagementmicroservice.dtos.DeviceDTO;
import com.example.devicemanagementmicroservice.dtos.DeviceDetailsDTO;
import com.example.devicemanagementmicroservice.services.DeviceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDetailsDTO deviceDTO) {
        UUID deviceID = deviceService.insert(deviceDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") UUID deviceId) {
        DeviceDTO dto = deviceService.findDeviceById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<List<DeviceDTO>> getMyDevices(@AuthenticationPrincipal Jwt jwt) {
        String userIdStr = jwt.getClaim("id");
        UUID userId = UUID.fromString(userIdStr);

        List<DeviceDTO> devices = deviceService.findDevicesByUserId(userId);
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable("id") UUID deviceId, @Valid @RequestBody DeviceDetailsDTO deviceDTO) {
        DeviceDTO dto = deviceService.update(deviceId, deviceDTO);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable("id") UUID deviceId) {
        deviceService.delete(deviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
