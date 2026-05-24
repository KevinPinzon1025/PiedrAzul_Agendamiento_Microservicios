/*package co.unicauca.Entity.facade;

import co.unicauca.Entity.model.Appointment;
import co.unicauca.Entity.state.CreatedAppointment;
import co.unicauca.Service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentFacadeTest {

    private AppointmentService appointmentService;
    private AppointmentFacade facade;

    private Appointment baseAppointment() {
        Appointment appointment = new Appointment(new CreatedAppointment());
        appointment.setObservation("Consulta general");
        appointment.setSchedulingDate(LocalDateTime.now());
        appointment.setAppointmenDate(LocalDateTime.now().plusDays(1));
        appointment.setPatient(1L);
        appointment.setProfessional(1L);
        appointment.setScheduler(1L);
        return appointment;
    }

    @BeforeEach
    void setUp() {
        appointmentService = mock(AppointmentService.class);
        facade = new AppointmentFacade(appointmentService);

        // Inyección manual del mock
        facade = Mockito.spy(facade);
        doReturn(appointmentService).when(facade).getAppointmentService();
    }

    @Test
    void facade_createsAppointment_andSavesIt() {
        Appointment appointment = baseAppointment();

        when(appointmentService.save(any(Appointment.class)))
                .thenReturn(appointment);

        Appointment result = facade.createAppointment(appointment);

        assertNotNull(result);

        verify(appointmentService, times(1))
                .save(any(Appointment.class));
    }

    @Test
    void facade_doesNotModifyObservationUnexpectedly() {
        Appointment appointment = baseAppointment();

        when(appointmentService.save(any(Appointment.class)))
                .thenReturn(appointment);

        facade.createAppointment(appointment);

        assertEquals("Consulta general", appointment.getObservation());
    }

    @Test
    void facade_callsServiceOnlyOnce() {
        Appointment appointment = baseAppointment();

        when(appointmentService.save(any(Appointment.class)))
                .thenReturn(appointment);

        facade.createAppointment(appointment);

        verify(appointmentService, times(1))
                .save(any(Appointment.class));
    }
}
 */