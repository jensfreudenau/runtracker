package com.runtracker.fit;

import com.runtracker.dto.SummaryDTO;
import com.runtracker.model.Run;
import com.runtracker.service.UtilsService;

public class SummaryToRunMapper {

    public static Run mapToRun(SummaryDTO summaryDTO, Run existingRun) {
        Run run = existingRun != null ? existingRun : new Run();

        run.setDistance(summaryDTO.getDouble(FitField.TOTAL_DISTANCE));
        run.setAverageSpeed(summaryDTO.getDouble(FitField.TOTAL_DISTANCE) / summaryDTO.getDouble(FitField.TOTAL_TIMER_TIME));
        run.setHeartrate(summaryDTO.getInt(FitField.AVG_HEART_RATE));
        run.setHeartrate_max(summaryDTO.getInt(FitField.MAX_HEART_RATE));
        run.setStarttime(summaryDTO.getFormattedTimestamp());
        run.setDuration(UtilsService.formatDuration(summaryDTO.getDouble(FitField.TOTAL_TIMER_TIME)));

        // Weitere Felder bei Bedarf hinzufügen:
        run.setAvg_power(summaryDTO.getInt(FitField.AVG_POWER));
        run.setMax_power(summaryDTO.getInt(FitField.MAX_POWER));
        run.setNormalized_power(summaryDTO.getInt(FitField.NORMALIZED_POWER));
        run.setAvg_temperature(summaryDTO.getInt(FitField.AVG_TEMPERATURE));
        run.setTotal_elapsed_time(summaryDTO.getDouble(FitField.TOTAL_ELAPSED_TIME));
        run.setTotal_timer_time(summaryDTO.getDouble(FitField.TOTAL_TIMER_TIME));
        run.setTotal_distance(summaryDTO.getDouble(FitField.TOTAL_DISTANCE));
        run.setTotal_cycles(summaryDTO.getInt(FitField.TOTAL_CYCLES));
        run.setTotal_work(summaryDTO.getDouble(FitField.TOTAL_WORK));
        run.setSport_profile_name(summaryDTO.getString(FitField.SPORT_PROFILE_NAME));
        run.setAvg_left_power_phase(summaryDTO.getDouble(FitField.AVG_LEFT_POWER_PHASE));
        run.setAvg_left_power_phase_peak(summaryDTO.getDouble(FitField.AVG_LEFT_POWER_PHASE_PEAK));
        run.setAvg_right_power_phase(summaryDTO.getDouble(FitField.AVG_RIGHT_POWER_PHASE));
        run.setAvg_right_power_phase_peak(summaryDTO.getDouble(FitField.AVG_RIGHT_POWER_PHASE_PEAK));
        run.setAvg_power_position(summaryDTO.getInt(FitField.AVG_POWER_POSITION));
        run.setMax_power_position(summaryDTO.getInt(FitField.MAX_POWER_POSITION));
        run.setEnhanced_avg_speed(summaryDTO.getDouble(FitField.ENHANCED_AVG_SPEED));
        run.setEnhanced_max_speed(summaryDTO.getDouble(FitField.ENHANCED_MAX_SPEED));
        run.setTraining_load_peak(summaryDTO.getDouble(FitField.TRAINING_LOAD_PEAK));
        run.setMessage_index(summaryDTO.getDouble(FitField.MESSAGE_INDEX));
        run.setTotal_calories(summaryDTO.getDouble(FitField.TOTAL_CALORIES));
        run.setAvg_power(summaryDTO.getInt(FitField.AVG_POWER));
        run.setMax_power(summaryDTO.getInt(FitField.MAX_POWER));
        run.setFirst_lap_index(summaryDTO.getDouble(FitField.FIRST_LAP_INDEX));
        run.setNum_laps(summaryDTO.getInt(FitField.NUM_LAPS));
        run.setNormalized_power(summaryDTO.getInt(FitField.NORMALIZED_POWER));
        run.setTraining_stress_score(summaryDTO.getDouble(FitField.TRAINING_STRESS_SCORE));
        run.setIntensity_factor(summaryDTO.getDouble(FitField.INTENSITY_FACTOR));
        run.setLeft_right_balance(summaryDTO.getDouble(FitField.LEFT_RIGHT_BALANCE));
        run.setThreshold_power(summaryDTO.getDouble(FitField.THRESHOLD_POWER));
        run.setEnhanced_avg_respiration_rate(summaryDTO.getDouble(FitField.ENHANCED_AVG_RESPIRATION_RATE));
        run.setEnhanced_max_respiration_rate(summaryDTO.getDouble(FitField.ENHANCED_MAX_RESPIRATION_RATE));
        run.setEnhanced_min_respiration_rate(summaryDTO.getDouble(FitField.ENHANCED_MIN_RESPIRATION_RATE));
        run.setEvent(summaryDTO.getDouble(FitField.EVENT));
        run.setEvent_type(summaryDTO.getDouble(FitField.EVENT_TYPE));
        run.setSport(summaryDTO.getInt(FitField.SPORT));
        run.setSub_sport(summaryDTO.getInt(FitField.SUB_SPORT));
        run.setAvg_heart_rate(summaryDTO.getDouble(FitField.AVG_HEART_RATE));
        run.setMax_heart_rate(summaryDTO.getDouble(FitField.MAX_HEART_RATE));
        run.setAvg_cadence(summaryDTO.getDouble(FitField.AVG_CADENCE));
        run.setMax_cadence(summaryDTO.getDouble(FitField.MAX_CADENCE));
        run.setTotal_training_effect(summaryDTO.getDouble(FitField.TOTAL_TRAINING_EFFECT));
        run.setTrigger(summaryDTO.getDouble(FitField.TRIGGER));
        run.setAvg_temperature(summaryDTO.getInt(FitField.AVG_TEMPERATURE));
        run.setMax_temperature(summaryDTO.getInt(FitField.MAX_TEMPERATURE));
        run.setAvg_fractional_cadence(summaryDTO.getDouble(FitField.AVG_FRACTIONAL_CADENCE));
        run.setMax_fractional_cadence(summaryDTO.getDouble(FitField.MAX_FRACTIONAL_CADENCE));
        run.setAvg_left_torque_effectiveness(summaryDTO.getDouble(FitField.AVG_LEFT_TORQUE_EFFECTIVENESS));
        run.setAvg_right_torque_effectiveness(summaryDTO.getDouble(FitField.AVG_RIGHT_TORQUE_EFFECTIVENESS));
        run.setAvg_left_pedal_smoothness(summaryDTO.getDouble(FitField.AVG_LEFT_PEDAL_SMOOTHNESS));
        run.setAvg_right_pedal_smoothness(summaryDTO.getDouble(FitField.AVG_RIGHT_PEDAL_SMOOTHNESS));
        run.setAvg_left_pco(summaryDTO.getDouble(FitField.AVG_LEFT_PCO));
        run.setAvg_right_pco(summaryDTO.getDouble(FitField.AVG_RIGHT_PCO));
        run.setAvg_cadence_position(summaryDTO.getDouble(FitField.AVG_CADENCE_POSITION));
        run.setMax_cadence_position(summaryDTO.getDouble(FitField.MAX_CADENCE_POSITION));
        run.setTotal_anaerobic_training_effect(summaryDTO.getDouble(FitField.TOTAL_ANAEROBIC_TRAINING_EFFECT));
        run.setMin_temperature(summaryDTO.getInt(FitField.MIN_TEMPERATURE));
        run.setStand_count(summaryDTO.getDouble(FitField.STAND_COUNT));
        run.setAvg_vam(summaryDTO.getDouble(FitField.AVG_VAM));
        run.setTime_standing(summaryDTO.getDouble(FitField.TIME_STANDING));
        run.setEnd_position_long(summaryDTO.getDouble(FitField.END_POSITION_LONG));
        run.setEnd_position_lat(summaryDTO.getDouble(FitField.END_POSITION_LAT));
        run.setSwc_long(summaryDTO.getDouble(FitField.SWC_LONG));
        run.setSwc_lat(summaryDTO.getDouble(FitField.SWC_LAT));
        run.setNec_long(summaryDTO.getDouble(FitField.NEC_LONG));
        run.setNec_lat(summaryDTO.getDouble(FitField.NEC_LAT));
        run.setStart_position_long(summaryDTO.getDouble(FitField.START_POSITION_LONG));
        run.setStart_position_lat(summaryDTO.getDouble(FitField.START_POSITION_LAT));
        run.setAvg_vertical_oscillation(summaryDTO.getDouble(FitField.AVG_VERTICAL_OSCILLATION));
        run.setAvg_stance_time_percent(summaryDTO.getDouble(FitField.AVG_STANCE_TIME_PERCENT));
        run.setAvg_stance_time(summaryDTO.getDouble(FitField.AVG_STANCE_TIME));
        run.setAvg_vertical_ratio(summaryDTO.getDouble(FitField.AVG_VERTICAL_RATIO));
        run.setAvg_stance_time_balance(summaryDTO.getDouble(FitField.AVG_STANCE_TIME_BALANCE));
        run.setAvg_step_length(summaryDTO.getDouble(FitField.AVG_STEP_LENGTH));
        return run;
    }
}
