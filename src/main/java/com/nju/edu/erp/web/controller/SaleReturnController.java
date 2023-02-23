package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.SaleReturnService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 201250208
 * 销售退货
 */
@RestController
@RequestMapping(path = "/sale-returns")
public class SaleReturnController {

    private final SaleReturnService saleReturnService;

    @Autowired
    public SaleReturnController(SaleReturnService saleReturnService){
        this.saleReturnService = saleReturnService;
    }
    /**
     * 制订销售退货单
     */
    @Authorized(roles = {Role.SALE_STAFF,Role.SALE_MANAGER,Role.GM,Role.ADMIN})
    @PostMapping(value = "/sheet-make")
    public Response makeSaleOrder(UserVO userVO, @RequestBody SaleReturnSheetVO saleReturnSheetVO){
        saleReturnService.makeSaleReturnSheet(userVO,saleReturnSheetVO);
        return Response.buildSuccess();
    }
    /**
     * 根据状态查看销售单
     */
    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) SaleReturnSheetState state)  {
        return Response.buildSuccess(saleReturnService.getSaleReturnSheetByState(state));
    }
    /**
     * 销售经理审批
     * @param saleReturnsSheetId 销售退货单id
     * @param state 修改后的状态("审批失败"/"待二级审批")
     */
    @Authorized (roles = {Role.SALE_MANAGER, Role.ADMIN})
    @GetMapping(value = "/first-approval")
    public Response firstApproval(@RequestParam("saleReturnsSheetId") String saleReturnsSheetId,
                                  @RequestParam("state")SaleReturnSheetState state){
        if(state.equals(SaleReturnSheetState.FAILURE)||state.equals(SaleReturnSheetState.PENDING_LEVEL_2)){
            saleReturnService.approval(saleReturnsSheetId,state);
            return Response.buildSuccess();
        }else {
            //TODO:code待修改
            return Response.buildFailed("000000","操作失败");
        }
    }
    /**
     * 总经理审批
     * @param saleReturnsSheetId 销售退货单id
     * @param state 修改后的状态("审批失败"/"审批完成")
     */
    @Authorized (roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/second-approval")
    public Response secondApproval(@RequestParam("saleReturnsSheetId") String saleReturnsSheetId,
                                   @RequestParam("state") SaleReturnSheetState state)  {
        if(state.equals(SaleReturnSheetState.FAILURE) || state.equals(SaleReturnSheetState.SUCCESS)) {
            saleReturnService.approval(saleReturnsSheetId,state);
            return Response.buildSuccess();
        } else {
            //TODO:code待修改
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }
    /**
     * 获取经营历程表
     */
    @PostMapping(value = "/get_business_process")
    @Authorized(roles = {Role.GM, Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response getBusinessProcess(@RequestBody BusinessProcessFilterVO filterVO){
        return Response.buildSuccess(saleReturnService.getBusinessProcess(filterVO));
    }

    /**
     * 红冲
     */
    @PostMapping(value = "red_flush")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response redFlush(@RequestBody SaleReturnSheetVO saleReturnSheetVO){
        saleReturnService.redFlush(saleReturnSheetVO);
        return Response.buildSuccess();
    }

    /**
     * 红冲且复制
     */
    @PostMapping(value = "red_flush_copy")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response redFlushCopy(@RequestBody SaleReturnSheetVO saleReturnSheetVO){
        return Response.buildSuccess(saleReturnService.redFlushCopy(saleReturnSheetVO));
    }

    /**
     * 复制进入
     */
    @PostMapping(value = "copyIn")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response copyIn(@RequestBody SaleReturnSheetVO saleReturnSheetVO){
        saleReturnService.copyIn(saleReturnSheetVO);
        return Response.buildSuccess();
    }

}
