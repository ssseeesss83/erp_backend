package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.service.ClockService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/clock")
public class ClockController {

    private final ClockService clockService;

    @Autowired
    public ClockController(ClockService clockService) {
        this.clockService = clockService;
    }

    @GetMapping("/clock_in")
    public Response clockIn(@RequestParam(value = "employName") String employName){
        return Response.buildSuccess(clockService.clockIn(employName));
    }

    /**
     * 获取某一员工打卡记录
     */
    @GetMapping("/show_employee_clock")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.HR})
    public Response getAllEmployeeClock(@RequestParam(value = "employName") String employName){
        return Response.buildSuccess(clockService.getAllEmployeeClockByName(employName));
    }
}
