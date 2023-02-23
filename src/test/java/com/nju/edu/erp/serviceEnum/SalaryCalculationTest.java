package com.nju.edu.erp.serviceEnum;

import com.nju.edu.erp.enums.PayMethod;
import com.nju.edu.erp.enums.StationName;
import com.nju.edu.erp.enums.salaryCalculation.SalaryCalculation;
import com.nju.edu.erp.enums.salaryCalculation.SalaryCalculationEnum;
import com.nju.edu.erp.enums.sheetState.SalarySystemSheetState;
import com.nju.edu.erp.enums.taxType.TaxTypeEnum;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.SalarySystemSheetVO;
import com.nju.edu.erp.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class SalaryCalculationTest {

    @Autowired
    CustomerService customerService;

    @Autowired
    SalarySystemService salarySystemService;

    @Autowired
    SaleService saleService;

    @Autowired
    SaleReturnService saleReturnService;

    @Autowired
    EmployeeService employeeService;

    @Test
    public void test1(){
        EmployeeVO employeeVO = employeeService.getOneByName("xiaoshoujingli");
        SalarySystemSheetVO salarySystemSheetVO = salarySystemService.getOneByNameAndLevel(employeeVO.getStationName(),employeeVO.getStationLevel());
        SalaryCalculation salaryCalculation = employeeVO.getSalaryCalculationType();
        Assertions.assertEquals(0,salaryCalculation.getSalary(employeeVO,salarySystemSheetVO,saleService,saleReturnService,"20225").compareTo(BigDecimal.valueOf(11540.00)));
    }

    @Test
    public void test2(){
        SalarySystemSheetVO salarySystemSheetVO = salarySystemService.getLatest();
        salarySystemSheetVO.getBasisSalary();
    }
}
