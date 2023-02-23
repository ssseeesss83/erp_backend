package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.EmployeeDao;
import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.SalarySystemSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.ClockService;
import com.nju.edu.erp.service.EmployeeService;
import com.nju.edu.erp.service.SalarySystemService;
import com.nju.edu.erp.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.nju.edu.erp.utils.Conversion.conversion;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDao employeeDao;

    private final SalarySystemService salarySystemService;

    private final UserService userService;

    private final ClockService clockService;

    @Autowired
    public EmployeeServiceImpl(EmployeeDao employeeDao, SalarySystemService salarySystemService, UserService userService, ClockService clockService) {
        this.employeeDao = employeeDao;
        this.salarySystemService = salarySystemService;
        this.userService = userService;
        this.clockService = clockService;
    }

    @Override
    public List<EmployeeVO> getAll() {
        List<EmployeeVO> employeeVOS = new ArrayList<>();
        for (EmployeePO employeePO: employeeDao.getAll()){
            SalarySystemSheetVO salarySystemSheetVO = salarySystemService.getOneByNameAndLevel(employeePO.getStationName(),employeePO.getStationLevel());
            employeeVOS.add(conversion(employeePO,salarySystemSheetVO));
        }
        return employeeVOS;
    }

    @Override
    public EmployeeVO getOneById(Integer employeeId) {
        EmployeePO employeePO = employeeDao.getOneById(employeeId);
        SalarySystemSheetVO salarySystemSheetVO = salarySystemService.getOneByNameAndLevel(employeePO.getStationName(),employeePO.getStationLevel());
        return conversion(employeePO,salarySystemSheetVO);
    }

    @Override
    public EmployeeVO getOneByName(String employeeName) {
        EmployeePO employeePO = employeeDao.getOneByName(employeeName);
        SalarySystemSheetVO salarySystemSheetVO = salarySystemService.getOneByNameAndLevel(employeePO.getStationName(),employeePO.getStationLevel());
        return conversion(employeePO,salarySystemSheetVO);    }

    @Override
    public EmployeeVO create(EmployeeVO employeeVO) {
        EmployeePO employeePO = new EmployeePO();
        BeanUtils.copyProperties(employeeVO,employeePO);
        employeeDao.create(employeePO);
        //自动获取系统登陆初始化账户
        UserVO userVO = UserVO.builder()
                .role(conversion(employeePO.getStationName()))
                .name(employeePO.getEmployeeName())
                .password("123456")
                .build();
        userService.register(userVO);
        //自动创建打卡表
        clockService.createNewEmployeeClock(employeePO.getEmployeeName());
        SalarySystemSheetVO salarySystemSheetVO = salarySystemService.getOneByNameAndLevel(employeePO.getStationName(),employeePO.getStationLevel());
        return conversion(employeeDao.getLatest(),salarySystemSheetVO);
    }

    @Override
    public void deleteById(Integer employeeId) {
        employeeDao.deleteById(employeeId);
    }

    @Override
    public EmployeeVO update(EmployeeVO employeeVO) {
        EmployeePO employeePO = new EmployeePO();
        BeanUtils.copyProperties(employeeVO,employeePO);
        employeeDao.update(employeePO);
        SalarySystemSheetVO salarySystemSheetVO = salarySystemService.getOneByNameAndLevel(employeePO.getStationName(),employeePO.getStationLevel());
        return conversion(employeeDao.getLatest(),salarySystemSheetVO);
    }
}
