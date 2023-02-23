package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetContentPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetPO;
import com.nju.edu.erp.model.vo.AccountVO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.payable.PayableSheetVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetContentVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetVO;
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
public class ReceiveServiceTest {

    @Autowired
    ReceiveService receiveService;

    @Autowired
    AccountService accountService;

    @Autowired
    CustomerService customerService;

    @Test
    public void receiveServiceTest(){
        if(receiveService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void makeReceiveSheet(){
        UserVO userVO = UserVO.builder()
                .name("testReceiveSheet")
                .role(Role.FINANCIAL_STAFF)
                .build();
        List<ReceiveSheetContentVO> receiveSheetContentVOS = new ArrayList<>();
        ReceiveSheetVO receiveSheetVO = ReceiveSheetVO.builder()
                .customer(2)
                .totalAmount(BigDecimal.valueOf(100000.00))
                .build();
        receiveSheetContentVOS.add(ReceiveSheetContentVO.builder()
                .name("测试账户1")
                .transferAmount(BigDecimal.valueOf(75000.00))
                .remark("转账1")
                .build());
        receiveSheetContentVOS.add(ReceiveSheetContentVO.builder()
                .name("测试账户2")
                .transferAmount(BigDecimal.valueOf(25000.00))
                .remark("转账2")
                .build());
        receiveSheetVO.setReceiveSheetContentVOS(receiveSheetContentVOS);
        receiveService.makeReceiveSheet(userVO,receiveSheetVO);
        ReceiveSheetVO latest = receiveService.getLatest();
        Assertions.assertEquals(2,latest.getCustomer());
        Assertions.assertEquals(0,latest.getTotalAmount().compareTo(BigDecimal.valueOf(100000.00)));

        List<ReceiveSheetContentVO> receiveSheetContentVOS1 = latest.getReceiveSheetContentVOS();
        for (ReceiveSheetContentVO contentVO:receiveSheetContentVOS1){
            if (contentVO.getName().equals("测试账户1")){
                Assertions.assertEquals(0,contentVO.getTransferAmount().compareTo(BigDecimal.valueOf(75000.00)));
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
    public void getReceiveSheetByState(){
        List<ReceiveSheetVO> receiveSheetVOS = receiveService.getReceiveSheetByState(ReceiveSheetState.PENDING);
        Assertions.assertEquals(1,receiveSheetVOS.size());
    }

//    @Test
//    @Transactional
//    @Rollback(value = true)
//    public void approval(){
//        ReceiveSheetVO receiveSheetVO = receiveService.getLatest();//SKD-20220702-00001
//        receiveService.approval(receiveSheetVO.getId(),ReceiveSheetState.SUCCESS);
//        receiveSheetVO = receiveService.getLatest();
//        //测试状态是否改变
//        List<ReceiveSheetVO> receiveSheetVOS = receiveService.getReceiveSheetByState(ReceiveSheetState.SUCCESS);
//        Assertions.assertEquals(1,receiveSheetVOS.size());
//        //测试对应账户余额是否增加
//        AccountVO accountVO1 = accountService.findByName("测试账户1");
//        AccountVO accountVO2 = accountService.findByName("测试账户2");
//        Assertions.assertEquals(0,accountVO1.getBalance().compareTo(BigDecimal.valueOf(1010000.00)));
//        Assertions.assertEquals(0,accountVO2.getBalance().compareTo(BigDecimal.valueOf(510000.00)));
//        //测试客户应付是否更改
//        CustomerPO customerPO = customerService.findCustomerById(receiveSheetVO.getCustomer());
//        Assertions.assertEquals(0,customerPO.getReceivable().compareTo(BigDecimal.valueOf(4411400.00)));
//    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getReceiveSheet(){
        BusinessProcessFilterVO receiveFilterVO = BusinessProcessFilterVO.builder()
                .begin(null)
                .end(null)
                .customerId(2)
                .operator("testReceiveSheet")
                .build();
        List<ReceiveSheetVO> receiveSheetVOS = receiveService.getBusinessProcess(receiveFilterVO);
        Assertions.assertEquals(3,receiveSheetVOS.size());
    }

//    @Test
//    @Transactional
//    @Rollback(value = true)
//    public void redFlushReceiveSheet(){
//        ReceiveSheetContentVO receiveSheetContentVO1 = ReceiveSheetContentVO.builder()
//                .name("测试账户1")
//                .transferAmount(BigDecimal.valueOf(10000))
//                .remark("转账5")
//                .build();
//        ReceiveSheetContentVO receiveSheetContentVO2 = ReceiveSheetContentVO.builder()
//                .name("测试账户2")
//                .transferAmount(BigDecimal.valueOf(50000))
//                .remark("转账6")
//                .build();
//        List<ReceiveSheetContentVO> receiveSheetContentVOS = new ArrayList<>();
//        receiveSheetContentVOS.add(receiveSheetContentVO1);
//        receiveSheetContentVOS.add(receiveSheetContentVO2);
//        ReceiveSheetVO receiveSheetVO = ReceiveSheetVO.builder()
//                .id("SKD-20220702-00002")
//                .customer(2)
//                .operator("testReceiveSheet")
//                .state(ReceiveSheetState.SUCCESS)
//                .totalAmount(BigDecimal.valueOf(50000))
//                .receiveSheetContentVOS(receiveSheetContentVOS)
//                .build();
//        receiveService.redFlush(receiveSheetVO);
//        ReceiveSheetVO latest = receiveService.getLatest();
//        ReceiveSheetContentVO latestContent1 = receiveService.getReceiveSheetContentVOS(latest.getId()).get(0);
//        ReceiveSheetContentVO latestContent2 = receiveService.getReceiveSheetContentVOS(latest.getId()).get(1);
//        Assertions.assertEquals(0,latestContent1.getTransferAmount().compareTo(BigDecimal.valueOf(-50000)));
//        Assertions.assertEquals(0,latestContent2.getTransferAmount().compareTo(BigDecimal.valueOf(-50000)));
//    }
}
