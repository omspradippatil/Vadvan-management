package com.vadhvan.repositories;

import com.vadhvan.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {
    List<Driver> findByStatus(String status);
    Optional<Driver> findByLicenseNo(String licenseNo);
    boolean existsByLicenseNo(String licenseNo);
}
