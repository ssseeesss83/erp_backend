package com.nju.edu.erp.model.vo.voucher;

import com.nju.edu.erp.enums.VoucherType;
import com.nju.edu.erp.model.po.voucher.VoucherLimitPO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherVO {

    private String voucherId;

    private String voucherName;

    private VoucherType voucherType;

    private BigDecimal voucherAmount;
    /**
     * 该优惠使用客户最低等级
     */
    private Integer customerLevel;

    private Date begin;

    private Date end;
    /**
     * 要使用该代金券dou
     */
    private List<VoucherLimitVO> voucherLimitVOS;
}