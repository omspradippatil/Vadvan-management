package com.vadhvan.controllers;

import com.vadhvan.dto.ApiResponse;
import com.vadhvan.models.*;
import com.vadhvan.services.CoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CoreController {

    private final CoreService coreService;

    public CoreController(CoreService coreService) {
        this.coreService = coreService;
    }

    // ========================================================================
    // VEHICLES
    // ========================================================================
    @GetMapping("/vehicles")
    public ResponseEntity<ApiResponse<List<Vehicle>>> getAllVehicles() {
        return ResponseEntity.ok(ApiResponse.success(coreService.getAllVehicles()));
    }

    @GetMapping("/vehicles/available")
    public ResponseEntity<ApiResponse<List<Vehicle>>> getAvailableVehicles() {
        return ResponseEntity.ok(ApiResponse.success(coreService.getAvailableVehicles()));
    }

    @GetMapping("/vehicles/{id}")
    public ResponseEntity<ApiResponse<Vehicle>> getVehicleById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(coreService.getVehicleById(id)));
    }

    @PostMapping("/vehicles")
    public ResponseEntity<ApiResponse<Vehicle>> createVehicle(@RequestBody Vehicle vehicle) {
        Vehicle created = coreService.createVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Vehicle created successfully"));
    }

    @PutMapping("/vehicles/{id}")
    public ResponseEntity<ApiResponse<Vehicle>> updateVehicle(@PathVariable String id, @RequestBody Vehicle vehicle) {
        Vehicle updated = coreService.updateVehicle(id, vehicle);
        return ResponseEntity.ok(ApiResponse.success(updated, "Vehicle updated successfully"));
    }

    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable String id) {
        coreService.deleteVehicle(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Vehicle deleted successfully"));
    }

    // ========================================================================
    // DRIVERS
    // ========================================================================
    @GetMapping("/drivers")
    public ResponseEntity<ApiResponse<List<Driver>>> getAllDrivers() {
        return ResponseEntity.ok(ApiResponse.success(coreService.getAllDrivers()));
    }

    @GetMapping("/drivers/available")
    public ResponseEntity<ApiResponse<List<Driver>>> getAvailableDrivers() {
        return ResponseEntity.ok(ApiResponse.success(coreService.getAvailableDrivers()));
    }

    @GetMapping("/drivers/{id}")
    public ResponseEntity<ApiResponse<Driver>> getDriverById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(coreService.getDriverById(id)));
    }

    @PostMapping("/drivers")
    public ResponseEntity<ApiResponse<Driver>> createDriver(@RequestBody Driver driver) {
        Driver created = coreService.createDriver(driver);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Driver created successfully"));
    }

    @PutMapping("/drivers/{id}")
    public ResponseEntity<ApiResponse<Driver>> updateDriver(@PathVariable String id, @RequestBody Driver driver) {
        Driver updated = coreService.updateDriver(id, driver);
        return ResponseEntity.ok(ApiResponse.success(updated, "Driver updated successfully"));
    }

    @DeleteMapping("/drivers/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDriver(@PathVariable String id) {
        coreService.deleteDriver(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Driver deleted successfully"));
    }

    // ========================================================================
    // TRIPS
    // ========================================================================
    @GetMapping("/trips")
    public ResponseEntity<ApiResponse<List<Trip>>> getAllTrips() {
        return ResponseEntity.ok(ApiResponse.success(coreService.getAllTrips()));
    }

    @GetMapping("/trips/{id}")
    public ResponseEntity<ApiResponse<Trip>> getTripById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(coreService.getTripById(id)));
    }

    @PostMapping("/trips")
    public ResponseEntity<ApiResponse<Trip>> createTrip(@RequestBody Trip trip) {
        Trip created = coreService.createTrip(trip);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Trip created successfully"));
    }

    @PutMapping("/trips/{id}")
    public ResponseEntity<ApiResponse<Trip>> updateTrip(@PathVariable String id, @RequestBody Trip trip) {
        Trip updated = coreService.updateTrip(id, trip);
        return ResponseEntity.ok(ApiResponse.success(updated, "Trip updated successfully"));
    }

    @DeleteMapping("/trips/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTrip(@PathVariable String id) {
        coreService.deleteTrip(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Trip deleted successfully"));
    }

    @PostMapping("/trips/{id}/dispatch")
    public ResponseEntity<ApiResponse<Trip>> dispatchTrip(@PathVariable String id) {
        Trip dispatched = coreService.dispatchTrip(id);
        return ResponseEntity.ok(ApiResponse.success(dispatched, "Trip dispatched successfully"));
    }

    @PostMapping("/trips/{id}/complete")
    public ResponseEntity<ApiResponse<Trip>> completeTrip(@PathVariable String id) {
        Trip completed = coreService.completeTrip(id);
        return ResponseEntity.ok(ApiResponse.success(completed, "Trip completed successfully"));
    }

    @PostMapping("/trips/{id}/cancel")
    public ResponseEntity<ApiResponse<Trip>> cancelTrip(@PathVariable String id) {
        Trip cancelled = coreService.cancelTrip(id);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Trip cancelled successfully"));
    }

    // ========================================================================
    // CONTAINERS
    // ========================================================================
    @GetMapping("/containers")
    public ResponseEntity<ApiResponse<List<Container>>> getAllContainers() {
        return ResponseEntity.ok(ApiResponse.success(coreService.getAllContainers()));
    }

    @GetMapping("/containers/requests")
    public ResponseEntity<ApiResponse<List<ContainerRequest>>> getContainerRequests() {
        return ResponseEntity.ok(ApiResponse.success(coreService.getAllContainerRequests()));
    }

    @PostMapping("/containers/requests")
    public ResponseEntity<ApiResponse<ContainerRequest>> createContainerRequest(@RequestBody ContainerRequest request) {
        ContainerRequest created = coreService.createContainerRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Container request created successfully"));
    }

    @GetMapping("/containers/{id}")
    public ResponseEntity<ApiResponse<Container>> getContainerById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(coreService.getContainerById(id)));
    }

    @PostMapping("/containers")
    public ResponseEntity<ApiResponse<Container>> createContainer(@RequestBody Container container) {
        Container created = coreService.createContainer(container);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Container created successfully"));
    }

    @PutMapping("/containers/{id}")
    public ResponseEntity<ApiResponse<Container>> updateContainer(@PathVariable String id, @RequestBody Container container) {
        Container updated = coreService.updateContainer(id, container);
        return ResponseEntity.ok(ApiResponse.success(updated, "Container updated successfully"));
    }

    @DeleteMapping("/containers/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContainer(@PathVariable String id) {
        coreService.deleteContainer(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Container deleted successfully"));
    }
}
