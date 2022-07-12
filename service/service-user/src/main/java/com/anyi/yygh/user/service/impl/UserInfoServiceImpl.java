package com.anyi.yygh.user.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.excel.util.StringUtils;
import com.anyi.common.exception.YyghException;
import com.anyi.common.helper.JwtHelper;
import com.anyi.common.result.RedisEnum;
import com.anyi.common.result.Result;
import com.anyi.common.result.ResultCodeEnum;
import com.anyi.yygh.client.SmsFeignClient;
import com.anyi.yygh.enums.AuthStatusEnum;
import com.anyi.yygh.model.user.Patient;
import com.anyi.yygh.model.user.UserInfo;
import com.anyi.yygh.user.mapper.UserInfoMapper;
import com.anyi.yygh.user.service.PatientService;
import com.anyi.yygh.user.service.UserInfoService;
import com.anyi.yygh.vo.user.LoginVo;
import com.anyi.yygh.vo.user.UserAuthVo;
import com.anyi.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 安逸i
 * @version 1.0
 */
@Service
public class UserInfoServiceImpl extends
        ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SmsFeignClient smsFeignClient;


    @Autowired
    private PatientService patientService;

    /**
     * 根据电话号码登录
     * @param loginVo
     * @return
     */
    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        // 获取电话号码和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        // 检查是否为空
        if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(code)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //TODO 比对验证码是否正确，跟redis中的code相比较
        String  myCode = (String) redisTemplate.opsForValue().get(RedisEnum.USER_CODE + phone);
        if (myCode == null){
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        if (!myCode.equals(code)){
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        // 查询数据库是否是第一次登录
        UserInfo one = getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getPhone, phone));

        if (one == null){
            one = new UserInfo();
            // 第一次登录存储到数据库中
            one.setPhone(phone);
            one.setName("安逸");
            save(one);
        }

        // 返回信息
        Map<String ,Object> map = new HashMap<>();
        String name =one.getName();
        if (StringUtil.isEmpty(name)){
            name = one.getNickName();
        }
        if (StringUtil.isEmpty(name)){
            name = one.getPhone();
        }
        // 生成token 并且存储到redis中
        String token = JwtHelper.createToken(one.getId(), name);
        redisTemplate.opsForValue().set(RedisEnum.USER_TOKEN + phone,token,30, TimeUnit.MINUTES);
        // 清除验证码
        redisTemplate.delete(RedisEnum.USER_CODE + phone);
        map.put("name", name);
        map.put("token", token);
        return map;
    }

    /**
     * 发送验证码
     * @param phone
     */
    @Override
    public void getCode(String phone) {
        // 发送验证码
        Result result = smsFeignClient.sendCode(phone);
        if (result.getCode() != 200){
            throw new YyghException(ResultCodeEnum.CODE_Send_ERROR);
        }
    }

    /**
     * 用户认证
     * @param userId
     * @param userAuthVo
     */
    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        // 查询用户信息
        UserInfo user = getById(userId);

        if (user ==null){
            throw new YyghException("用户不存在",40000);
        }
        // 设置认证信息，后面审核显示
        user.setName(userAuthVo.getName());
        user.setCertificatesNo(userAuthVo.getCertificatesNo());
        user.setCertificatesType(userAuthVo.getCertificatesType());
        user.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        // 更新信息
        updateById(user);
    }

    /**
     * 分页查询用户信息
     * @param pageParam
     * @param userInfoQueryVo
     * @return
     */
    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
        //UserInfoQueryVo获取条件值
        String name = userInfoQueryVo.getKeyword(); //用户名称
        Integer status = userInfoQueryVo.getStatus();//用户状态
        Integer authStatus = userInfoQueryVo.getAuthStatus(); //认证状态
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin(); //开始时间
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd(); //结束时间
        //对条件值进行非空判断
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)) {
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(status)) {
            wrapper.eq("status",status);
        }
        if(!StringUtils.isEmpty(authStatus)) {
            wrapper.eq("auth_status",authStatus);
        }
        if(!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time",createTimeEnd);
        }
        //调用mapper的方法
        IPage<UserInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        //编号变成对应值封装
        pages.getRecords().stream().forEach(item -> {
            this.packageUserInfo(item);
        });
        return pages;
    }

    /**
     * 锁定和解锁用户
     * @param userId
     * @param status
     */
    @Override
    public void lock(Long userId, Integer status) {
        if(status.intValue() == 0 || status.intValue() == 1) {
            UserInfo userInfo = this.getById(userId);
            userInfo.setStatus(status);
            this.updateById(userInfo);
        }
    }

    /**
     * 显示用户详细信息
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> show(Long userId) {
        Map<String,Object> map = new HashMap<>();
        //根据userid查询用户信息
        UserInfo userInfo = this.packageUserInfo(baseMapper.selectById(userId));
        map.put("userInfo",userInfo);
        //根据userid查询就诊人信息
        List<Patient> patientList = patientService.findAll(userId);
        map.put("patientList",patientList);
        return map;
    }

    /**
     * 认证用户信息
     * @param userId
     * @param authStatus
     */
    @Override
    public void approval(Long userId, Integer authStatus) {
        if(authStatus.intValue()==2 || authStatus.intValue()==-1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }

    //编号变成对应值封装
    private UserInfo packageUserInfo(UserInfo userInfo) {
        //处理认证状态编码
        userInfo.getParam().put("authStatusString", AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        //处理用户状态 0  1
        String statusString = userInfo.getStatus().intValue()==0 ?"锁定" : "正常";
        userInfo.getParam().put("statusString",statusString);
        return userInfo;
    }
}
