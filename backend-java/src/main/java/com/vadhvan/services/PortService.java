package com.vadhvan.services;

import com.vadhvan.models.*;
import com.vadhvan.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PortService {

    private final ShipRepository shipRepository;
    private final DockRepository dockRepository;
    private final WarehouseRepository warehouseRepository;
    private final RailTrackRepository railTrackRepository;
    private final EquipmentRepository equipmentRepository;
    private final MaintenanceLogRepository maintenanceLogRepository;
    private final FuelLogRepository fuelLogRepository;
    private final ExpenseRepository expenseRepository;
    private final ContainerRepository containerRepository;

    public PortService(
            ShipRepository shipRepository,
            DockRepository dockRepository,
            WarehouseRepository warehouseRepository,
            RailTrackRepository railTrackRepository,
            EquipmentRepository equipmentRepository,
            MaintenanceLogRepository maintenanceLogRepository,
            FuelLogRepository fuelLogRepository,
            ExpenseRepository expenseRepository,
            ContainerRepository containerRepository
    ) {
        this.shipRepository = shipRepository;
        this.dockRepository = dockRepository;
        this.warehouseRepository = warehouseRepository;
        this.railTrackRepository = railTrackRepository;
        this.equipmentRepository = equipmentRepository;
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.fuelLogRepository = fuelLogRepository;
        this.expenseRepository = expenseRepository;
        this.containerRepository = containerRepository;
    }

    // ========================================================================
    // SHIPS
    // ========================================================================
    @Transactional(readOnly = true)
    public List<Ship> getAllShips() {
        return shipRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Ship getShipById(String id) {
        return shipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ship not found with id: " + id));
    }

    @Transactional
    public Ship createShip(Ship ship) {
        if (shipRepository.existsByImoNumber(ship.getImoNumber())) {
            throw new IllegalArgumentException("Ship with IMO " + ship.getImoNumber() + " already exists");
        }
        return shipRepository.save(ship);
    }

    @Transactional
    public Ship updateShip(String id, Ship updated) {
        Ship existing = getShipById(id);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getArrivalTime() != null) existing.setArrivalTime(updated.getArrivalTime());
        if (updated.getExpectedDeparture() != null) existing.setExpectedDeparture(updated.getExpectedDeparture());
        if (updated.getDockId() != null) existing.setDockId(updated.getDockId());
        if (updated.getContainerCount() != null) existing.setContainerCount(updated.getContainerCount());
        if (updated.getPriority() != null) existing.setPriority(updated.getPriority());
        if (updated.getCargoType() != null) existing.setCargoType(updated.getCargoType());
        if (updated.getShipLength() != null) existing.setShipLength(updated.getShipLength());
        if (updated.getShipWidth() != null) existing.setShipWidth(updated.getShipWidth());
        if (updated.getDraft() != null) existing.setDraft(updated.getDraft());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        return shipRepository.save(existing);
    }

    @Transactional
    public void deleteShip(String id) {
        shipRepository.deleteById(id);
    }

    @Transactional
    public Ship dockShip(String shipId, String dockId) {
        Ship ship = getShipById(shipId);
        Dock dock = dockRepository.findById(dockId)
                .orElseThrow(() -> new IllegalArgumentException("Dock not found with id: " + dockId));

        if (!"AVAILABLE".equalsIgnoreCase(dock.getStatus()) && !shipId.equals(dock.getAssignedShipId())) {
            throw new IllegalArgumentException("Dock " + dock.getDockNumber() + " is currently occupied or unavailable.");
        }

        dock.setStatus("OCCUPIED");
        dock.setAssignedShipId(shipId);
        dockRepository.save(dock);

        ship.setStatus("DOCKED");
        ship.setDockId(dockId);
        return shipRepository.save(ship);
    }

    @Transactional
    public Ship departShip(String shipId) {
        Ship ship = getShipById(shipId);
        if (ship.getDockId() != null) {
            dockRepository.findById(ship.getDockId()).ifPresent(dock -> {
                dock.setStatus("AVAILABLE");
                dock.setAssignedShipId(null);
                dockRepository.save(dock);
            });
        }
        ship.setStatus("DEPARTED");
        ship.setDockId(null);
        return shipRepository.save(ship);
    }

    // ========================================================================
    // DOCKS
    // ========================================================================
    @Transactional(readOnly = true)
    public List<Dock> getAllDocks() {
        return dockRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Dock getDockById(String id) {
        return dockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dock not found with id: " + id));
    }

    @Transactional
    public Dock createDock(Dock dock) {
        if (dockRepository.existsByDockNumber(dock.getDockNumber())) {
            throw new IllegalArgumentException("Dock " + dock.getDockNumber() + " already exists");
        }
        return dockRepository.save(dock);
    }

    @Transactional
    public Dock updateDock(String id, Dock updated) {
        Dock existing = getDockById(id);
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getAssignedShipId() != null) existing.setAssignedShipId(updated.getAssignedShipId());
        if (updated.getAssignedCraneId() != null) existing.setAssignedCraneId(updated.getAssignedCraneId());
        if (updated.getContainerCount() != null) existing.setContainerCount(updated.getContainerCount());
        if (updated.getEstimatedCompletion() != null) existing.setEstimatedCompletion(updated.getEstimatedCompletion());
        if (updated.getWarehouseId() != null) existing.setWarehouseId(updated.getWarehouseId());
        return dockRepository.save(existing);
    }

    @Transactional
    public void deleteDock(String id) {
        Dock dock = getDockById(id);
        if (dock.getAssignedShipId() != null && !dock.getAssignedShipId().isBlank()) {
            throw new IllegalArgumentException("Cannot delete dock while a ship is assigned to it.");
        }
        List<Container> containers = containerRepository.findBySourceDockId(id);
        if (!containers.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete dock with active containers.");
        }
        dockRepository.deleteById(id);
    }

    // ========================================================================
    // WAREHOUSES
    // ========================================================================
    @Transactional(readOnly = true)
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Warehouse getWarehouseById(String id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found with id: " + id));
    }

    @Transactional
    public Warehouse createWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Transactional
    public Warehouse updateWarehouse(String id, Warehouse updated) {
        Warehouse existing = getWarehouseById(id);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getCapacity() != null) existing.setCapacity(updated.getCapacity());
        if (updated.getAvailableSpace() != null) existing.setAvailableSpace(updated.getAvailableSpace());
        if (updated.getOccupiedSpace() != null) existing.setOccupiedSpace(updated.getOccupiedSpace());
        if (updated.getLocationLat() != null) existing.setLocationLat(updated.getLocationLat());
        if (updated.getLocationLng() != null) existing.setLocationLng(updated.getLocationLng());
        return warehouseRepository.save(existing);
    }

    @Transactional
    public void deleteWarehouse(String id) {
        List<Container> containers = containerRepository.findByDestWarehouseId(id);
        if (!containers.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete warehouse with active containers.");
        }
        warehouseRepository.deleteById(id);
    }

    // ========================================================================
    // RAIL TRACKS
    // ========================================================================
    @Transactional(readOnly = true)
    public List<RailTrack> getAllRailTracks() {
        return railTrackRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RailTrack getRailTrackById(String id) {
        return railTrackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rail track not found with id: " + id));
    }

    @Transactional
    public RailTrack createRailTrack(RailTrack railTrack) {
        if (railTrackRepository.existsByTrackNumber(railTrack.getTrackNumber())) {
            throw new IllegalArgumentException("Rail track " + railTrack.getTrackNumber() + " already exists");
        }
        return railTrackRepository.save(railTrack);
    }

    @Transactional
    public RailTrack updateRailTrack(String id, RailTrack updated) {
        RailTrack existing = getRailTrackById(id);
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getCapacity() != null) existing.setCapacity(updated.getCapacity());
        if (updated.getDestination() != null) existing.setDestination(updated.getDestination());
        if (updated.getDepartureTime() != null) existing.setDepartureTime(updated.getDepartureTime());
        if (updated.getContainerIds() != null) existing.setContainerIds(updated.getContainerIds());
        return railTrackRepository.save(existing);
    }

    @Transactional
    public void deleteRailTrack(String id) {
        railTrackRepository.deleteById(id);
    }

    // ========================================================================
    // EQUIPMENT
    // ========================================================================
    @Transactional(readOnly = true)
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Equipment> getAvailableEquipment() {
        return equipmentRepository.findByStatus("AVAILABLE");
    }

    @Transactional(readOnly = true)
    public Equipment getEquipmentById(String id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with id: " + id));
    }

    @Transactional
    public Equipment createEquipment(Equipment equipment) {
        if (equipmentRepository.existsByEquipmentNumber(equipment.getEquipmentNumber())) {
            throw new IllegalArgumentException("Equipment with number " + equipment.getEquipmentNumber() + " already exists");
        }
        return equipmentRepository.save(equipment);
    }

    @Transactional
    public Equipment updateEquipment(String id, Equipment updated) {
        Equipment existing = getEquipmentById(id);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getType() != null) existing.setType(updated.getType());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getHealthScore() != null) existing.setHealthScore(updated.getHealthScore());
        if (updated.getMaintenanceDue() != null) existing.setMaintenanceDue(updated.getMaintenanceDue());
        if (updated.getAssignedDockId() != null) existing.setAssignedDockId(updated.getAssignedDockId());
        if (updated.getOperatorId() != null) existing.setOperatorId(updated.getOperatorId());
        return equipmentRepository.save(existing);
    }

    @Transactional
    public void deleteEquipment(String id) {
        long logs = maintenanceLogRepository.countByEquipmentId(id);
        if (logs > 0) {
            throw new IllegalArgumentException("Cannot delete equipment with existing maintenance history.");
        }
        equipmentRepository.deleteById(id);
    }

    // ========================================================================
    // MAINTENANCE LOGS
    // ========================================================================
    @Transactional(readOnly = true)
    public List<MaintenanceLog> getAllMaintenanceLogs() {
        return maintenanceLogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MaintenanceLog getMaintenanceLogById(String id) {
        return maintenanceLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maintenance log not found with id: " + id));
    }

    @Transactional
    public MaintenanceLog createMaintenanceLog(MaintenanceLog log) {
        return maintenanceLogRepository.save(log);
    }

    @Transactional
    public MaintenanceLog updateMaintenanceLog(String id, MaintenanceLog updated) {
        MaintenanceLog existing = getMaintenanceLogById(id);
        if (updated.getType() != null) existing.setType(updated.getType());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        if (updated.getTechnicianName() != null) existing.setTechnicianName(updated.getTechnicianName());
        if (updated.getCost() != null) existing.setCost(updated.getCost());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getScheduledAt() != null) existing.setScheduledAt(updated.getScheduledAt());
        if (updated.getCompletedAt() != null) existing.setCompletedAt(updated.getCompletedAt());
        return maintenanceLogRepository.save(existing);
    }

    @Transactional
    public MaintenanceLog openMaintenanceLog(String id) {
        MaintenanceLog log = getMaintenanceLogById(id);
        log.setStatus("OPEN");
        return maintenanceLogRepository.save(log);
    }

    @Transactional
    public MaintenanceLog closeMaintenanceLog(String id) {
        MaintenanceLog log = getMaintenanceLogById(id);
        log.setStatus("COMPLETED");
        log.setCompletedAt(LocalDateTime.now());
        return maintenanceLogRepository.save(log);
    }

    @Transactional
    public void deleteMaintenanceLog(String id) {
        maintenanceLogRepository.deleteById(id);
    }

    // ========================================================================
    // FUEL LOGS
    // ========================================================================
    @Transactional(readOnly = true)
    public List<FuelLog> getAllFuelLogs() {
        return fuelLogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public FuelLog getFuelLogById(String id) {
        return fuelLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fuel log not found with id: " + id));
    }

    @Transactional
    public FuelLog createFuelLog(FuelLog log) {
        if (log.getTotalCost() == null && log.getQuantityLitres() != null && log.getCostPerLitre() != null) {
            log.setTotalCost(log.getQuantityLitres() * log.getCostPerLitre());
        }
        return fuelLogRepository.save(log);
    }

    @Transactional
    public FuelLog updateFuelLog(String id, FuelLog updated) {
        FuelLog existing = getFuelLogById(id);
        if (updated.getQuantityLitres() != null) existing.setQuantityLitres(updated.getQuantityLitres());
        if (updated.getCostPerLitre() != null) existing.setCostPerLitre(updated.getCostPerLitre());
        if (updated.getTotalCost() != null) existing.setTotalCost(updated.getTotalCost());
        if (updated.getMileage() != null) existing.setMileage(updated.getMileage());
        if (updated.getDistanceCovered() != null) existing.setDistanceCovered(updated.getDistanceCovered());
        return fuelLogRepository.save(existing);
    }

    @Transactional
    public void deleteFuelLog(String id) {
        fuelLogRepository.deleteById(id);
    }

    // ========================================================================
    // EXPENSES
    // ========================================================================
    @Transactional(readOnly = true)
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Expense getExpenseById(String id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found with id: " + id));
    }

    @Transactional
    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Transactional
    public Expense updateExpense(String id, Expense updated) {
        Expense existing = getExpenseById(id);
        if (updated.getType() != null) existing.setType(updated.getType());
        if (updated.getAmount() != null) existing.setAmount(updated.getAmount());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        return expenseRepository.save(existing);
    }

    @Transactional
    public void deleteExpense(String id) {
        expenseRepository.deleteById(id);
    }
}
