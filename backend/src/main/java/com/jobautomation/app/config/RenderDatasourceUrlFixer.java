package com.jobautomation.app.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import java.util.Map;

/**
 * Render's free DB injects a postgres:// URL. Spring needs jdbc:postgresql://.
 * This initializer rewrites it before any beans are created.
 */
public class RenderDatasourceUrlFixer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        ConfigurableEnvironment env = ctx.getEnvironment();
        // Render injects DB URL under multiple possible env var names
        String[] candidates = {"SPRING_DATASOURCE_URL", "DATABASE_URL", "DB_URL"};
        for (String key : candidates) {
            String url = env.getProperty(key, "");
            if (url.startsWith("postgres://")) {
                String fixed = "jdbc:postgresql://" + url.substring("postgres://".length());
                env.getPropertySources().addFirst(new MapPropertySource(
                    "renderUrlFix", Map.of("spring.datasource.url", fixed)
                ));
                return;
            } else if (url.startsWith("jdbc:postgresql://")) {
                env.getPropertySources().addFirst(new MapPropertySource(
                    "renderUrlFix", Map.of("spring.datasource.url", url)
                ));
                return;
            }
        }
    }
}
