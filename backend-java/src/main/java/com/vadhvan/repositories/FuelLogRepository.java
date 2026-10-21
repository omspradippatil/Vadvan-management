package com.vadhvan.repositories;

import com.vadhvan.models.FuelLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FuelLogRepository extends JpaRepository<FuelLog, String> {
    List<FuelLog> findByVehicleId(String vehicleId);
    List<FuelLog> findByDriverId(String driverId);
    List<FuelLog> findByTripId(String tripId);
    long countByTripId(String tripId);
}
