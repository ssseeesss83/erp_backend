package com.nju.edu.erp.model.vo.clock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClockInVO {
    /**
     * 员工姓名,前端只需要传入员工姓名就ok了
     */
    private String EmployeeName;
    /**
     * 打卡时间
     */
    private Date clockTime;
}
