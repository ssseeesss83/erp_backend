package com.nju.edu.erp.model.vo.payable;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.nju.edu.erp.config.converter.PayableSheetStateConverter;
import com.nju.edu.erp.enums.sheetState.PayableSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayableSheetVO {

    /**
     * FKD-yyyyMMdd-xxxxx
     */
    @ExcelProperty(value = "付款单id")
    private String id;

    /**
     * 客户id
     */
    @ExcelProperty(value = "客户id")
    private Integer customer;
    /**
     * 操作者
     */
    @ExcelProperty(value = "操作者")
    private String operator;

    @ExcelIgnore
    private PayableSheetState state;

//    private String accountName;
    @ExcelProperty(value = "总金额")
    private BigDecimal totalAmount;

    @ExcelIgnore
    List<PayableSheetContentVO> payableSheetContentVOS;
}
