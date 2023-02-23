package com.nju.edu.erp.model.po;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnualBonusPO {

    private String employeeName;
    /**
     * 发放基础月薪的数量
     */
    private Integer basisSalaryAmount;

    private Integer year;

}
