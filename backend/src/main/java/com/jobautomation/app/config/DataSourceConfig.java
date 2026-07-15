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
        String raw = firstNonBlank(
            System.getenv("DATABASE_URL"),
            System.getenv("SPRING_DATASOURCE_URL"),
            System.getProperty("spring.datasource.url"),
            "jdbc:postgresql://localhost:5432/job_automation"
        );

        String jdbcUrl = toJdbcUrl(raw);
        log.info("DataSource connecting to: {}", jdbcUrl.replaceAll(":[^/@:]+@", ":***@"));

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(jdbcUrl);
        cfg.setMaximumPoolSize(3);
        cfg.setMinimumIdle(1);
        cfg.setConnectionTimeout(30000);
        cfg.setIdleTimeout(600000);
        cfg.setMaxLifetime(1800000);

        // If no @ in URL, credentials must be set separately
        if (!jdbcUrl.contains("@")) {
            cfg.setUsername(firstNonBlank(System.getenv("DB_USER"), System.getenv("PGUSER"), "postgres"));
            cfg.setPassword(firstNonBlank(System.getenv("DB_PASS"), System.getenv("PGPASSWORD"), ""));
        }

        return new HikariDataSource(cfg);
    }

    private String toJdbcUrl(String raw) {
        // Strip any existing jdbc: prefix to normalize
        String url = raw.replaceFirst("^jdbc:", "");

        // Normalize scheme to postgresql://
        url = url.replaceFirst("^postgres://", "postgresql://");

        // Inject port 5432 if missing: host/db → host:5432/db
        // Pattern: postgresql://user:pass@host/db  (no port)
        url = url.replaceFirst(
            "(postgresql://[^@]+@[^/:]+)(/)",
            "$15432$2"
        );

        return "jdbc:" + url;
    }

    private String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return "";
    }
}
