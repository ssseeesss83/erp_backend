package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.PayMethod;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalarySheetPO {
    /**
     * GZD-yyyyMMdd-xxxxx
     */
    private String id;
    /**
     * 操作者
     */
    private String operator;
    /**
     * 员工姓名
     */
    private String employeeName;
    /**
     * 员工账户
     */
    private String employeeAccount;
    /**
     * 应发工资
     */
    private BigDecimal shouldPay;
    /**
     * 税款
     */
    private BigDecimal tax;
    /**
     * 实发工资
     */
    private BigDecimal realPay;
    /**
     * 薪资发放方式
     */
    private PayMethod payMethod;
    /**
     * 工资账户
     */
    private String companyAccount;
    /**
     * state
     */
    private SalarySheetState state;
    /**
     * 创建时间
     */
    private Date createTime;

}
