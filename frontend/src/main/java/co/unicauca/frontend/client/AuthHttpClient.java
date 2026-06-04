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
                throw new RuntimeException(registerErrorMessage(response.statusCode(), response.body()));
            }

            if (response.body() == null || response.body().isBlank()) {
                return null;
            }

            return JsonUtil.fromJson(response.body(), UserResponse.class);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("No fue posible conectar con el servicio de autenticación.");
        }
    }

    private String registerErrorMessage(int statusCode, String body) {
        if (statusCode == 403 || statusCode == 409) {
            return "Ya existe un usuario con ese número de documento.";
        }

        if (body != null && !body.isBlank()) {
            return "No se pudo registrar: " + body;
        }

        return "No se pudo registrar. Código de respuesta: " + statusCode;
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
