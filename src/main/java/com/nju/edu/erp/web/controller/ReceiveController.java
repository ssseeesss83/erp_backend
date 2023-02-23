package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.receive.ReceiveSheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetVO;
import com.nju.edu.erp.service.ReceiveService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/receive")
public class ReceiveController {

    private final ReceiveService receiveService;

    @Autowired
    public ReceiveController(ReceiveService receiveService) {
        this.receiveService = receiveService;
    }

    @PostMapping(value = "/sheet-make")
    @Authorized(roles = {Role.FINANCIAL_STAFF,Role.ADMIN})
    public Response makeReceiveOrder(UserVO userVO, @RequestBody ReceiveSheetVO receiveSheetVO){
        receiveService.makeReceiveSheet(userVO,receiveSheetVO);
        return Response.buildSuccess();
    }

    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) ReceiveSheetState state){
        return Response.buildSuccess(receiveService.getReceiveSheetByState(state));
    }

    @GetMapping(value = "/sheet-approval")
    @Authorized (roles = {Role.GM, Role.ADMIN})
    public Response approval(@RequestParam("receiveSheetId") String receiveSheetId,
                             @RequestParam("state") ReceiveSheetState state){
        if(state.equals(ReceiveSheetState.FAILURE) || state.equals(ReceiveSheetState.SUCCESS)) {
            receiveService.approval(receiveSheetId, state);
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
        return Response.buildSuccess(receiveService.getBusinessProcess(filterVO));
    }
    /**
     * 红冲
     */
    @PostMapping(value = "red_flush")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response redFlush(@RequestBody ReceiveSheetVO receiveSheetVO){
        receiveService.redFlush(receiveSheetVO);
        return Response.buildSuccess();
    }

    /**
     * 红冲且复制
     */
    @PostMapping(value = "red_flush_copy")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response redFlushCopy(@RequestBody ReceiveSheetVO receiveSheetVO){
        return Response.buildSuccess(receiveService.redFlushCopy(receiveSheetVO));
    }

    /**
     * 复制进入
     */
    @PostMapping(value = "copyIn")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response copyIn(@RequestBody ReceiveSheetVO receiveSheetVO){
        receiveService.copyIn(receiveSheetVO);
        return Response.buildSuccess();
    }
}