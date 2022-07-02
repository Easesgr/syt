package com.anyi.yygh.service.impl;

import com.anyi.yygh.client.DictFeignClient;
import com.anyi.yygh.enums.DictEnum;
import com.anyi.yygh.model.hosp.Hospital;
import com.anyi.yygh.repository.HospitalRepository;
import com.anyi.yygh.service.HospitalService;
import com.anyi.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 安逸i
 * @version 1.0
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Resource
    private DictFeignClient dictFeignClient;

    /**
     * 上传医院信息
     * @param hospital
     */
    @Override
    public void save(Hospital hospital) {
        String hoscode = hospital.getHoscode();
        Hospital targetHospital = hospitalRepository.getHospitalByHoscode(hoscode);
        hospital.setLogoData(hospital.getLogoData().replaceAll(" ", "+"));
        if (targetHospital !=null){
            // 如果不是空，就是更新，需要填写基本信息
            hospital.setStatus(targetHospital.getStatus());
            hospital.setCreateTime(targetHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospital.setStatus(0);
            hospitalRepository.save(hospital);
        }else{
            hospital.setUpdateTime(new Date());
            hospital.setCreateTime(new Date());
            hospital.setIsDeleted(0);
            hospital.setStatus(0);
            hospitalRepository.save(hospital);
        }

    }

    @Override
    public Hospital getHospital(String hoscode) {
        // 根据 hoscode 查询医院信息
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }
    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        // 创建一个排序规则
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //创建一个pageable对象 分页信息配置
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        // 是否有查询条件 创建一个需要查询的类，后面example传入
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

        //创建实例
        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

        // 医院等级和医院的地址调用cmn中的方法实现添加值
        pages.getContent().stream().forEach(item->{
            this.setMessage(item);
        });
        return pages;
    }

    // 更新上线状态
    @Override
    public void updateStatus(String id, Integer status) {
        // 状态合法性判断
        if(status.intValue() == 0 || status.intValue() == 1) {
            // 根据id查询出数据
            Hospital hospital = hospitalRepository.findById(id).get();
            // 更新状态信息
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            // 重新保存起来
            hospitalRepository.save(hospital);
        }
    }
    @Override
    public Map<String, Object> show(String id) {
        Map<String, Object> result = new HashMap<>();

        Hospital hospital = this.setMessage(hospitalRepository.getHospitalById(id));
        result.put("hospital", hospital);

        //单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospital != null){
            return hospital.getHosname();
        }
        return null;
    }

    /**
     * 医院模糊查询
     * @param hosname
     * @return
     */
    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    private Hospital setMessage(Hospital hospital) {
        // 调用远程接口获得对应的医院等级
        String hostypeString = dictFeignClient.getName(DictEnum.HOSTYPE.getDictCode(), hospital.getHostype());
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        String districtString = dictFeignClient.getName(hospital.getDistrictCode());
        Map<String, Object> params = hospital.getParam();
        params.put("hostypeString",hostypeString);
        params.put("fullAddress", provinceString + cityString + districtString);
        return  hospital;
    }
}

