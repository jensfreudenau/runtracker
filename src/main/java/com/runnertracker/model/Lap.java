package com.runnertracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor // Lombok Annotation, optional
public class Lap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int lapNumber;
    private LocalTime time;
    private Double distance; // in km
    private Double heartrate;
    private Double heartrate_max;
    private String duration;
    private Double calories;
    private Double maximum_speed;
    private Double height;

    public Lap() {

    }

    @ManyToOne(fetch = FetchType.LAZY) // Viele Runden zu einem Lauf
    @JoinColumn(name = "run_id", referencedColumnName = "id", nullable = false) // Fremdschlüssel: Jede Runde muss einem Lauf zugeordnet sein
    private Run run;
}
