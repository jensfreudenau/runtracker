package com.runtracker.fit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum FitField {
    TOTAL_DISTANCE("total_distance"),
    TOTAL_TIMER_TIME("total_timer_time"),
    AVG_HEART_RATE("avg_heart_rate"),
    MAX_HEART_RATE("max_heart_rate"),
    TOTAL_CALORIES("total_calories"),
    TIMESTAMP("timestamp"),
    START_TIME("start_time"),
    HEARTRATE_MAX("heartrate_max"),
    AVG_CADENCE("avg_cadence"),
    MAX_CADENCE("max_cadence"),
    AVG_POWER("avg_power"),
    NORMALIZED_POWER("normalized_power"),
    MAX_POWER("max_power"),
    NUM_LAPS("num_laps"),
    AVG_TEMPERATURE("avg_temperature"),
    MAX_TEMPERATURE("max_temperature"),
    TOTAL_ASCENT("total_ascent"),
    TOTAL_DESCENT("total_descent"),
    TRAINING_STRESS_SCORE("training_stress_score"),
    INTENSITY_FACTOR("intensity_factor"),
    TOTAL_WORK("total_work"),
    TOTAL_TRAINING_EFFECT("total_training_effect"),
    TOTAL_ANAEROBIC_TRAINING_EFFECT("total_anaerobic_training_effect"),
    TOTAL_CYCLES("total_cycles"),
    SPORT_PROFILE_NAME("sport_profile_name"),
    AVG_LEFT_POWER_PHASE("avg_left_power_phase"),
    AVG_LEFT_POWER_PHASE_PEAK("avg_left_power_phase_peak"),
    AVG_RIGHT_POWER_PHASE("avg_right_power_phase"),
    AVG_RIGHT_POWER_PHASE_PEAK("avg_right_power_phase_peak"),
    AVG_POWER_POSITION("avg_power_position"),
    MAX_POWER_POSITION("max_power_position"),
    ENHANCED_AVG_SPEED("enhanced_avg_speed"),
    ENHANCED_MAX_SPEED("enhanced_max_speed"),
    TRAINING_LOAD_PEAK("training_load_peak"),
    MESSAGE_INDEX("message_index"),
    FIRST_LAP_INDEX("first_lap_index"),
    LEFT_RIGHT_BALANCE("left_right_balance"),
    THRESHOLD_POWER("threshold_power"),
    ENHANCED_AVG_RESPIRATION_RATE("enhanced_avg_respiration_rate"),
    ENHANCED_MAX_RESPIRATION_RATE("enhanced_max_respiration_rate"),
    ENHANCED_MIN_RESPIRATION_RATE("enhanced_min_respiration_rate"),
    EVENT("event"),
    EVENT_TYPE("event_type"),
    SPORT("sport"),
    SUB_SPORT("sub_sport"),
    AVG_FRACTIONAL_CADENCE("avg_fractional_cadence"),
    MAX_FRACTIONAL_CADENCE("max_fractional_cadence"),
    AVG_LEFT_TORQUE_EFFECTIVENESS("avg_left_torque_effectiveness"),
    AVG_RIGHT_TORQUE_EFFECTIVENESS("avg_right_torque_effectiveness"),
    AVG_LEFT_PEDAL_SMOOTHNESS("avg_left_pedal_smoothness"),
    AVG_RIGHT_PEDAL_SMOOTHNESS("avg_right_pedal_smoothness"),
    AVG_LEFT_PCO("avg_left_pco"),
    AVG_RIGHT_PCO("avg_right_pco"),
    AVG_CADENCE_POSITION("avg_cadence_position"),
    MAX_CADENCE_POSITION("max_cadence_position"),
    MIN_TEMPERATURE("min_temperature"),
    TOTAL_ELAPSED_TIME("total_elapsed_time"),
    TRIGGER("trigger"),
    STAND_COUNT("stand_count"),
    AVG_VAM("avg_vam"),
    TIME_STANDING("time_standing"),
    END_POSITION_LONG("end_position_long"),
    END_POSITION_LAT("end_position_lat"),
    SWC_LONG("swc_long"),
    SWC_LAT("swc_lat"),
    NEC_LONG("nec_long"),
    NEC_LAT("nec_lat"),
    START_POSITION_LONG("start_position_long"),
    START_POSITION_LAT("start_position_lat"),
    AVG_VERTICAL_OSCILLATION("avg_vertical_oscillation"),
    AVG_STANCE_TIME_PERCENT("avg_stance_time_percent"),
    AVG_STANCE_TIME("avg_stance_time"),
    AVG_VERTICAL_RATIO("avg_vertical_ratio"),
    AVG_STANCE_TIME_BALANCE("avg_stance_time_balance"),
    AVG_STEP_LENGTH("avg_step_length"),
    ENHANCED_MIN_ALTITUDE("enhanced_min_altitude"),
    ENHANCED_MAX_ALTITUDE("enhanced_max_altitude"),
    INTENSITY("intensity"),
    LAP_TRIGGER("lap_trigger");

    private final String fitName;

    public static Optional<FitField> fromFitName(String fitName) {
        return Arrays.stream(values())
                .filter(field -> field.fitName.equalsIgnoreCase(fitName))
                .findFirst();
    }
    FitField(String fitName) {
        this.fitName = fitName;
    }

    public String getFitName() {
        return fitName;
    }

    private static final Map<String, FitField> BY_NAME = new HashMap<>();

    static {
        for (FitField field : values()) {
            BY_NAME.put(field.fitName, field);
        }
    }
}

