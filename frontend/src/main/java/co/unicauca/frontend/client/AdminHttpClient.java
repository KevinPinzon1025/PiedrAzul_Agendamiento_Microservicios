package co.unicauca.frontend.client;

import co.unicauca.frontend.dto.ProfessionalDTO;
import co.unicauca.frontend.dto.WorkingDayDTO;
import co.unicauca.frontend.util.IAdapter;
import co.unicauca.frontend.util.JsonUtil;
import co.unicauca.frontend.util.ProfessionalAdapter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class AdminHttpClient {

    private final String BASE_URL =
            "http://localhost:8080/appointment";

    private final HttpClient client =
            HttpClient.newHttpClient();

    private List<ProfessionalDTO> professionalCache;

    private final IAdapter professionalAdapter =
            new ProfessionalAdapter();

    public List<String> getAllProfessionals() {

        try {

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            BASE_URL + "/professionals"
                                    )
                            )
                            .GET()
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            professionalCache =
                    JsonUtil.fromJsonList(
                            response.body(),
                            ProfessionalDTO.class
                    );

            return (List<String>) professionalAdapter
                    .adapt(professionalCache);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    public Long getProfessionalIdByName(
            String professionalName
    ) {

        if (professionalCache == null) {

            getAllProfessionals();
        }

        return professionalCache.stream()
                .filter(
                        p -> p.getProfName()
                                .equals(professionalName)
                )
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Profesional no encontrado"
                        )
                )
                .getId();
    }

    public void configureAvailability(
            Long professionalId,
            List<WorkingDayDTO> workingDays
    ) {

        try {

            String json =
                    JsonUtil.toJson(workingDays);

            String url =
                    BASE_URL +
                            "/agenda/" +
                            professionalId +
                            "/availability";

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(json)
                            )
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() != 200) {

                throw new RuntimeException(
                        "Error configurando disponibilidad: "
                                + response.body()
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }
}