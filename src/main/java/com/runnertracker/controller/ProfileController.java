package com.runnertracker.controller;

import com.runnertracker.model.Profile;
import com.runnertracker.service.ProfileService;
import com.runnertracker.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

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
//    public String uploadImage(@RequestParam("imageFile") MultipartFile imageFile,  RedirectAttributes redirectAttributes, Model model) { // Model für direkte Meldungen
    public String saveProfile(@RequestParam("imageFile") MultipartFile imageFile, @ModelAttribute Profile profile, RedirectAttributes redirectAttributes) {
        String username = getCurrentUsername();
        Long userId = userService.getCurrentUserId();
        // 1. Validierung der Datei
        if (!imageFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Bitte wählen Sie eine Datei zum Hochladen aus!");
            redirectAttributes.addFlashAttribute("messageType", "error");
//            return "redirect:/profile"; // Zurück zum Formular mit Fehlermeldung


            // Überprüfe den Dateityp (Beispiel: nur Bilder)
            String contentType = imageFile.getContentType();
            String newFilename = "";
            if (contentType == null || (!contentType.startsWith("image/jpeg") &&
                    !contentType.startsWith("image/png") &&
                    !contentType.startsWith("image/gif"))) {
                redirectAttributes.addFlashAttribute("message", "Ungültiger Dateityp. Nur JPG, PNG oder GIF erlaubt.");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/profile";
            }

            try {
                // 2. Dateinamen generieren (um Konflikte zu vermeiden und Originalnamen zu verbergen)
                String originalFilename = imageFile.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                newFilename = UUID.randomUUID().toString() + fileExtension; // Eindeutiger Dateiname

                // 3. Zielpfad erstellen (falls nicht vorhanden)
                Path uploadPath = Paths.get(UPLOAD_DIR + "/" + userId + "/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 4. Datei speichern
                Path filePath = uploadPath.resolve(newFilename);
                Files.copy(imageFile.getInputStream(), filePath); // Speichert die Datei

                redirectAttributes.addFlashAttribute("message",
                        "Bild '" + originalFilename + "' erfolgreich hochgeladen als '" + newFilename + "'!");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } catch (IOException e) {
                System.err.println("Fehler beim Hochladen der Datei: " + e.getMessage());
                redirectAttributes.addFlashAttribute("message",
                        "Fehler beim Hochladen der Datei: " + e.getMessage());
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/profile";
            }
            profile.setPhoto(newFilename);
        }
        try {

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
