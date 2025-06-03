package com.runnertracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    private Integer sport_id;
    private LocalTime time;
    private Double distance; // in km
    private Double heartrate;
    private Double heartrate_max;
    private Double calories;
    private Double maximum_speed;
    private Double height;
    private String duration;
    private String name;
    private String notes;
    private String location;
    private String description;
    private Double averageSpeed;
    public Run() {}
    // Standardkonstruktor (für JPA benötigt)
    public Run(String name, LocalDate date, LocalTime time, String location, String description, Double averageSpeed) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.description = description;
        this.averageSpeed = averageSpeed;
    }

    // Constructor zum einfachen Erstellen (optional)
    public Run(LocalDate date) {
        this.date = date;
    }

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading ist hier oft effizienter
    @JoinColumn(name = "user_id", nullable = false) // Fremdschlüsselspalte in der run-Tabelle
    private User user;
    // NEU: Beziehung zu Lap

    @OneToMany(mappedBy = "run", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Lap> laps = new ArrayList<>(); // Wichtig: Liste initialisieren, um NullPointerExceptions zu vermeiden

}

