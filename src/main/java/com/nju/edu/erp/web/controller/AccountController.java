package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.vo.AccountVO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/findAll")
    @Authorized(roles = {Role.ADMIN,Role.FINANCIAL_STAFF})
    public Response findAll(){
        return Response.buildSuccess(accountService.findAll());
    }

    @PostMapping("/create")
    @Authorized(roles = {Role.ADMIN})
    public Response createAccount(@RequestBody AccountVO accountVO){
        return Response.buildSuccess(accountService.createAccount(accountVO));
    }

    @GetMapping("/delete")
    @Authorized(roles = {Role.ADMIN})
    public Response deleteAccount(@RequestParam(value = "name") String name){
        accountService.deleteAccount(name);
        return Response.buildSuccess();
    }

    /**
     * 注意：只允许更改账户余额，不允许更改name
     */
    @PostMapping("/update")
    @Authorized(roles = {Role.ADMIN})
    public Response updateAccount(@RequestBody AccountVO accountVO){
        return Response.buildSuccess(accountService.updateAccount(accountVO));
    }

}
