package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.UserDao;
import com.nju.edu.erp.enums.sheetState.PayableSheetState;
import com.nju.edu.erp.enums.sheetState.TransferSheetState;
import com.nju.edu.erp.model.vo.TransferVO;
import com.nju.edu.erp.model.vo.clock.ClockVO;
import com.nju.edu.erp.model.vo.payable.PayableSheetVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.nju.edu.erp.utils.Conversion.getYearAndMonth;

@SpringBootTest
public class TransferServiceTest {

    @Autowired
    TransferService transferService;

    @Test
    public void transferServiceServiceTest(){
        if(transferService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getAllByState(){
        List<TransferVO> transferVOS = transferService.getAllByState(TransferSheetState.PENDING);
        Assertions.assertEquals(2,transferVOS.size());
    }
    @Test
    @Transactional
    @Rollback(value = true)
    public void getAll(){
        List<TransferVO> transferVOS = transferService.getAll();
        Assertions.assertEquals(3,transferVOS.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void updateStateToSuccess(){
        //也测了create和getLatest
        TransferVO transferVO = TransferVO.builder()
                .sourceAccount("source4")
                .targetAccount("target4")
                .amount(BigDecimal.valueOf(4000.00))
                .state(TransferSheetState.PENDING)
                .build();

        transferService.create(transferVO);
        transferService.updateStateToSuccess(transferService.getLatest().getId());
        TransferVO latest = transferService.getLatest();
        Assertions.assertEquals(TransferSheetState.SUCCESS,latest.getState());
    }



    @Test
    @Transactional
    @Rollback(value = true)
    public void deleteById(){
        List<TransferVO> transferVOS = transferService.getAll();
        int size = transferVOS.size();
        transferService.deleteById("CWZZD-20220706-00001");
        transferVOS = transferService.getAll();
        Assertions.assertEquals(size-1,transferVOS.size());

    }



}
