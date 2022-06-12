package com.anyi.yygh.service;

import com.anyi.yygh.model.hosp.Department;
import com.anyi.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

/**
 * @author 安逸i
 * @version 1.0
 */
public interface DepartmentService {
    void save(Department department);

    Page<Department> selectPage(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(Department department);
}
