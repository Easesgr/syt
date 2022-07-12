package com.anyi.yygh.user.controller;

import com.anyi.common.result.Result;
import com.anyi.common.utils.AuthContextHolder;
import com.anyi.yygh.model.user.Patient;
import com.anyi.yygh.user.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 安逸i
 * @version 1.0
 */

@Api(tags = "就诊人管理")
@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {

    @Autowired
    private PatientService patientService;

    // 查询就诊人列表
    @ApiOperation("查询就诊人列表")
    @GetMapping("/auth/findAll")
    public Result findAll(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list = patientService.findAll(userId);
        return Result.ok(list);
    }
    // 添加就诊人
    @ApiOperation("添加就诊人")
    @PostMapping("auth/save")
    public Result save(@RequestBody Patient patient, HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }
    //根据id获取就诊人信息
    @ApiOperation("根据id获取就诊人信息")
    @GetMapping("auth/get/{id}")
    public Result getPatient(@PathVariable Long id) {
        Patient patient = patientService.getPatientId(id);
        return Result.ok(patient);
    }
    //修改就诊人
    @ApiOperation("修改就诊人")
    @PostMapping("auth/update")
    public Result updatePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Result.ok();
    }
    //删除就诊人
    @ApiOperation("删除就诊人")
    @DeleteMapping("auth/remove/{id}")
    public Result removePatient(@PathVariable Long id) {
        patientService.removeById(id);
        return Result.ok();
    }

}