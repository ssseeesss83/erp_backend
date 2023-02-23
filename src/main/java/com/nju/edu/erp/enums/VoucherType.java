package com.nju.edu.erp.enums;

public enum VoucherType implements BaseEnum<VoucherType,String>{
    CASH("代金券"),
    COMBINATION("商品组合包"),
    GIFT("商品赠送")
    ;

    private final String value;

    VoucherType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
