package com.nju.edu.erp.model.vo.voucher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherLimitVO {

    private String voucherId;

    private String pid;

    private Integer quantity;

}