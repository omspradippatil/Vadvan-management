package com.vadhvan.repositories;

import com.vadhvan.models.Dock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DockRepository extends JpaRepository<Dock, String> {
    List<Dock> findByStatus(String status);
    Optional<Dock> findByDockNumber(String dockNumber);
    Optional<Dock> findByAssignedShipId(String shipId);
    boolean existsByDockNumber(String dockNumber);
}
