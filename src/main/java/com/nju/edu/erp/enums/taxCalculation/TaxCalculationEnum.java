package com.nju.edu.erp.enums.taxCalculation;

import com.nju.edu.erp.enums.BaseEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TaxCalculationEnum implements BaseEnum<TaxCalculationEnum,String>,TaxCalculation {

    INDIVIDUAL_INCOME_TAX("个人所得税"){
        final List<BigDecimal> taxLevel = new ArrayList<BigDecimal>(){{
            add(BigDecimal.valueOf(0));
            add(BigDecimal.valueOf(3000));
            add(BigDecimal.valueOf(5000));
            add(BigDecimal.valueOf(10000));
            add(BigDecimal.valueOf(20000));
            add(BigDecimal.valueOf(Integer.MAX_VALUE));
        }};
        final List<BigDecimal> taxMap = new ArrayList<BigDecimal>(){{
            add(BigDecimal.valueOf(0));
            add(BigDecimal.valueOf(0.05));
            add(BigDecimal.valueOf(0.07));
            add(BigDecimal.valueOf(0.1));
            add(BigDecimal.valueOf(0.2));
        }};
        @Override
        public BigDecimal getTax(BigDecimal salary){
            BigDecimal tax = BigDecimal.ZERO;
            int index = 1;
            for (;index<taxLevel.size();index++){
                if (0 < salary.compareTo(taxLevel.get(index))){
                    tax = tax.add((taxLevel.get(index).subtract(taxLevel.get(index-1))).multiply(taxMap.get(index-1)));
                }else {
                    tax = tax.add((salary.subtract(taxLevel.get(index-1))).multiply(taxMap.get(index-1)));
                    break;
                }
            }
            return tax;
        }
    },
    UNEMPLOYMENT_INSURANCE("失业保险"){
        final List<BigDecimal> taxLevel = new ArrayList<BigDecimal>(){{
            add(BigDecimal.valueOf(0));
            add(BigDecimal.valueOf(3000));
            add(BigDecimal.valueOf(10000));
            add(BigDecimal.valueOf(Integer.MAX_VALUE));
        }};
        final List<BigDecimal> taxMap = new ArrayList<BigDecimal>(){{
            add(BigDecimal.valueOf(0));
            add(BigDecimal.valueOf(0.02));
            add(BigDecimal.valueOf(0.04));
        }};
        @Override
        public BigDecimal getTax(BigDecimal salary){
            BigDecimal tax = BigDecimal.ZERO;
            int index = 1;
            for (;index<taxLevel.size();index++){
                if (0 < salary.compareTo(taxLevel.get(index))){
                    tax = tax.add((taxLevel.get(index).subtract(taxLevel.get(index-1))).multiply(taxMap.get(index-1)));
                }else {
                    tax = tax.add((salary.subtract(taxLevel.get(index-1))).multiply(taxMap.get(index-1)));
                    break;
                }
            }
            return tax;
        }
    },
    HOUSING_ACCUMULATION_FUND("住房公积金"){
        final List<BigDecimal> taxLevel = new ArrayList<BigDecimal>(){{
            add(BigDecimal.valueOf(0));
            add(BigDecimal.valueOf(3000));
            add(BigDecimal.valueOf(5000));
            add(BigDecimal.valueOf(10000));
            add(BigDecimal.valueOf(Integer.MAX_VALUE));
        }};
        final List<BigDecimal> taxMap = new ArrayList<BigDecimal>(){{
            add(BigDecimal.valueOf(0));
            add(BigDecimal.valueOf(0.05));
            add(BigDecimal.valueOf(0.06));
            add(BigDecimal.valueOf(0.07));
        }};
        @Override
        public BigDecimal getTax(BigDecimal salary){
            BigDecimal tax = BigDecimal.ZERO;
            int index = 1;
            for (;index<taxLevel.size();index++){
                if (0 < salary.compareTo(taxLevel.get(index))){
                    tax = tax.add((taxLevel.get(index).subtract(taxLevel.get(index-1))).multiply(taxMap.get(index-1)));
                }else {
                    tax = tax.add((salary.subtract(taxLevel.get(index-1))).multiply(taxMap.get(index-1)));
                    break;
                }
            }
            return tax;
        }
    };

    private final String value;

    TaxCalculationEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
