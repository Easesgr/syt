package com.anyi.yygh.service.impl;

import com.anyi.yygh.model.hosp.Department;
import com.anyi.yygh.repository.DepartmentRepository;
import com.anyi.yygh.service.DepartmentService;
import com.anyi.yygh.vo.hosp.DepartmentQueryVo;
import com.anyi.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 安逸i
 * @version 1.0
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Department department) {
        // 查询是否存在
        Department departmentExist = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if (departmentExist !=null){
            department.setCreateTime(departmentExist.getCreateTime());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }else {
            department.setIsDeleted(0);
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            departmentRepository.save(department);
        }

    }

    /**
     * 分页查询数据
     * @param page
     * @param limit
     * @param departmentQueryVo
     * @return
     */
    @Override
    public Page<Department> selectPage(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //0为第一页
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        Department department = new Department();
        // 复制
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);

        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

        //创建实例
        Example<Department> example = Example.of(department, matcher);
        Page<Department> pages = departmentRepository.findAll(example, pageable);
        return pages;
    }

    // 删除
    @Override
    public void remove(Department department) {
        Department one = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        departmentRepository.deleteById(one.getId());
    }

    /**
     * 根据医院编号获取科室信息
     * @param hoscode
     * @return
     */
    @Override
    public List<DepartmentVo> getList(String hoscode) {
        //创建list集合，用于最终数据封装
        List<DepartmentVo> result = new ArrayList<>();

        // 创建一个科室填入查找条件
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        // 将科室条件放入到example
        Example example = Example.of(departmentQuery);
        //所有科室列表 departmentList 采用example方式查找所有的科室信息
        List<Department> departmentList = departmentRepository.findAll(example);

        Map<String, List<Department>> deparmentMap =
                // stream过滤条件，将list结合对象收集成以大科室编号为键，所有小科室为list集合
                departmentList.stream()
                        .collect(Collectors.groupingBy(Department::getBigcode));

        //遍历map集合 遍历大科室信息
        for(Map.Entry<String,List<Department>> entry : deparmentMap.entrySet()) {
            //大科室编号
            String bigcode = entry.getKey();
            //大科室编号对应的全局数据
            List<Department> deparment1List = entry.getValue();
            //封装大科室
            DepartmentVo bigDepartmentVo = new DepartmentVo();
            bigDepartmentVo.setDepcode(bigcode);
            bigDepartmentVo.setDepname(deparment1List.get(0).getBigname());

            //封装小科室
            List<DepartmentVo> children = new ArrayList<>();
            for(Department department: deparment1List) {
                DepartmentVo smallDepartment=  new DepartmentVo();
                smallDepartment.setDepcode(department.getDepcode());
                smallDepartment.setDepname(department.getDepname());
                //封装到list集合
                children.add(smallDepartment);
            }
            //把小科室list集合放到大科室children里面
            bigDepartmentVo.setChildren(children);
            //放到最终result里面
            result.add(bigDepartmentVo);
        }
        //返回
        return result;
    }

    /**
     * 根据医院编号和科室号，获取科室名称
     * @param hoscode
     * @param depcode
     * @return
     */
    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            return department.getDepname();
        }
        return null;
    }


}
