package com.vadhvan.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url:}")
    private String rawUrl;

    @Value("${spring.datasource.username:postgres}")
    private String defaultUsername;

    @Value("${spring.datasource.password:postgres}")
    private String defaultPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        String dbUrl = rawUrl;
        String username = defaultUsername;
        String password = defaultPassword;

        // If DATABASE_URL environment variable is provided in URI format (e.g. postgres://user:pass@host:5432/db)
        if (dbUrl != null && (dbUrl.startsWith("postgres://") || dbUrl.startsWith("postgresql://"))) {
            try {
                // Remove ?pgbouncer=true or other non-standard params if present
                String cleanUrl = dbUrl.replace("postgres://", "http://").replace("postgresql://", "http://");
                URI uri = new URI(cleanUrl);

                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String path = uri.getPath();

                if (uri.getUserInfo() != null) {
                    String[] userInfo = uri.getUserInfo().split(":");
                    username = userInfo[0];
                    if (userInfo.length > 1) {
                        password = userInfo[1];
                    }
                }

                dbUrl = "jdbc:postgresql://" + host + ":" + port + path;
                if (uri.getQuery() != null) {
                    dbUrl += "?" + uri.getQuery();
                }
            } catch (Exception ignored) {
                // Fallback to standard url handling
            }
        }

        if (dbUrl == null || dbUrl.isBlank()) {
            dbUrl = "jdbc:postgresql://localhost:5432/vadhvan_port";
        }

        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(dbUrl)
                .username(username)
                .password(password)
                .build();
    }
}
