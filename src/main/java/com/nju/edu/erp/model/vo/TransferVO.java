package com.nju.edu.erp.model.vo;

import com.nju.edu.erp.enums.sheetState.TransferSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferVO {
    /**
     * id CWZZD 财务转账单,用于提醒财务线下转账
     */
    private String id;
    /**
     * 大概率都是公司账户转出，用于提醒财务去线下操作
     */
    private String sourceAccount;

    private String targetAccount;

    private BigDecimal amount;

    private TransferSheetState state;

}
