package com.vadhvan.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "capacity", nullable = false)
    private Double capacity;

    @Column(name = "\"availableSpace\"", nullable = false)
    private Double availableSpace;

    @Column(name = "\"occupiedSpace\"", nullable = false)
    private Double occupiedSpace = 0.0;

    @Column(name = "\"locationLat\"")
    private Double locationLat;

    @Column(name = "\"locationLng\"")
    private Double locationLng;

    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"updatedAt\"", nullable = false)
    private LocalDateTime updatedAt;

    public Warehouse() {}

    @PrePersist
    protected void onCreate() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.occupiedSpace == null) this.occupiedSpace = 0.0;
        if (this.availableSpace == null && this.capacity != null) {
            this.availableSpace = this.capacity - this.occupiedSpace;
        }
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
    public Double getCapacity() { return capacity; }
    public void setCapacity(Double capacity) { this.capacity = capacity; }
    public Double getAvailableSpace() { return availableSpace; }
    public void setAvailableSpace(Double availableSpace) { this.availableSpace = availableSpace; }
    public Double getOccupiedSpace() { return occupiedSpace; }
    public void setOccupiedSpace(Double occupiedSpace) { this.occupiedSpace = occupiedSpace; }
    public Double getLocationLat() { return locationLat; }
    public void setLocationLat(Double locationLat) { this.locationLat = locationLat; }
    public Double getLocationLng() { return locationLng; }
    public void setLocationLng(Double locationLng) { this.locationLng = locationLng; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
