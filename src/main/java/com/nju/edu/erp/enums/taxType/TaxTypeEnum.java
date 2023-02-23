package com.nju.edu.erp.enums.taxType;

import com.nju.edu.erp.enums.BaseEnum;
import com.nju.edu.erp.enums.taxCalculation.TaxCalculation;
import com.nju.edu.erp.enums.taxCalculation.TaxCalculationEnum;

import java.util.ArrayList;
import java.util.List;

public enum TaxTypeEnum implements BaseEnum<TaxTypeEnum,String>,TaxType {

    FIVE_AND_ONE("五险一金"){
        final List<TaxCalculation> taxCalculations = new ArrayList<TaxCalculation>(){{
            add(TaxCalculationEnum.INDIVIDUAL_INCOME_TAX);
        }};
        @Override
        public List<TaxCalculation> getTaxCalculationList() {
            return taxCalculations;
        }
    },
    EIGHT_AND_TWO("八险二金"){
        final List<TaxCalculation> taxCalculations = new ArrayList<TaxCalculation>(){{
            add(TaxCalculationEnum.INDIVIDUAL_INCOME_TAX);
            add(TaxCalculationEnum.UNEMPLOYMENT_INSURANCE);
            add(TaxCalculationEnum.HOUSING_ACCUMULATION_FUND);
        }};
        @Override
        public List<TaxCalculation> getTaxCalculationList() {
            return taxCalculations;
        }
    };


    private final String value;

    TaxTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
