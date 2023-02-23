package com.nju.edu.erp.model.vo.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessProcessFilterVO {
    /**
     * 起始时间，为空则不限制
     */
    private Date begin;
    /**
     * 截至时间，为空则不限制
     */
    private Date end;
    /**
     * 客户id
     */
    private Integer customerId;
    /**
     * 业务员
     */
    private String operator;
}
