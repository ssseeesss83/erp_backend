package com.nju.edu.erp.enums.sheetState;

import com.nju.edu.erp.enums.BaseEnum;

public enum TransferSheetState implements BaseEnum<TransferSheetState,String> {

    PENDING("待转账"),
    SUCCESS("已完成")
    ;

    private final String value;

    TransferSheetState(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
