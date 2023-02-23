package com.nju.edu.erp.model.vo;

import com.nju.edu.erp.enums.PayMethod;
import com.nju.edu.erp.enums.StationName;
import com.nju.edu.erp.enums.salaryCalculation.SalaryCalculationEnum;
import com.nju.edu.erp.enums.sheetState.SalarySystemSheetState;
import com.nju.edu.erp.enums.taxCalculation.TaxCalculationEnum;
import com.nju.edu.erp.enums.taxType.TaxTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalarySystemSheetVO {

    /**
     * 岗位名称
     */
    private StationName stationName;
    /**
     * 岗位级别
     */
    private Integer stationLevel;
    /**
     * 基本工资
     */
    private BigDecimal basisSalary;
    /**
     * 岗位工资
     */
    private BigDecimal stationSalary;
    /**
     * 薪资计算方式
     */
    private SalaryCalculationEnum salaryCalculationType;
    /**
     * 薪资发放方式
     */
    private PayMethod payMethod;
    /**
     * 纳税种类
     */
    private TaxTypeEnum taxType;
//    private TaxCalculationEnum taxType;
    /**
     * 单据状态
     */
    private SalarySystemSheetState state;
}
