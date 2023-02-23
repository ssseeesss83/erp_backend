package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.VoucherType;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.voucher.VoucherVO;

import java.util.List;

public interface VoucherService {

    List<VoucherVO> getAllByType(VoucherType type);
    /**
     * 获取可用代金券(基于商品、用户id、日期、)
     */
    List<VoucherVO> getAvailableVoucher(SaleSheetVO saleSheetVO, Integer customerId, VoucherType type);

    void create(VoucherVO voucherVO);

    void delete(String voucherId);

    VoucherVO getLatest();

    VoucherVO getOneById(String voucherId);
}
