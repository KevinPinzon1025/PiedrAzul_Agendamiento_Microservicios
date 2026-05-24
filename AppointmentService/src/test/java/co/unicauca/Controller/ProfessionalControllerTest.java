package co.unicauca.Controller;

import co.unicauca.Entity.model.Professional;
import co.unicauca.Service.ProfessionalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProfessionalControllerTest {

    @Mock
    private ProfessionalService professionalService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        ProfessionalController controller = new ProfessionalController(professionalService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
                .build();
    }

    @Test
    void listAllProfessionals_returnsOkAndBody() throws Exception {
        Professional p = new Professional();
        p.setId(5L);
        p.setProfName("Dra. López");
        when(professionalService.findAll()).thenReturn(List.of(p));

        mockMvc.perform(get("/appointment/professionals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[0].profName").value("Dra. López"));

        verify(professionalService).findAll();
    }

    @Test
    void listAllProfessionals_empty_returnsOk() throws Exception {
        when(professionalService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/appointment/professionals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
