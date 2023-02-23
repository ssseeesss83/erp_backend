package com.nju.edu.erp.model.po.receive;

import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
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
public class ReceiveSheetPO {
    /**
     * SKD-yyyyMMdd-xxxxx
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

    private ReceiveSheetState state;

    private BigDecimal totalAmount;

    private Date createTime;
}
