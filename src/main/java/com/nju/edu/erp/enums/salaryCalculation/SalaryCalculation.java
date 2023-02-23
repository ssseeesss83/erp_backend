package com.nju.edu.erp.enums.salaryCalculation;

import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.model.vo.SalarySystemSheetVO;
import com.nju.edu.erp.service.Impl.SaleReturnServiceImpl;
import com.nju.edu.erp.service.SaleReturnService;
import com.nju.edu.erp.service.SaleService;

import java.math.BigDecimal;

public interface SalaryCalculation {
    /**
     * 获取税前工资
     */
    public BigDecimal getSalary(EmployeeVO employeeVO, SalarySystemSheetVO sheetVO, SaleService saleService, SaleReturnService saleReturnService,String yearAndMonth);
}
