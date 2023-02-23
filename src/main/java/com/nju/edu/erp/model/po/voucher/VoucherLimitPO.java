package com.nju.edu.erp.model.po.voucher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherLimitPO {
    /**
     * 自增id
     */
    private Integer id;

    private String voucherId;

    private String pid;

    private Integer quantity;

}
