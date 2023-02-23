package com.nju.edu.erp.model.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalarySheetVO {
    /**
     * GZD-yyyyMMdd-xxxxx
     */
    @ExcelProperty(value = "工资单id")
    private String id;
    /**
     * 操作者
     */
    @ExcelProperty(value = "操作者")
    private String operator;
    /**
     * 员工姓名
     */
    @ExcelProperty(value = "员工姓名")
    private String employeeName;
    /**
     * 员工账户
     */
    @ExcelProperty(value = "员工账户")
    private String employeeAccount;
    /**
     * 应发工资
     */
    @ExcelProperty(value = "应发工资")
    private BigDecimal shouldPay;
    /**
     * 税款
     */
    @ExcelProperty(value = "税款")
    private BigDecimal tax;
    /**
     * 实发工资
     */
    @ExcelProperty(value = "实发工资")
    private BigDecimal realPay;
    /**
     * 工资账户
     */
    @ExcelProperty(value = "工资账户")
    private String companyAccount;
    /**
     * 状态
     */
    @ExcelIgnore
    private SalarySheetState state;
}
