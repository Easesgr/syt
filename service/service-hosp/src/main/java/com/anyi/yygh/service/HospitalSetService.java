package com.anyi.yygh.service;


import com.anyi.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 医院设置表 服务类
 * </p>
 *
 * @author anyi
 * @since 2022-06-10
 */
public interface HospitalSetService extends IService<HospitalSet> {

    String getSignKey(String hoscode);
}
