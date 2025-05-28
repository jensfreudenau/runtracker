package com.runnertracker.controller;

import com.runnertracker.model.Profile;
import com.runnertracker.service.ProfileService;
import com.runnertracker.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeParseException;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public String profilePage(Model model) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return "redirect:/runs";
        }
        Optional<Profile> profile = profileService.findProfileByUserId(currentUserId);
        // 3. Daten dem Model hinzufügen
        // Hier entpacken wir das Optional:
        // Wenn ein Profil vorhanden ist, wird es dem Model hinzugefügt.
        // Wenn nicht, wird null hinzugefügt (oder du könntest eine Fehlermeldung/Umleitung hinzufügen).
        model.addAttribute("profile", profile.orElse(null));
        return "profile/profile";
    }

    @GetMapping("/edit/{id}")
    public String showEditProfileForm(Model model, RedirectAttributes redirectAttributes) {
        Long userId = userService.getCurrentUserId();
        return profileService.findProfileByUserId(userId)
                .map(profile -> {
                    model.addAttribute("profile", profile);
                    model.addAttribute("pageTitle", "Profil bearbeiten");
                    return "profile/profile-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Profil nicht gefunden oder nicht berechtigt.");
                    return "redirect:/profile";
                });
    }

    // Verarbeitet das Absenden des Formulars (Erstellen und Bearbeiten)
    @PostMapping
    public String saveProfile(@ModelAttribute Profile profile, RedirectAttributes redirectAttributes) {
        String username = getCurrentUsername();
        try {
            // Optional: Datenvalidierung hier hinzufügen (z.B. @Valid)
            // Konvertierung von String zu LocalDate, falls nicht direkt im Model/Binding
            // Oder im Formular direkt das passende Format verwenden.
            // Example:
//             if (run.getDate() == null && run.getDateString() != null) {
//                 run.setDate(LocalDate.parse(run.getDateString()));
//             }

            profileService.saveProfile(profile, username);
            redirectAttributes.addFlashAttribute("message", "Profil erfolgreich gespeichert!");
            return "redirect:/profile";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/profile"; // Oder zurück zum Formular bleiben mit Fehlermeldung
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("error", "Ungültiges Datumsformat. Bitte YYYY-MM-DD verwenden.");
            return "redirect:/profile"; // Oder zurück zum Formular bleiben mit Fehlermeldung
        }
    }

    // Löscht einen Lauf
    @GetMapping("/delete/{id}")
    public String deleteProfile(RedirectAttributes redirectAttributes) {
        String username = getCurrentUsername();
        try {
//            Long userId = userService.getCurrentUserId();
            profileService.deleteProfile(1L);
            redirectAttributes.addFlashAttribute("message", "Profil erfolgreich gelöscht!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    // Hilfsmethode, um den Benutzernamen des eingeloggten Benutzers zu erhalten
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
