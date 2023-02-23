package com.nju.edu.erp.service;


import com.nju.edu.erp.enums.VoucherType;
import com.nju.edu.erp.model.po.voucher.VoucherPO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.voucher.VoucherLimitVO;
import com.nju.edu.erp.model.vo.voucher.VoucherVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class VoucherServiceTest {

    @Autowired
    VoucherService voucherService;
    @Autowired
    SaleService saleService;

    @Test
    public void voucherServiceServiceTest(){
        if(voucherService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getAllByType(){
        List<VoucherVO> voucherVOS = voucherService.getAllByType(VoucherType.CASH);
        Assertions.assertEquals(2,voucherVOS.size());
        voucherVOS = voucherService.getAllByType(VoucherType.COMBINATION);
        Assertions.assertEquals(1,voucherVOS.size());
        voucherVOS = voucherService.getAllByType(VoucherType.GIFT);
        Assertions.assertEquals(1,voucherVOS.size());
    }



    @Test
    @Transactional
    @Rollback(value = true)
    public void createAndDelete(){
        List<VoucherVO> voucherVOS = voucherService.getAllByType(VoucherType.CASH);
        int size = voucherVOS.size();
        VoucherVO voucherVO = VoucherVO.builder()
                .voucherName("OnlyForTest")
                .voucherType(VoucherType.CASH)
                .build();
        List<VoucherLimitVO> voucherLimitVOS = new ArrayList<>();
        voucherLimitVOS.add(VoucherLimitVO.builder().voucherId(voucherVO.getVoucherId()).pid("0000").quantity(10).build());
        voucherVO.setVoucherLimitVOS(voucherLimitVOS);
        voucherService.create(voucherVO);
        voucherVOS = voucherService.getAllByType(VoucherType.CASH);
        Assertions.assertEquals(size+1,voucherVOS.size());
        VoucherVO latest = voucherService.getLatest();
        voucherService.delete(latest.getVoucherId());
        voucherVOS = voucherService.getAllByType(VoucherType.CASH);
        Assertions.assertEquals(size,voucherVOS.size());

    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getAvailableVoucher(){
        Integer customerId = 1;
        SaleSheetVO saleSheetVO = saleService.getSaleSheetById("XSD-20220524-00001");
        List<VoucherVO> voucherVOS = voucherService.getAvailableVoucher(saleSheetVO, customerId, VoucherType.CASH);
        int size = voucherVOS.size();
        Assertions.assertEquals(1, size);

    }



}
