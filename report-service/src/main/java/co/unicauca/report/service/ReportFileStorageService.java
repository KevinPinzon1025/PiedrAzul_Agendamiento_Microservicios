package co.unicauca.report.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ReportFileStorageService {

    private final Path outputDirectory;

    public ReportFileStorageService(
            @Value("${piedrAzul.reports.output-dir:generated-reports/appointments}") String outputDirectory
    ) {
        Path configuredPath = Path.of(outputDirectory);
        this.outputDirectory = configuredPath.isAbsolute()
                ? configuredPath
                : Path.of(System.getProperty("user.dir")).resolve(configuredPath);
    }

    public Path saveCsv(String fileName, String csvContent) {
        try {
            Files.createDirectories(outputDirectory);
            Path reportPath = outputDirectory.resolve(fileName).normalize();
            Files.writeString(reportPath, csvContent, StandardCharsets.UTF_8);
            return reportPath;
        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible guardar el archivo CSV generado", exception);
        }
    }
}
