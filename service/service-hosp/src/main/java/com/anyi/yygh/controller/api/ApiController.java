package com.anyi.yygh.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.anyi.common.exception.YyghException;
import com.anyi.common.helper.HttpRequestHelper;
import com.anyi.common.result.Result;
import com.anyi.common.result.ResultCodeEnum;
import com.anyi.common.utils.MD5;
import com.anyi.yygh.model.hosp.Department;
import com.anyi.yygh.model.hosp.Hospital;
import com.anyi.yygh.model.hosp.HospitalSet;
import com.anyi.yygh.model.hosp.Schedule;
import com.anyi.yygh.service.DepartmentService;
import com.anyi.yygh.service.HospitalService;
import com.anyi.yygh.service.HospitalSetService;
import com.anyi.yygh.service.ScheduleService;
import com.anyi.yygh.vo.hosp.DepartmentQueryVo;
import com.anyi.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 安逸i
 * @version 1.0
 */

@RestController
@CrossOrigin
@Api(tags ="医院对接接口Api")
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;


    @Autowired
    private ScheduleService scheduleService;

    // 上传医院信息
    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request){
        // 解析成字符串
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String strJson = JSONObject.toJSONString(stringObjectMap);
        // 转换成java对象
        Hospital hospital = JSONObject.parseObject(strJson, Hospital.class);
        String hoscode = hospital.getHoscode();
        checkSignKey(stringObjectMap,hoscode);
        hospitalService.save(hospital);
        return Result.ok();
    }
    // 查看医院信息
    @PostMapping("/hospital/show")
    public Result showHospital(HttpServletRequest request){
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String strJson = JSONObject.toJSONString(stringObjectMap);
        Hospital hospital = JSONObject.parseObject(strJson, Hospital.class);
        checkSignKey(stringObjectMap, hospital.getHoscode());
        Hospital data = hospitalService.getHospital(hospital.getHoscode());
        return Result.ok(data);
    }


    // 添加科室
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        // 解析成字符串
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String strJson = JSONObject.toJSONString(stringObjectMap);
        // 转换成java对象
        Department department = JSONObject.parseObject(strJson, Department.class);
        String hoscode = department.getHoscode();
        checkSignKey(stringObjectMap,hoscode);
        departmentService.save(department);

        return Result.ok();
    }
    // 查看科室
    @PostMapping("/department/list")
    public Result showDepartment(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
        checkSignKey(paramMap, hoscode);
        //非必填
        String depcode = (String)paramMap.get("depcode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String)paramMap.get("limit"));
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        departmentQueryVo.setDepcode(depcode);
        Page<Department> pageModel = departmentService.selectPage(page, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }
    // 删除科室
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request){
        // 解析成字符串
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String strJson = JSONObject.toJSONString(stringObjectMap);
        // 转换成java对象
        Department department = JSONObject.parseObject(strJson, Department.class);
        departmentService.remove(department);
        return Result.ok();
    }

    // 添加排班

    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        // 解析成字符串
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String strJson = JSONObject.toJSONString(stringObjectMap);
        // 转换成java对象
        Schedule schedule = JSONObject.parseObject(strJson, Schedule.class);
        String hoscode = schedule.getHoscode();
        checkSignKey(stringObjectMap,hoscode);
        scheduleService.save(schedule);
        return Result.ok();
    }

    // 分页查询排班
    @PostMapping("/schedule/list")
    public Result list(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
        checkSignKey(paramMap, hoscode);
        //非必填
        String depcode = (String)paramMap.get("depcode");
        String  doccode = (String) paramMap.get("doccode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String)paramMap.get("limit"));
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        scheduleQueryVo.setDoccode(doccode);
        Page<Schedule> pageModel = scheduleService.selectPage(page, limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }

    // 删除排班

    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        // 解析成字符串
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String strJson = JSONObject.toJSONString(stringObjectMap);
        // 转换成java对象
        Schedule schedule = JSONObject.parseObject(strJson, Schedule.class);
        scheduleService.remove(schedule);
        return Result.ok();
    }
    /**
     * 验证签名
     * @param stringObjectMap
     * @param hoscode
     */
    private void checkSignKey(Map<String, Object> stringObjectMap,String hoscode) {
        // 签名验证
        String signKey = hospitalSetService.getSignKey(hoscode);
        // 校验签名
        String  sign = (String) stringObjectMap.get("sign");
        if (!(sign.equals(signKey))){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
    }
}
