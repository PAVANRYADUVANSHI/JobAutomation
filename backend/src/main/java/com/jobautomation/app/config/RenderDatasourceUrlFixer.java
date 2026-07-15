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
        String url = env.getProperty("SPRING_DATASOURCE_URL", "");
        if (url.startsWith("postgres://")) {
            String fixed = "jdbc:postgresql://" + url.substring("postgres://".length());
            env.getPropertySources().addFirst(new MapPropertySource(
                "renderUrlFix", Map.of("spring.datasource.url", fixed)
            ));
        }
    }
}
