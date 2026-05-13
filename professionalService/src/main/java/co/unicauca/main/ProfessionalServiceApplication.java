package co.unicauca.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "co.unicauca")
@EnableJpaRepositories(basePackages = "co.unicauca.Repository")
@EntityScan(basePackages = "co.unicauca.Entity")
public class ProfessionalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfessionalServiceApplication.class, args);
    }
}
