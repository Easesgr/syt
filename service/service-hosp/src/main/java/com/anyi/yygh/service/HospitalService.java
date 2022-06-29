package com.anyi.yygh.service;

import com.anyi.yygh.model.hosp.Hospital;
import com.anyi.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

/**
 * @author 安逸i
 * @version 1.0
 */
public interface HospitalService {
    // 上传医院信息
    void save(Hospital hospital);

    Hospital getHospital(String hoscode);

    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Object show(String id);

    String getHospName(String hoscode);
}
