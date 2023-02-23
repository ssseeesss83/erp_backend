package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.ClockPO;
import com.nju.edu.erp.model.vo.clock.ClockInVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ClockDao {
    /**
     *在对应月份进行打卡,在sql中进行缺勤次数计算
     */
    int clockIn(ClockInVO clock);

    List<ClockPO> getAllEmployeeClock();

    List<ClockPO> getAllEmployeeClockByName(@Param("name")String name);
    /**
     * 获取某个月打卡记录
     */
    ClockPO getEmployeeClockByNameAndTime(@Param("name")String name,@Param("yearAndMonth") String yearAndMonth);
    /**
     * 新建对应员工当月的打卡表,在每个月月初进行,需要判断是否已经存在
     */
    int createEmployeeClock(@Param("name")String name,@Param("yearAndMonth")String yearAndMonth);
}
