package com.anyi.yygh.service;

import com.anyi.yygh.model.hosp.Schedule;
import com.anyi.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author 安逸i
 * @version 1.0
 */
public interface ScheduleService {
    void save(Schedule schedule);

    Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(Schedule schedule);

    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);

    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);
}
