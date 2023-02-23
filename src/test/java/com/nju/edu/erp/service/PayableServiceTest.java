package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.PayableSheetState;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.vo.AccountVO;
import com.nju.edu.erp.model.vo.SaleDetailSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.filter.SaleFilterVO;
import com.nju.edu.erp.model.vo.payable.PayableSheetContentVO;
import com.nju.edu.erp.model.vo.payable.PayableSheetVO;
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
public class PayableServiceTest {

    @Autowired
    PayableService payableService;

    @Autowired
    AccountService accountService;

    @Autowired
    CustomerService customerService;

    @Test
    public void payableServiceTest(){
        if(payableService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void makePayableSheet(){
        UserVO userVO = UserVO.builder()
                .name("testPayableSheet")
                .role(Role.FINANCIAL_STAFF)
                .build();
        List<PayableSheetContentVO> payableSheetContentVOS = new ArrayList<>();
        PayableSheetVO payableSheetVO = PayableSheetVO.builder()
                .customer(1)
                .totalAmount(BigDecimal.valueOf(200000.00))
                .build();
        payableSheetContentVOS.add(PayableSheetContentVO.builder()
                .name("测试账户1")
                .transferAmount(BigDecimal.valueOf(175000.00))
                .remark("转账（付款）1")
                .build());
        payableSheetContentVOS.add(PayableSheetContentVO.builder()
                .name("测试账户2")
                .transferAmount(BigDecimal.valueOf(25000.00))
                .remark("转账（付款）2")
                .build());
        payableSheetVO.setPayableSheetContentVOS(payableSheetContentVOS);
        payableService.makePayableSheet(userVO,payableSheetVO);
        PayableSheetVO latest = payableService.getLatest();
        Assertions.assertEquals(1,latest.getCustomer());
        Assertions.assertEquals(0,latest.getTotalAmount().compareTo(BigDecimal.valueOf(200000.00)));

        List<PayableSheetContentVO> payableSheetContentVOS_1 = latest.getPayableSheetContentVOS();
        for (PayableSheetContentVO contentVO: payableSheetContentVOS_1){
            if (contentVO.getName().equals("测试账户1")){
                Assertions.assertEquals(0,contentVO.getTransferAmount().compareTo(BigDecimal.valueOf(175000.00)));
                Assertions.assertEquals("转账1",contentVO.getRemark());
            }else {
                Assertions.assertEquals(0,contentVO.getTransferAmount().compareTo(BigDecimal.valueOf(25000.00)));
                Assertions.assertEquals("转账2",contentVO.getRemark());
            }
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getPayableSheetByState(){
        List<PayableSheetVO> payableSheetVOS = payableService.getPayableSheetByState(PayableSheetState.PENDING);
        Assertions.assertEquals(2,payableSheetVOS.size());
    }

//    @Test
//    @Transactional
//    @Rollback(value = true)
//    public void approval(){
//        PayableSheetVO payableSheetVO = payableService.getOneById("FKD-20220702-00001");//FKD-20220703-00001
//        payableService.approval(payableSheetVO.getId(),PayableSheetState.SUCCESS);
//        payableSheetVO = payableService.getLatest();
//        //测试状态是否改变
//        List<PayableSheetVO> payableSheetVOS = payableService.getPayableSheetByState(PayableSheetState.SUCCESS);
//        Assertions.assertEquals(3,payableSheetVOS.size());
//        //测试对应账户余额是否减少
//        AccountVO accountVO1 = accountService.findByName("测试账户1");
//        AccountVO accountVO2 = accountService.findByName("测试账户2");
//        Assertions.assertEquals(0,accountVO1.getBalance().compareTo(BigDecimal.valueOf(990000.00)));
//        Assertions.assertEquals(0,accountVO2.getBalance().compareTo(BigDecimal.valueOf(490000.00)));
//        //测试客户应收是否更改
//        CustomerPO customerPO = customerService.findCustomerById(payableSheetVO.getCustomer());
//        Assertions.assertEquals(0,customerPO.getPayable().compareTo(BigDecimal.valueOf(6480000.00)));
//    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getPayableSheet(){
        BusinessProcessFilterVO payableFilterVO = BusinessProcessFilterVO.builder()
                .begin(null)
                .end(null)
                .customerId(1)
                .operator("testPayableSheet")
                .build();
        List<PayableSheetVO> payableSheetVOS = payableService.getBusinessProcess(payableFilterVO);
        Assertions.assertEquals(2,payableSheetVOS.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void redFlushPayableSheet(){
        PayableSheetContentVO payableSheetContentVO1 = PayableSheetContentVO.builder()
                .name("测试账户1")
                .transferAmount(BigDecimal.valueOf(50000))
                .remark("转账5")
                .build();
        PayableSheetContentVO payableSheetContentVO2 = PayableSheetContentVO.builder()
                .name("测试账户2")
                .transferAmount(BigDecimal.valueOf(50000))
                .remark("转账6")
                .build();
        List<PayableSheetContentVO> payableSheetContentVOS = new ArrayList<>();
        payableSheetContentVOS.add(payableSheetContentVO1);
        payableSheetContentVOS.add(payableSheetContentVO2);
        PayableSheetVO payableSheetVO = PayableSheetVO.builder()
                .id("FKD-20220702-00002")
                .customer(2)
                .operator("testPayableSheet")
                .state(PayableSheetState.SUCCESS)
                .totalAmount(BigDecimal.valueOf(100000))
                .payableSheetContentVOS(payableSheetContentVOS)
                .build();
        payableService.redFlush(payableSheetVO);
        PayableSheetVO latest = payableService.getLatest();
        Assertions.assertEquals(0,latest.getTotalAmount().compareTo(BigDecimal.valueOf(-100000)));
        PayableSheetContentVO latestContent1 = payableService.getPayableSheetContentVOS(latest.getId()).get(0);
        PayableSheetContentVO latestContent2 = payableService.getPayableSheetContentVOS(latest.getId()).get(1);
        Assertions.assertEquals(0,latestContent1.getTransferAmount().compareTo(BigDecimal.valueOf(-50000)));
        Assertions.assertEquals(0,latestContent2.getTransferAmount().compareTo(BigDecimal.valueOf(-50000)));
    }
}
