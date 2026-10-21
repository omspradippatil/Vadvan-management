package com.vadhvan.repositories;

import com.vadhvan.models.ContainerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContainerRequestRepository extends JpaRepository<ContainerRequest, String> {
    List<ContainerRequest> findByStatus(String status);
    List<ContainerRequest> findByContainerId(String containerId);
}
