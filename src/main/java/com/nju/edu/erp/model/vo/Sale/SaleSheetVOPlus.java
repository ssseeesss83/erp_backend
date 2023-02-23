package com.nju.edu.erp.model.vo.Sale;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetContentVO;
import com.nju.edu.erp.model.vo.voucher.VoucherLimitVO;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleSheetVOPlus extends SaleSheetVO {

    /**
     * 进货单单据编号（格式为：XSD-yyyyMMdd-xxxxx), 新建单据时前端传null
     */
    @ExcelProperty(value = "销售单id")
    private String id;
    /**
     * 供应商id
     */
    @ExcelProperty(value = "客户id")
    private Integer supplier;
    /**
     * 业务员
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
    @ExcelProperty(value = "折让前总额")
    private BigDecimal rawTotalAmount;
    /**
     * 单据状态, 新建单据时前端传null
     */
    @ExcelIgnore
    private SaleSheetState state;
    /**
     * 折让后总额, 新建单据时前端传null
     */
    @ExcelProperty(value = "折让后总额")
    private BigDecimal finalAmount;
    /**
     * 折扣
     */
    @ExcelProperty(value = "折扣")
    private BigDecimal discount;
    /**
     * 使用代金券总额
     */
    @ExcelProperty(value = "使用代金券总额")
    private BigDecimal voucherAmount;
    /**
     * 进货单具体内容
     */
    @ExcelIgnore
    List<SaleSheetContentVO> saleSheetContent;

    @ExcelIgnore
    private List<VoucherLimitVO> voucherLimitVOS;
}
