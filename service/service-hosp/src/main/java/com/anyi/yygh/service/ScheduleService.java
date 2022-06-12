package com.anyi.yygh.service;

import com.anyi.yygh.model.hosp.Schedule;
import com.anyi.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

/**
 * @author 安逸i
 * @version 1.0
 */
public interface ScheduleService {
    void save(Schedule schedule);

    Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(Schedule schedule);
}
