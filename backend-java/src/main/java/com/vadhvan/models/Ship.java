package com.vadhvan.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ships")
public class Ship {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "\"imoNumber\"", nullable = false, unique = true)
    private String imoNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "\"arrivalTime\"", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "\"expectedDeparture\"")
    private LocalDateTime expectedDeparture;

    @Column(name = "\"dockId\"")
    private String dockId;

    @Column(name = "\"containerCount\"", nullable = false)
    private Integer containerCount = 0;

    @Column(name = "priority", nullable = false)
    private String priority = "MEDIUM";

    @Column(name = "\"cargoType\"", nullable = false)
    private String cargoType;

    @Column(name = "\"shipLength\"")
    private Double shipLength;

    @Column(name = "\"shipWidth\"")
    private Double shipWidth;

    @Column(name = "draft")
    private Double draft;

    @Column(name = "status", nullable = false)
    private String status = "WAITING";

    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"updatedAt\"", nullable = false)
    private LocalDateTime updatedAt;

    public Ship() {}

    @PrePersist
    protected void onCreate() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.status == null) this.status = "WAITING";
        if (this.priority == null) this.priority = "MEDIUM";
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
    public String getImoNumber() { return imoNumber; }
    public void setImoNumber(String imoNumber) { this.imoNumber = imoNumber; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public LocalDateTime getExpectedDeparture() { return expectedDeparture; }
    public void setExpectedDeparture(LocalDateTime expectedDeparture) { this.expectedDeparture = expectedDeparture; }
    public String getDockId() { return dockId; }
    public void setDockId(String dockId) { this.dockId = dockId; }
    public Integer getContainerCount() { return containerCount; }
    public void setContainerCount(Integer containerCount) { this.containerCount = containerCount; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getCargoType() { return cargoType; }
    public void setCargoType(String cargoType) { this.cargoType = cargoType; }
    public Double getShipLength() { return shipLength; }
    public void setShipLength(Double shipLength) { this.shipLength = shipLength; }
    public Double getShipWidth() { return shipWidth; }
    public void setShipWidth(Double shipWidth) { this.shipWidth = shipWidth; }
    public Double getDraft() { return draft; }
    public void setDraft(Double draft) { this.draft = draft; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
