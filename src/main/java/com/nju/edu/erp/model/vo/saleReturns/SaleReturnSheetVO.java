package com.nju.edu.erp.model.vo.saleReturns;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.nju.edu.erp.enums.sheetState.PurchaseReturnsSheetState;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 201250208
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleReturnSheetVO {

    /**
     * 进货退货单单据编号（格式为：XSTHD-yyyyMMdd-xxxxx
     * 必要
     */
    @ExcelProperty(value = "销售退货单id")
    private String id;
    /**
     * 关联的销售单id
     * 必要
     */
    @ExcelProperty(value = "关联销售单id")
    private String saleSheetId;
    /**
     * 客户、经销商id
     * 必要
     */
    @ExcelProperty(value = "客户id")
    private Integer supplier;
    /**
     * 业务员
     * 必要
     */
    @ExcelProperty(value = "业务员")
    private String salesman;
    /**
     * 操作员
     */
    @ExcelProperty(value = "操作员")
    private String operator;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
    /**
     * 折让前总额
     */
    @ExcelProperty(value = "折让前退款总额")
    private BigDecimal totalAmount;
    /**
     * 折让后总额, 新建单据时前端传null
     */
    @ExcelProperty(value = "折让后退款总额")
    private BigDecimal finalAmount;
    /**
     * 折扣(null)
     */
    @ExcelProperty(value = "折扣")
    private BigDecimal discount;
    /**
     * 使用代金券总额(null)
     */
    @ExcelProperty(value = "使用代金券总额")
    private BigDecimal voucherAmount;
    /**
     * 单据状态(null)
     */
    @ExcelIgnore
    private SaleReturnSheetState state;
    /**
     * 退货单具体内容
     */
    @ExcelIgnore
    List<SaleReturnSheetContentVO> saleReturnSheetContent;
}
