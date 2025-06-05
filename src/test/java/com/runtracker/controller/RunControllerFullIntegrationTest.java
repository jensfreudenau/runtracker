package com.runtracker.controller;

import com.runtracker.model.Run;
import com.runtracker.service.FitFileService;
import com.runtracker.service.RunService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// @SpringBootTest lädt den gesamten Spring-Anwendungskontext
// Das ist notwendig, wenn du echte Services (wie FitFileService) nutzen möchtest
@SpringBootTest
@AutoConfigureMockMvc
class RunControllerFullIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Der echte FitFileService wird jetzt von Spring injiziert
    @Autowired
    private FitFileService fitFileService;

    // RunService sollte gemockt werden, es sei denn, du hast eine H2-Datenbank oder ähnliches
    // und möchtest die Datenbankinteraktion auch testen.
    // Für diesen Test bleiben wir beim Mock, da der Fokus auf FitFileService liegt.
    @MockBean
    private RunService runService;

    // Die interne runs-Liste des Controllers (nur für diesen Test, nicht in Prod)
    private List<Run> controllerInternalRunsList;


    // Pfad zur echten Test-FIT-Datei
    private static final String VALID_FIT_FILE_PATH = "test-data/ohne_avg.fit";
    // ANPASSEN: Ersetze dies durch den Namen deiner echten Testdatei
    // UND Passe den erwarteten Wert im Test an die tatsächliche Geschwindigkeit deiner Datei an!


    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) throws NoSuchFieldException, IllegalAccessException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        // Workaround: Zugriff auf die private 'runs'-Liste des Controllers
        // Im echten Code würden Sie runService.saveRun() mocken und überprüfen.
        RunController runController = webApplicationContext.getBean(RunController.class);
        java.lang.reflect.Field runsField = RunController.class.getDeclaredField("runs");
        runsField.setAccessible(true);
        controllerInternalRunsList = (List<Run>) runsField.get(runController);
        controllerInternalRunsList.clear(); // Stelle sicher, dass die Liste für jeden Test sauber ist
        // Wenn dein nextId nicht final ist und du ihn zurücksetzen möchtest
        // java.lang.reflect.Field nextIdField = RunController.class.getDeclaredField("nextId");
        // nextIdField.setAccessible(true);
        // nextIdField.set(runController, 1L);
    }


    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Sollte eine echte FIT-Datei erfolgreich hochladen und die korrekte Geschwindigkeit speichern")
    void uploadFitFile_withRealFile_success() throws Exception {
        // Lese die echte FIT-Datei als Byte-Array
        byte[] fileContent = Files.readAllBytes(Paths.get(new ClassPathResource(VALID_FIT_FILE_PATH).getURI()));

        MockMultipartFile file = new MockMultipartFile(
                "file", // Name des Request-Parameters im Controller
                "my_real_activity.fit", // Originaldateiname
                "application/octet-stream", // MIME-Type für FIT-Dateien
                fileContent // Der Inhalt der echten Datei
        );

        // Erwartete Durchschnittsgeschwindigkeit aus deiner echten FIT-Datei
        // <<< WICHTIG: Passe diesen Wert an deine tatsächliche Testdatei an! >>>
        double expectedAverageSpeedKmPerHour = 21.22; // Beispielwert, ERSETZE DIESEN!


        mockMvc.perform(multipart("/runs/uploadFit")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/runs"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message",
                        String.format("Datei erfolgreich hochgeladen! Durchschnittsgeschwindigkeit: %.2f km/h", expectedAverageSpeedKmPerHour)));

        // Überprüfe, ob der Lauf zur internen Liste hinzugefügt wurde
        assertFalse(controllerInternalRunsList.isEmpty(), "Ein Lauf sollte der internen Liste hinzugefügt worden sein.");
        assertEquals(1, controllerInternalRunsList.size(), "Es sollte genau ein Lauf hinzugefügt worden sein.");
        assertEquals("my_real_activity.fit", controllerInternalRunsList.get(0).getName(), "Der Name des Laufs sollte korrekt sein.");
        assertEquals(expectedAverageSpeedKmPerHour, controllerInternalRunsList.get(0).getAverageSpeed(), 0.01,
                "Die Durchschnittsgeschwindigkeit sollte korrekt sein (Toleranz 0.01 für Rundung).");
    }

    // Die anderen Tests (emptyFile, fitParsingFails, noSpeedFound, genericError)
    // können prinzipiell die gleichen bleiben, da sie nicht direkt den FitFileService in voller Funktionalität nutzen.
    // Wenn du möchtest, dass fitParsingFails auch mit einer "echt" korrupten Datei getestet wird,
    // müsstest du eine solche Datei bereitstellen und den Mock weglassen.
    // Für diesen Beispieltest bleiben sie als Mock-Tests, um den Fokus auf den "Real-File"-Test zu legen.

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Sollte eine Fehlermeldung anzeigen, wenn die Datei leer ist")
    void uploadFitFile_emptyFile() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.fit",
                "application/octet-stream",
                new byte[0]
        );

        // Wir brauchen das Mocking nicht mehr, da der Controller den echten Service nutzt
        // aber für leere Dateien kommt der Service gar nicht zum Einsatz.
        // Der Controller fängt das selbst ab.

        mockMvc.perform(multipart("/runs/uploadFit")
                        .file(emptyFile)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/runs/edit"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "Bitte wählen Sie eine Datei zum Hochladen aus."));

        assertTrue(controllerInternalRunsList.isEmpty(), "Es sollte kein Lauf hinzugefügt worden sein.");
    }
}
