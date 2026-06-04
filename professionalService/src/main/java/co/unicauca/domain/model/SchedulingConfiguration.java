package co.unicauca.domain.model;

import co.unicauca.domain.valueobject.SchedulingWindow;

public class SchedulingConfiguration {
    private final Long id;
    private final SchedulingWindow autonomousSchedulingWindow;

    public SchedulingConfiguration(Long id, SchedulingWindow autonomousSchedulingWindow) {
        this.id = id;
        this.autonomousSchedulingWindow = autonomousSchedulingWindow;
    }

    public Long getId() { return id; }
    public SchedulingWindow getAutonomousSchedulingWindow() { return autonomousSchedulingWindow; }
}
