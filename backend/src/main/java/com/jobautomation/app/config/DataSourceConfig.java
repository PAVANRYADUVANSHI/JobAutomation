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

        // Handle postgres:// and postgresql:// — both need jdbc:postgresql://
        String jdbcUrl;
        if (raw.startsWith("jdbc:")) {
            jdbcUrl = raw;
        } else if (raw.startsWith("postgresql://")) {
            jdbcUrl = "jdbc:postgresql://" + raw.substring("postgresql://".length());
        } else if (raw.startsWith("postgres://")) {
            jdbcUrl = "jdbc:postgresql://" + raw.substring("postgres://".length());
        } else {
            jdbcUrl = raw;
        }

        log.info("DataSource connecting to: {}", jdbcUrl.replaceAll(":[^/@:]+@", ":***@"));

        boolean credsEmbedded = jdbcUrl.contains("@");

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
