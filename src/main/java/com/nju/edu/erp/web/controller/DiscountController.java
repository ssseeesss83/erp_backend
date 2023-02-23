package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.DiscountVO;
import com.nju.edu.erp.service.DiscountService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/discount")
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping("/make_discount")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response makeDiscount(@RequestBody DiscountVO discountVO){
        discountService.create(discountVO);
        return Response.buildSuccess();
    }
    @GetMapping("/delete")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response makeVoucher(@RequestParam("voucherId") String discountId){
        discountService.delete(discountId);
        return Response.buildSuccess();
    }

    @GetMapping("get_available_discount")
    public Response getAvailableByLevel(@RequestParam("level")Integer level){
        return Response.buildSuccess(discountService.getAllByLevel(level));
    }
    /**
     * 只包含当下可以使用的
     */
    @PostMapping("get_all")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response getAll(){
        return Response.buildSuccess(discountService.getAll());
    }

}
