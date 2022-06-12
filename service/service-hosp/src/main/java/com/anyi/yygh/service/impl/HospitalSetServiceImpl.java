package com.anyi.yygh.service.impl;

import com.anyi.yygh.mapper.HospitalSetMapper;
import com.anyi.yygh.model.hosp.HospitalSet;
import com.anyi.yygh.service.HospitalSetService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 医院设置表 服务实现类
 * </p>
 *
 * @author anyi
 * @since 2022-06-10
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    /**
     * 获取签名
     * @param hoscode
     * @return
     */
    @Override
    public String getSignKey(String hoscode) {
        HospitalSet one = getOne(new LambdaQueryWrapper<HospitalSet>()
                .eq(HospitalSet::getHoscode, hoscode));
        if (one == null){
            return null;
        }
        return one.getSignKey();
    }
}
