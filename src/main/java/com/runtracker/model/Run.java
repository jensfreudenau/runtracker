package com.runtracker.model;

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
    private String starttime;
    private Integer sport_id;
    private LocalTime time;
    private Double distance; // in km
    private Integer heartrate;
    private Integer heartrate_max;
    private Integer calories;
    private Integer cadence;
    private Integer avg_power;
    private Integer normalized_power;
    private Integer max_power;
    private Integer num_laps;
    private Integer avg_temperature;
    private Integer max_temperature;
    private Integer total_ascent;
    private Integer total_descent;
    private Double maximum_speed;
    private Double training_stress_score;
    private Double intensity_factor;
    private Double total_training_effect;
    private Double total_anaerobic_training_effect;
    private Double height;
    private Double total_work;
    private String duration;
    private String name;
    private String notes;
    private String location;
    private String description;
    private Double averageSpeed;
    private Double total_elapsed_time;
    private Double total_timer_time;
    private Double total_distance;
    private Integer total_cycles;
    private String sport_profile_name;
    private Double avg_left_power_phase;
    private Double avg_left_power_phase_peak;
    private Double avg_right_power_phase;
    private Double avg_right_power_phase_peak;
    private Integer avg_power_position;
    private Integer max_power_position;
    private Double enhanced_avg_speed;
    private Double enhanced_max_speed;
    private Double training_load_peak;
    private Double message_index;
    private Double total_calories;
    private Double first_lap_index;
    private Double left_right_balance;
    private Double threshold_power;
    private Double enhanced_avg_respiration_rate;
    private Double enhanced_max_respiration_rate;
    private Double enhanced_min_respiration_rate;
    private Double event;
    private Double event_type;
    private Integer sport;
    private Integer sub_sport;
    private Double avg_heart_rate;
    private Double max_heart_rate;
    private Double avg_cadence;
    private Double max_cadence;
    private Double trigger;
    private Double avg_fractional_cadence;
    private Double max_fractional_cadence;
    private Double avg_left_torque_effectiveness;
    private Double avg_right_torque_effectiveness;
    private Double avg_left_pedal_smoothness;
    private Double avg_right_pedal_smoothness;
    private Double avg_left_pco;
    private Double avg_right_pco;
    private Double avg_cadence_position;
    private Double max_cadence_position;
    private Integer min_temperature;
    private Double stand_count;
    private Double avg_vam;
    private Double time_standing;
    private Double end_position_long;
    private Double end_position_lat;
    private Double swc_long;
    private Double swc_lat;
    private Double nec_long;
    private Double nec_lat;
    private Double start_position_long;
    private Double start_position_lat;
    private Double avg_vertical_oscillation;
    private Double avg_stance_time_percent;
    private Double avg_stance_time;
    private Double avg_vertical_ratio;
    private Double avg_stance_time_balance;
    private Double avg_step_length;


    public Run() {
    }

    // Standardkonstruktor (für JPA benötigt)
    public Run(String name, String starttime, String location, String description, Double averageSpeed) {
        this.name = name;
        this.starttime = starttime;
        this.location = location;
        this.description = description;
        this.averageSpeed = averageSpeed;
    }

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading ist hier oft effizienter
    @JoinColumn(name = "user_id", nullable = false) // Fremdschlüsselspalte in der run-Tabelle
    private User user;
    // NEU: Beziehung zu Lap

    @OneToMany(mappedBy = "run", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Lap> laps = new ArrayList<>(); // Wichtig: Liste initialisieren, um NullPointerExceptions zu vermeiden
}

