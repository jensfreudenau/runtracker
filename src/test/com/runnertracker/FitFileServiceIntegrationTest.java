package test.com.runnertracker;

import com.runnertracker.service.FitFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // Startet den Spring Boot Kontext für den Test
class FitFileServiceIntegrationTest { // Verwende "IntegrationTest" im Namen für Klarheit

    @Autowired // Injiziert den Service, den wir testen wollen
    private FitFileService fitFileService;

    // Pfade zu den Test-FIT-Dateien im src/test/resources Ordner
    private static final String VALID_FIT_FILE_PATH_WITH_AVG_SPEED = "Fahrt_am_Nachmittag.fit";
    private static final String VALID_FIT_FILE_PATH_NO_AVG_SPEED = "ohne_avg.fit"; // Optional
//    private static final String MALFORMED_FIT_FILE_PATH = "test-data/malformed.fit"; // Optional

    // Hinweis: Ersetze die Platzhalter-Dateien durch echte FIT-Dateien in src/test/resources/test-data/


    @BeforeEach
    void setUp() {
        // Hier könntest du bei Bedarf Setup-Logik hinzufügen,
        // z.B. Datenbank-Cleanup, wenn es ein Service wäre, der mit einer DB interagiert.
    }

//    @Test
//    @DisplayName("Sollte die Durchschnittsgeschwindigkeit aus einer gültigen FIT-Datei lesen, wenn avg_speed vorhanden ist")
//    void shouldReadAverageSpeedFromValidFitFileWithAvgSpeed() throws Exception {
//        // Gebe eine echte FIT-Datei mit bekannter Durchschnittsgeschwindigkeit an.
//        // Stelle sicher, dass die Datei im src/test/resources/test-data/ liegt.
//        try (InputStream inputStream = new ClassPathResource(VALID_FIT_FILE_PATH_WITH_AVG_SPEED).getInputStream()) {
//            double averageSpeed = fitFileService.getAverageSpeedFromFitFile(inputStream);
//
//            // Annahme: Die FIT-Datei 'valid_activity_with_avg_speed.fit' hat eine avg_speed von 3.0 m/s
//            // Passe den erwarteten Wert an deine tatsächliche Testdatei an!
//            assertEquals(3.0, averageSpeed, 0.01, "Die Durchschnittsgeschwindigkeit sollte korrekt ausgelesen werden.");
//        }
//    }

    @Test
    @DisplayName("Sollte die Durchschnittsgeschwindigkeit aus einer gültigen FIT-Datei berechnen, wenn avg_speed fehlt")
    void shouldCalculateAverageSpeedIfAvgSpeedIsMissing() throws Exception {
        // Erstelle eine FIT-Datei (oder finde eine), bei der avg_speed fehlt,
        // aber total_distance und total_timer_time vorhanden sind.
        // Z.B. 1000m in 200s -> 5.0 m/s
        try (InputStream inputStream = new ClassPathResource(VALID_FIT_FILE_PATH_NO_AVG_SPEED).getInputStream()) {
            double averageSpeed = fitFileService.getAverageSpeedFromFitFile(inputStream);

            // Annahme: Die FIT-Datei 'valid_activity_no_avg_speed.fit' hat eine avg_speed von 5.0 m/s (berechnet)
            // Passe den erwarteten Wert an deine tatsächliche Testdatei an!
            assertEquals(5.0, averageSpeed, 0.01, "Die Durchschnittsgeschwindigkeit sollte korrekt berechnet werden.");
        }
    }

//    @Test
//    @DisplayName("Sollte -1.0 zurückgeben, wenn keine relevante Geschwindigkeitsdaten gefunden werden")
//    void shouldReturnNegativeOneIfNoSpeedDataFound() throws Exception {
//        // Erstelle eine FIT-Datei, die keine Session-Daten oder keine relevanten Geschwindigkeitsfelder enthält.
//        // Eine leere oder minimale FIT-Datei könnte hier funktionieren.
//        try (InputStream inputStream = new ClassPathResource("test-data/empty_or_minimal.fit").getInputStream()) {
//            double averageSpeed = fitFileService.getAverageSpeedFromFitFile(inputStream);
//
//            assertEquals(-1.0, averageSpeed, "Sollte -1.0 zurückgeben, wenn keine Geschwindigkeitsdaten gefunden werden.");
//        }
//    }

//    @Test
//    @DisplayName("Sollte FitException werfen für eine fehlerhafte FIT-Datei")
//    void shouldThrowFitExceptionForMalformedFile() {
//        // Eine bewusst beschädigte oder unvollständige .fit-Datei
//        try (InputStream inputStream = new ClassPathResource(MALFORMED_FIT_FILE_PATH).getInputStream()) {
//            // Erwarte, dass die Methode eine FitException wirft
//            assertThrows(FitException.class, () -> fitFileService.getAverageSpeedFromFitFile(inputStream),
//                    "Sollte eine FitException werfen, wenn die Datei fehlerhaft ist.");
//        } catch (IOException e) {
//            fail("Failed to load malformed test file: " + e.getMessage());
//        }
//    }

    @Test
    @DisplayName("Sollte IOException werfen, wenn InputStream ungültig ist")
    void shouldThrowIOExceptionForInvalidInputStream() {
        // Ein geschlossener InputStream oder ein null InputStream
        // (decode.read() wirft IOException bei einem geschlossenen Stream)
        InputStream closedInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Stream closed");
            }
        };

        assertThrows(IOException.class, () -> fitFileService.getAverageSpeedFromFitFile(closedInputStream),
                "Sollte IOException werfen, wenn der InputStream ungültig ist.");
    }
}
