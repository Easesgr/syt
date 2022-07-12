package com.anyi.yygh.user.service;

import com.anyi.yygh.model.user.Patient;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 安逸i
 * @version 1.0
 */
public interface PatientService extends IService<Patient> {
    // 当前用户下所有的就诊人员
    List<Patient> findAll(Long userId);

    // 根据id获取就诊人员
    Patient getPatientId(Long id);


}
