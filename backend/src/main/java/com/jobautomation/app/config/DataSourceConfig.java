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
        HikariConfig cfg = new HikariConfig();
        cfg.setDriverClassName("org.postgresql.Driver");
        cfg.setMaximumPoolSize(3);
        cfg.setMinimumIdle(1);
        cfg.setConnectionTimeout(30000);
        cfg.setIdleTimeout(600000);
        cfg.setMaxLifetime(1800000);

        // Strategy 1: use individual PG* vars (most reliable — no URL parsing)
        String pgHost = env("PGHOST");
        String pgPort = env("PGPORT", "5432");
        String pgDb   = env("PGDATABASE", env("POSTGRES_DB", "railway"));
        String pgUser = env("PGUSER", env("POSTGRES_USER", env("DB_USER", "postgres")));
        String pgPass = env("PGPASSWORD", env("POSTGRES_PASSWORD", env("DB_PASS", "")));

        if (pgHost != null) {
            String url = String.format("jdbc:postgresql://%s:%s/%s", pgHost, pgPort, pgDb);
            log.info("DataSource via PG vars: {}:{}/{}", pgHost, pgPort, pgDb);
            cfg.setJdbcUrl(url);
            cfg.setUsername(pgUser);
            cfg.setPassword(pgPass);
            return new HikariDataSource(cfg);
        }

        // Strategy 2: parse DATABASE_URL / SPRING_DATASOURCE_URL manually
        String raw = env("DATABASE_URL", env("SPRING_DATASOURCE_URL",
            "jdbc:postgresql://localhost:5432/job_automation"));

        // Strip jdbc: prefix
        String url = raw.startsWith("jdbc:") ? raw.substring(5) : raw;
        // Normalize scheme
        url = url.replaceFirst("^postgres://", "postgresql://")
                 .replaceFirst("^postgresql://", "postgresql://");

        // Extract user:pass@host:port/db
        // Format: postgresql://user:pass@host/db  OR  postgresql://user:pass@host:port/db
        try {
            String withoutScheme = url.substring("postgresql://".length());
            int atIdx = withoutScheme.lastIndexOf('@');
            String userInfo = withoutScheme.substring(0, atIdx);
            String hostDb = withoutScheme.substring(atIdx + 1);

            String user = userInfo.split(":", 2)[0];
            String pass = userInfo.split(":", 2)[1];

            int slashIdx = hostDb.indexOf('/');
            String hostPort = slashIdx >= 0 ? hostDb.substring(0, slashIdx) : hostDb;
            String db = slashIdx >= 0 ? hostDb.substring(slashIdx + 1) : "postgres";

            String host = hostPort.contains(":") ? hostPort.split(":")[0] : hostPort;
            String port = hostPort.contains(":") ? hostPort.split(":")[1] : "5432";

            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, db);
            log.info("DataSource via URL parse: {}:{}/{}", host, port, db);
            cfg.setJdbcUrl(jdbcUrl);
            cfg.setUsername(user);
            cfg.setPassword(pass);
        } catch (Exception e) {
            log.error("URL parse failed: {} — using raw", e.getMessage());
            cfg.setJdbcUrl(raw.startsWith("jdbc:") ? raw : "jdbc:postgresql://" + raw);
            cfg.setUsername(pgUser != null ? pgUser : "postgres");
            cfg.setPassword(pgPass != null ? pgPass : "");
        }

        return new HikariDataSource(cfg);
    }

    private String env(String key) { return System.getenv(key); }
    private String env(String key, String def) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : def;
    }
}
