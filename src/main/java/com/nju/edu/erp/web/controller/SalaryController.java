package com.nju.edu.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.vo.SalarySheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseCountingVO;
import com.nju.edu.erp.service.SalarySheetService;
import com.nju.edu.erp.utils.ExcelUtil;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/salary")
public class SalaryController {

    private final SalarySheetService salarySheetService;

    @Autowired
    public SalaryController(SalarySheetService salarySheetService) {
        this.salarySheetService = salarySheetService;
    }

    /**
     * 指定指定月份的工资单
     */
    @GetMapping(value = "/sheet-make")
    @Authorized(roles = {Role.FINANCIAL_STAFF,Role.ADMIN})
    public Response makeSalaryOrder(UserVO userVO,
                                    @RequestParam(value = "employeeName")String employeeName,
                                    @RequestParam(value = "companyAccount")String companyAccount,
                                    @RequestParam(value = "year")Integer year,
                                    @RequestParam(value = "month")Integer month){
        salarySheetService.makeSalarySheet(userVO,employeeName,companyAccount,year,month);
        return Response.buildSuccess();
    }
    /**
     * 根据状态查看
     */
    @GetMapping(value = "/sheet-show")
    @Authorized(roles = {Role.FINANCIAL_STAFF,Role.GM,Role.ADMIN})
    public Response showSheetByState(@RequestParam(value = "state")SalarySheetState state){
        return Response.buildSuccess(salarySheetService.getSalarySheetByState(state));
    }

    /**
     * 总经理审批
     */
    @GetMapping(value = "/sheet-approval")
    @Authorized(roles = {Role.GM,Role.ADMIN})
    public Response secondApproval(@RequestParam("salarySheetId") String salarySheetId,
                                  @RequestParam("state") SalarySheetState state){
        if(state.equals(SalarySheetState.FAILURE) || state.equals(SalarySheetState.SUCCESS)) {
            salarySheetService.approval(salarySheetId, state);
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
        return Response.buildSuccess(salarySheetService.getBusinessProcess(filterVO));
    }

    /**
     * 红冲
     */
    @PostMapping(value = "red_flush")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response redFlush(@RequestBody SalarySheetVO salarySheetVO){
        salarySheetService.redFlush(salarySheetVO);
        return Response.buildSuccess();
    }

    /**
     * 红冲且复制
     */
    @PostMapping(value = "red_flush_copy")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response redFlushCopy(@RequestBody SalarySheetVO salarySheetVO){
        return Response.buildSuccess(salarySheetService.redFlushCopy(salarySheetVO));
    }

    /**
     * 复制进入
     */
    @PostMapping(value = "copyIn")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response copyIn(@RequestBody SalarySheetVO salarySheetVO){
        salarySheetService.copyIn(salarySheetVO);
        return Response.buildSuccess();
    }
}
