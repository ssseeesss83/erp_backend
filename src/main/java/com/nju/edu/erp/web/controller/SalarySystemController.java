package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.StationName;
import com.nju.edu.erp.enums.sheetState.SalarySystemSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.vo.SalarySystemSheetVO;
import com.nju.edu.erp.service.SalarySystemService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/salary_system")
public class SalarySystemController {

    private final SalarySystemService salarySystemService;

    @Autowired
    public SalarySystemController(SalarySystemService salarySystemService) {
        this.salarySystemService = salarySystemService;
    }

    @Authorized(roles = {Role.HR, Role.ADMIN})
    @PostMapping(value = "/sheet-make")
    public Response makeSalarySystemOrder(@RequestBody SalarySystemSheetVO salarySystemSheetVO)  {
        salarySystemService.makeSalarySystemSheet(salarySystemSheetVO);
        return Response.buildSuccess();
    }

    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) SalarySystemSheetState state)  {
        return Response.buildSuccess(salarySystemService.getSheetByState(state));
    }

    @GetMapping(value = "/first-approval")
    @Authorized(roles = {Role.HR, Role.ADMIN})
    public Response firstApproval(@RequestParam("name") StationName name,
                                  @RequestParam("level") Integer level,
                                  @RequestParam("state") SalarySystemSheetState state)  {
        if(state.equals(SalarySystemSheetState.FAILURE) || state.equals(SalarySystemSheetState.PENDING_LEVEL_2)) {
            salarySystemService.approval(name,level, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }
    @GetMapping(value = "/second-approval")
    @Authorized(roles = {Role.GM,Role.ADMIN})
    public Response secondApproval(@RequestParam("name") StationName name,
                                  @RequestParam("level") Integer level,
                                  @RequestParam("state") SalarySystemSheetState state)  {
        if(state.equals(SalarySystemSheetState.FAILURE) || state.equals(SalarySystemSheetState.SUCCESS)) {
            salarySystemService.approval(name,level, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }
}
