package edu.ivytech.budget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

/**
 * Entry point. Run this class (or "mvn spring-boot:run")
 * and open http://localhost:8080 in a browser.
 */
@SpringBootApplication
public class BudgetingApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BudgetingApplication.class, args);
    }

    /**
     * Always use US format (dollars, 1,500.00) no matter what language
     * the user's browser is set to.
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(Locale.US);
    }

    /** Allows the .war file to also run on a standalone Tomcat server. */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BudgetingApplication.class);
    }
}
