package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.StationName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeePO {
    /**
     * id,自增,前端不指定，由后端决定
     */
    private Integer employeeId;
    /**
     * 员工姓名
     */
    private String employeeName;
    /**
     * sex,女,男
     */
    private String sex;
    /**
     * 出生日期
     */
    private Date birth;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 工资卡账户
     */
    private String employeeAccount;
    /**
     * 岗位
     */
    private StationName stationName;
    /**
     * 岗位级别
     */
    private Integer stationLevel;

}
