package com.vadhvan.repositories;

import com.vadhvan.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    List<Vehicle> findByStatus(String status);
    Optional<Vehicle> findByRegistrationNo(String registrationNo);
    boolean existsByRegistrationNo(String registrationNo);
}
