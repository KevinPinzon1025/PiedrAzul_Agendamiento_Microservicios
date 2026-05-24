package co.unicauca.Controller;

import co.unicauca.Service.ProfessionalWorkingDayService;
import co.unicauca.infra.dto.SlotResponseDTO;
import co.unicauca.infra.dto.WorkingDayDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProfessionalAvailabilityControllerTest {

    @Mock
    private ProfessionalWorkingDayService professionalWorkingDayService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = objectMapperForApi();

    @BeforeEach
    void setUp() {
        ProfessionalAvailabilityController controller =
                new ProfessionalAvailabilityController(professionalWorkingDayService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(
                        new StringHttpMessageConverter(StandardCharsets.UTF_8),
                        new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    private static ObjectMapper objectMapperForApi() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Test
    void configureAvailability_returnsOkMessage() throws Exception {
        doNothing().when(professionalWorkingDayService)
                .configureAvailability(eq(10L), anyList());

        WorkingDayDTO dto = new WorkingDayDTO(
                DayOfWeek.MONDAY,
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                30
        );

        mockMvc.perform(post("/appointment/agenda/10/availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(dto))))
                .andExpect(status().isOk())
                .andExpect(content().string("Disponibilidad configurada correctamente"));

        verify(professionalWorkingDayService).configureAvailability(eq(10L), anyList());
    }

    @Test
    void getAvailableSlots_returnsOkAndJson() throws Exception {
        List<SlotResponseDTO> slots = List.of(
                new SlotResponseDTO(LocalTime.of(9, 0), LocalTime.of(9, 30), true),
                new SlotResponseDTO(LocalTime.of(9, 30), LocalTime.of(10, 0), true)
        );
        when(professionalWorkingDayService.getAvailableSlots(2L, LocalDate.of(2026, 5, 12)))
                .thenReturn(slots);

        mockMvc.perform(get("/appointment/agenda/2/available-slots")
                        .param("date", "2026-05-12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].startTime").value("09:00:00"))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[1].endTime").value("10:00:00"));

        verify(professionalWorkingDayService).getAvailableSlots(
                eq(2L),
                eq(LocalDate.of(2026, 5, 12))
        );
    }

    @Test
    void getAvailableSlots_whenNoSlots_returnsEmptyArray() throws Exception {
        when(professionalWorkingDayService.getAvailableSlots(1L, LocalDate.of(2026, 5, 10)))
                .thenReturn(List.of());

        mockMvc.perform(get("/appointment/agenda/1/available-slots")
                        .param("date", "2026-05-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
