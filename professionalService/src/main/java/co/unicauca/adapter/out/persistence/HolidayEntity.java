package co.unicauca.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "holidays")
@Getter
@Setter
public class HolidayEntity {
    @Id
    @Column(name = "holiday_date")
    private LocalDate date;

    @Column(nullable = false, length = 150)
    private String name;
}
