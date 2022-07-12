package com.anyi.yygh.user.service;

import com.anyi.yygh.model.user.UserInfo;
import com.anyi.yygh.vo.user.LoginVo;
import com.anyi.yygh.vo.user.UserAuthVo;
import com.anyi.yygh.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author 安逸i
 * @version 1.0
 */
public interface UserInfoService extends IService<UserInfo> {
    // 登录
    Map<String, Object> login(LoginVo loginVo);
    // 发送验证码
    void getCode(String phone);

    // 用户认证
    void userAuth(Long userId, UserAuthVo userAuthVo);

    // 分页查询所有用户
    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    // 锁定和解锁用户
    void lock(Long userId, Integer status);

    // 显示用户详细信息
    Map<String, Object> show(Long userId);

    // 认证用户信息
    void approval(Long userId, Integer authStatus);
}
