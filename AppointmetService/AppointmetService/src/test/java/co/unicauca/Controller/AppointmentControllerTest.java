package co.unicauca.Controller;

import co.unicauca.Entity.facade.AppointmentFacade;
import co.unicauca.Entity.model.Appointment;
import co.unicauca.Entity.model.Patient;
import co.unicauca.Entity.model.Professional;
import co.unicauca.Service.AppointmentService;
import co.unicauca.infra.dto.CreateAppointmentRequestDTO;
import co.unicauca.infra.dto.SchedulingType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private AppointmentFacade appointmentFacade;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = objectMapperForApi();

    @BeforeEach
    void setUp() {
        AppointmentController controller = new AppointmentController(appointmentFacade, appointmentService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    private static ObjectMapper objectMapperForApi() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Test
    void listAll_returnsOkAndJsonArray() throws Exception {
        Appointment a = sampleAppointment(10L);
        when(appointmentService.findAll()).thenReturn(List.of(a));

        mockMvc.perform(get("/appointment/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idAppointment").value(10))
                .andExpect(jsonPath("$[0].observation").value("Consulta"));

        verify(appointmentService).findAll();
    }

    @Test
    void findByProfessionalAndDate_returnsOk() throws Exception {
        LocalDate date = LocalDate.of(2026, 5, 20);
        when(appointmentService.findByProfessionalAndDate("Dr. Pérez", date))
                .thenReturn(List.of(sampleAppointment(1L)));

        mockMvc.perform(get("/appointment/search")
                        .param("professional", "Dr. Pérez")
                        .param("date", "2026-05-20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idAppointment").value(1));

        verify(appointmentService).findByProfessionalAndDate(eq("Dr. Pérez"), eq(date));
    }

    @Test
    void create_returnsCreatedWithLocation() throws Exception {
        Appointment created = sampleAppointment(42L);
        when(appointmentFacade.createAppointment(any(CreateAppointmentRequestDTO.class)))
                .thenReturn(created);

        CreateAppointmentRequestDTO body = new CreateAppointmentRequestDTO();
        body.setPatientId(1L);
        body.setProfessionalId(2L);
        body.setObservation("Consulta");
        body.setAppointmentDate(LocalDateTime.of(2026, 6, 1, 9, 0));
        body.setSchedulingType(SchedulingType.MANUAL);

        mockMvc.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.containsString("/appointment/42")))
                .andExpect(jsonPath("$.idAppointment").value(42));

        verify(appointmentFacade).createAppointment(any(CreateAppointmentRequestDTO.class));
    }

    @Test
    void cancel_whenFound_returnsNoContent() throws Exception {
        when(appointmentService.cancelById(7L)).thenReturn(true);

        mockMvc.perform(delete("/appointment/7"))
                .andExpect(status().isNoContent());

        verify(appointmentService).cancelById(7L);
    }

    @Test
    void cancel_whenNotFound_returns404() throws Exception {
        when(appointmentService.cancelById(99L)).thenReturn(false);

        mockMvc.perform(delete("/appointment/99"))
                .andExpect(status().isNotFound());
    }

    private static Appointment sampleAppointment(long id) {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setPatName("Paciente");

        Professional professional = new Professional();
        professional.setId(2L);
        professional.setProfName("Profesional");

        Appointment appointment = new Appointment();
        appointment.setIdAppointment(id);
        appointment.setSchedulingDate(LocalDateTime.of(2026, 5, 10, 8, 0));
        appointment.setAppointmentDate(LocalDateTime.of(2026, 5, 15, 10, 0));
        appointment.setObservation("Consulta");
        appointment.setPatient(patient);
        appointment.setProfessional(professional);
        return appointment;
    }
}
