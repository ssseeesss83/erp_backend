package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.PayMethod;
import com.nju.edu.erp.enums.salaryCalculation.SalaryCalculationEnum;
import com.nju.edu.erp.enums.sheetState.SalarySystemSheetState;
import com.nju.edu.erp.enums.taxCalculation.TaxCalculationEnum;
import com.nju.edu.erp.enums.taxType.TaxTypeEnum;
import com.nju.edu.erp.model.vo.SalarySystemSheetVO;
import com.nju.edu.erp.enums.salaryCalculation.SalaryCalculation;
import com.nju.edu.erp.enums.StationName;


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
public class SalarySystemServiceTest {

    @Autowired
    SalarySystemService salarySystemService;


    @Test
    public void salaryServiceTest(){
        if(salarySystemService==null){
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
    public void makeSalarySystemSheet(){
        SalarySystemSheetVO salarySystemSheetVO1 = SalarySystemSheetVO.builder()
                .stationName(StationName.SALE_STAFF)
                .stationLevel(2)
                .basisSalary(BigDecimal.valueOf(0))
                .stationSalary(BigDecimal.valueOf(4000.00))
                .salaryCalculationType(SalaryCalculationEnum.BASIS_AND_COMMISSION)
                .payMethod(PayMethod.CASH)
                .taxType(TaxTypeEnum.FIVE_AND_ONE)
                .state(SalarySystemSheetState.PENDING_LEVEL_1)
                .build();
        SalarySystemSheetVO salarySystemSheetVO2 = SalarySystemSheetVO.builder()
                .stationName(StationName.SALE_STAFF)
                .stationLevel(1)
                .basisSalary(BigDecimal.valueOf(0))
                .stationSalary(BigDecimal.valueOf(3000.00))
                .salaryCalculationType(SalaryCalculationEnum.BASIS_AND_COMMISSION)
                .payMethod(PayMethod.CASH)
                .taxType(TaxTypeEnum.FIVE_AND_ONE)
                .state(SalarySystemSheetState.PENDING_LEVEL_1)
                .build();


        salarySystemService.makeSalarySystemSheet(salarySystemSheetVO2);
        SalarySystemSheetVO latest = salarySystemService.getLatest();
        Assertions.assertEquals(StationName.SALE_STAFF,latest.getStationName());
        Assertions.assertEquals(1,latest.getStationLevel());
        Assertions.assertEquals(BigDecimal.valueOf(0.00).compareTo(latest.getBasisSalary()), 0);
        Assertions.assertEquals(BigDecimal.valueOf(3000.00).compareTo(latest.getStationSalary()), 0 );
        Assertions.assertEquals(SalaryCalculationEnum.BASIS_AND_COMMISSION,latest.getSalaryCalculationType());
        Assertions.assertEquals(PayMethod.CASH,latest.getPayMethod());
        Assertions.assertEquals(TaxTypeEnum.FIVE_AND_ONE,latest.getTaxType());
        Assertions.assertEquals(SalarySystemSheetState.PENDING_LEVEL_1,latest.getState());

    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getSheetByState(){
        List<SalarySystemSheetVO> salarySystemSheetVOS = salarySystemService.getSheetByState(SalarySystemSheetState.SUCCESS);
        Assertions.assertEquals(3,salarySystemSheetVOS.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void approval_1(){
        SalarySystemSheetVO salarySystemSheetVO = salarySystemService.getOneByNameAndLevel(StationName.INVENTORY_MANAGER, 1);
        salarySystemService.approval(salarySystemSheetVO.getStationName(),salarySystemSheetVO.getStationLevel(),SalarySystemSheetState.PENDING_LEVEL_2);
        SalarySystemSheetVO sheet = salarySystemService.getOneByNameAndLevel(StationName.INVENTORY_MANAGER, 1);
        Assertions.assertEquals(SalarySystemSheetState.PENDING_LEVEL_2,sheet.getState());
        //测试状态是否改变
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void approval_2(){
        SalarySystemSheetVO salarySystemSheetVO = salarySystemService.getOneByNameAndLevel(StationName.INVENTORY_MANAGER, 2);
        salarySystemService.approval(salarySystemSheetVO.getStationName(),salarySystemSheetVO.getStationLevel(),SalarySystemSheetState.SUCCESS);
        SalarySystemSheetVO sheet = salarySystemService.getOneByNameAndLevel(StationName.INVENTORY_MANAGER, 2);
        Assertions.assertEquals(SalarySystemSheetState.SUCCESS,sheet.getState());
        //测试状态是否改变
    }
}
