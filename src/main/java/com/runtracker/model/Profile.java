package com.runtracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@NoArgsConstructor // Lombok Annotation
@AllArgsConstructor // Lombok Annotation, optional
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String photo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // Erwartet YYYY-MM-DD für eingehende Daten (vom HTML-Date-Input)
    private LocalDate birthday;
    private String sex;
    private String place;
    private String mainClub;
    private Float weight;
    private String url;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading ist hier oft effizienter
    @JoinColumn(name = "user_id", nullable = false) // Fremdschlüsselspalte in der run-Tabelle
    private User user;
}
