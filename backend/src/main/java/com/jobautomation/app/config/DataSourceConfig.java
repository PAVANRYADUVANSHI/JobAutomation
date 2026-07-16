package com.jobautomation.app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration @Slf4j
public class DataSourceConfig {

    @Bean @Primary
    public DataSource dataSource() {
        // Render injects PGHOST, PGPORT, PGDATABASE, PGUSER, PGPASSWORD automatically
        String host = e("PGHOST", e("DB_HOST", "localhost"));
        String port = e("PGPORT", "5432");
        String db   = e("PGDATABASE", e("POSTGRES_DB", e("DB_NAME", "job_automation")));
        String user = e("PGUSER",     e("POSTGRES_USER", e("DB_USER", "postgres")));
        String pass = e("PGPASSWORD", e("POSTGRES_PASSWORD", e("DB_PASS", "")));

        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, db);
        log.info("DB → {}:{}/{} user={}", host, port, db, user);

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(user);
        cfg.setPassword(pass);
        cfg.setDriverClassName("org.postgresql.Driver");
        cfg.setMaximumPoolSize(3);
        cfg.setMinimumIdle(1);
        cfg.setConnectionTimeout(30000);
        cfg.setIdleTimeout(600000);
        cfg.setMaxLifetime(1800000);
        return new HikariDataSource(cfg);
    }

    private String e(String key, String def) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : def;
    }
}
