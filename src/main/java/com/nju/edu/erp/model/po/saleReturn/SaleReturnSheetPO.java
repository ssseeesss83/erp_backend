package com.nju.edu.erp.model.po.saleReturn;

import com.nju.edu.erp.enums.sheetState.PurchaseReturnsSheetState;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
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
public class SaleReturnSheetPO {
    /**
     * XS退货单单据编号（格式为：XSTHD-yyyyMMdd-xxxxx
     */
    private String id;
    /**
     * 关联的进货单id
     */
    private String saleSheetId;
    /**
     * 客户、经销商id
     */
    private Integer supplier;
    /**
     * 业务员
     */
    private String salesman;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 备注
     */
    private String remark;
    /**
     * 退货的总额合计
     */
    private BigDecimal totalAmount;
    /**
     * 最终退货总额
     */
    private BigDecimal finalAmount;
    /**
     * 折让
     */
    private BigDecimal discount;
    /**
     * 使用代金券总额
     */
    private BigDecimal voucherAmount;
    /**
     * 单据状态
     */
    private SaleReturnSheetState state;
    /**
     * 创建时间
     */
    private Date createTime;
}
