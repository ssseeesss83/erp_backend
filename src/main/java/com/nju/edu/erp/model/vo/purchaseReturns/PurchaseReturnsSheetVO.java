package com.nju.edu.erp.model.vo.purchaseReturns;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.nju.edu.erp.enums.sheetState.PurchaseReturnsSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseReturnsSheetVO {
    /**
     * 进货退货单单据编号（格式为：JHTHD-yyyyMMdd-xxxxx
     */
    @ExcelProperty(value = "进货退货单id")
    private String id;
    /**
     * 关联的进货单id
     */
    @ExcelProperty(value = "关联的进货单id")
    private String purchaseSheetId;
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
     * 退货的总额合计
     */
    @ExcelProperty(value = "退货的总额合计")
    private BigDecimal totalAmount;
    /**
     * 单据状态
     */
    @ExcelIgnore
    private PurchaseReturnsSheetState state;
    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 进货单具体内容
     */
    @ExcelIgnore
    List<PurchaseReturnsSheetContentVO> purchaseReturnsSheetContent;
}