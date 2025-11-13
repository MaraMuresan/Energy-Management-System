package com.example.devicemanagementmicroservice.controllers;

import com.example.devicemanagementmicroservice.dtos.DeviceDTO;
import com.example.devicemanagementmicroservice.dtos.DeviceDetailsDTO;
import com.example.devicemanagementmicroservice.services.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Device", description = "CRUD operations for  device management")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    @Operation(
            summary = "Get all devices",
            description = "Returns a list of all devices stored in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Devices retrieved successfully")
    })
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    @Operation(
            summary = "Create a new device",
            description = "Creates a device entity and returns its UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Device created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid device data")
    })
    public ResponseEntity<UUID> insertDevice(
            @Valid @RequestBody
            @Parameter(description = "Device details needed to create device") DeviceDetailsDTO deviceDTO) {
        UUID deviceID = deviceService.insert(deviceDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    @Operation(
            summary = "Get device by ID",
            description = "Returns device information for the specified device ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Device not found")
    })
    public ResponseEntity<DeviceDTO> getDevice(
            @PathVariable("id")
            @Parameter(description = "UUID of the device to be retrieved") UUID deviceId) {
        DeviceDTO dto = deviceService.findDeviceById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get logged-in user's devices",
            description = "Returns the list of devices assigned to the currently authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Devices retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – missing or invalid token")
    })
    public ResponseEntity<List<DeviceDTO>> getMyDevices(
            @AuthenticationPrincipal
            @Parameter(description = "JWT extracted from the Authorization header") Jwt jwt) {
        String userIdStr = jwt.getClaim("id");
        UUID userId = UUID.fromString(userIdStr);

        List<DeviceDTO> devices = deviceService.findDevicesByUserId(userId);
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @Operation(
            summary = "Update device",
            description = "Updates the device identified by its UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid update data"),
            @ApiResponse(responseCode = "404", description = "Device not found")
    })
    public ResponseEntity<DeviceDTO> updateDevice(
            @PathVariable("id")
            @Parameter(description = "UUID of the device to be updated") UUID deviceId,
            @Valid @RequestBody
            @Parameter(description = "Updated device details") DeviceDetailsDTO deviceDTO) {
        DeviceDTO dto = deviceService.update(deviceId, deviceDTO);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete device",
            description = "Deletes a device given its UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Device deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Device not found")
    })
    public ResponseEntity<Void> deleteDevice(
            @PathVariable("id")
            @Parameter(description = "UUID of the device to be deleted") UUID deviceId) {
        deviceService.delete(deviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
