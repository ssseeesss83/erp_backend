package com.nju.edu.erp.web.controller;


import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.PurchaseSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.CustomerPurchaseAmountPO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVOPlus;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.filter.SaleFilterVO;
import com.nju.edu.erp.model.vo.voucher.VoucherVO;
import com.nju.edu.erp.service.SaleService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(path = "/sale")
public class SaleController {

    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    /**
     * 销售人员制定销售单, 只传回赠送类型的vo
     */
    @Authorized (roles = {Role.SALE_STAFF, Role.SALE_MANAGER, Role.GM, Role.ADMIN})
    @PostMapping(value = "/sheet-make")
    public Response makePurchaseOrder(UserVO userVO, @RequestBody SaleSheetVO saleSheetVO,
                                      @RequestParam("voucherId")String voucherId) {
        saleService.makeSaleSheet(userVO, saleSheetVO, voucherId);
        return Response.buildSuccess();
    }

    /**
     * 根据状态查看销售单
     */
    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) SaleSheetState state)  {
        return Response.buildSuccess(saleService.getSaleSheetByState(state));
    }

    /**
     * 销售经理审批
     * @param saleSheetId 进货单id
     * @param state 修改后的状态("审批失败"/"待二级审批")
     */
    @GetMapping(value = "/first-approval")
    @Authorized (roles = {Role.SALE_MANAGER, Role.ADMIN})
    public Response firstApproval(@RequestParam("saleSheetId") String saleSheetId,
                                  @RequestParam("state") SaleSheetState state)  {
        if(state.equals(SaleSheetState.FAILURE) || state.equals(SaleSheetState.PENDING_LEVEL_2)) {
            saleService.approval(saleSheetId, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }

    /**
     * 总经理审批
     * @param saleSheetId 进货单id
     * @param state 修改后的状态("审批失败"/"审批完成")
     */
    @Authorized (roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/second-approval")
    public Response secondApproval(@RequestParam("saleSheetId") String saleSheetId,
                                   @RequestParam("state") SaleSheetState state)  {
        if(state.equals(SaleSheetState.FAILURE) || state.equals(SaleSheetState.SUCCESS)) {
            saleService.approval(saleSheetId, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }

    /**
     * 获取某个销售人员某段时间内消费总金额最大的客户(不考虑退货情况,销售单不需要审批通过,如果这样的客户有多个,仅保留一个)
     * @param salesman 销售人员的名字
     * @param beginDateStr 开始时间字符串 格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @param endDateStr 结束时间字符串 格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @return
     */
    @GetMapping("/maxAmountCustomer")
    @Authorized(roles = {Role.SALE_MANAGER,Role.GM, Role.ADMIN})
    public Response getMaxAmountCustomerOfSalesmanByTime(@RequestParam String salesman, @RequestParam String beginDateStr, @RequestParam String endDateStr){
        CustomerPurchaseAmountPO ans=saleService.getMaxAmountCustomerOfSalesmanByTime(salesman,beginDateStr,endDateStr);
        return Response.buildSuccess(ans);
    }

    /**
     * 根据销售单Id搜索销售单信息
     * @param id 销售单Id
     * @return 销售单全部信息
     */
    @GetMapping(value = "/find-sheet")
    public Response findBySheetId(@RequestParam(value = "id") String id)  {
        return Response.buildSuccess(saleService.getSaleSheetById(id));
    }

    /**
     * 获取销售明细表
     * @param saleFilterVO 筛选信息
     * @return
     */
    @PostMapping(value = "/get_detail_sale_sheet")
    @Authorized(roles = {Role.GM, Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response getDetailSaleSheet(@RequestBody SaleFilterVO saleFilterVO){
        return Response.buildSuccess(saleService.getDetailSaleSheet(saleFilterVO));
    }
    /**
     * 获取经营历程表,只获取通过审批成功的单据
     */
    @PostMapping(value = "/get_business_process")
    @Authorized(roles = {Role.GM, Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response getBusinessProcess(@RequestBody BusinessProcessFilterVO filterVO){
        return Response.buildSuccess(saleService.getBusinessProcess(filterVO));
    }

    /**
     * 红冲
     */
    @PostMapping(value = "red_flush")
    @Authorized(roles = {Role.FINANCIAL_STAFF,Role.ADMIN})
    public Response redFlush(@RequestBody SaleSheetVO saleSheetVO){
        saleService.redFlush(saleSheetVO);
        return Response.buildSuccess();
    }

    /**
     * 红冲且复制
     */
    @PostMapping(value = "red_flush_copy")
    @Authorized(roles = {Role.FINANCIAL_STAFF,Role.ADMIN})
    public Response redFlushCopy(@RequestBody SaleSheetVO saleSheetVO){
        return Response.buildSuccess(saleService.redFlushCopy(saleSheetVO));
    }

    /**
     * 复制进入
     */
    @PostMapping(value = "copyIn")
    @Authorized(roles = {Role.FINANCIAL_STAFF,Role.ADMIN})
    public Response copyIn(@RequestBody SaleSheetVO saleSheetVO){
        saleService.copyIn(saleSheetVO);
        return Response.buildSuccess();
    }
}
