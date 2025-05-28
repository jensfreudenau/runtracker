package com.runnertracker.controller;

import com.runnertracker.model.Run;
import com.runnertracker.service.RunService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/runs")
public class RunController {
    private final RunService runService;

    public RunController(RunService runService) {
        this.runService = runService;
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

        return "runs/runs";
    }

    // Zeigt das Formular zum Erstellen eines neuen Laufs an
    @GetMapping("/new")
    public String showCreateRunForm(Model model) {
        model.addAttribute("run", new Run()); // Leeres Run-Objekt für das Formular
        model.addAttribute("pageTitle", "Neuen Lauf hinzufügen");
        return "runs/run-form";
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

    // Verarbeitet das Absenden des Formulars (Erstellen und Bearbeiten)
    @PostMapping
    public String saveRun(@ModelAttribute Run run, RedirectAttributes redirectAttributes) {
        String username = getCurrentUsername();
        try {
            // Optional: Datenvalidierung hier hinzufügen (z.B. @Valid)
            // Konvertierung von String zu LocalDate, falls nicht direkt im Model/Binding
            // Oder im Formular direkt das passende Format verwenden.
            // Example:
//             if (run.getDate() == null && run.getDateString() != null) {
//                 run.setDate(LocalDate.parse(run.getDateString()));
//             }

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
