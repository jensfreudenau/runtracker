package com.runnertracker.exceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Die Datei ist zu groß! Maximale Dateigröße ist " +
                (exc.getMaxUploadSize() / (1024 * 1024)) + "MB."); // Konvertiere Bytes in MB
        redirectAttributes.addFlashAttribute("messageType", "error");
        return "redirect:/profile"; // Oder zu einer anderen Fehlerseite
    }
}
