package co.unicauca.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "scheduling_configurations")
@Getter
@Setter
public class SchedulingConfigurationEntity {
    @Id
    private Long id;

    @Column(name = "autonomous_scheduling_window_weeks", nullable = false)
    private Integer autonomousSchedulingWindowWeeks;
}
