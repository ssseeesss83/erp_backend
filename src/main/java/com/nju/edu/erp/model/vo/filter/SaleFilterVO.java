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
public class SaleFilterVO {
    /**
     * 起始时间，为空则不限制
     */
    private Date begin = null;
    /**
     * 截至时间，为空则不限制
     */
    private Date end = null;
    /**
     * 商品名，为空则不限制
     */
    private String name;
    /**
     * 客户id，为空则不限制
     */
    private Integer supplier;
    /**
     * 操作员，为空则不限制
     */
    private String operator;
}
