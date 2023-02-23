package com.nju.edu.erp.model.vo;

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
public class BusinessVO {

    /**
     * 理论收入: 销售额-退货额
     */
    @ExcelProperty(value = "理论收入")
    private BigDecimal rawIncome;
    /**
     * 折让: 折扣+代金券（包含代金券、赠送货物、货物打包价）
     */
    @ExcelProperty(value = "折让金额")
    private BigDecimal allowance;
    /**
     * 实际收入
     */
    @ExcelProperty(value = "实际收入")
    private BigDecimal finalIncome;
    /**
     * 商品总成本支出
     */
    @ExcelProperty(value = "商品总成本支出")
    private BigDecimal productTotalCost;
    /**
     * 人力成本
     */
    @ExcelProperty(value = "人力成本")
    private BigDecimal employeeTotalCost;
    /**
     * 总支出: 商品成本+人力成本
     */
    @ExcelProperty(value = "总支出")
    private BigDecimal cost;
    /**
     * 利润
     */
    @ExcelProperty(value = "利润")
    private BigDecimal profits;

}
