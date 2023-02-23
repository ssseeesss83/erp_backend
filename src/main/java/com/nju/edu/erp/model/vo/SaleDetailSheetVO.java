package com.nju.edu.erp.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetailSheetVO {
    /**
     * 时间
     */
    @ExcelProperty(value = "时间")
    private String saleOrSaleReturnTime;
    /**
     * 商品名
     */
    @ExcelProperty(value = "商品名")
    private String name;
    /**
     * 商品型号
     */
    @ExcelProperty(value = "商品型号")
    private String productType;
    /**
     * 商品数量
     */
    @ExcelProperty(value = "商品数量")
    private Integer quantity;
    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    private BigDecimal unitPrice;
    /**
     * 总额
     */
    @ExcelProperty(value = "总额")
    private BigDecimal totalPrice;
    /**
     * 对应单据类型（销售单/销售退货单）
     * 直接传入单据编号，开头分别为XSD和XSTHD
     */
    @ExcelProperty(value = "单据编号")
    private String sheetId;
}
