package com.vadhvan.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "settings")
public class Settings {

    @Id
    @Column(name = "id", nullable = false)
    private String id = "settings";

    @Column(name = "\"orgName\"", nullable = false)
    private String orgName = "Vadhvan Port";

    @Column(name = "theme", nullable = false)
    private String theme = "light";

    @Column(name = "language", nullable = false)
    private String language = "en";

    @Column(name = "\"notificationPrefs\"", nullable = false)
    private String notificationPrefs = "{}";

    @Column(name = "\"updatedAt\"", nullable = false)
    private LocalDateTime updatedAt;

    public Settings() {}

    public Settings(String id, String orgName, String theme, String language, String notificationPrefs) {
        this.id = id != null ? id : "settings";
        this.orgName = orgName != null ? orgName : "Vadhvan Port";
        this.theme = theme != null ? theme : "light";
        this.language = language != null ? language : "en";
        this.notificationPrefs = notificationPrefs != null ? notificationPrefs : "{}";
    }

    @PrePersist
    @PreUpdate
    protected void onSave() {
        if (this.id == null || this.id.isBlank()) {
            this.id = "settings";
        }
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getNotificationPrefs() { return notificationPrefs; }
    public void setNotificationPrefs(String notificationPrefs) { this.notificationPrefs = notificationPrefs; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
