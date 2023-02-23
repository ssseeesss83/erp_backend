package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.service.BusinessService;
import com.nju.edu.erp.utils.Conversion;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/business")
public class BusinessController {

    private final BusinessService businessService;

    @Autowired
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    /**
     * 查看指定月份的经营情况表
     */
    @GetMapping("/show-sheet")
    @Authorized(roles = {Role.ADMIN,Role.FINANCIAL_STAFF,Role.GM})
    public Response getBusinessSheet(@RequestParam("year") Integer year,
                                     @RequestParam("month") Integer month){
        return Response.buildSuccess(businessService.getMonthBusinessSheet(Conversion.getYearAndMonth(year, month)));
    }

}
