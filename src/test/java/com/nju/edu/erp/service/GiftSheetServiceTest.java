package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.GiftSheetState;
import com.nju.edu.erp.model.po.gift.GiftSheetPO;
import com.nju.edu.erp.model.vo.gift.GiftSheetContentVO;
import com.nju.edu.erp.model.vo.gift.GiftSheetVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class GiftSheetServiceTest {

    @Autowired
    GiftSheetService giftSheetService;

    @Test
    public void GiftSheetServiceServiceTest(){
        if(giftSheetService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getAllByState(){
        List<GiftSheetVO> giftSheetVOS = giftSheetService.getGiftSheetByState(GiftSheetState.PENDING);
        Assertions.assertEquals(2,giftSheetVOS.size());
    }



    @Test
    @Transactional
    @Rollback(value = true)
    public void approval(){
        List<GiftSheetContentVO> giftSheetContentVOS = new ArrayList<>();
        GiftSheetContentVO giftSheetContentVO = GiftSheetContentVO.builder()
                .giftSheetId("onlyForTest")
                .pid("4000001")
                .quantity(1)
                .build();
        giftSheetContentVOS.add(giftSheetContentVO);
        GiftSheetVO giftSheetVO = GiftSheetVO.builder()
                .id("onlyForTest")
                .saleSheetId("oneSaleSheet")
                .giftSheetContentVOS(giftSheetContentVOS)
                .build();
        giftSheetService.makeGiftSheet(giftSheetVO);
        List<GiftSheetVO> giftSheetVOS = giftSheetService.getGiftSheetByState(GiftSheetState.SUCCESS);
        int formerSize = giftSheetVOS.size();
        giftSheetService.approval("oneSaleSheet", GiftSheetState.SUCCESS);
        giftSheetVOS = giftSheetService.getGiftSheetByState(GiftSheetState.SUCCESS);
        int size = giftSheetVOS.size();
        Assertions.assertEquals(formerSize+1, size);

    }



}
