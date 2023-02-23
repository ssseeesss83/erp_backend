package com.nju.edu.erp.model.vo.payable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayableSheetContentVO {

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
