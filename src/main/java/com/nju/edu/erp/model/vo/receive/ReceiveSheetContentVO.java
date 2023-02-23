package com.nju.edu.erp.model.vo.receive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiveSheetContentVO {

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
