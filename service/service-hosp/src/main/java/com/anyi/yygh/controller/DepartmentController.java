package com.anyi.yygh.controller;

import com.anyi.common.result.Result;
import com.anyi.yygh.model.hosp.Department;
import com.anyi.yygh.repository.DepartmentRepository;
import com.anyi.yygh.service.DepartmentService;
import com.anyi.yygh.service.ScheduleService;
import com.anyi.yygh.vo.hosp.DepartmentVo;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 安逸i
 * @version 1.0
 */
@RestController
//@CrossOrigin
@Api(tags = "后台科室管理")
@RequestMapping("/admin/hosp/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;



    /**
     * 根据医院编号查询科室
     */
    @ApiOperation("根据医院编号查询科室")
    @GetMapping("/list/{hoscode}")
    public Result getList(@PathVariable String hoscode){
        List<DepartmentVo> list = departmentService.getList(hoscode);
        return Result.ok(list);
    }

}
