package com.nju.edu.erp.enums.taxType;

import com.nju.edu.erp.enums.taxCalculation.TaxCalculation;

import java.util.List;

public interface TaxType {
    public List<TaxCalculation> getTaxCalculationList();
}
