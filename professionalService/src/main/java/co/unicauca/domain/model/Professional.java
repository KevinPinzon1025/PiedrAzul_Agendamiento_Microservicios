package co.unicauca.domain.model;

import co.unicauca.domain.exception.InvalidAvailabilityException;
import co.unicauca.domain.valueobject.ProfessionalEmail;
import co.unicauca.domain.valueobject.ProfessionalId;
import co.unicauca.domain.valueobject.ProfessionalName;
import co.unicauca.domain.valueobject.ProfessionalType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Professional {
    private ProfessionalId id;
    private ProfessionalName name;
    private ProfessionalEmail email;
    private ProfessionalType type;
    private final List<WorkingDay> workingDays = new ArrayList<>();

    public Professional(ProfessionalId id, ProfessionalName name, ProfessionalEmail email, ProfessionalType type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.type = Objects.requireNonNull(type, "El tipo de profesional es obligatorio");
    }

    public static Professional createNew(ProfessionalName name, ProfessionalEmail email, ProfessionalType type) {
        return new Professional(null, name, email, type);
    }

    public void updateBasicInformation(ProfessionalName name, ProfessionalEmail email, ProfessionalType type) {
        this.name = name;
        this.email = email;
        this.type = Objects.requireNonNull(type, "El tipo de profesional es obligatorio");
    }

    public void configureAvailability(List<WorkingDay> newWorkingDays) {
        if (newWorkingDays == null || newWorkingDays.isEmpty()) {
            throw new InvalidAvailabilityException("Debe configurar al menos una franja de atención");
        }
        validateNoOverlaps(newWorkingDays);
        workingDays.clear();
        workingDays.addAll(newWorkingDays);
    }

    private void validateNoOverlaps(List<WorkingDay> days) {
        for (int i = 0; i < days.size(); i++) {
            for (int j = i + 1; j < days.size(); j++) {
                if (days.get(i).overlaps(days.get(j))) {
                    throw new InvalidAvailabilityException("Existen franjas horarias solapadas para el mismo día");
                }
            }
        }
    }

    public ProfessionalId getId() { return id; }
    public void assignId(ProfessionalId id) { this.id = id; }
    public ProfessionalName getName() { return name; }
    public ProfessionalEmail getEmail() { return email; }
    public ProfessionalType getType() { return type; }
    public List<WorkingDay> getWorkingDays() { return Collections.unmodifiableList(workingDays); }
}
