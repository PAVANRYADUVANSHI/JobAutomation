package com.jobautomation.app.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * Runs before any beans are created. Reads DATABASE_URL / SPRING_DATASOURCE_URL
 * directly from System.getenv (bypasses Spring property binding order issues)
 * and rewrites postgres:// → jdbc:postgresql:// into spring.datasource.url.
 */
public class RenderDatasourceUrlFixer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        String[] candidates = {"DATABASE_URL", "SPRING_DATASOURCE_URL", "DB_URL"};
        for (String key : candidates) {
            String url = System.getenv(key);
            if (url == null || url.isBlank()) continue;

            String jdbc = url.startsWith("postgres://")
                ? "jdbc:postgresql://" + url.substring("postgres://".length())
                : url;

            ctx.getEnvironment().getPropertySources().addFirst(
                new MapPropertySource("renderUrlFix", Map.of("spring.datasource.url", jdbc))
            );
            return;
        }
    }
}
