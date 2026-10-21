package com.vadhvan.repositories;

import com.vadhvan.models.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShipRepository extends JpaRepository<Ship, String> {
    List<Ship> findByStatus(String status);
    Optional<Ship> findByImoNumber(String imoNumber);
    boolean existsByImoNumber(String imoNumber);
}
