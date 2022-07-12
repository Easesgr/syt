package com.anyi.yygh.user.controller;

import com.anyi.common.result.Result;
import com.anyi.common.utils.AuthContextHolder;
import com.anyi.yygh.model.user.UserInfo;
import com.anyi.yygh.user.mapper.UserInfoMapper;
import com.anyi.yygh.user.service.UserInfoService;
import com.anyi.yygh.vo.user.LoginVo;
import com.anyi.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 安逸i
 * @version 1.0
 */

@RestController
@Api(tags = "前台用户登录")
@RequestMapping("/api/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation("根据手机号登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){
        Map<String ,Object> info = userInfoService.login(loginVo);
        return Result.ok(info);
    }

    @ApiOperation("发送验证码")
    @GetMapping("/code/{phone}")
    public Result getCode(@PathVariable String phone){
        userInfoService.getCode(phone);
        return Result.ok();
    }
    @ApiOperation("用户认证接口")
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        //传递两个参数，第一个参数用户id，第二个参数认证数据vo对象
        userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
        return Result.ok();
    }

    @ApiOperation("获取用户id信息接口")
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }

}
