package com.runtracker.fit;

import com.runtracker.dto.LapDTO;
import com.runtracker.model.Lap;


public class LapToRunMapper {

    public static Lap mapToLap(LapDTO lapDTO, Lap lap) {
        lap.setDistance(lapDTO.getDouble(FitField.TOTAL_DISTANCE));
        lap.setTimestamp(lapDTO.getInt(FitField.TIMESTAMP));
        lap.setStartTime(lapDTO.getDouble(FitField.START_TIME));
        lap.setStart_position_lat(lapDTO.getDouble(FitField.START_POSITION_LAT));
        lap.setStart_position_long(lapDTO.getDouble(FitField.START_POSITION_LONG));
        lap.setEnd_position_lat(lapDTO.getDouble(FitField.END_POSITION_LAT));
        lap.setEnd_position_long(lapDTO.getDouble(FitField.END_POSITION_LONG));
        lap.setTotal_elapsed_time(lapDTO.getDouble(FitField.TOTAL_ELAPSED_TIME));
        lap.setTotal_timer_time(lapDTO.getDouble(FitField.TOTAL_TIMER_TIME));
        lap.setTotal_distance(lapDTO.getDouble(FitField.TOTAL_DISTANCE));
        lap.setTotal_cycles(lapDTO.getDouble(FitField.TOTAL_CYCLES));
        lap.setTotal_work(lapDTO.getDouble(FitField.TOTAL_WORK));
        lap.setEnhanced_avg_speed(lapDTO.getDouble(FitField.ENHANCED_AVG_SPEED));
        lap.setEnhanced_max_speed(lapDTO.getDouble(FitField.ENHANCED_MAX_SPEED));
        lap.setEnhanced_min_altitude(lapDTO.getDouble(FitField.ENHANCED_MIN_ALTITUDE));
        lap.setEnhanced_max_altitude(lapDTO.getDouble(FitField.ENHANCED_MAX_ALTITUDE));
        lap.setLapNumber(lapDTO.getInt(FitField.MESSAGE_INDEX));
        lap.setTotal_calories(lapDTO.getDouble(FitField.TOTAL_CALORIES));
        lap.setAvg_power(lapDTO.getInt(FitField.AVG_POWER));
        lap.setMax_power(lapDTO.getInt(FitField.MAX_POWER));
        lap.setTotal_ascent(lapDTO.getInt(FitField.TOTAL_ASCENT));
        lap.setTotal_descent(lapDTO.getInt(FitField.TOTAL_DESCENT));
        lap.setNormalized_power(lapDTO.getDouble(FitField.NORMALIZED_POWER));
        lap.setAvg_stance_time(lapDTO.getDouble(FitField.AVG_STANCE_TIME));
        lap.setAvg_vertical_ratio(lapDTO.getDouble(FitField.AVG_VERTICAL_RATIO));
        lap.setAvg_stance_time_balance(lapDTO.getDouble(FitField.AVG_STANCE_TIME_BALANCE));
        lap.setAvg_step_length(lapDTO.getDouble(FitField.AVG_STEP_LENGTH));
        lap.setEnhanced_avg_respiration_rate(lapDTO.getDouble(FitField.ENHANCED_AVG_RESPIRATION_RATE));
        lap.setEnhanced_max_respiration_rate(lapDTO.getDouble(FitField.ENHANCED_MAX_RESPIRATION_RATE));
        lap.setEvent(lapDTO.getDouble(FitField.EVENT));
        lap.setEvent_type(lapDTO.getDouble(FitField.EVENT_TYPE));
        lap.setAvg_heart_rate(lapDTO.getInt(FitField.AVG_HEART_RATE));
        lap.setMax_heart_rate(lapDTO.getInt(FitField.MAX_HEART_RATE));
        lap.setAvg_cadence(lapDTO.getInt(FitField.AVG_CADENCE));
        lap.setMax_cadence(lapDTO.getInt(FitField.MAX_CADENCE));
        lap.setIntensity(lapDTO.getDouble(FitField.INTENSITY));
        lap.setLap_trigger(lapDTO.getDouble(FitField.LAP_TRIGGER));
        lap.setSport(lapDTO.getInt(FitField.SPORT));
        lap.setSub_sport(lapDTO.getInt(FitField.SUB_SPORT));
        lap.setAvg_temperature(lapDTO.getInt(FitField.AVG_TEMPERATURE));
        lap.setMax_temperature(lapDTO.getInt(FitField.MAX_TEMPERATURE));
        lap.setAvg_fractional_cadence(lapDTO.getDouble(FitField.AVG_FRACTIONAL_CADENCE));
        lap.setMax_fractional_cadence(lapDTO.getDouble(FitField.MAX_FRACTIONAL_CADENCE));
        lap.setMin_temperature(lapDTO.getInt(FitField.MIN_TEMPERATURE));

        return lap;
    }
}
