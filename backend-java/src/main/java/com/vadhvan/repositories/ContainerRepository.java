package com.vadhvan.repositories;

import com.vadhvan.models.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContainerRepository extends JpaRepository<Container, String> {
    List<Container> findByStatus(String status);
    Optional<Container> findByContainerCode(String containerCode);
    boolean existsByContainerCode(String containerCode);
    List<Container> findByDestWarehouseId(String warehouseId);
    List<Container> findBySourceDockId(String sourceDockId);
}
