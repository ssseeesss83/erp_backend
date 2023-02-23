package com.nju.edu.erp.service;


import com.nju.edu.erp.model.vo.clock.ClockVO;

import java.util.List;

public interface ClockService {

    String clockIn(String employeeName);

    List<ClockVO> getAllEmployeeClock();

    List<ClockVO> getAllEmployeeClockByName(String employeeName);
    /**
     * 获取某个月打卡记录
     */
    ClockVO getEmployeeClockByNameAndTime(String name,String yearAndMonth);
    /**
     * 创建所有员工，新一月的打卡记录
     */
    void createAllClockSheet();
    /**
     * 创建新员工的打卡表
     */
    void createNewEmployeeClock(String employeeName);

}
