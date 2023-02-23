package com.nju.edu.erp.model.po.payable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayableSheetContentPO {

    /**
     * 自增id
     */
    private Integer id;
    /**
     * 收款单id
     */
    private String payableSheetId;
    /**
     * 银行账户
     */
    private String name;

    /**
     * 转账金额
     */
    private BigDecimal transferAmount;

    private String remark;
}
