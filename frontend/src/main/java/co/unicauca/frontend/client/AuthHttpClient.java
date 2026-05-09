package co.unicauca.frontend.client;

import co.unicauca.frontend.dto.AuthResponse;
import co.unicauca.frontend.dto.LoginRequest;
import co.unicauca.frontend.dto.RegisterRequest;
import co.unicauca.frontend.dto.UserResponse;
import co.unicauca.frontend.util.JsonUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthHttpClient {

    private final String BASE_URL = "http://localhost:8081/api/auth";
    private final HttpClient client = HttpClient.newHttpClient();

    public UserResponse register(RegisterRequest request) {
        try {
            String json = JsonUtil.toJson(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 201) {
                throw new RuntimeException("Error registrando usuario: " + response.body());
            }

            return JsonUtil.fromJson(response.body(), UserResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            String json = JsonUtil.toJson(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error iniciando sesion: " + response.body());
            }

            return JsonUtil.fromJson(response.body(), AuthResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
