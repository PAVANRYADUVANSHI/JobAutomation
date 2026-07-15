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
        log.info("DataSource URL: {}", jdbcUrl.replaceAll(":[^/@:]+@", ":***@"));

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(jdbcUrl);
        cfg.setDriverClassName("org.postgresql.Driver");
        cfg.setMaximumPoolSize(3);
        cfg.setMinimumIdle(1);
        cfg.setConnectionTimeout(30000);
        cfg.setIdleTimeout(600000);
        cfg.setMaxLifetime(1800000);

        if (!jdbcUrl.contains("@")) {
            cfg.setUsername(firstNonBlank(System.getenv("DB_USER"), System.getenv("PGUSER"), "postgres"));
            cfg.setPassword(firstNonBlank(System.getenv("DB_PASS"), System.getenv("PGPASSWORD"), ""));
        }

        return new HikariDataSource(cfg);
    }

    private String toJdbcUrl(String raw) {
        // Remove jdbc: prefix to normalize
        String url = raw.startsWith("jdbc:") ? raw.substring(5) : raw;

        // Normalize to postgresql://
        if (url.startsWith("postgres://")) {
            url = "postgresql://" + url.substring("postgres://".length());
        }

        // Inject :5432 before the path if no port present
        // e.g. postgresql://user:pass@host/db  →  postgresql://user:pass@host:5432/db
        if (url.startsWith("postgresql://")) {
            // Find the @ then look for host part
            int atIdx = url.lastIndexOf('@');
            if (atIdx >= 0) {
                String afterAt = url.substring(atIdx + 1); // host/db or host:port/db
                if (!afterAt.contains(":")) {
                    // No port — inject :5432 before the /
                    int slashIdx = afterAt.indexOf('/');
                    if (slashIdx >= 0) {
                        String host = afterAt.substring(0, slashIdx);
                        String rest = afterAt.substring(slashIdx);
                        url = url.substring(0, atIdx + 1) + host + ":5432" + rest;
                    }
                }
            }
        }

        return "jdbc:" + url;
    }

    private String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return "";
    }
}
