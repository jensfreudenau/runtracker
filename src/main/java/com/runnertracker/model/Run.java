package com.runnertracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor // Lombok Annotation, optional
public class Run {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Verwende LocalDate und die @DateTimeFormat-Annotation
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // Erwartet YYYY-MM-DD für eingehende Daten (vom HTML-Date-Input)
    private LocalDate date;
    private Double distance; // in km
    private String duration;
    private String notes;

    // Standardkonstruktor (für JPA benötigt)
    public Run() {
    }

    // Constructor zum einfachen Erstellen (optional)
    public Run(LocalDate date) {
        this.date = date;
    }

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading ist hier oft effizienter
    @JoinColumn(name = "user_id", nullable = false) // Fremdschlüsselspalte in der run-Tabelle
    private User user;
}

