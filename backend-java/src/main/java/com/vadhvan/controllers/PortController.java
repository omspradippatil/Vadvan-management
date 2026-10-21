package com.vadhvan.controllers;

import com.vadhvan.dto.ApiResponse;
import com.vadhvan.models.*;
import com.vadhvan.services.PortService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PortController {

    private final PortService portService;

    public PortController(PortService portService) {
        this.portService = portService;
    }

    // ========================================================================
    // SHIPS
    // ========================================================================
    @GetMapping("/ships")
    public ResponseEntity<ApiResponse<List<Ship>>> getAllShips() {
        return ResponseEntity.ok(ApiResponse.success(portService.getAllShips()));
    }

    @GetMapping("/ships/{id}")
    public ResponseEntity<ApiResponse<Ship>> getShipById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(portService.getShipById(id)));
    }

    @PostMapping("/ships")
    public ResponseEntity<ApiResponse<Ship>> createShip(@RequestBody Ship ship) {
        Ship created = portService.createShip(ship);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Ship registered successfully"));
    }

    @PutMapping("/ships/{id}")
    public ResponseEntity<ApiResponse<Ship>> updateShip(@PathVariable String id, @RequestBody Ship ship) {
        Ship updated = portService.updateShip(id, ship);
        return ResponseEntity.ok(ApiResponse.success(updated, "Ship updated successfully"));
    }

    @DeleteMapping("/ships/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShip(@PathVariable String id) {
        portService.deleteShip(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Ship deleted successfully"));
    }

    @PostMapping("/ships/{id}/dock")
    public ResponseEntity<ApiResponse<Ship>> dockShip(@PathVariable String id, @RequestBody Map<String, String> payload) {
        String dockId = payload.get("dockId");
        if (dockId == null || dockId.isBlank()) {
            throw new IllegalArgumentException("dockId is required to dock ship");
        }
        Ship docked = portService.dockShip(id, dockId);
        return ResponseEntity.ok(ApiResponse.success(docked, "Ship docked successfully"));
    }

    @PostMapping("/ships/{id}/depart")
    public ResponseEntity<ApiResponse<Ship>> departShip(@PathVariable String id) {
        Ship departed = portService.departShip(id);
        return ResponseEntity.ok(ApiResponse.success(departed, "Ship departed successfully"));
    }

    // ========================================================================
    // DOCKS
    // ========================================================================
    @GetMapping("/docks")
    public ResponseEntity<ApiResponse<List<Dock>>> getAllDocks() {
        return ResponseEntity.ok(ApiResponse.success(portService.getAllDocks()));
    }

    @GetMapping("/docks/{id}")
    public ResponseEntity<ApiResponse<Dock>> getDockById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(portService.getDockById(id)));
    }

    @PostMapping("/docks")
    public ResponseEntity<ApiResponse<Dock>> createDock(@RequestBody Dock dock) {
        Dock created = portService.createDock(dock);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Dock created successfully"));
    }

    @PutMapping("/docks/{id}")
    public ResponseEntity<ApiResponse<Dock>> updateDock(@PathVariable String id, @RequestBody Dock dock) {
        Dock updated = portService.updateDock(id, dock);
        return ResponseEntity.ok(ApiResponse.success(updated, "Dock updated successfully"));
    }

    @DeleteMapping("/docks/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDock(@PathVariable String id) {
        portService.deleteDock(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Dock deleted successfully"));
    }

    // ========================================================================
    // WAREHOUSES
    // ========================================================================
    @GetMapping("/warehouses")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getAllWarehouses() {
        return ResponseEntity.ok(ApiResponse.success(portService.getAllWarehouses()));
    }

    @GetMapping("/warehouses/{id}")
    public ResponseEntity<ApiResponse<Warehouse>> getWarehouseById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(portService.getWarehouseById(id)));
    }

    @PostMapping("/warehouses")
    public ResponseEntity<ApiResponse<Warehouse>> createWarehouse(@RequestBody Warehouse warehouse) {
        Warehouse created = portService.createWarehouse(warehouse);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Warehouse created successfully"));
    }

    @PutMapping("/warehouses/{id}")
    public ResponseEntity<ApiResponse<Warehouse>> updateWarehouse(@PathVariable String id, @RequestBody Warehouse warehouse) {
        Warehouse updated = portService.updateWarehouse(id, warehouse);
        return ResponseEntity.ok(ApiResponse.success(updated, "Warehouse updated successfully"));
    }

    @DeleteMapping("/warehouses/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWarehouse(@PathVariable String id) {
        portService.deleteWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Warehouse deleted successfully"));
    }

    // ========================================================================
    // RAIL TRACKS
    // ========================================================================
    @GetMapping("/rail-tracks")
    public ResponseEntity<ApiResponse<List<RailTrack>>> getAllRailTracks() {
        return ResponseEntity.ok(ApiResponse.success(portService.getAllRailTracks()));
    }

    @GetMapping("/rail-tracks/{id}")
    public ResponseEntity<ApiResponse<RailTrack>> getRailTrackById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(portService.getRailTrackById(id)));
    }

    @PostMapping("/rail-tracks")
    public ResponseEntity<ApiResponse<RailTrack>> createRailTrack(@RequestBody RailTrack railTrack) {
        RailTrack created = portService.createRailTrack(railTrack);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Rail track created successfully"));
    }

    @PutMapping("/rail-tracks/{id}")
    public ResponseEntity<ApiResponse<RailTrack>> updateRailTrack(@PathVariable String id, @RequestBody RailTrack railTrack) {
        RailTrack updated = portService.updateRailTrack(id, railTrack);
        return ResponseEntity.ok(ApiResponse.success(updated, "Rail track updated successfully"));
    }

    @DeleteMapping("/rail-tracks/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRailTrack(@PathVariable String id) {
        portService.deleteRailTrack(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Rail track deleted successfully"));
    }

    // ========================================================================
    // EQUIPMENT
    // ========================================================================
    @GetMapping("/equipment")
    public ResponseEntity<ApiResponse<List<Equipment>>> getAllEquipment() {
        return ResponseEntity.ok(ApiResponse.success(portService.getAllEquipment()));
    }

    @GetMapping("/equipment/available")
    public ResponseEntity<ApiResponse<List<Equipment>>> getAvailableEquipment() {
        return ResponseEntity.ok(ApiResponse.success(portService.getAvailableEquipment()));
    }

    @GetMapping("/equipment/{id}")
    public ResponseEntity<ApiResponse<Equipment>> getEquipmentById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(portService.getEquipmentById(id)));
    }

    @PostMapping("/equipment")
    public ResponseEntity<ApiResponse<Equipment>> createEquipment(@RequestBody Equipment equipment) {
        Equipment created = portService.createEquipment(equipment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Equipment registered successfully"));
    }

    @PutMapping("/equipment/{id}")
    public ResponseEntity<ApiResponse<Equipment>> updateEquipment(@PathVariable String id, @RequestBody Equipment equipment) {
        Equipment updated = portService.updateEquipment(id, equipment);
        return ResponseEntity.ok(ApiResponse.success(updated, "Equipment updated successfully"));
    }

    @DeleteMapping("/equipment/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEquipment(@PathVariable String id) {
        portService.deleteEquipment(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Equipment deleted successfully"));
    }

    // ========================================================================
    // MAINTENANCE LOGS
    // ========================================================================
    @GetMapping("/maintenance")
    public ResponseEntity<ApiResponse<List<MaintenanceLog>>> getAllMaintenanceLogs() {
        return ResponseEntity.ok(ApiResponse.success(portService.getAllMaintenanceLogs()));
    }

    @GetMapping("/maintenance/{id}")
    public ResponseEntity<ApiResponse<MaintenanceLog>> getMaintenanceLogById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(portService.getMaintenanceLogById(id)));
    }

    @PostMapping("/maintenance")
    public ResponseEntity<ApiResponse<MaintenanceLog>> createMaintenanceLog(@RequestBody MaintenanceLog log) {
        MaintenanceLog created = portService.createMaintenanceLog(log);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Maintenance log created successfully"));
    }

    @PutMapping("/maintenance/{id}")
    public ResponseEntity<ApiResponse<MaintenanceLog>> updateMaintenanceLog(@PathVariable String id, @RequestBody MaintenanceLog log) {
        MaintenanceLog updated = portService.updateMaintenanceLog(id, log);
        return ResponseEntity.ok(ApiResponse.success(updated, "Maintenance log updated successfully"));
    }

    @PostMapping("/maintenance/{id}/open")
    public ResponseEntity<ApiResponse<MaintenanceLog>> openMaintenanceLog(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(portService.openMaintenanceLog(id), "Maintenance reopened"));
    }

    @PostMapping("/maintenance/{id}/close")
    public ResponseEntity<ApiResponse<MaintenanceLog>> closeMaintenanceLog(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(portService.closeMaintenanceLog(id), "Maintenance closed"));
    }

    @DeleteMapping("/maintenance/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMaintenanceLog(@PathVariable String id) {
        portService.deleteMaintenanceLog(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Maintenance log deleted successfully"));
    }

    // ========================================================================
    // FUEL LOGS
    // ========================================================================
    @GetMapping("/fuel")
    public ResponseEntity<ApiResponse<List<FuelLog>>> getAllFuelLogs() {
        return ResponseEntity.ok(ApiResponse.success(portService.getAllFuelLogs()));
    }

    @GetMapping("/fuel/{id}")
    public ResponseEntity<ApiResponse<FuelLog>> getFuelLogById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(portService.getFuelLogById(id)));
    }

    @PostMapping("/fuel")
    public ResponseEntity<ApiResponse<FuelLog>> createFuelLog(@RequestBody FuelLog log) {
        FuelLog created = portService.createFuelLog(log);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Fuel log added successfully"));
    }

    @PutMapping("/fuel/{id}")
    public ResponseEntity<ApiResponse<FuelLog>> updateFuelLog(@PathVariable String id, @RequestBody FuelLog log) {
        FuelLog updated = portService.updateFuelLog(id, log);
        return ResponseEntity.ok(ApiResponse.success(updated, "Fuel log updated successfully"));
    }

    @DeleteMapping("/fuel/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFuelLog(@PathVariable String id) {
        portService.deleteFuelLog(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Fuel log deleted successfully"));
    }

    // ========================================================================
    // EXPENSES
    // ========================================================================
    @GetMapping("/expenses")
    public ResponseEntity<ApiResponse<List<Expense>>> getAllExpenses() {
        return ResponseEntity.ok(ApiResponse.success(portService.getAllExpenses()));
    }

    @GetMapping("/expenses/{id}")
    public ResponseEntity<ApiResponse<Expense>> getExpenseById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(portService.getExpenseById(id)));
    }

    @PostMapping("/expenses")
    public ResponseEntity<ApiResponse<Expense>> createExpense(@RequestBody Expense expense) {
        Expense created = portService.createExpense(expense);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Expense logged successfully"));
    }

    @PutMapping("/expenses/{id}")
    public ResponseEntity<ApiResponse<Expense>> updateExpense(@PathVariable String id, @RequestBody Expense expense) {
        Expense updated = portService.updateExpense(id, expense);
        return ResponseEntity.ok(ApiResponse.success(updated, "Expense updated successfully"));
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable String id) {
        portService.deleteExpense(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Expense deleted successfully"));
    }
}
