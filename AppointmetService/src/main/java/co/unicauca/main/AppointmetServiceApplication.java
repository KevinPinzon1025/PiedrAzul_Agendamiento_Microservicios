package co.unicauca.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "co.unicauca")
@EnableJpaRepositories(basePackages = "co.unicauca.Repository")
@EntityScan(basePackages = "co.unicauca.Entity")
public class AppointmetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmetServiceApplication.class, args);
	}

}
