package com.vadhvan.services;

import com.vadhvan.models.*;
import com.vadhvan.repositories.*;
import com.vadhvan.utils.SseManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CoreService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final TripRepository tripRepository;
    private final ContainerRepository containerRepository;
    private final ContainerRequestRepository containerRequestRepository;
    private final SseManager sseManager;

    public CoreService(
            VehicleRepository vehicleRepository,
            DriverRepository driverRepository,
            TripRepository tripRepository,
            ContainerRepository containerRepository,
            ContainerRequestRepository containerRequestRepository,
            SseManager sseManager
    ) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.tripRepository = tripRepository;
        this.containerRepository = containerRepository;
        this.containerRequestRepository = containerRequestRepository;
        this.sseManager = sseManager;
    }

    // ========================================================================
    // VEHICLES
    // ========================================================================
    @Transactional(readOnly = true)
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByStatus("AVAILABLE");
    }

    @Transactional(readOnly = true)
    public Vehicle getVehicleById(String id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + id));
    }

    @Transactional
    public Vehicle createVehicle(Vehicle vehicle) {
        if (vehicleRepository.existsByRegistrationNo(vehicle.getRegistrationNo())) {
            throw new IllegalArgumentException("Vehicle with registration " + vehicle.getRegistrationNo() + " already exists");
        }
        Vehicle saved = vehicleRepository.save(vehicle);
        sseManager.broadcast("vehicle_update", saved);
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
        return saved;
    }

    @Transactional
    public Vehicle updateVehicle(String id, Vehicle updated) {
        Vehicle existing = getVehicleById(id);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getModel() != null) existing.setModel(updated.getModel());
        if (updated.getType() != null) existing.setType(updated.getType());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getMaxCapacity() != null) existing.setMaxCapacity(updated.getMaxCapacity());
        if (updated.getOdometer() != null) existing.setOdometer(updated.getOdometer());
        if (updated.getFuelLevel() != null) existing.setFuelLevel(updated.getFuelLevel());
        if (updated.getHealthScore() != null) existing.setHealthScore(updated.getHealthScore());
        if (updated.getLocationLat() != null) existing.setLocationLat(updated.getLocationLat());
        if (updated.getLocationLng() != null) existing.setLocationLng(updated.getLocationLng());
        if (updated.getDriverId() != null) existing.setDriverId(updated.getDriverId());
        Vehicle saved = vehicleRepository.save(existing);
        sseManager.broadcast("vehicle_update", saved);
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
        return saved;
    }

    @Transactional
    public void deleteVehicle(String id) {
        long activeTrips = tripRepository.countByVehicleId(id);
        if (activeTrips > 0) {
            throw new IllegalArgumentException("Cannot delete vehicle because it is assigned to one or more trips.");
        }
        vehicleRepository.deleteById(id);
        sseManager.broadcast("vehicle_update", Map.of("id", id, "deleted", true));
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
    }

    // ========================================================================
    // DRIVERS
    // ========================================================================
    @Transactional(readOnly = true)
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByStatus("AVAILABLE");
    }

    @Transactional(readOnly = true)
    public Driver getDriverById(String id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + id));
    }

    @Transactional
    public Driver createDriver(Driver driver) {
        if (driverRepository.existsByLicenseNo(driver.getLicenseNo())) {
            throw new IllegalArgumentException("Driver with license " + driver.getLicenseNo() + " already exists");
        }
        Driver saved = driverRepository.save(driver);
        sseManager.broadcast("driver_update", saved);
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
        return saved;
    }

    @Transactional
    public Driver updateDriver(String id, Driver updated) {
        Driver existing = getDriverById(id);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getPhone() != null) existing.setPhone(updated.getPhone());
        if (updated.getLicenseCategory() != null) existing.setLicenseCategory(updated.getLicenseCategory());
        if (updated.getLicenseExpiry() != null) existing.setLicenseExpiry(updated.getLicenseExpiry());
        if (updated.getEmergencyContact() != null) existing.setEmergencyContact(updated.getEmergencyContact());
        if (updated.getSafetyScore() != null) existing.setSafetyScore(updated.getSafetyScore());
        if (updated.getExperienceYears() != null) existing.setExperienceYears(updated.getExperienceYears());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getVehicleId() != null) existing.setVehicleId(updated.getVehicleId());
        Driver saved = driverRepository.save(existing);
        sseManager.broadcast("driver_update", saved);
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
        return saved;
    }

    @Transactional
    public void deleteDriver(String id) {
        long activeTrips = tripRepository.countByDriverId(id);
        if (activeTrips > 0) {
            throw new IllegalArgumentException("Cannot delete driver because they are assigned to one or more trips.");
        }
        driverRepository.deleteById(id);
        sseManager.broadcast("driver_update", Map.of("id", id, "deleted", true));
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
    }

    // ========================================================================
    // TRIPS
    // ========================================================================
    @Transactional(readOnly = true)
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Trip getTripById(String id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found with id: " + id));
    }

    @Transactional
    public Trip createTrip(Trip trip) {
        Vehicle vehicle = getVehicleById(trip.getVehicleId());
        if (trip.getCargoWeight() != null && trip.getCargoWeight() > vehicle.getMaxCapacity()) {
            throw new IllegalArgumentException("Cargo weight (" + trip.getCargoWeight() + " tons) exceeds vehicle maximum capacity (" + vehicle.getMaxCapacity() + " tons).");
        }

        getDriverById(trip.getDriverId());
        Trip saved = tripRepository.save(trip);
        sseManager.broadcast("trip_update", saved);
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
        return saved;
    }

    @Transactional
    public Trip updateTrip(String id, Trip updated) {
        Trip existing = getTripById(id);

        if (updated.getVehicleId() != null) {
            Vehicle vehicle = getVehicleById(updated.getVehicleId());
            Double weight = updated.getCargoWeight() != null ? updated.getCargoWeight() : existing.getCargoWeight();
            if (weight != null && weight > vehicle.getMaxCapacity()) {
                throw new IllegalArgumentException("Cargo weight (" + weight + " tons) exceeds vehicle maximum capacity (" + vehicle.getMaxCapacity() + " tons).");
            }
            existing.setVehicleId(updated.getVehicleId());
        }

        if (updated.getCargoWeight() != null && updated.getVehicleId() == null) {
            Vehicle vehicle = getVehicleById(existing.getVehicleId());
            if (updated.getCargoWeight() > vehicle.getMaxCapacity()) {
                throw new IllegalArgumentException("Cargo weight (" + updated.getCargoWeight() + " tons) exceeds vehicle maximum capacity (" + vehicle.getMaxCapacity() + " tons).");
            }
            existing.setCargoWeight(updated.getCargoWeight());
        }

        if (updated.getDriverId() != null) existing.setDriverId(updated.getDriverId());
        if (updated.getSource() != null) existing.setSource(updated.getSource());
        if (updated.getDestination() != null) existing.setDestination(updated.getDestination());
        if (updated.getPriority() != null) existing.setPriority(updated.getPriority());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getNotes() != null) existing.setNotes(updated.getNotes());

        Trip saved = tripRepository.save(existing);
        sseManager.broadcast("trip_update", saved);
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
        return saved;
    }

    @Transactional
    public Trip dispatchTrip(String id) {
        Trip trip = getTripById(id);
        trip.setStatus("IN_PROGRESS");
        trip.setDispatchedAt(LocalDateTime.now());
        Trip saved = tripRepository.save(trip);
        sseManager.broadcast("trip_update", saved);
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
        return saved;
    }

    @Transactional
    public Trip completeTrip(String id) {
        Trip trip = getTripById(id);
        trip.setStatus("COMPLETED");
        trip.setCompletedAt(LocalDateTime.now());
        Trip saved = tripRepository.save(trip);
        sseManager.broadcast("trip_update", saved);
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
        return saved;
    }

    @Transactional
    public Trip cancelTrip(String id) {
        Trip trip = getTripById(id);
        trip.setStatus("CANCELLED");
        trip.setCancelledAt(LocalDateTime.now());
        Trip saved = tripRepository.save(trip);
        sseManager.broadcast("trip_update", saved);
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
        return saved;
    }

    @Transactional
    public void deleteTrip(String id) {
        tripRepository.deleteById(id);
        sseManager.broadcast("trip_update", Map.of("id", id, "deleted", true));
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
    }

    // ========================================================================
    // CONTAINERS & REQUESTS
    // ========================================================================
    @Transactional(readOnly = true)
    public List<Container> getAllContainers() {
        return containerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Container getContainerById(String id) {
        return containerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Container not found with id: " + id));
    }

    @Transactional
    public Container createContainer(Container container) {
        if (containerRepository.existsByContainerCode(container.getContainerCode())) {
            throw new IllegalArgumentException("Container with code " + container.getContainerCode() + " already exists");
        }
        Container saved = containerRepository.save(container);
        sseManager.broadcast("container_update", saved);
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
        return saved;
    }

    @Transactional
    public Container updateContainer(String id, Container updated) {
        Container existing = getContainerById(id);
        if (updated.getWeight() != null) existing.setWeight(updated.getWeight());
        if (updated.getPriority() != null) existing.setPriority(updated.getPriority());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getSourceDockId() != null) existing.setSourceDockId(updated.getSourceDockId());
        if (updated.getDestWarehouseId() != null) existing.setDestWarehouseId(updated.getDestWarehouseId());
        if (updated.getShipId() != null) existing.setShipId(updated.getShipId());
        if (updated.getCraneId() != null) existing.setCraneId(updated.getCraneId());
        Container saved = containerRepository.save(existing);
        sseManager.broadcast("container_update", saved);
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
        return saved;
    }

    @Transactional
    public void deleteContainer(String id) {
        long trips = tripRepository.countByContainerId(id);
        if (trips > 0) {
            throw new IllegalArgumentException("Cannot delete container assigned to active trips.");
        }
        containerRepository.deleteById(id);
        sseManager.broadcast("container_update", Map.of("id", id, "deleted", true));
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
    }

    @Transactional(readOnly = true)
    public List<ContainerRequest> getAllContainerRequests() {
        return containerRequestRepository.findAll();
    }

    @Transactional
    public ContainerRequest createContainerRequest(ContainerRequest request) {
        ContainerRequest saved = containerRequestRepository.save(request);
        sseManager.broadcast("dashboard_update", Map.of("trigger", true));
        return saved;
    }
}
