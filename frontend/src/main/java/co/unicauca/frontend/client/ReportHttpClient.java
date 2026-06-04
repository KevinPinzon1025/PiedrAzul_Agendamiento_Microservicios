package co.unicauca.frontend.client;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReportHttpClient {

    private static final String BASE_URL = "http://localhost:8086/reports/appointments";

    private final HttpClient client = HttpClient.newHttpClient();

    public CsvReport downloadAppointmentCsv(String professional, LocalDate date) {
        try {
            String encodedProfessional = URLEncoder.encode(professional, StandardCharsets.UTF_8);
            String url = BASE_URL
                    + "/csv?professional="
                    + encodedProfessional
                    + "&date="
                    + date;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<byte[]> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofByteArray()
            );

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Error descargando reporte. Status: "
                                + response.statusCode()
                );
            }

            return new CsvReport(
                    resolveFilename(response),
                    response.body()
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String resolveFilename(HttpResponse<byte[]> response) {
        Optional<String> contentDisposition = response.headers()
                .firstValue("Content-Disposition");

        String filename = contentDisposition
                .map(this::extractFilename)
                .filter(extractedFilename -> !extractedFilename.isBlank())
                .orElse("citas.csv");

        return sanitizeFilename(filename);
    }

    private String extractFilename(String contentDisposition) {
        String[] parts = contentDisposition.split(";");

        for (String part : parts) {
            String trimmed = part.trim();

            if (trimmed.startsWith("filename*=")) {
                return decodeEncodedFilename(trimmed.substring("filename*=".length()));
            }

            if (trimmed.startsWith("filename=")) {
                return decodeMimeEncodedFilename(cleanFilename(trimmed.substring("filename=".length())));
            }
        }

        return "";
    }

    private String decodeEncodedFilename(String value) {
        String cleaned = cleanFilename(value);
        int encodedValueStart = cleaned.indexOf("''");

        if (encodedValueStart >= 0) {
            cleaned = cleaned.substring(encodedValueStart + 2);
        }

        return java.net.URLDecoder.decode(cleaned, StandardCharsets.UTF_8);
    }

    private String cleanFilename(String value) {
        String cleaned = value.trim();

        if (cleaned.startsWith("\"") && cleaned.endsWith("\"") && cleaned.length() > 1) {
            return cleaned.substring(1, cleaned.length() - 1);
        }

        return cleaned;
    }

    private String decodeMimeEncodedFilename(String value) {
        if (!value.startsWith("=?") || !value.endsWith("?=")) {
            return value;
        }

        String encoded = value.substring(2, value.length() - 2);
        String[] parts = encoded.split("\\?", 3);

        if (parts.length != 3 || !parts[1].equalsIgnoreCase("Q")) {
            return value;
        }

        try {
            Charset charset = Charset.forName(parts[0]);
            return charset.decode(ByteBuffer.wrap(decodeQuotedPrintable(parts[2]))).toString();
        } catch (Exception ignored) {
            return value;
        }
    }

    private byte[] decodeQuotedPrintable(String value) {
        List<Byte> bytes = new ArrayList<>();

        for (int index = 0; index < value.length(); index++) {
            char current = value.charAt(index);

            if (current == '_' ) {
                bytes.add((byte) ' ');
                continue;
            }

            if (current == '=' && index + 2 < value.length()) {
                String hex = value.substring(index + 1, index + 3);

                try {
                    bytes.add((byte) Integer.parseInt(hex, 16));
                    index += 2;
                    continue;
                } catch (NumberFormatException ignored) {
                }
            }

            bytes.add((byte) current);
        }

        byte[] result = new byte[bytes.size()];

        for (int index = 0; index < bytes.size(); index++) {
            result[index] = bytes.get(index);
        }

        return result;
    }

    private String sanitizeFilename(String value) {
        String cleaned = value
                .replaceAll("[\\\\/:*?\"<>|]", "_")
                .trim();

        return cleaned.isBlank() ? "citas.csv" : cleaned;
    }

    public static class CsvReport {
        private final String filename;
        private final byte[] content;

        public CsvReport(String filename, byte[] content) {
            this.filename = filename;
            this.content = content;
        }

        public String getFilename() {
            return filename;
        }

        public byte[] getContent() {
            return content;
        }
    }
}
