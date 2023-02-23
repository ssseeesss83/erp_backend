package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.EmployeePO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface EmployeeDao {

    List<EmployeePO> getAll();

    EmployeePO getOneById(Integer employeeId);

    EmployeePO getLatest();

    int create(EmployeePO employeePO);

    int deleteById(Integer employId);

    int update(EmployeePO employeePO);

    EmployeePO getOneByName(String employeeName);
}
