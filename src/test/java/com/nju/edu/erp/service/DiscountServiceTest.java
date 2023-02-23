package com.nju.edu.erp.service;


import com.nju.edu.erp.enums.VoucherType;
import com.nju.edu.erp.model.vo.DiscountVO;
import com.nju.edu.erp.model.vo.voucher.VoucherVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class DiscountServiceTest {

    @Autowired
    DiscountService discountService;

    @Test
    public void discountServiceServiceTest(){
        if(discountService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getAllByLevel(){
        List<DiscountVO> discountVOS = discountService.getAllByLevel(Integer.valueOf(2));
        int size = discountVOS.size();
        Assertions.assertEquals(1,size);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getAll(){
        List<DiscountVO> discountVOS = discountService.getAll();
        int size = discountVOS.size();
        Assertions.assertEquals(2,size);
    }



    @Test
    @Transactional
    @Rollback(value = true)
    public void createAndDelete(){
        List<DiscountVO> discountVOS = discountService.getAll();
        int size = discountVOS.size();
        DiscountVO discountVO = DiscountVO.builder()
                .build();
        discountService.create(discountVO);
        discountVOS = discountService.getAll();
        Assertions.assertEquals(size+1,discountVOS.size());
        discountService.delete("ZKD-20220707-00001");
        discountVOS = discountService.getAll();
        Assertions.assertEquals(size,discountVOS.size());

    }



}
