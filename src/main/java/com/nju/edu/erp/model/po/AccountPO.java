package com.nju.edu.erp.model.po;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountPO {

    /**
     * 自增id
     */
    @ExcelProperty("自增id")
    private Integer id;
    @ExcelProperty("账户名")
    private String name;
    @ExcelProperty("账户余额")
    private BigDecimal balance;
}
