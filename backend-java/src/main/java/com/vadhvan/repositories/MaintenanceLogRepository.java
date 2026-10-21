package com.vadhvan.repositories;

import com.vadhvan.models.MaintenanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, String> {
    List<MaintenanceLog> findByStatus(String status);
    List<MaintenanceLog> findByVehicleId(String vehicleId);
    List<MaintenanceLog> findByEquipmentId(String equipmentId);
    long countByVehicleId(String vehicleId);
    long countByEquipmentId(String equipmentId);
}
