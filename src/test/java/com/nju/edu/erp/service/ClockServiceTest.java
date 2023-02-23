package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.UserDao;
import com.nju.edu.erp.enums.StationName;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.clock.ClockInVO;
import com.nju.edu.erp.model.vo.clock.ClockVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.nju.edu.erp.utils.Conversion.getYearAndMonth;

@SpringBootTest
public class ClockServiceTest {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    ClockService clockService;
    @Autowired
    UserDao userDao;

    @Test
    public void clockServiceServiceTest(){
        if(clockService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void clockIn_nextMonth_clockSuccess(){
        String result = clockService.clockIn("yuangong1");
        System.out.println(result);
    }
    @Test
    @Transactional
    @Rollback(value = true)
    public void clockIn_thisMonth_clockHasBeenDone(){
        String result = clockService.clockIn("yuangong2");
        System.out.println(result);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getAllEmployeeClock(){
        List<ClockVO> clockVOS = clockService.getAllEmployeeClock();
        Assertions.assertEquals(4,clockVOS.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getAllEmployeeClockByName(){
        List<ClockVO> clockVOS = clockService.getAllEmployeeClockByName("yuangong1");
        Assertions.assertEquals(1,clockVOS.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getEmployeeClockByNameAndTime(){
        ClockVO clockVO = clockService.getEmployeeClockByNameAndTime("yuangong1","20226");
        Assertions.assertEquals(clockVO.getClockTimes(),4);
        Assertions.assertEquals(getYearAndMonth(clockVO.getLastClockTime()),"20226");
        Assertions.assertEquals(clockVO.getAbsenceTimes(),0);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void createNewEmployeeClock(){
        clockService.createNewEmployeeClock("ceshiyuangong");
        List<ClockVO> clockVOS = clockService.getAllEmployeeClockByName("ceshiyuangong");
        Assertions.assertEquals(1,clockVOS.size());
    }


}
