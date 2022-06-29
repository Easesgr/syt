package com.anyi.yygh.repository;

import com.anyi.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author 安逸i
 * @version 1.0
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {
    Schedule findScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
}
