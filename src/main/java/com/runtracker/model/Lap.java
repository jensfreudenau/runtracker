package com.runtracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Getter
@Setter
@AllArgsConstructor // Lombok Annotation, optional
public class Lap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    private LocalTime time;
    private int lapNumber;
    private Double distance;
    private int heartrate;
    private int heartrate_max;
    private String duration;
    private Double total_calories;
    private Double maximum_speed;
    private Double height;
    private Integer timestamp;
//    private Integer runId;
//    @Column(name = "start_time")
    private Double startTime;
    private Double start_position_lat;
    private Double start_position_long;
    private Double end_position_lat;
    private Double end_position_long;
    private Double total_elapsed_time;
    private Double total_timer_time;
    private Double total_distance;
    private Double total_cycles;
    private Double total_work;
    private Double enhanced_avg_speed;
    private Double enhanced_max_speed;
    private Double enhanced_min_altitude;
    private Double enhanced_max_altitude;
    private Integer message_index;
    private Double avg_power;
    private Double max_power;
    private Integer total_ascent;
    private Integer total_descent;
    private Double normalized_power;
    private Double avg_vertical_oscillation;
    private Double avg_stance_time_percent;
    private Double avg_stance_time;
    private Double avg_vertical_ratio;
    private Double avg_stance_time_balance;
    private Double avg_step_length;
    private Double enhanced_avg_respiration_rate;
    private Double enhanced_max_respiration_rate;
    private Double event;
    private Double event_type;
    private Integer avg_heart_rate;
    private Integer max_heart_rate;
    private Double avg_cadence;
    private Integer max_cadence;
    private Double intensity;
    private Double lap_trigger;
    private Integer sport;
    private Integer sub_sport;
    private Integer avg_temperature;
    private Integer max_temperature;
    private Double avg_fractional_cadence;
    private Double max_fractional_cadence;
    private Integer min_temperature ;
    public Lap() {

    }

    @ManyToOne(fetch = FetchType.LAZY) // Viele Runden zu einem Lauf
    @JoinColumn(name = "run_id", referencedColumnName = "id", nullable = false) // Fremdschlüssel: Jede Runde muss einem Lauf zugeordnet sein
    private Run run;
}
