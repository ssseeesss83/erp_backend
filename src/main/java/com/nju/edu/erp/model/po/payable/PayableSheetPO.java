package com.nju.edu.erp.model.po.payable;

import com.nju.edu.erp.enums.sheetState.PayableSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayableSheetPO {
    /**
     * FKD-yyyyMMdd-xxxxx
     */
    private String id;
    /**
     * 客户id
     */
    private Integer customer;
    /**
     * 操作员
     */
    private String operator;

//    private String accountName;

    private PayableSheetState state;

    private BigDecimal totalAmount;

    private Date createTime;
}
