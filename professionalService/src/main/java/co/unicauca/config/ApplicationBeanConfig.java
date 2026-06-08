package co.unicauca.config;

import co.unicauca.domain.service.AvailableSlotsGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfig {

    @Bean
    public AvailableSlotsGenerator availableSlotsGenerator() {
        return new AvailableSlotsGenerator();
    }
}
