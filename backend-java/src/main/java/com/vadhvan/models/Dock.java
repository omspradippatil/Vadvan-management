package com.vadhvan.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "docks")
public class Dock {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "\"dockNumber\"", nullable = false, unique = true)
    private String dockNumber;

    @Column(name = "status", nullable = false)
    private String status = "AVAILABLE";

    @Column(name = "\"assignedShipId\"", unique = true)
    private String assignedShipId;

    @Column(name = "\"assignedCraneId\"")
    private String assignedCraneId;

    @Column(name = "\"containerCount\"", nullable = false)
    private Integer containerCount = 0;

    @Column(name = "\"estimatedCompletion\"")
    private LocalDateTime estimatedCompletion;

    @Column(name = "\"warehouseId\"")
    private String warehouseId;

    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"updatedAt\"", nullable = false)
    private LocalDateTime updatedAt;

    public Dock() {}

    @PrePersist
    protected void onCreate() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.status == null) this.status = "AVAILABLE";
        if (this.containerCount == null) this.containerCount = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDockNumber() { return dockNumber; }
    public void setDockNumber(String dockNumber) { this.dockNumber = dockNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAssignedShipId() { return assignedShipId; }
    public void setAssignedShipId(String assignedShipId) { this.assignedShipId = assignedShipId; }
    public String getAssignedCraneId() { return assignedCraneId; }
    public void setAssignedCraneId(String assignedCraneId) { this.assignedCraneId = assignedCraneId; }
    public Integer getContainerCount() { return containerCount; }
    public void setContainerCount(Integer containerCount) { this.containerCount = containerCount; }
    public LocalDateTime getEstimatedCompletion() { return estimatedCompletion; }
    public void setEstimatedCompletion(LocalDateTime estimatedCompletion) { this.estimatedCompletion = estimatedCompletion; }
    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
