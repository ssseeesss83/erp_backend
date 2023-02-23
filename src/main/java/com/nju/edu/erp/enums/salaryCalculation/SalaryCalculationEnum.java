package com.nju.edu.erp.enums.salaryCalculation;

import com.nju.edu.erp.enums.BaseEnum;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.SalarySystemSheetVO;
import com.nju.edu.erp.service.Impl.SaleReturnServiceImpl;
import com.nju.edu.erp.service.SaleReturnService;
import com.nju.edu.erp.service.SaleService;

import java.math.BigDecimal;

public enum SalaryCalculationEnum implements BaseEnum<SalaryCalculationEnum,String>, SalaryCalculation {
    MONTHLY("月薪制"){
        @Override
        public BigDecimal getSalary(EmployeeVO employeeVO, SalarySystemSheetVO sheetVO, SaleService saleService, SaleReturnService saleReturnService,String yearAndMonth){
            return sheetVO.getBasisSalary().add(sheetVO.getStationSalary());
        }
    },
    BASIS_AND_COMMISSION("提成制"){
        @Override
        public BigDecimal getSalary(EmployeeVO employeeVO, SalarySystemSheetVO sheetVO, SaleService saleService, SaleReturnService saleReturnService,String yearAndMonth){
            BigDecimal saleAmount = saleService.getSaleManTotalAmount(employeeVO.getEmployeeName(),yearAndMonth);
            BigDecimal saleReturnAmount = saleReturnService.getSaleManTotalAmount(employeeVO.getEmployeeName(),yearAndMonth);
            BigDecimal saleTotalAmount = saleAmount.subtract(saleReturnAmount);
            return sheetVO.getBasisSalary().add(saleTotalAmount.multiply(BigDecimal.valueOf(0.001)));
        }
    },
    YEARLY("年薪制"){
        @Override
        public BigDecimal getSalary(EmployeeVO employeeVO, SalarySystemSheetVO sheetVO, SaleService saleService, SaleReturnService saleReturnService,String yearAndMonth){
            return sheetVO.getBasisSalary();
        }
    };

    private final String value;

    SalaryCalculationEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
