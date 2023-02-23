package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.UserDao;
import com.nju.edu.erp.enums.StationName;
import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.clock.ClockVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.nju.edu.erp.utils.Conversion.getYearAndMonth;

@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    ClockService clockService;
    @Autowired
    UserDao userDao;

    @Test
    public void employeeServiceServiceTest(){
        if(employeeService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getAll(){
        List<EmployeeVO> employeeVOS = employeeService.getAll();
        Assertions.assertEquals(6,employeeVOS.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getOneById(){
        EmployeeVO employeeVO = employeeService.getOneById(1);
        Assertions.assertEquals(0,employeeVO.getEmployeeName().compareTo("yuangong1"));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getOneByName(){
        EmployeeVO employeeVO = employeeService.getOneByName("yuangong1");
        Assertions.assertEquals(0,employeeVO.getEmployeeId().compareTo(1));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void createEmployee(){
        EmployeeVO employeeVO = EmployeeVO.builder()
                .employeeName("yuangong5")
                .sex("男")
                .birth(null)
                .phone("55555555")
                .employeeAccount("05555555")
                .stationName(StationName.SALE_MANAGER)
                .stationLevel(1)
                .build();
        employeeService.create(employeeVO);
        ClockVO clockVO = clockService.getEmployeeClockByNameAndTime("yuangong5", getYearAndMonth(new Date()));
        employeeVO = employeeService.getOneByName("yuangong5");
        Assertions.assertEquals(employeeVO.getEmployeeName(),userDao.findByUsername("yuangong5").getName());
        Assertions.assertEquals("123456",userDao.findByUsername("yuangong5").getPassword());
        Assertions.assertEquals(employeeVO.getStationName().toString(),userDao.findByUsername("yuangong5").getRole().toString());
        Assertions.assertEquals("yuangong5",employeeVO.getEmployeeName());
        Assertions.assertEquals(0,employeeVO.getPhone().compareTo("55555555"));
        Assertions.assertEquals("yuangong5", clockVO.getEmployeeName());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void updateEmployee(){
        EmployeeVO employeeVO = employeeService.getOneById(1);
        employeeVO.setPhone("01111111");
        employeeService.update(employeeVO);
        employeeVO = employeeService.getOneById(1);
        Assertions.assertEquals(0,employeeVO.getPhone().compareTo("01111111"));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void deleteEmployee(){
        employeeService.deleteById(5);
        Assertions.assertEquals(5,employeeService.getAll().size());
    }
}
