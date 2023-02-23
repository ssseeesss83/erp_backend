package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.EmployeeDao;
import com.nju.edu.erp.model.vo.EmployeeVO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeVO> getAll();

    EmployeeVO getOneById(Integer employeeId);

    EmployeeVO getOneByName(String employeeName);

    /**
     * 创建新员工的同时，创建新用户的登陆账户和打卡记录
     */
    EmployeeVO create(EmployeeVO employeeVO);

    void deleteById(Integer employeeId);

    EmployeeVO update(EmployeeVO employeeVO);
}
