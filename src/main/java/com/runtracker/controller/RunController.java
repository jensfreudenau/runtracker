package com.runtracker.controller;

import com.runtracker.dto.LapDTO;
import com.runtracker.dto.SummaryDTO;
import com.runtracker.exceptionHandler.FitException;
import com.runtracker.fit.FitField;
import com.runtracker.fit.FitFileService;
import com.runtracker.fit.LapToRunMapper;
import com.runtracker.fit.SummaryToRunMapper;
import com.runtracker.model.Lap;
import com.runtracker.model.Run;
import com.runtracker.service.LapService;
import com.runtracker.service.RunService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/runs")
public class RunController {
    private final RunService runService;
    private final LapService lapService;
    private final FitFileService fitFileService;
    private Long runId;

    public RunController(RunService runService, LapService lapService, FitFileService fitFileService) {
        this.runService = runService;
        this.lapService = lapService;
        this.fitFileService = fitFileService;
    }

    @GetMapping
    public String runsPage(Model model) {
        // 1. Aktuellen Benutzer ermitteln
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Holt den Benutzernamen des eingeloggten Benutzers
        // 2. Läufe des Benutzers aus der Datenbank abrufen
        List<Run> userRuns = runService.findRunsByUsername(username);
        // 3. Daten dem Model hinzufügen
        model.addAttribute("runs", userRuns); // "runs" ist der Name, unter dem die Daten in der View verfügbar sind
        model.addAttribute("pageTitle", "Trainings");
        return "runs/runs";
    }

    @GetMapping("/events")
    public String showCalendar(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            Model model) {
        LocalDate displayDate;
        if (year != null && month != null) {
            displayDate = LocalDate.of(year, month, 1);
        } else {
            displayDate = LocalDate.now(); // Standard: aktuelles Datum
        }

        return "runs/calendar"; // Verweist auf die Thymeleaf-Vorlage "calendar.html"
    }

    @GetMapping("/{id}")
    public String viewRunDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        String username = getCurrentUsername();
        return runService.findRunByIdAndUsername(id, username)
                .map(run -> {
                    model.addAttribute("run", run);
                    // Lade Runden explizit, falls LAZY Fetching und sie nicht bereits geladen wurden
                    model.addAttribute("laps", runService.getLapsForRun(id, username));
                    model.addAttribute("newLap", new Lap()); // Für das Hinzufügen-Formular
                    model.addAttribute("pageTitle", "Laufdetails");
                    return "runs/run-detail"; // Dies ist die neue View-Datei
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Lauf nicht gefunden oder nicht berechtigt.");
                    return "redirect:/runs";
                });
    }

    // Zeigt das Formular zum Erstellen eines neuen Laufs an
    @GetMapping("/new")
    public String showCreateRunForm(Model model) {
        model.addAttribute("run", new Run()); // Leeres Run-Objekt für das Formular
        model.addAttribute("pageTitle", "Neuen Lauf hinzufügen");
        return "runs/run-form";
    }

    @PostMapping("/{runId}/addLap")
    public String addLapToRun(@PathVariable Long runId, @ModelAttribute Lap newLap, RedirectAttributes redirectAttributes) {
        String username = getCurrentUsername();
        try {
            runService.addLapToRun(runId, newLap, username);
            redirectAttributes.addFlashAttribute("message", "Runde erfolgreich hinzugefügt!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/runs/" + runId; // Zurück zur Detailseite
    }

    // Zeigt das Formular zum Bearbeiten eines bestehenden Laufs an
    @GetMapping("/edit/{id}")
    public String showEditRunForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        String username = getCurrentUsername();
        return runService.findRunByIdAndUsername(id, username)
                .map(run -> {
                    model.addAttribute("run", run);
                    model.addAttribute("pageTitle", "Lauf bearbeiten");
                    return "runs/run-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Lauf nicht gefunden oder nicht berechtigt.");
                    return "redirect:/runs"; // Weiterleitung zurück zur Läufe-Übersicht
                });
    }

    // --- Neue Methode für den FIT-Upload ---
    @PostMapping({"/uploadFit", "/uploadFit/{id}"})
    public String uploadFitFile(@PathVariable(name = "id", required = false) Long id, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Bitte wählen Sie eine Datei zum Hochladen aus.");
            return "redirect:/runs/edit";
        }

        try (InputStream inputStream = file.getInputStream()) {
            SummaryDTO summaryDTO = fitFileService.getSummary(inputStream);

            Run run = (id != null)
                    ? runService.findById(id).orElse(new Run())
                    : new Run();

            run = SummaryToRunMapper.mapToRun(summaryDTO, run);
            run.setName(file.getOriginalFilename());
            run.setDate(LocalDate.now());
            run.setTime(LocalTime.now());
            runId = runService.saveRun(run, getCurrentUsername());
            redirectAttributes.addFlashAttribute("message", String.format("Datei erfolgreich hochgeladen!"));


        } catch (FitException e) {
            redirectAttributes.addFlashAttribute("message", "Fehler beim Parsen der FIT-Datei: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Fehler beim Hochladen oder Verarbeiten der Datei: " + e.getMessage());
            e.printStackTrace();
        }

        try (InputStream inputStream = file.getInputStream()) {
            if (runId != null) {

                List<LapDTO> lapDTOs = fitFileService.getLaps(inputStream);
                Run run = runService.findById(runId).orElseThrow();
                for (LapDTO lapDTO : lapDTOs) {
                    int start_time = lapDTO.getInt(FitField.START_TIME); // oder anderer eindeutiger Wert
                    Optional<Lap> existing = lapService.findByRunIdAndStartTime(runId, start_time);

                    Lap lap = existing.orElse(new Lap());
                    lap = LapToRunMapper.mapToLap(lapDTO, lap);

                    lap.setRun(run);
                    lap.setDate(LocalDate.now());
                    lap.setTime(LocalTime.now());

                    lapService.save(lap);
                }
            }
        } catch (IOException | FitException ex) {
            throw new RuntimeException(ex);
        }


        return "redirect:/runs";
    }

    // Verarbeitet das Absenden des Formulars (Erstellen und Bearbeiten)
    @PostMapping
    public String saveRun(@ModelAttribute Run run, RedirectAttributes redirectAttributes) {
        String username = getCurrentUsername();
        try {
            Long runId = runService.saveRun(run, username);
            redirectAttributes.addFlashAttribute("message", "Lauf erfolgreich gespeichert!");
            return "redirect:/runs";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/runs/new"; // Oder zurück zum Formular bleiben mit Fehlermeldung
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("error", "Ungültiges Datumsformat. Bitte YYYY-MM-DD verwenden.");
            return "redirect:/runs/new"; // Oder zurück zum Formular bleiben mit Fehlermeldung
        }
    }

    // Löscht einen Lauf
    @GetMapping("/delete/{id}")
    public String deleteRun(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String username = getCurrentUsername();
        try {
            runService.deleteRun(id, username);
            redirectAttributes.addFlashAttribute("message", "Lauf erfolgreich gelöscht!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/runs";
    }

    // Hilfsmethode, um den Benutzernamen des eingeloggten Benutzers zu erhalten
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
