package com.runtracker.controller;

import com.runtracker.exceptionHandler.FitException;
import com.runtracker.model.Lap;
import com.runtracker.model.Run;
import com.runtracker.service.FitFileService;
import com.runtracker.service.RunService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/runs")
public class RunController {
    private final RunService runService;
    private final FitFileService fitFileService;
    private List<Run> runs = new ArrayList<>();
    public Long nextId = 1L; // Einfacher Zähler für IDs

    public RunController(RunService runService, FitFileService fitFileService) {
        this.runService = runService;
        this.fitFileService = fitFileService; // Jetzt korrekt zugewiesen

        // Deine Dummy-Daten-Initialisierung
        runs.add(new Run("Morgenlauf", LocalDate.of(2025, 6, 1), LocalTime.of(8, 0), "Park", "Frischer Start", 3.0));
        runs.add(new Run("Abendjogging", LocalDate.of(2025, 6, 3), LocalTime.of(19, 30), "Waldweg", "Nach der Arbeit", 3.5));
        runs.forEach(run -> run.setId(nextId++));
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

        List<Run> sportEvents = new ArrayList<>();
        // Beispieltermine für den aktuellen Monat (Juni 2025)
//        sportEvents.add(new Run("Fußballtraining", LocalDate.of(2025, 6, 10), LocalTime.of(18, 0), "Sportplatz A", "Wöchentliches Training"));
//        sportEvents.add(new Run("Schwimmkurs", LocalDate.of(2025, 6, 12), LocalTime.of(16, 30), "Hallenbad", "Fortgeschrittenenkurs"));
//        sportEvents.add(new Run("Basketballspiel", LocalDate.of(2025, 6, 10), LocalTime.of(20, 0), "Turnhalle B", "Freundschaftsspiel"));
//        sportEvents.add(new Run("Lauf-Event", LocalDate.of(2025, 6, 15), LocalTime.of(9, 0), "Stadtpark", "5km Lauf"));
//        sportEvents.add(new Run("Fußballtraining", LocalDate.of(2025, 6, 10), LocalTime.of(18, 0), "Sportplatz A", "Wöchentliches Training"));
//        sportEvents.add(new Run("Schwimmkurs", LocalDate.of(2025, 6, 12), LocalTime.of(16, 30), "Hallenbad", "Fortgeschrittenenkurs"));
//        sportEvents.add(new Run("Basketballspiel", LocalDate.of(2025, 6, 10), LocalTime.of(20, 0), "Turnhalle B", "Freundschaftsspiel"));
//        sportEvents.add(new Run("Lauf-Event", LocalDate.of(2025, 6, 15), LocalTime.of(9, 0), "Stadtpark", "5km Lauf"));
//        // Beispieltermine für einen anderen Monat (Juli 2025)
//        sportEvents.add(new Run("Yoga-Kurs", LocalDate.of(2025, 7, 5), LocalTime.of(10, 0), "Gemeinschaftsraum", "Einsteigerkurs"));
//        sportEvents.add(new Run("Wandertag", LocalDate.of(2025, 7, 20), LocalTime.of(8, 0), "Berge", "Ganztagswanderung"));
        List<Run> filteredEvents = sportEvents.stream()
                .filter(event -> event.getDate().getYear() == displayDate.getYear() && event.getDate().getMonth() == displayDate.getMonth())
                .toList();
        // Gruppiere die Termine nach Datum für eine einfachere Anzeige im Kalender

        Map<LocalDate, List<Run>> eventsByDate = filteredEvents.stream().collect(Collectors.groupingBy(Run::getDate));
        model.addAttribute("eventsByDate", eventsByDate);
        model.addAttribute("currentDate", displayDate); // Für die Anzeige des aktuellen Monats/Jahres

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
    @PostMapping("/uploadFit")
    public String uploadFitFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Bitte wählen Sie eine Datei zum Hochladen aus.");
            return "redirect:/runs/edit";
        }

        try (InputStream inputStream = file.getInputStream()) {
            double averageSpeed = fitFileService.getAvgSpeedKmPerHour(inputStream);

            if (averageSpeed != -1.0) {
                // Hier könntest du weitere Metadaten aus der FIT-Datei extrahieren,
                // z.B. Startzeit, Datum, Distanz, etc., um einen vollständigeren Run zu erstellen.
                // Für dieses Beispiel erstellen wir nur einen einfachen Run mit der Geschwindigkeit.

                Run newRun = new Run();
                newRun.setName(file.getOriginalFilename() != null ? file.getOriginalFilename() : "FIT-Aktivität");
                newRun.setDate(LocalDate.now()); // Standardmäßig das heutige Datum
                newRun.setTime(LocalTime.now()); // Standardmäßig die aktuelle Zeit
                newRun.setAverageSpeed(averageSpeed);
                newRun.setDescription("Importiert aus FIT-Datei.");

                // Füge den neuen Lauf zu deiner Liste/Datenbank hinzu
                newRun.setId(nextId++);
                runs.add(newRun);

                redirectAttributes.addFlashAttribute("message",
                        String.format("Datei erfolgreich hochgeladen! Durchschnittsgeschwindigkeit: %.2f km/h", averageSpeed));
            } else {
                redirectAttributes.addFlashAttribute("message",
                        "FIT-Datei erfolgreich hochgeladen, aber keine Durchschnittsgeschwindigkeit gefunden.");
            }

        } catch (FitException e) {
            redirectAttributes.addFlashAttribute("message", "Fehler beim Parsen der FIT-Datei: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Fehler beim Hochladen oder Verarbeiten der Datei: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/runs"; // Weiterleitung zurück zur Bearbeitungsseite
    }

    // Verarbeitet das Absenden des Formulars (Erstellen und Bearbeiten)
    @PostMapping
    public String saveRun(@ModelAttribute Run run, RedirectAttributes redirectAttributes) {
        String username = getCurrentUsername();
        try {
            runService.saveRun(run, username);
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
