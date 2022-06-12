package com.anyi.yygh.service.impl;


import com.anyi.yygh.model.hosp.Schedule;
import com.anyi.yygh.repository.ScheduleRepository;
import com.anyi.yygh.service.ScheduleService;
import com.anyi.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 安逸i
 * @version 1.0
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    /**
     * 上传排版信息
     * @param schedule
     */
    @Override
    public void save(Schedule schedule) {
        // 查询是否存在
        Schedule scheduleExist = scheduleRepository.findScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(),schedule.getHosScheduleId());
        if (scheduleExist !=null){
            schedule.setCreateTime(scheduleExist.getCreateTime());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }else {
            schedule.setIsDeleted(0);
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }
    }

    /**
     * 分页查询排班
     * @param page
     * @param limit
     * @param scheduleQueryVo
     * @return
     */
    @Override
    public Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //0为第一页
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        Schedule schedule = new Schedule();
        // 复制
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setIsDeleted(0);

        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

        //创建实例
        Example<Schedule> example = Example.of(schedule, matcher);
        Page<Schedule> pages = scheduleRepository.findAll(example, pageable);
        return pages;
    }

    /**
     * 删除排班
     * @param schedule
     */
    @Override
    public void remove(Schedule schedule) {
        Schedule one = scheduleRepository.findScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        scheduleRepository.deleteById(one.getId());
    }
}
