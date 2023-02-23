package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.ClockDao;
import com.nju.edu.erp.dao.EmployeeDao;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.ClockPO;
import com.nju.edu.erp.model.po.EmployeePO;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.clock.ClockInVO;
import com.nju.edu.erp.model.vo.clock.ClockVO;
import com.nju.edu.erp.service.ClockService;
import com.nju.edu.erp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.nju.edu.erp.utils.Conversion.*;

@Service
public class ClockServiceImpl implements ClockService {

    private final ClockDao clockDao;

    private final EmployeeDao employeeDao;

    @Autowired
    public ClockServiceImpl(ClockDao clockDao, EmployeeDao employeeDao) {
        this.clockDao = clockDao;
        this.employeeDao = employeeDao;
    }

    @Override
    public String clockIn(String employeeName) {
        ClockInVO clockInVO = ClockInVO.builder()
                .EmployeeName(employeeName)
                .clockTime(new Date())
                .build();
        ClockPO clockPO = clockDao.getEmployeeClockByNameAndTime(clockInVO.getEmployeeName(),getYearAndMonth(clockInVO.getClockTime()));
        //判断是否进入下一月
        if (clockPO==null&clockDao.getAllEmployeeClockByName(employeeName).size()>0){
            createAllClockSheet();
            clockPO = clockDao.getEmployeeClockByNameAndTime(clockInVO.getEmployeeName(),getYearAndMonth(clockInVO.getClockTime()));
        }
        if (clockPO==null) throw new MyServiceException("A0003","打卡对象不存在");
        //判读今天是否已打卡, 该条件判断需测试
        if (clockInVO.getClockTime().after(clockPO.getLastClockTime())&
                getMonthDay(clockInVO.getClockTime())>getMonthDay(clockPO.getLastClockTime())){
            if (clockDao.clockIn(clockInVO)==0) throw new MyServiceException("A0003","打卡对象不存在");
            return "打卡成功";
        }else {
            return "今日已打卡";
        }
    }

    @Override
    public List<ClockVO> getAllEmployeeClock() {
        List<ClockPO> clockPOS = clockDao.getAllEmployeeClock();
        List<ClockVO> clockVOS = new ArrayList<>();
        for (ClockPO clockPO:clockPOS){
            clockVOS.add(conversion(clockPO));
        }
        return clockVOS;
    }

    @Override
    public List<ClockVO> getAllEmployeeClockByName(String employeeName) {
        List<ClockPO> clockPOS = clockDao.getAllEmployeeClockByName(employeeName);
        List<ClockVO> clockVOS = new ArrayList<>();
        for (ClockPO clockPO:clockPOS){
            clockVOS.add(conversion(clockPO));
        }
        return clockVOS;
    }

    @Override
    public ClockVO getEmployeeClockByNameAndTime(String name, String yearAndMonth) {
        //yearAndMonth格式为20227
        return conversion(clockDao.getEmployeeClockByNameAndTime(name,yearAndMonth));
    }

    @Override
    public void createAllClockSheet() {
        List<EmployeePO> employeePOS = employeeDao.getAll();
        String yearAndMonth = getYearAndMonth(new Date());
        for (EmployeePO employeePO:employeePOS){
            // 无当月打卡表则创建
            if (clockDao.getEmployeeClockByNameAndTime(employeePO.getEmployeeName(),yearAndMonth)==null){
                clockDao.createEmployeeClock(employeePO.getEmployeeName(),yearAndMonth);
            }
        }
    }

    @Override
    public void createNewEmployeeClock(String employeeName) {
        String yearAndMonth = getYearAndMonth(new Date());
        if (clockDao.createEmployeeClock(employeeName,yearAndMonth)==0)throw new MyServiceException("A0002","创建打卡记录失败");
    }
}
