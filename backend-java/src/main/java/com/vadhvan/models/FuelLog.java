package com.vadhvan.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fuel_logs")
public class FuelLog {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "\"vehicleId\"", nullable = false)
    private String vehicleId;

    @Column(name = "\"driverId\"")
    private String driverId;

    @Column(name = "\"tripId\"")
    private String tripId;

    @Column(name = "\"quantityLitres\"", nullable = false)
    private Double quantityLitres;

    @Column(name = "\"costPerLitre\"", nullable = false)
    private Double costPerLitre;

    @Column(name = "\"totalCost\"", nullable = false)
    private Double totalCost;

    @Column(name = "mileage")
    private Double mileage;

    @Column(name = "\"distanceCovered\"")
    private Double distanceCovered;

    @Column(name = "\"loggedAt\"", nullable = false)
    private LocalDateTime loggedAt;

    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public FuelLog() {}

    @PrePersist
    protected void onCreate() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.loggedAt == null) this.loggedAt = LocalDateTime.now();
        if (this.totalCost == null && this.quantityLitres != null && this.costPerLitre != null) {
            this.totalCost = this.quantityLitres * this.costPerLitre;
        }
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }
    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }
    public Double getQuantityLitres() { return quantityLitres; }
    public void setQuantityLitres(Double quantityLitres) { this.quantityLitres = quantityLitres; }
    public Double getCostPerLitre() { return costPerLitre; }
    public void setCostPerLitre(Double costPerLitre) { this.costPerLitre = costPerLitre; }
    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
    public Double getMileage() { return mileage; }
    public void setMileage(Double mileage) { this.mileage = mileage; }
    public Double getDistanceCovered() { return distanceCovered; }
    public void setDistanceCovered(Double distanceCovered) { this.distanceCovered = distanceCovered; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
