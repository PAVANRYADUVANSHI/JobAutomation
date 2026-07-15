package com.jobautomation.app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        // Render auto-injects DATABASE_URL on every service with a linked DB.
        // Also check SPRING_DATASOURCE_URL (set via render.yaml envVars).
        // Fall back to localhost for local dev.
        String raw = firstNonBlank(
            System.getenv("DATABASE_URL"),
            System.getenv("SPRING_DATASOURCE_URL"),
            System.getProperty("spring.datasource.url"),
            "jdbc:postgresql://localhost:5432/job_automation"
        );

        String jdbcUrl = raw.startsWith("postgres://")
            ? "jdbc:postgresql://" + raw.substring("postgres://".length())
            : raw;

        log.info("DataSource connecting to: {}", jdbcUrl.replaceAll(":[^/@:]+@", ":***@"));

        // For postgres:// style URLs, credentials are embedded in the URL
        boolean credsEmbedded = raw.startsWith("postgres://") || jdbcUrl.contains("@");

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(jdbcUrl);
        cfg.setDriverClassName("org.postgresql.Driver");
        cfg.setMaximumPoolSize(3);
        cfg.setMinimumIdle(1);
        cfg.setConnectionTimeout(30000);
        cfg.setIdleTimeout(600000);
        cfg.setMaxLifetime(1800000);

        if (!credsEmbedded) {
            cfg.setUsername(firstNonBlank(System.getenv("DB_USER"), System.getenv("PGUSER"), "postgres"));
            cfg.setPassword(firstNonBlank(System.getenv("DB_PASS"), System.getenv("PGPASSWORD"), ""));
        }

        return new HikariDataSource(cfg);
    }

    private String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return "";
    }
}
