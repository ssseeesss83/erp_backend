package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.model.vo.SaleDetailSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.filter.SaleFilterVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnSheetContentVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnSheetVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 201250208
 */
public interface SaleReturnService {

    void makeSaleReturnSheet(UserVO userVO, SaleReturnSheetVO saleReturnSheetVO);

    List<SaleReturnSheetVO> getSaleReturnSheetByState(SaleReturnSheetState state);

    void approval(String saleReturnSheetId,SaleReturnSheetState state);

    /**
     * 获取销售明细表
     * @param saleFilterVO 筛选信息
     * @return
     */
    List<SaleDetailSheetVO> getDetailSaleSheet(SaleFilterVO saleFilterVO);
    /**
     * 获取对应Employee的销售金额
     */
    BigDecimal getSaleManTotalAmount(String employeeName,String yearAndMonth);
    /**
     * 折后
     */
    BigDecimal getFinalTotalAmountByMonth(String yearAndMonth);
    /**
     * 折前
     */
    BigDecimal getRawTotalAmountByMonth(String yearAndMonth);

    List<SaleReturnSheetVO> getBusinessProcess(BusinessProcessFilterVO filterVO);

    SaleReturnSheetVO getOneBySaleSheetId(String saleSheetId);

    List<SaleReturnSheetContentVO> getSaleReturnSheetContentVOS(String sheetId);

    SaleReturnSheetVO getLatest();

    void redFlush(SaleReturnSheetVO saleReturnSheetVO);

    SaleReturnSheetVO redFlushCopy(SaleReturnSheetVO saleReturnSheetVO);

    void copyIn(SaleReturnSheetVO saleReturnSheetVO);
}

