package com.anyi.yygh.user.service.impl;

import com.anyi.yygh.client.DictFeignClient;
import com.anyi.yygh.enums.DictEnum;
import com.anyi.yygh.model.user.Patient;
import com.anyi.yygh.user.mapper.PatientMapper;
import com.anyi.yygh.user.service.PatientService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 安逸i
 * @version 1.0
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private DictFeignClient dictFeignClient;


    /**
     * 获取当前用户下所有的就诊人员
     * @param userId
     * @return
     */
    @Override
    public List<Patient> findAll(Long userId) {
        // 根据id查询所有就诊人员
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId,userId);
        List<Patient> list = list(wrapper);
        // 封装就诊人员其他参数
        list.forEach(item->{
            this.packPatient(item);
        });

        return list;
    }

    /**
     * 根据 patientId获取就诊人信息
     * @param id
     * @return
     */
    @Override
    public Patient getPatientId(Long id) {
        Patient patient = getById(id);
        patient = this.packPatient(patient);
        return patient;
    }

    // 查询字典信息，封装就诊人员其他信息
    private Patient packPatient(Patient patient) {
        //根据证件类型编码，获取证件类型具体指
        String certificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());//联系人证件
        //联系人证件类型
        String contactsCertificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(),patient.getContactsCertificatesType());
        //省
        String provinceString = dictFeignClient.getName(patient.getProvinceCode());
        //市
        String cityString = dictFeignClient.getName(patient.getCityCode());
        //区
        String districtString = dictFeignClient.getName(patient.getDistrictCode());
        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
        return patient;
    }
}
