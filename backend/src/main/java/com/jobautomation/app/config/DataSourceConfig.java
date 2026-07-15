package com.jobautomation.app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration @Slf4j
public class DataSourceConfig {

    @Value("${spring.datasource.url:}") private String url;
    @Value("${DATABASE_URL:}") private String databaseUrl;
    @Value("${spring.datasource.username:postgres}") private String username;
    @Value("${spring.datasource.password:}") private String password;

    @Bean @Primary
    public DataSource dataSource() {
        String resolved = resolveUrl();
        log.info("DataSource URL resolved: {}", resolved.replaceAll(":[^:@]+@", ":***@"));

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(resolved);
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(3);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setConnectionInitSql("SELECT 1");

        // For postgres:// URLs, username/password are embedded — don't set separately
        if (!resolved.contains("@")) {
            config.setUsername(username);
            config.setPassword(password);
        }

        return new HikariDataSource(config);
    }

    private String resolveUrl() {
        // Try SPRING_DATASOURCE_URL first, then DATABASE_URL
        String raw = (url != null && !url.isBlank()) ? url : databaseUrl;
        if (raw == null || raw.isBlank()) {
            return "jdbc:postgresql://localhost:5432/job_automation";
        }
        // Render injects postgres:// — rewrite to jdbc:postgresql://
        if (raw.startsWith("postgres://")) {
            return "jdbc:postgresql://" + raw.substring("postgres://".length());
        }
        return raw;
    }
}
