package com.vadhvan.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rail_tracks")
public class RailTrack {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "\"trackNumber\"", nullable = false, unique = true)
    private String trackNumber;

    @Column(name = "status", nullable = false)
    private String status = "AVAILABLE";

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "destination")
    private String destination;

    @Column(name = "\"departureTime\"")
    private LocalDateTime departureTime;

    @Column(name = "\"containerIds\"", nullable = false)
    private String containerIds = "[]";

    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"updatedAt\"", nullable = false)
    private LocalDateTime updatedAt;

    public RailTrack() {}

    @PrePersist
    protected void onCreate() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.status == null) this.status = "AVAILABLE";
        if (this.containerIds == null) this.containerIds = "[]";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTrackNumber() { return trackNumber; }
    public void setTrackNumber(String trackNumber) { this.trackNumber = trackNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
    public String getContainerIds() { return containerIds; }
    public void setContainerIds(String containerIds) { this.containerIds = containerIds; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
