package com.runtracker.controller;

import com.runtracker.exceptionHandler.FitException;
import com.runtracker.model.Run;
import com.runtracker.service.FitFileService;
import com.runtracker.service.RunService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity; // Also ensure this is imported

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(RunController.class)
class RunControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RunService runService;

    @MockBean
    private FitFileService fitFileService;

    private List<Run> controllerInternalRunsList;


    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) throws NoSuchFieldException, IllegalAccessException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity()) // Apply Spring Security configuration
                .build();

        RunController runController = webApplicationContext.getBean(RunController.class);
        java.lang.reflect.Field runsField = RunController.class.getDeclaredField("runs");
        runsField.setAccessible(true);
        controllerInternalRunsList = (List<Run>) runsField.get(runController);
        controllerInternalRunsList.clear();
        // Assuming your RunController has a setId method or you manage nextId differently
        // If nextId is directly in the controller and not reset by Spring, you might need to reset it via reflection too.
        // For simplicity, for now, we'll assume it starts fresh or isn't critical for these specific tests.
        // If your controller has setId(Long id) for nextId, you could do:
        // runController.setId(1L); // assuming a setter exists
    }


    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Sollte eine Datei erfolgreich hochladen und Durchschnittsgeschwindigkeit speichern")
    void uploadFitFile_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test_activity.fit",
                "application/octet-stream",
                "simulated fit content".getBytes()
        );

        when(fitFileService.getAvgSpeedKmPerHour(any(InputStream.class))).thenReturn(10.5);

        mockMvc.perform(multipart("/runs/uploadFit")
                        .file(file)
                        .with(csrf())) // <<< ADD THIS LINE FOR CSRF TOKEN
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/runs"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "Datei erfolgreich hochgeladen! Durchschnittsgeschwindigkeit: 37,80 km/h"));

        assertFalse(controllerInternalRunsList.isEmpty(), "Ein Lauf sollte der internen Liste hinzugefügt worden sein.");
    }

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

        mockMvc.perform(multipart("/runs/uploadFit")
                        .file(emptyFile)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/runs/edit"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "Bitte wählen Sie eine Datei zum Hochladen aus."));

        assertTrue(controllerInternalRunsList.isEmpty(), "Es sollte kein Lauf hinzugefügt worden sein.");
    }

    // ... (repeat for other @PostMapping tests) ...

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Sollte eine Fehlermeldung anzeigen, wenn das Parsing der FIT-Datei fehlschlägt")
    void uploadFitFile_fitParsingFails() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "corrupt.fit",
                "application/octet-stream",
                "corrupt content".getBytes()
        );

        when(fitFileService.getAvgSpeedKmPerHour(any(InputStream.class)))
                .thenThrow(new FitException("Simulierter Parsing-Fehler"));

        mockMvc.perform(multipart("/runs/uploadFit")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/runs"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "Fehler beim Parsen der FIT-Datei: Simulierter Parsing-Fehler"));

        assertTrue(controllerInternalRunsList.isEmpty(), "Es sollte kein Lauf hinzugefügt worden sein bei Parsing-Fehler.");
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Sollte eine Fehlermeldung anzeigen, wenn keine Geschwindigkeit in FIT-Datei gefunden wurde")
    void uploadFitFile_noSpeedFound() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "no_speed.fit",
                "application/octet-stream",
                "no speed content".getBytes()
        );

        when(fitFileService.getAvgSpeedKmPerHour(any(InputStream.class))).thenReturn(-1.0);

        mockMvc.perform(multipart("/runs/uploadFit")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/runs"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "FIT-Datei erfolgreich hochgeladen, aber keine Durchschnittsgeschwindigkeit gefunden."));

        assertTrue(controllerInternalRunsList.isEmpty(), "Es sollte kein Lauf hinzugefügt worden sein, wenn keine Geschwindigkeit gefunden wurde.");
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Sollte eine generische Fehlermeldung anzeigen, wenn ein unbekannter Fehler auftritt")
    void uploadFitFile_genericError() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "unknown_error.fit",
                "application/octet-stream",
                "error content".getBytes()
        );

        when(fitFileService.getAvgSpeedKmPerHour(any(InputStream.class)))
                .thenThrow(new RuntimeException("Unbekannter Fehler"));

        mockMvc.perform(multipart("/runs/uploadFit")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/runs"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "Fehler beim Hochladen oder Verarbeiten der Datei: Unbekannter Fehler"));

        assertTrue(controllerInternalRunsList.isEmpty(), "Es sollte kein Lauf hinzugefügen worden sein bei generischem Fehler.");
    }

    // ... (any other @PostMapping tests like saveRun or addLapToRun should also use .with(csrf()))
}
