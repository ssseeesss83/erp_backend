package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.GiftSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.service.GiftSheetService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/gift_sheet")
public class GiftSheetController {

    private final GiftSheetService giftSheetService;

    @Autowired
    public GiftSheetController(GiftSheetService giftSheetService) {
        this.giftSheetService = giftSheetService;
    }

    /**
     * 根据状态查看
     */
    @GetMapping(value = "/sheet-show")
    @Authorized(roles = {Role.GM,Role.ADMIN})
    public Response showSheetByState(@RequestParam("state")GiftSheetState state){
        return Response.buildSuccess(giftSheetService.getGiftSheetByState(state));
    }

    @GetMapping(value = "/sheet-approval")
    @Authorized(roles = {Role.GM,Role.ADMIN})
    public Response approval(@RequestParam("id")String sheetId,
                             @RequestParam("state")GiftSheetState state){
        giftSheetService.approval(sheetId,state);
        return Response.buildSuccess();
    }
}
