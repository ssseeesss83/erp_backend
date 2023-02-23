package com.nju.edu.erp.model.vo;


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
public class DiscountVO {

    private Integer customerLevel;

    private BigDecimal discount;

    private Date begin;

    private Date end;

}
