package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.vo.SalarySheetVO;


import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class SalarySheetServiceTest {

    @Autowired
    SalarySheetService salarySheetService;
    @Autowired
    SalarySheetDao salarySheetDao;

    @Test
    public void salaryServiceTest(){
        if(salarySheetService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    //测试 void makeSalarySystemSheet(SalarySystemSheetVO salarySystemSheetVO);
    //    SalarySystemSheetVO getLatest();
    @Test
    @Transactional
    @Rollback(value = true)
    public void makeSalarySheet(){
        UserVO userVO1 = UserVO.builder()
                .name("caiwu")
                .role(Role.FINANCIAL_STAFF)
                .password("123456")
                .build();
        salarySheetService.makeSalarySheet(userVO1,"xiaoshoujingli","99999999", 2022,5);
        SalarySheetVO latest = salarySheetService.getLatestSheet();
        Assertions.assertEquals(0,latest.getShouldPay().compareTo(BigDecimal.valueOf(11140)));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void makeSalarySheet2(){
        UserVO userVO1 = UserVO.builder()
                .name("caiwu")
                .role(Role.FINANCIAL_STAFF)
                .password("123456")
                .build();
        salarySheetService.makeSalarySheet(userVO1,"xiaoshoujingli","99999999", 2022,7);
        SalarySheetVO latest = salarySheetService.getLatestSheet();
        Assertions.assertEquals(0,latest.getShouldPay().compareTo(BigDecimal.valueOf(5600)));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getSheetByState(){
        List<SalarySheetVO> salarySheetVOS = salarySheetService.getSalarySheetByState(SalarySheetState.SUCCESS);
        Assertions.assertEquals(1,salarySheetVOS.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void approval_2(){
        salarySheetService.approval("GZD-20220705-00002", SalarySheetState.SUCCESS);
        SalarySheetVO sheet = salarySheetService.getSheetById("GZD-20220705-00002");
        Assertions.assertEquals(SalarySheetState.SUCCESS,sheet.getState());
        //测试状态是否改变
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getSalarySheet(){
        BusinessProcessFilterVO salaryFilterVO = BusinessProcessFilterVO.builder()
                .begin(null)
                .end(null)
                //.customerId(2)
                .operator("caiwu")
                .build();
        List<SalarySheetVO> salarySheetVOS = salarySheetService.getBusinessProcess(salaryFilterVO);
        Assertions.assertEquals(1,salarySheetVOS.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void redFlushSalarySheet(){
        SalarySheetVO salarySheetVO = SalarySheetVO.builder()
                .id("GZD-20220705-00001")
                .operator("caiwu")
                .employeeName("yuangong1")
                .employeeAccount("01111111")
                .shouldPay(BigDecimal.valueOf(100))
                .tax(BigDecimal.valueOf(10))
                .realPay(BigDecimal.valueOf(90))
                .companyAccount("99999999")
                .state(SalarySheetState.SUCCESS)
                .build();
        salarySheetService.redFlush(salarySheetVO);
        SalarySheetVO latest = salarySheetService.getLatestSheet();
        Assertions.assertEquals(0,latest.getShouldPay().compareTo(BigDecimal.valueOf(-100)));
    }
}
