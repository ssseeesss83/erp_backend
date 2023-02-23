package com.nju.edu.erp.enums;

public enum PayMethod implements BaseEnum<PayMethod,String>{

    TRANSFER("转账"),
    CASH("现金");

    private final String value;

    PayMethod(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
