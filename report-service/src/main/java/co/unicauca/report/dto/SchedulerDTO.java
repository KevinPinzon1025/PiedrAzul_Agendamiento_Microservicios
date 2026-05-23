package co.unicauca.report.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchedulerDTO {
    private Long id;
    private String schedulerName;
    private String name;
}
