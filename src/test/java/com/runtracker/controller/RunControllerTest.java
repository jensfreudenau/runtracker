package com.runtracker.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity; // Also ensure this is imported

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(RunController.class)
class RunControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private RunService runService;
//
//    @MockBean
//    private FitFileService fitFileService;
//
//    private List<Run> controllerInternalRunsList;
//
//    @TestConfiguration
//    static class MockConfig {
//        @Bean
//        public FitFileService fitFileService() {
//            return Mockito.mock(FitFileService.class);
//        }
//
//        @Bean
//        public RunController runController(FitFileService fitFileService) {
//            // Fake-Liste der Runs im Controller
//            return new RunController(fitFileService) {
//                {
//                    this.runs = new ArrayList<>();
//                    this.nextId = 1L;
//                }
//            };
//        }
//    }
//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext) throws NoSuchFieldException, IllegalAccessException {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(webApplicationContext)
//                .apply(springSecurity()) // Apply Spring Security configuration
//                .build();
//
//        RunController runController = webApplicationContext.getBean(RunController.class);
//        java.lang.reflect.Field runsField = RunController.class.getDeclaredField("runs");
//        runsField.setAccessible(true);
//        controllerInternalRunsList = (List<Run>) runsField.get(runController);
//        controllerInternalRunsList.clear();
//        // Assuming your RunController has a setId method or you manage nextId differently
//        // If nextId is directly in the controller and not reset by Spring, you might need to reset it via reflection too.
//        // For simplicity, for now, we'll assume it starts fresh or isn't critical for these specific tests.
//        // If your controller has setId(Long id) for nextId, you could do:
//        // runController.setId(1L); // assuming a setter exists
//    }
//
//
//    @Test
//    @WithMockUser(username = "testuser")
//    @DisplayName("Sollte eine Datei erfolgreich hochladen und Durchschnittsgeschwindigkeit speichern")
//    void uploadFitFile_success() throws Exception {
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                "test_activity.fit",
//                "application/octet-stream",
//                "simulated fit content".getBytes()
//        );
//
//        when(fitFileService.getAvgSpeedKmPerHour(any(InputStream.class))).thenReturn(10.5);
//
//        mockMvc.perform(multipart("/runs/uploadFit")
//                        .file(file)
//                        .with(csrf())) // <<< ADD THIS LINE FOR CSRF TOKEN
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/runs"))
//                .andExpect(flash().attributeExists("message"))
//                .andExpect(flash().attribute("message", "Datei erfolgreich hochgeladen! Durchschnittsgeschwindigkeit: 37,80 km/h"));
//
//        assertFalse(controllerInternalRunsList.isEmpty(), "Ein Lauf sollte der internen Liste hinzugefügt worden sein.");
//    }
//
//    @Test
//    @WithMockUser(username = "testuser")
//    @DisplayName("Sollte eine Fehlermeldung anzeigen, wenn die Datei leer ist")
//    void uploadFitFile_emptyFile() throws Exception {
//        MockMultipartFile emptyFile = new MockMultipartFile(
//                "file",
//                "empty.fit",
//                "application/octet-stream",
//                new byte[0]
//        );
//
//        mockMvc.perform(multipart("/runs/uploadFit")
//                        .file(emptyFile)
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/runs/edit"))
//                .andExpect(flash().attributeExists("message"))
//                .andExpect(flash().attribute("message", "Bitte wählen Sie eine Datei zum Hochladen aus."));
//
//        assertTrue(controllerInternalRunsList.isEmpty(), "Es sollte kein Lauf hinzugefügt worden sein.");
//    }
//
//    // ... (repeat for other @PostMapping tests) ...
//
//    @Test
//    @WithMockUser(username = "testuser")
//    @DisplayName("Sollte eine Fehlermeldung anzeigen, wenn das Parsing der FIT-Datei fehlschlägt")
//    void uploadFitFile_fitParsingFails() throws Exception {
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                "corrupt.fit",
//                "application/octet-stream",
//                "corrupt content".getBytes()
//        );
//
//        when(fitFileService.getAvgSpeedKmPerHour(any(InputStream.class)))
//                .thenThrow(new FitException("Simulierter Parsing-Fehler"));
//
//        mockMvc.perform(multipart("/runs/uploadFit")
//                        .file(file)
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/runs"))
//                .andExpect(flash().attributeExists("message"))
//                .andExpect(flash().attribute("message", "Fehler beim Parsen der FIT-Datei: Simulierter Parsing-Fehler"));
//
//        assertTrue(controllerInternalRunsList.isEmpty(), "Es sollte kein Lauf hinzugefügt worden sein bei Parsing-Fehler.");
//    }
//
//    @Test
//    @WithMockUser(username = "testuser")
//    @DisplayName("Sollte eine Fehlermeldung anzeigen, wenn keine Geschwindigkeit in FIT-Datei gefunden wurde")
//    void uploadFitFile_noSpeedFound() throws Exception {
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                "no_speed.fit",
//                "application/octet-stream",
//                "no speed content".getBytes()
//        );
//
//        when(fitFileService.getAvgSpeedKmPerHour(any(InputStream.class))).thenReturn(-1.0);
//
//        mockMvc.perform(multipart("/runs/uploadFit")
//                        .file(file)
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/runs"))
//                .andExpect(flash().attributeExists("message"))
//                .andExpect(flash().attribute("message", "FIT-Datei erfolgreich hochgeladen, aber keine Durchschnittsgeschwindigkeit gefunden."));
//
//        assertTrue(controllerInternalRunsList.isEmpty(), "Es sollte kein Lauf hinzugefügt worden sein, wenn keine Geschwindigkeit gefunden wurde.");
//    }
//
//    @Test
//    @WithMockUser(username = "testuser")
//    @DisplayName("Sollte eine generische Fehlermeldung anzeigen, wenn ein unbekannter Fehler auftritt")
//    void uploadFitFile_genericError() throws Exception {
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                "unknown_error.fit",
//                "application/octet-stream",
//                "error content".getBytes()
//        );
//
//        when(fitFileService.getAvgSpeedKmPerHour(any(InputStream.class)))
//                .thenThrow(new RuntimeException("Unbekannter Fehler"));
//
//        mockMvc.perform(multipart("/runs/uploadFit")
//                        .file(file)
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/runs"))
//                .andExpect(flash().attributeExists("message"))
//                .andExpect(flash().attribute("message", "Fehler beim Hochladen oder Verarbeiten der Datei: Unbekannter Fehler"));
//
//        assertTrue(controllerInternalRunsList.isEmpty(), "Es sollte kein Lauf hinzugefügen worden sein bei generischem Fehler.");
//    }
//    private MockMultipartFile validFitFile;
//
//    @BeforeEach
//    void setup() {
//        validFitFile = new MockMultipartFile(
//                "file", "run.fit", "application/octet-stream", "valid fit data".getBytes()
//        );
//    }
//
//    @Test
//    void testUploadFitFile_withoutId_shouldCreateNewRun() throws Exception {
//        Mockito.when(fitFileService.getAvgSpeedKmPerHour(Mockito.any())).thenReturn(3.0); // m/s
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadFit")
//                        .file(validFitFile)
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/runs"))
//                .andExpect(flash().attribute("message", Matchers.containsString("Durchschnittsgeschwindigkeit")));
//    }
//
//    @Test
//    void testUploadFitFile_withId_shouldUpdateExistingRun() throws Exception {
//        Mockito.when(fitFileService.getAvgSpeedKmPerHour(Mockito.any())).thenReturn(2.5);
//
//        // Erstelle Run mit ID 5
//        Run existingRun = new Run();
//        existingRun.setId(5L);
//        existingRun.setName("Alt");
//        existingRun.setAverageSpeed(0.0);
//
//        RunController controller = (RunController) mockMvc.getDispatcherServlet()
//                .getWebApplicationContext()
//                .getBean(RunController.class);
//
//        controller.runs.add(existingRun);
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadFit/5")
//                        .file(validFitFile)
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/runs"))
//                .andExpect(flash().attribute("message", Matchers.containsString("Durchschnittsgeschwindigkeit")));
//
//        // Prüfe ob Run aktualisiert wurde
//        Run updated = controller.runs.stream().filter(r -> r.getId() == 5L).findFirst().orElse(null);
//        assert updated != null;
//        assert updated.getAverageSpeed() == 2.5;
//    }
}
