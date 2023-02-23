package com.nju.edu.erp.serviceEnum;

import com.nju.edu.erp.enums.taxCalculation.TaxCalculation;
import com.nju.edu.erp.enums.taxCalculation.TaxCalculationEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TaxCalculationTest {

    @Test
    public void test1(){
        TaxCalculation taxCalculation = TaxCalculationEnum.INDIVIDUAL_INCOME_TAX;
        System.out.println(taxCalculation.getTax(BigDecimal.valueOf(10000.00)));
    }

    @Test
    public void test2(){
        TaxCalculation taxCalculation = TaxCalculationEnum.HOUSING_ACCUMULATION_FUND;
        System.out.println(taxCalculation.getTax(BigDecimal.valueOf(10000.00)));
    }

    @Test
    public void test3(){
        TaxCalculation taxCalculation = TaxCalculationEnum.UNEMPLOYMENT_INSURANCE;
        System.out.println(taxCalculation.getTax(BigDecimal.valueOf(10000.00)));
    }
}
