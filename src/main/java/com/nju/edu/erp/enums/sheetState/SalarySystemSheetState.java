package com.nju.edu.erp.enums.sheetState;

import com.nju.edu.erp.enums.BaseEnum;
import com.nju.edu.erp.model.po.SalarySystemSheetPO;

public enum SalarySystemSheetState implements BaseEnum<SalarySystemSheetState,String> {
    PENDING_LEVEL_1("待一级审批"), // 待人力资源审批
    PENDING_LEVEL_2("待二级审批"), // 待总经理审批
    SUCCESS("审批完成"),
    FAILURE("审批失败");

    private final String value;

    SalarySystemSheetState(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
