package com.vadhvan.repositories;

import com.vadhvan.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, String> {
    List<Trip> findByStatus(String status);
    Optional<Trip> findByTripNumber(String tripNumber);
    long countByVehicleId(String vehicleId);
    long countByDriverId(String driverId);
    long countByContainerId(String containerId);
    List<Trip> findByVehicleId(String vehicleId);
    List<Trip> findByDriverId(String driverId);
}
