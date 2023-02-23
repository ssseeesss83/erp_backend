package com.nju.edu.erp.serviceEnum;

import com.nju.edu.erp.enums.taxCalculation.TaxCalculation;
import com.nju.edu.erp.enums.taxType.TaxType;
import com.nju.edu.erp.enums.taxType.TaxTypeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class TaxTypeTest {

    @Test
    public void test1(){
        TaxType taxType = TaxTypeEnum.EIGHT_AND_TWO;
        List<TaxCalculation> taxCalculations = taxType.getTaxCalculationList();
        for (TaxCalculation taxCalculation:taxCalculations){
            System.out.println(taxCalculation.getTax(BigDecimal.valueOf(10000.00)));
        }
    }
}
