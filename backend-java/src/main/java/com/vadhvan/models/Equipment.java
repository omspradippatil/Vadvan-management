package com.vadhvan.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "\"equipmentNumber\"", nullable = false, unique = true)
    private String equipmentNumber;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "status", nullable = false)
    private String status = "AVAILABLE";

    @Column(name = "\"healthScore\"", nullable = false)
    private Integer healthScore = 100;

    @Column(name = "\"maintenanceDue\"")
    private LocalDateTime maintenanceDue;

    @Column(name = "\"assignedDockId\"")
    private String assignedDockId;

    @Column(name = "\"operatorId\"")
    private String operatorId;

    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"updatedAt\"", nullable = false)
    private LocalDateTime updatedAt;

    public Equipment() {}

    @PrePersist
    protected void onCreate() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.status == null) this.status = "AVAILABLE";
        if (this.healthScore == null) this.healthScore = 100;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEquipmentNumber() { return equipmentNumber; }
    public void setEquipmentNumber(String equipmentNumber) { this.equipmentNumber = equipmentNumber; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getHealthScore() { return healthScore; }
    public void setHealthScore(Integer healthScore) { this.healthScore = healthScore; }
    public LocalDateTime getMaintenanceDue() { return maintenanceDue; }
    public void setMaintenanceDue(LocalDateTime maintenanceDue) { this.maintenanceDue = maintenanceDue; }
    public String getAssignedDockId() { return assignedDockId; }
    public void setAssignedDockId(String assignedDockId) { this.assignedDockId = assignedDockId; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
