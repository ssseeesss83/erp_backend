package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.PayableSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.payable.PayableSheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.payable.PayableSheetVO;
import com.nju.edu.erp.service.PayableService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/payable")
public class PayableController {

    private final PayableService payableService;

    @Autowired
    public PayableController(PayableService payableService) {
        this.payableService = payableService;
    }

    @PostMapping(value = "/sheet-make")
    @Authorized(roles = {Role.FINANCIAL_STAFF,Role.ADMIN})
    public Response makePayableOrder(UserVO userVO, @RequestBody PayableSheetVO payableSheetVO){
        payableService.makePayableSheet(userVO,payableSheetVO);
        return Response.buildSuccess();
    }

    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) PayableSheetState state){
        return Response.buildSuccess(payableService.getPayableSheetByState(state));
    }

    @GetMapping(value = "/sheet-approval")
    @Authorized (roles = {Role.GM, Role.ADMIN})
    public Response approval(@RequestParam("payableSheetId") String payableSheetId,
                             @RequestParam("state") PayableSheetState state){
        if(state.equals(PayableSheetState.FAILURE) || state.equals(PayableSheetState.SUCCESS)) {
            payableService.approval(payableSheetId, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }

    /**
     * 获取经营历程表
     */
    @PostMapping(value = "/get_business_process")
    @Authorized(roles = {Role.GM, Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response getBusinessProcess(@RequestBody BusinessProcessFilterVO filterVO){
        return Response.buildSuccess(payableService.getBusinessProcess(filterVO));
    }

    /**
     * 红冲
     */
    @PostMapping(value = "red_flush")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response redFlush(@RequestBody PayableSheetVO payableSheetVO){
        payableService.redFlush(payableSheetVO);
        return Response.buildSuccess();
    }

    /**
     * 红冲且复制
     */
    @PostMapping(value = "red_flush_copy")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response redFlushCopy(@RequestBody PayableSheetVO payableSheetVO){
        return Response.buildSuccess(payableService.redFlushCopy(payableSheetVO));
    }

    /**
     * 复制进入
     */
    @PostMapping(value = "copyIn")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response copyIn(@RequestBody PayableSheetVO payableSheetVO){
        payableService.copyIn(payableSheetVO);
        return Response.buildSuccess();
    }
}
