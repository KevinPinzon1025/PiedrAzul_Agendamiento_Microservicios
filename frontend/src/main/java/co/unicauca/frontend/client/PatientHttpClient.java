package co.unicauca.frontend.client;

import co.unicauca.frontend.dto.CreatePatientRequestDTO;
import co.unicauca.frontend.util.JsonUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PatientHttpClient {

    private static final String BASE_URL =
            "http://localhost:8084/api/patients";

    private final HttpClient client =
            HttpClient.newHttpClient();

    public void createPatient(CreatePatientRequestDTO request) {

        try {

            String json =
                    JsonUtil.toJson(request);

            HttpRequest httpRequest =
                    HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .build();

            HttpResponse<String> response =
                    client.send(
                            httpRequest,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() != 200 &&
                    response.statusCode() != 201) {

                throw new RuntimeException(
                        "Error registrando paciente. Status: "
                                + response.statusCode()
                                + " Body: "
                                + response.body()
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}