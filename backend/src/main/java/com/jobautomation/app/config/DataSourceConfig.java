package com.jobautomation.app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

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

        log.info("Raw DB URL scheme: {}", raw.split("://")[0]);

        HikariConfig cfg = new HikariConfig();
        cfg.setDriverClassName("org.postgresql.Driver");
        cfg.setMaximumPoolSize(3);
        cfg.setMinimumIdle(1);
        cfg.setConnectionTimeout(30000);
        cfg.setIdleTimeout(600000);
        cfg.setMaxLifetime(1800000);

        try {
            // Normalize to a parseable URI by ensuring jdbc: prefix is removed
            // and scheme is postgresql://
            String uriStr = raw
                .replaceFirst("^jdbc:", "")
                .replaceFirst("^postgres://", "postgresql://");

            URI uri = new URI(uriStr);
            String host = uri.getHost();
            int port = uri.getPort() > 0 ? uri.getPort() : 5432;
            String db = uri.getPath().replaceFirst("^/", "");
            String userInfo = uri.getUserInfo();

            String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, db);
            log.info("DataSource JDBC URL: {}", jdbcUrl);
            cfg.setJdbcUrl(jdbcUrl);

            if (userInfo != null && userInfo.contains(":")) {
                cfg.setUsername(userInfo.split(":", 2)[0]);
                cfg.setPassword(userInfo.split(":", 2)[1]);
            } else {
                cfg.setUsername(firstNonBlank(System.getenv("DB_USER"), System.getenv("PGUSER"), "postgres"));
                cfg.setPassword(firstNonBlank(System.getenv("DB_PASS"), System.getenv("PGPASSWORD"), ""));
            }
        } catch (Exception e) {
            log.error("Failed to parse DB URL, using raw: {}", e.getMessage());
            cfg.setJdbcUrl(raw.startsWith("jdbc:") ? raw : "jdbc:" + raw);
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
