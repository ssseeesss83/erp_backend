package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.CustomerPurchaseAmountPO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.SaleDetailSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.filter.SaleFilterVO;
import com.nju.edu.erp.model.vo.voucher.VoucherLimitVO;
import com.nju.edu.erp.model.vo.voucher.VoucherVO;

import java.math.BigDecimal;
import java.util.List;

public interface SaleService {

    /**
     * 指定销售单
     */
    void makeSaleSheet(UserVO userVO, SaleSheetVO saleSheetVO, String voucherId);

    /**
     * 根据单据状态获取销售单
     * @param state
     * @return
     */
    List<SaleSheetVO> getSaleSheetByState(SaleSheetState state);

    /**
     * 审批单据
     * @param saleSheetId
     * @param state
     */
    void approval(String saleSheetId, SaleSheetState state);

    /**
     * 获取某个销售人员某段时间内消费总金额最大的客户(不考虑退货情况,销售单不需要审批通过,如果这样的客户有多个，仅保留一个)
     * @param salesman 销售人员的名字
     * @param beginDateStr 开始时间字符串
     * @param endDateStr 结束时间字符串
     * @return
     */
    CustomerPurchaseAmountPO getMaxAmountCustomerOfSalesmanByTime(String salesman,String beginDateStr,String endDateStr);

    /**
     * 根据销售单Id搜索销售单信息
     * @param saleSheetId 销售单Id
     * @return 销售单全部信息
     */
    SaleSheetVO getSaleSheetById(String saleSheetId);

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
     * 获取的是折让前金额
     */
    BigDecimal getRawTotalAmountByMonth(String yearAndMonth);
    /**
     * 获取折让后金额
     */
    BigDecimal getFinalTotalAmountByMonth(String yearAndMonth);


    List<SaleSheetVO> getBusinessProcess(BusinessProcessFilterVO filterVO);

    List<SaleSheetContentVO> getSaleSheetContentVOS(String sheetId);

    SaleSheetVO getLatest();

    void redFlush(SaleSheetVO saleSheetVO);

    //
    SaleSheetVO redFlushCopy(SaleSheetVO saleSheetVO);

    void copyIn(SaleSheetVO saleSheetVO);
}
