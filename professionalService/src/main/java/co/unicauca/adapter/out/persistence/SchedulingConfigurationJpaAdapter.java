package co.unicauca.adapter.out.persistence;

import co.unicauca.domain.model.SchedulingConfiguration;
import co.unicauca.domain.valueobject.SchedulingWindow;
import co.unicauca.port.out.SchedulingConfigurationRepositoryPort;
import org.springframework.stereotype.Repository;

@Repository
public class SchedulingConfigurationJpaAdapter implements SchedulingConfigurationRepositoryPort {
    private static final Long DEFAULT_ID = 1L;
    private static final int DEFAULT_WINDOW_WEEKS = 4;
    private final SpringDataSchedulingConfigurationRepository repository;

    public SchedulingConfigurationJpaAdapter(SpringDataSchedulingConfigurationRepository repository) {
        this.repository = repository;
    }

    @Override
    public SchedulingConfiguration getConfiguration() {
        return repository.findById(DEFAULT_ID)
                .map(entity -> new SchedulingConfiguration(entity.getId(), new SchedulingWindow(entity.getAutonomousSchedulingWindowWeeks())))
                .orElseGet(() -> save(new SchedulingConfiguration(DEFAULT_ID, new SchedulingWindow(DEFAULT_WINDOW_WEEKS))));
    }

    @Override
    public SchedulingConfiguration save(SchedulingConfiguration configuration) {
        SchedulingConfigurationEntity entity = new SchedulingConfigurationEntity();
        entity.setId(DEFAULT_ID);
        entity.setAutonomousSchedulingWindowWeeks(configuration.getAutonomousSchedulingWindow().weeks());
        SchedulingConfigurationEntity saved = repository.save(entity);
        return new SchedulingConfiguration(saved.getId(), new SchedulingWindow(saved.getAutonomousSchedulingWindowWeeks()));
    }
}
