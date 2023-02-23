package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.VoucherType;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.voucher.VoucherVO;
import com.nju.edu.erp.service.VoucherService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/voucher")
public class VoucherController {

    private final VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping("/make_voucher")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response makeVoucher(@RequestBody VoucherVO voucherVO){
        voucherService.create(voucherVO);
        return Response.buildSuccess();
    }

    @GetMapping("/delete")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response makeVoucher(@RequestParam("voucherId") String voucherId){
        voucherService.delete(voucherId);
        return Response.buildSuccess();
    }

    /**
     * 可获取不同类型的优惠券(优惠券、商品组合降价、商品赠送)
     */
    @PostMapping("/get_available_voucher")
    public Response getAvailableVoucher(@RequestBody SaleSheetVO saleSheetVO,
                                        @RequestParam("customerId") Integer customerId,
                                        @RequestParam("type")VoucherType type){
        return Response.buildSuccess(voucherService.getAvailableVoucher(saleSheetVO,customerId,type));
    }

    @GetMapping("/get_all_voucher")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response getAllByType(@RequestParam("type")VoucherType type){
        return Response.buildSuccess(voucherService.getAllByType(type));
    }
}
