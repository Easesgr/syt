package com.anyi.yygh.controller.api;

import com.anyi.common.result.Result;
import com.anyi.yygh.model.hosp.Hospital;
import com.anyi.yygh.service.HospitalService;
import com.anyi.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author 安逸i
 * @version 1.0
 */
@Api(tags = "医院管理接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospitalApiController {

    @Autowired
    private HospitalService hospitalService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @PathVariable Integer page,
            @PathVariable Integer limit,
            HospitalQueryVo hospitalQueryVo) {
        //显示上线的医院
        //hospitalQueryVo.setStatus(1);
        Page<Hospital> pageModel = hospitalService.selectPage(page, limit, hospitalQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "根据医院名称获取医院列表")
    @GetMapping("findByHosname/{hosname}")
    public Result findByHosname(
            @ApiParam(name = "hosname", value = "医院名称", required = true)
            @PathVariable String hosname) {
        return Result.ok(hospitalService.findByHosname(hosname));
    }
}
