package com.anyi.yygh.repository;

import com.anyi.yygh.model.hosp.Department;
import com.anyi.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 安逸i
 * @version 1.0
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {
    Department getDepartmentByHoscodeAndDepcode(String hoscode,String depcode);
}
