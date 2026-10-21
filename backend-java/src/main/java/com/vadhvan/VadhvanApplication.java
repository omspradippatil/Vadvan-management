package com.vadhvan;

import com.vadhvan.models.User;
import com.vadhvan.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;

@SpringBootApplication
@EnableScheduling
public class VadhvanApplication {

    public static void main(String[] args) {
        loadEnvIfPresent();
        assignAvailablePort();
        SpringApplication.run(VadhvanApplication.class, args);
    }

    private static void assignAvailablePort() {
        int preferredPort = 5001;
        String portStr = System.getProperty("PORT", System.getenv("PORT"));
        if (portStr != null) {
            try {
                preferredPort = Integer.parseInt(portStr.trim());
            } catch (Exception ignored) {}
        }

        int availablePort = findAvailablePort(preferredPort);
        System.setProperty("server.port", String.valueOf(availablePort));
        System.out.println("==================================================================");
        if (availablePort != preferredPort) {
            System.out.println("⚠️  [PortSync] Port " + preferredPort + " was occupied! Auto-shifted to port: " + availablePort);
        } else {
            System.out.println("🚀 [PortSync] Server port ready: " + availablePort);
        }
        System.out.println("==================================================================");

        writeActivePortFiles(availablePort);
    }

    private static int findAvailablePort(int startPort) {
        for (int p = startPort; p < startPort + 50; p++) {
            try (ServerSocket socket = new ServerSocket(p)) {
                return p;
            } catch (IOException ignored) {
                // Port is occupied, try next one
            }
        }
        return startPort;
    }

    private static void writeActivePortFiles(int port) {
        String[] paths = {
                ".active_backend_port",
                "../.active_backend_port",
                "backend-java/.active_port",
                ".active_port"
        };
        for (String p : paths) {
            try {
                File file = new File(p);
                File parent = file.getParentFile();
                if (parent == null || parent.exists()) {
                    Files.writeString(file.toPath(), String.valueOf(port));
                }
            } catch (Exception ignored) {}
        }
    }

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.findByEmail("omspradippatil@gmail.com").ifPresent(user -> {
                user.setPasswordHash(passwordEncoder.encode("Admin@123"));
                user.setRole("ADMIN");
                user.setStatus("ACTIVE");
                userRepository.save(user);
            });
            if (!userRepository.existsByEmail("admin@vadhvanport.in")) {
                User admin = new User(
                        "c_demo_admin_123",
                        "admin@vadhvanport.in",
                        passwordEncoder.encode("Admin@123"),
                        "Administrator",
                        "ADMIN",
                        "ACTIVE"
                );
                userRepository.save(admin);
            }
        };
    }

    private static void loadEnvIfPresent() {
        String[] potentialPaths = {
                ".env",
                "../backend/.env",
                "backend/.env",
                "backend-java/.env"
        };

        for (String path : potentialPaths) {
            File envFile = new File(path);
            if (envFile.exists() && envFile.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#") || !line.contains("=")) {
                            continue;
                        }
                        int eqIdx = line.indexOf('=');
                        String key = line.substring(0, eqIdx).trim();
                        String value = line.substring(eqIdx + 1).trim();

                        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
                            value = value.substring(1, value.length() - 1);
                        } else if (value.startsWith("'") && value.endsWith("'") && value.length() >= 2) {
                            value = value.substring(1, value.length() - 1);
                        }

                        if (System.getProperty(key) == null && System.getenv(key) == null) {
                            System.setProperty(key, value);
                        }
                    }
                } catch (Exception ignored) {
                }
                break;
            }
        }
    }
}
