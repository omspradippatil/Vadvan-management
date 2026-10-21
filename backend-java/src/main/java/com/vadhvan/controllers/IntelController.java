package com.vadhvan.controllers;

import com.vadhvan.dto.ApiResponse;
import com.vadhvan.models.*;
import com.vadhvan.security.UserPrincipal;
import com.vadhvan.services.IntelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class IntelController {

    private final IntelService intelService;

    public IntelController(IntelService intelService) {
        this.intelService = intelService;
    }

    // ========================================================================
    // SSE REAL-TIME EVENTS
    // ========================================================================
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter(180_000L); // 3 minutes timeout
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("Connected to Vadhvan Port real-time stream"));
        } catch (IOException e) {
            emitter.complete();
        }
        return emitter;
    }

    // ========================================================================
    // PORT HEALTH & RECOMMENDATIONS
    // ========================================================================
    @GetMapping("/port-health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPortHealth() {
        return ResponseEntity.ok(ApiResponse.success(intelService.getPortHealth()));
    }

    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<Map<String, String>>> getRecommendation() {
        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "action", "OPTIMIZE_LOGISTICS",
                "message", "Port operations are flowing smoothly. Ensure crane and reach stacker preventative checks are on schedule."
        )));
    }

    // ========================================================================
    // ANALYTICS & KPIS
    // ========================================================================
    @GetMapping("/analytics/kpis")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getKPIs() {
        return ResponseEntity.ok(ApiResponse.success(intelService.getKPIs()));
    }

    @GetMapping("/analytics/charts")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCharts() {
        return ResponseEntity.ok(ApiResponse.success(intelService.getCharts()));
    }

    // ========================================================================
    // REPORTS
    // ========================================================================
    @GetMapping("/reports/fleet")
    public ResponseEntity<ApiResponse<List<Vehicle>>> getFleetReport() {
        return ResponseEntity.ok(ApiResponse.success(intelService.getFleetReport()));
    }

    @GetMapping("/reports/trips")
    public ResponseEntity<ApiResponse<List<Trip>>> getTripReport() {
        return ResponseEntity.ok(ApiResponse.success(intelService.getTripReport()));
    }

    @GetMapping("/reports/fuel")
    public ResponseEntity<ApiResponse<List<FuelLog>>> getFuelReport() {
        return ResponseEntity.ok(ApiResponse.success(intelService.getFuelReport()));
    }

    @GetMapping("/reports/expenses")
    public ResponseEntity<ApiResponse<List<Expense>>> getExpenseReport() {
        return ResponseEntity.ok(ApiResponse.success(intelService.getExpenseReport()));
    }

    @GetMapping("/reports/containers")
    public ResponseEntity<ApiResponse<List<Container>>> getContainerReport() {
        return ResponseEntity.ok(ApiResponse.success(intelService.getContainerReport()));
    }

    // ========================================================================
    // NOTIFICATIONS
    // ========================================================================
    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse<List<Notification>>> getNotifications(@AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getId() : null;
        return ResponseEntity.ok(ApiResponse.success(intelService.getNotifications(userId)));
    }

    @PutMapping("/notifications/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markRead(@PathVariable String id) {
        intelService.markNotificationRead(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Notification marked as read"));
    }

    @PutMapping("/notifications/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllRead() {
        intelService.markAllNotificationsRead();
        return ResponseEntity.ok(ApiResponse.success(null, "All notifications marked as read"));
    }

    // ========================================================================
    // SETTINGS
    // ========================================================================
    @GetMapping("/settings")
    public ResponseEntity<ApiResponse<Settings>> getSettings() {
        return ResponseEntity.ok(ApiResponse.success(intelService.getSettings()));
    }

    @PutMapping("/settings")
    public ResponseEntity<ApiResponse<Settings>> updateSettings(@RequestBody Settings settings) {
        return ResponseEntity.ok(ApiResponse.success(intelService.updateSettings(settings), "Settings updated"));
    }

    // ========================================================================
    // ACTIVITY LOGS
    // ========================================================================
    @GetMapping("/activity")
    public ResponseEntity<ApiResponse<List<ActivityLog>>> getActivity() {
        return ResponseEntity.ok(ApiResponse.success(intelService.getActivityLogs()));
    }

    // ========================================================================
    // USERS MANAGEMENT (ADMIN)
    // ========================================================================
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(intelService.getAllUsers()));
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(intelService.getUserById(id)));
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        User created = intelService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "User created successfully"));
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable String id, @RequestBody User user) {
        User updated = intelService.updateUser(id, user);
        return ResponseEntity.ok(ApiResponse.success(updated, "User updated successfully"));
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        intelService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }
}
