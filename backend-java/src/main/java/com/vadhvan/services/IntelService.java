package com.vadhvan.services;

import com.vadhvan.models.*;
import com.vadhvan.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class IntelService {

    private final DockRepository dockRepository;
    private final WarehouseRepository warehouseRepository;
    private final VehicleRepository vehicleRepository;
    private final TripRepository tripRepository;
    private final ContainerRepository containerRepository;
    private final FuelLogRepository fuelLogRepository;
    private final ExpenseRepository expenseRepository;
    private final NotificationRepository notificationRepository;
    private final ActivityLogRepository activityLogRepository;
    private final SettingsRepository settingsRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public IntelService(
            DockRepository dockRepository,
            WarehouseRepository warehouseRepository,
            VehicleRepository vehicleRepository,
            TripRepository tripRepository,
            ContainerRepository containerRepository,
            FuelLogRepository fuelLogRepository,
            ExpenseRepository expenseRepository,
            NotificationRepository notificationRepository,
            ActivityLogRepository activityLogRepository,
            SettingsRepository settingsRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.dockRepository = dockRepository;
        this.warehouseRepository = warehouseRepository;
        this.vehicleRepository = vehicleRepository;
        this.tripRepository = tripRepository;
        this.containerRepository = containerRepository;
        this.fuelLogRepository = fuelLogRepository;
        this.expenseRepository = expenseRepository;
        this.notificationRepository = notificationRepository;
        this.activityLogRepository = activityLogRepository;
        this.settingsRepository = settingsRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ========================================================================
    // PORT HEALTH SCORE
    // ========================================================================
    @Transactional(readOnly = true)
    public Map<String, Object> getPortHealth() {
        List<Dock> docks = dockRepository.findAll();
        List<Warehouse> warehouses = warehouseRepository.findAll();
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Trip> trips = tripRepository.findAll();

        long totalDocks = docks.size();
        long occupiedDocks = docks.stream().filter(d -> "OCCUPIED".equalsIgnoreCase(d.getStatus())).count();
        double dockScore = totalDocks == 0 ? 100.0 : ((double) (totalDocks - occupiedDocks) / totalDocks) * 100.0;

        double totalWarehouseCapacity = warehouses.stream().mapToDouble(Warehouse::getCapacity).sum();
        double totalAvailableSpace = warehouses.stream().mapToDouble(Warehouse::getAvailableSpace).sum();
        double warehouseScore = totalWarehouseCapacity == 0 ? 100.0 : (totalAvailableSpace / totalWarehouseCapacity) * 100.0;

        double avgVehicleHealth = vehicles.isEmpty() ? 100.0 :
                vehicles.stream().mapToInt(Vehicle::getHealthScore).average().orElse(100.0);

        long inProgressTrips = trips.stream().filter(t -> "IN_PROGRESS".equalsIgnoreCase(t.getStatus())).count();
        double logisticsScore = trips.isEmpty() ? 100.0 : Math.max(0, 100.0 - (inProgressTrips * 2.5));

        int overallScore = (int) Math.round((dockScore * 0.3) + (warehouseScore * 0.25) + (avgVehicleHealth * 0.25) + (logisticsScore * 0.2));

        Map<String, Object> response = new HashMap<>();
        response.put("score", overallScore);
        response.put("status", overallScore >= 80 ? "OPTIMAL" : overallScore >= 50 ? "MODERATE" : "CRITICAL");
        response.put("factors", Map.of(
                "dockAvailability", Math.round(dockScore),
                "warehouseCapacity", Math.round(warehouseScore),
                "fleetHealth", Math.round(avgVehicleHealth),
                "logisticsFlow", Math.round(logisticsScore)
        ));
        return response;
    }

    // ========================================================================
    // ANALYTICS & KPIS
    // ========================================================================
    @Transactional(readOnly = true)
    public Map<String, Object> getKPIs() {
        long totalVehicles = vehicleRepository.count();
        long activeVehicles = vehicleRepository.findByStatus("ON_TRIP").size();
        long availableVehicles = vehicleRepository.findByStatus("AVAILABLE").size();

        long totalTrips = tripRepository.count();
        long completedTrips = tripRepository.findByStatus("COMPLETED").size();
        long inProgressTrips = tripRepository.findByStatus("IN_PROGRESS").size();

        long totalDocks = dockRepository.count();
        long occupiedDocks = dockRepository.findByStatus("OCCUPIED").size();

        long totalContainers = containerRepository.count();

        double totalFuelCost = fuelLogRepository.findAll().stream().mapToDouble(FuelLog::getTotalCost).sum();
        double totalExpenses = expenseRepository.findAll().stream().mapToDouble(Expense::getAmount).sum();

        Map<String, Object> kpis = new HashMap<>();
        kpis.put("totalVehicles", totalVehicles);
        kpis.put("activeVehicles", activeVehicles);
        kpis.put("availableVehicles", availableVehicles);
        kpis.put("totalTrips", totalTrips);
        kpis.put("completedTrips", completedTrips);
        kpis.put("inProgressTrips", inProgressTrips);
        kpis.put("totalDocks", totalDocks);
        kpis.put("occupiedDocks", occupiedDocks);
        kpis.put("totalContainers", totalContainers);
        kpis.put("totalFuelCost", Math.round(totalFuelCost * 100.0) / 100.0);
        kpis.put("totalExpenses", Math.round(totalExpenses * 100.0) / 100.0);
        return kpis;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getCharts() {
        List<Trip> trips = tripRepository.findAll();
        Map<String, Long> tripStatusDist = new HashMap<>();
        for (Trip trip : trips) {
            tripStatusDist.put(trip.getStatus(), tripStatusDist.getOrDefault(trip.getStatus(), 0L) + 1);
        }

        List<Vehicle> vehicles = vehicleRepository.findAll();
        Map<String, Long> vehicleStatusDist = new HashMap<>();
        for (Vehicle v : vehicles) {
            vehicleStatusDist.put(v.getStatus(), vehicleStatusDist.getOrDefault(v.getStatus(), 0L) + 1);
        }

        Map<String, Object> charts = new HashMap<>();
        charts.put("tripDistribution", tripStatusDist);
        charts.put("vehicleDistribution", vehicleStatusDist);
        return charts;
    }

    // ========================================================================
    // REPORTS
    // ========================================================================
    @Transactional(readOnly = true)
    public List<Vehicle> getFleetReport() {
        return vehicleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Trip> getTripReport() {
        return tripRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<FuelLog> getFuelReport() {
        return fuelLogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpenseReport() {
        return expenseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Container> getContainerReport() {
        return containerRepository.findAll();
    }

    // ========================================================================
    // NOTIFICATIONS
    // ========================================================================
    @Transactional(readOnly = true)
    public List<Notification> getNotifications(String userId) {
        if (userId != null) {
            return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public void markNotificationRead(String id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    @Transactional
    public void markAllNotificationsRead() {
        List<Notification> all = notificationRepository.findAll();
        all.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(all);
    }

    // ========================================================================
    // SETTINGS
    // ========================================================================
    @Transactional(readOnly = true)
    public Settings getSettings() {
        return settingsRepository.findById("settings")
                .orElseGet(() -> settingsRepository.save(new Settings("settings", "Vadhvan Port", "light", "en", "{}")));
    }

    @Transactional
    public Settings updateSettings(Settings updated) {
        Settings existing = getSettings();
        if (updated.getOrgName() != null) existing.setOrgName(updated.getOrgName());
        if (updated.getTheme() != null) existing.setTheme(updated.getTheme());
        if (updated.getLanguage() != null) existing.setLanguage(updated.getLanguage());
        if (updated.getNotificationPrefs() != null) existing.setNotificationPrefs(updated.getNotificationPrefs());
        return settingsRepository.save(existing);
    }

    // ========================================================================
    // ACTIVITY LOGS
    // ========================================================================
    @Transactional(readOnly = true)
    public List<ActivityLog> getActivityLogs() {
        return activityLogRepository.findAllByOrderByCreatedAtDesc();
    }

    // ========================================================================
    // USERS MANAGEMENT (ADMIN)
    // ========================================================================
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(String id, User updated) {
        User existing = getUserById(id);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getRole() != null) existing.setRole(updated.getRole());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getPasswordHash() != null && !updated.getPasswordHash().isBlank()) {
            existing.setPasswordHash(passwordEncoder.encode(updated.getPasswordHash()));
        }
        return userRepository.save(existing);
    }

    @Transactional
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
