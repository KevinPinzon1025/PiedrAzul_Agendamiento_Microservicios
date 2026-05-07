
package co.unicauca.frontend.client;


import co.unicauca.frontend.dto.AppointmentDTO;
import co.unicauca.frontend.dto.ProfessionalDTO;
import co.unicauca.frontend.util.IAdapter;
import co.unicauca.frontend.util.JsonUtil;
import co.unicauca.frontend.util.ProfessionalAdapter;
import co.unicauca.frontend.dto.PatientDTO;
import co.unicauca.frontend.util.PatientAdapter;

import java.net.URI;
import java.net.http.*;
import java.time.LocalDate;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AppointmentHttpClient {

    private final String BASE_URL = "http://localhost:8080/appointment";
    private final HttpClient client = HttpClient.newHttpClient();
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

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            List<ProfessionalDTO> professionals =
                    JsonUtil.fromJsonList(
                            response.body(),
                            ProfessionalDTO.class
                    );

            return (List<String>) professionalAdapter.adapt(professionals);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllPatients() {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL+"/patients"))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            List<PatientDTO> patients =
                    JsonUtil.fromJsonList(
                            response.body(),
                            PatientDTO.class
                    );

            return (List<String>) patientAdapter.adapt(patients);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}