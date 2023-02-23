package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.TransferSheetState;
import com.nju.edu.erp.service.TransferService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/transfer")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * 已完成线下转账
     */
    @Authorized(roles = {Role.SALE_MANAGER,Role.ADMIN})
    @GetMapping(value = "/complete_transfer")
    public Response completeTransfer(@RequestParam("id") String id){
        transferService.updateStateToSuccess(id);
        return Response.buildSuccess();
    }

    /**
     * 根据状态展示
     */
    @Authorized(roles = {Role.SALE_MANAGER,Role.GM,Role.ADMIN})
    @GetMapping(value = "/sheet_show")
    public Response getAllByState(@RequestParam("state") TransferSheetState state){
        return Response.buildSuccess(transferService.getAllByState(state));
    }
}
