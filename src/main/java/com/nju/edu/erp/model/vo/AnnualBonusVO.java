package com.nju.edu.erp.model.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnualBonusVO {

    private String employeeName;
    /**
     * 发放基础月薪的数量
     */
    private Integer basisSalaryAmount;

    private Integer year;

}
