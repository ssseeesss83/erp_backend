package com.nju.edu.erp.model.vo.clock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClockVO {
    /**
     * 员工姓名
     */
    private String EmployeeName;
    /**
     * 已打卡次数
     */
    private Integer clockTimes;
    /**
     * 缺勤次数
     */
    private Integer absenceTimes;
    /**
     * 上一次打卡时间
     */
    private Date lastClockTime;
    /**
     * 打卡月份
     */
    private String yearAndMonth;

}
