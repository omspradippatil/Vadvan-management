package com.vadhvan.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "containers")
public class Container {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "\"containerCode\"", nullable = false, unique = true)
    private String containerCode;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "priority", nullable = false)
    private String priority = "MEDIUM";

    @Column(name = "status", nullable = false)
    private String status = "STORED";

    @Column(name = "\"sourceDockId\"")
    private String sourceDockId;

    @Column(name = "\"destWarehouseId\"")
    private String destWarehouseId;

    @Column(name = "\"shipId\"")
    private String shipId;

    @Column(name = "\"craneId\"")
    private String craneId;

    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"updatedAt\"", nullable = false)
    private LocalDateTime updatedAt;

    public Container() {}

    @PrePersist
    protected void onCreate() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.priority == null) this.priority = "MEDIUM";
        if (this.status == null) this.status = "STORED";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getContainerCode() { return containerCode; }
    public void setContainerCode(String containerCode) { this.containerCode = containerCode; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSourceDockId() { return sourceDockId; }
    public void setSourceDockId(String sourceDockId) { this.sourceDockId = sourceDockId; }
    public String getDestWarehouseId() { return destWarehouseId; }
    public void setDestWarehouseId(String destWarehouseId) { this.destWarehouseId = destWarehouseId; }
    public String getShipId() { return shipId; }
    public void setShipId(String shipId) { this.shipId = shipId; }
    public String getCraneId() { return craneId; }
    public void setCraneId(String craneId) { this.craneId = craneId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
