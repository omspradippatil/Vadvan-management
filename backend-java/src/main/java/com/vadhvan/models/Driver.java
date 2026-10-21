package com.vadhvan.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "\"photoUrl\"")
    private String photoUrl;

    @Column(name = "\"licenseNo\"", nullable = false, unique = true)
    private String licenseNo;

    @Column(name = "\"licenseCategory\"", nullable = false)
    private String licenseCategory;

    @Column(name = "\"licenseExpiry\"", nullable = false)
    private LocalDateTime licenseExpiry;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "\"emergencyContact\"")
    private String emergencyContact;

    @Column(name = "\"safetyScore\"", nullable = false)
    private Integer safetyScore = 100;

    @Column(name = "\"experienceYears\"", nullable = false)
    private Integer experienceYears = 0;

    @Column(name = "status", nullable = false)
    private String status = "AVAILABLE";

    @Column(name = "\"vehicleId\"")
    private String vehicleId;

    @Column(name = "\"userId\"")
    private String userId;

    @Column(name = "\"createdAt\"", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "\"updatedAt\"", nullable = false)
    private LocalDateTime updatedAt;

    public Driver() {}

    @PrePersist
    protected void onCreate() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.status == null) this.status = "AVAILABLE";
        if (this.safetyScore == null) this.safetyScore = 100;
        if (this.experienceYears == null) this.experienceYears = 0;
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
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getLicenseNo() { return licenseNo; }
    public void setLicenseNo(String licenseNo) { this.licenseNo = licenseNo; }
    public String getLicenseCategory() { return licenseCategory; }
    public void setLicenseCategory(String licenseCategory) { this.licenseCategory = licenseCategory; }
    public LocalDateTime getLicenseExpiry() { return licenseExpiry; }
    public void setLicenseExpiry(LocalDateTime licenseExpiry) { this.licenseExpiry = licenseExpiry; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    public Integer getSafetyScore() { return safetyScore; }
    public void setSafetyScore(Integer safetyScore) { this.safetyScore = safetyScore; }
    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
