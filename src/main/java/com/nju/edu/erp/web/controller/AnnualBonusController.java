package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.AnnualBonusVO;
import com.nju.edu.erp.service.AnnualBonusService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/annual_bonus")
public class AnnualBonusController {

    private final AnnualBonusService annualBonusService;

    @Autowired
    public AnnualBonusController(AnnualBonusService annualBonusService) {
        this.annualBonusService = annualBonusService;
    }

    @PostMapping("/create")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response create(@RequestBody AnnualBonusVO annualBonusVO){
        annualBonusService.create(annualBonusVO);
        return Response.buildSuccess();
    }

    @GetMapping("/delete")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response delete(@RequestParam("employeeName") String employeeName,
                           @RequestParam("year") Integer year){
        annualBonusService.delete(employeeName, year);
        return Response.buildSuccess();
    }

    @GetMapping("/get_all")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response getAll(){
        return Response.buildSuccess(annualBonusService.getAll());
    }

    @GetMapping("/get_by_name_and_year")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response getOneByCustomerNameAndYear(@RequestParam("employeeName") String employeeName,
                                                @RequestParam("year") Integer year){
        return Response.buildSuccess(annualBonusService.getOneByCustomerNameAndYear(employeeName, year));
    }
}
