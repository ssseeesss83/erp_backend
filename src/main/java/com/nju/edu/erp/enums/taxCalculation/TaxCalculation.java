package com.nju.edu.erp.enums.taxCalculation;

import java.math.BigDecimal;

public interface TaxCalculation {
    public BigDecimal getTax(BigDecimal salary);
}
