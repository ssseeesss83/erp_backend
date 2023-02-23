package com.nju.edu.erp.enums.handlers;

import com.nju.edu.erp.enums.*;
import com.nju.edu.erp.enums.salaryCalculation.SalaryCalculationEnum;
import com.nju.edu.erp.enums.sheetState.*;
import com.nju.edu.erp.enums.taxCalculation.TaxCalculationEnum;
import com.nju.edu.erp.enums.taxType.TaxTypeEnum;
import org.apache.ibatis.type.MappedTypes;

/**
 * 枚举转换的公共模块
 *
 */
@MappedTypes(value = {PurchaseSheetState.class, WarehouseInputSheetState.class, WarehouseOutputSheetState.class, CustomerType.class, SaleSheetState.class, PurchaseReturnsSheetState.class, SalarySheetState.class,SalarySystemSheetState.class,SalaryCalculationEnum.class,SaleReturnSheetState.class,ReceiveSheetState.class,PayableSheetState.class, TaxCalculationEnum.class, TaxTypeEnum.class, PayMethod.class, StationName.class,SalarySheetState.class, TransferSheetState.class,GiftSheetState.class, VoucherType.class})
public class SysEnumTypeHandler<E extends Enum<E> & BaseEnum> extends BaseEnumTypeHandler<E> {
    /**
     * 设置配置文件设置的转换类以及枚举类内容，供其他方法更便捷高效的实现
     *
     * @param type 配置文件中设置的转换类
     */
    public SysEnumTypeHandler(Class<E> type) {
        super(type);
    }
}