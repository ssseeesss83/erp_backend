package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.VoucherType;
import com.nju.edu.erp.model.po.voucher.VoucherLimitPO;
import com.nju.edu.erp.model.po.voucher.VoucherPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import javax.sound.sampled.Line;
import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface VoucherDao {

    int create(VoucherPO voucherPO);

    int createVoucherLimit(List<VoucherLimitPO> voucherLimitPOS);

    List<VoucherPO> getAll();
    /**
     * 有效时间内
     */
    List<VoucherPO> getAllByType(VoucherType voucherType, Date date);
    /**
     * 有效时间内
     */
    List<VoucherPO> getAllByTypeAndCustomerLevel(VoucherType voucherType, Integer customerLevel, Date date);

    List<VoucherLimitPO> getLimitByVoucherId(String voucherId);

    VoucherPO getLatest();

    int delete(String voucherId);

    VoucherPO getOneById(String voucherId);
}
