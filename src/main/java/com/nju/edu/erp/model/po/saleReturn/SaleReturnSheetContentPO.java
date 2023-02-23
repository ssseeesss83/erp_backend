package com.nju.edu.erp.model.po.saleReturn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 201250208
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleReturnSheetContentPO {
    /**
     * 自增id
     */
    private Integer id;
    /**
     * 销售退货单id
     */
    private String saleReturnsSheetId;
    /**
     * 商品id
     */
    private String pid;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 总金额
     */
    private BigDecimal totalPrice;
    /**
     * 备注
     */
    private String remark;
}
