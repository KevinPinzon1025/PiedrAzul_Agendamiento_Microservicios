
package co.unicauca.frontend.client;


import co.unicauca.frontend.dto.*;
import co.unicauca.frontend.util.IAdapter;
import co.unicauca.frontend.util.JsonUtil;
import co.unicauca.frontend.util.ProfessionalAdapter;
import co.unicauca.frontend.util.PatientAdapter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AppointmentHttpClient {

    private final String BASE_URL = "http://localhost:8080/appointment";
    private final HttpClient client = HttpClient.newHttpClient();

    private List<PatientDTO> patientCache;

    private List<ProfessionalDTO> professionalCache;

    private final IAdapter professionalAdapter =
            new ProfessionalAdapter();
    private final IAdapter patientAdapter =
            new PatientAdapter();

    public List<AppointmentDTO> findByProfessionalAndDate(
            String professional,
            LocalDate date
    ) {

        try {

            String encodedProfessional =
                    URLEncoder.encode(
                            professional,
                            StandardCharsets.UTF_8
                    );

            String url =BASE_URL +
                        "/search?professional=" + encodedProfessional +
                        "&date=" + date;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            return JsonUtil.fromJsonList(
                    response.body(),
                    AppointmentDTO.class
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllProfessionals() {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/professionals"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            professionalCache = JsonUtil.fromJsonList(
                    response.body(),
                    ProfessionalDTO.class
            );

            return (List<String>) professionalAdapter.adapt(professionalCache);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    public Long getProfessionalIdByName(String name) {

        if (professionalCache == null) {

            getAllProfessionals();
        }

        return professionalCache.stream()
                .filter(p -> p.getProfName().equals(name))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Profesional no encontrado"
                        ))
                .getId();
    }

    public List<String> getAllPatients() {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/patients"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            patientCache = JsonUtil.fromJsonList(
                    response.body(),
                    PatientDTO.class
            );

            return (List<String>) patientAdapter.adapt(patientCache);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    public Long getPatientIdByName(String name) {

        if (patientCache == null) {

            getAllPatients();
        }

        return patientCache.stream()
                .filter(p -> p.getPatName().equals(name))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Paciente no encontrado"
                        ))
                .getId();
    }

    public void createAppointment(CreateAppointmentRequestDTO request) {

        try {

            String json = JsonUtil.toJson(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL ))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 201) {

                throw new RuntimeException(
                        "Error creando cita: " + response.body()
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    public List<String> getAvailableSlots(
            Long professionalId,
            LocalDate date
    ) {

        try {

            String url =
                    BASE_URL +
                            "/agenda/" +
                            professionalId +
                            "/available-slots?date=" +
                            date;

            System.out.println("Consultando URL:");
            System.out.println(url);

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .GET()
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

          /*  System.out.println("STATUS:");
            System.out.println(response.statusCode());

            System.out.println("BODY:");
            System.out.println(response.body());*/

            if (response.statusCode() != 200) {

                throw new RuntimeException(
                        "Error obteniendo slots. Status: "
                                + response.statusCode()
                                + " Body: "
                                + response.body()
                );
            }

            ObjectMapper mapper = new ObjectMapper();

            List<SlotResponseDTO> slots =
                    mapper.readValue(
                            response.body(),
                            new TypeReference<List<SlotResponseDTO>>() {}
                    );

            List<String> formattedSlots =
                    new ArrayList<>();

            for (SlotResponseDTO slot : slots) {

                formattedSlots.add(
                        slot.getStartTime()
                                .substring(0, 5)
                );
            }

            return formattedSlots;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }
}