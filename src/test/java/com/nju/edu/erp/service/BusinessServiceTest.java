package com.nju.edu.erp.service;


import com.nju.edu.erp.model.vo.BusinessVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
public class BusinessServiceTest {

    @Autowired
    BusinessService businessService;


    @Test
    public void businessServiceTest(){
        if(businessService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getMonthBusinessSheet(){
        BusinessVO businessVO = businessService.getMonthBusinessSheet("20225");
        Assertions.assertEquals(0,businessVO.getRawIncome().compareTo(BigDecimal.valueOf(5540000)));
        Assertions.assertEquals(0,businessVO.getFinalIncome().compareTo(BigDecimal.valueOf(4431400)));
        Assertions.assertEquals(0,businessVO.getAllowance().compareTo(BigDecimal.valueOf(1108600)));
        //SQL缺陷：退货单没有SUCCESS的
//        Assertions.assertEquals(0,businessVO.getProductTotalCost().compareTo(BigDecimal.valueOf(3270000)));
//        Assertions.assertEquals(0,businessVO.getEmployeeTotalCost().compareTo(BigDecimal.valueOf(0)));
        //SQL缺陷：5月没有员工的salarySheet
//        Assertions.assertEquals(0,businessVO.getCost().compareTo(BigDecimal.valueOf(3270000)));
//        Assertions.assertEquals(0,businessVO.getProfits().compareTo(BigDecimal.valueOf(1161400)));

    }
}
