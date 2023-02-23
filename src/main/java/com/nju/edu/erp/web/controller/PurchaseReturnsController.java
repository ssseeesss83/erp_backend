package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.PurchaseReturnsSheetState;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.purchaseReturns.PurchaseReturnsSheetVO;
import com.nju.edu.erp.service.PurchaseReturnsService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/purchase-returns")
public class PurchaseReturnsController {

    private final PurchaseReturnsService purchaseReturnsService;

    @Autowired
    public PurchaseReturnsController(PurchaseReturnsService purchaseReturnsService) {
        this.purchaseReturnsService = purchaseReturnsService;
    }

    /**
     * 销售人员制定进货退货单
     */
    @Authorized (roles = {Role.SALE_STAFF, Role.SALE_MANAGER, Role.GM, Role.ADMIN})
    @PostMapping(value = "/sheet-make")
    public Response makePurchaseOrder(UserVO userVO, @RequestBody PurchaseReturnsSheetVO purchaseReturnsSheetVO)  {
        purchaseReturnsService.makePurchaseReturnsSheet(userVO, purchaseReturnsSheetVO);
        return Response.buildSuccess();
    }

    /**
     * 销售经理审批
     * @param purchaseReturnsSheetId 进货退货单id
     * @param state 修改后的状态("审批失败"/"待二级审批")
     */
    @GetMapping(value = "/first-approval")
    @Authorized (roles = {Role.SALE_MANAGER, Role.GM, Role.ADMIN})
    public Response firstApproval(@RequestParam("purchaseReturnsSheetId") String purchaseReturnsSheetId,
                                  @RequestParam("state") PurchaseReturnsSheetState state)  {
        if(state.equals(PurchaseReturnsSheetState.FAILURE) || state.equals(PurchaseReturnsSheetState.PENDING_LEVEL_2)) {
            purchaseReturnsService.approval(purchaseReturnsSheetId, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }

    /**
     * 总经理审批
     * @param purchaseReturnsSheetId 进货退货单id
     * @param state 修改后的状态("审批失败"/"审批完成")
     */
    @Authorized (roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/second-approval")
    public Response secondApproval(@RequestParam("purchaseReturnsSheetId") String purchaseReturnsSheetId,
                                   @RequestParam("state") PurchaseReturnsSheetState state)  {
        if(state.equals(PurchaseReturnsSheetState.FAILURE) || state.equals(PurchaseReturnsSheetState.SUCCESS)) {
            purchaseReturnsService.approval(purchaseReturnsSheetId, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }

    /**
     * 根据状态查看进货退货单
     */
    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) PurchaseReturnsSheetState state)  {
        return Response.buildSuccess(purchaseReturnsService.getPurchaseReturnsSheetByState(state));
    }
    /**
     * 获取经营历程表
     */
    @PostMapping(value = "/get_business_process")
    @Authorized(roles = {Role.GM, Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response getBusinessProcess(@RequestBody BusinessProcessFilterVO filterVO){
        return Response.buildSuccess(purchaseReturnsService.getBusinessProcess(filterVO));
    }

    /**
     * 红冲
     */
    @PostMapping(value = "red_flush")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response redFlush(@RequestBody PurchaseReturnsSheetVO purchaseReturnsSheetVO){
        purchaseReturnsService.redFlush(purchaseReturnsSheetVO);
        return Response.buildSuccess();
    }

    /**
     * 红冲且复制
     */
    @PostMapping(value = "red_flush_copy")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response redFlushCopy(@RequestBody PurchaseReturnsSheetVO purchaseReturnsSheetVO){
        return Response.buildSuccess(purchaseReturnsService.redFlushCopy(purchaseReturnsSheetVO));
    }

    /**
     * 复制进入
     */
    @PostMapping(value = "copyIn")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response copyIn(@RequestBody PurchaseReturnsSheetVO purchaseReturnsSheetVO){
        purchaseReturnsService.copyIn(purchaseReturnsSheetVO);
        return Response.buildSuccess();
    }
}