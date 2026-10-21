package com.vadhvan;

import com.vadhvan.models.User;
import com.vadhvan.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@SpringBootApplication
public class VadhvanApplication {

    public static void main(String[] args) {
        loadEnvIfPresent();
        SpringApplication.run(VadhvanApplication.class, args);
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

                        // If not already set in system properties or environment, set system property
                        if (System.getProperty(key) == null && System.getenv(key) == null) {
                            System.setProperty(key, value);
                        }
                    }
                } catch (Exception ignored) {
                    // Fail silently, fallback to application.properties defaults
                }
                break;
            }
        }
    }
}
