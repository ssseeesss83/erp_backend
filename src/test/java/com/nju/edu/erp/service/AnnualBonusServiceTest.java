package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.AnnualBonusVO;
import com.nju.edu.erp.service.Impl.AnnualBonusServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class AnnualBonusServiceTest {

    @Autowired
    AnnualBonusService annualBonusService;

    @Test
    public void annualBonusService(){
        if(annualBonusService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void findAll(){
        List<AnnualBonusVO> annualBonusVOS = annualBonusService.getAll();
        Assertions.assertEquals(2,annualBonusVOS.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void findByNameAndYear(){
        AnnualBonusVO annualBonusVO = annualBonusService.getOneByCustomerNameAndYear("测试员工2",2022);
        Assertions.assertEquals(0,annualBonusVO.getBasisSalaryAmount().compareTo(4));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void createAnnualBonus(){
        AnnualBonusVO annualBonusVO = AnnualBonusVO.builder()
                .employeeName("测试员工3")
                .basisSalaryAmount(6)
                .year(2022)
                .build();
        annualBonusService.create(annualBonusVO);
        annualBonusVO = annualBonusService.getOneByCustomerNameAndYear("测试员工3",2022);
        Assertions.assertEquals(0,annualBonusVO.getBasisSalaryAmount().compareTo(6));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void deleteAnnualBonus(){
        annualBonusService.delete("测试员工1",2022);
        Assertions.assertEquals(1,annualBonusService.getAll().size());
        annualBonusService.delete("测试员工2",2022);
        Assertions.assertEquals(0,annualBonusService.getAll().size());
    }
}
