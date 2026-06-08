package co.unicauca.adapter.out.db.repository;

import co.unicauca.adapter.out.db.model.SchedulingConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSchedulingConfigurationRepository
        extends JpaRepository<SchedulingConfigurationEntity, Long> {
}
