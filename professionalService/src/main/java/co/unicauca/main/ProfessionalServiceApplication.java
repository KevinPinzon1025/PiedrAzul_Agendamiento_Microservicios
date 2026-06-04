package co.unicauca.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "co.unicauca")
@EnableJpaRepositories(basePackages = "co.unicauca.adapter.out.persistence")
@EntityScan(basePackages = "co.unicauca.adapter.out.persistence")
public class ProfessionalServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProfessionalServiceApplication.class, args);
    }
}
