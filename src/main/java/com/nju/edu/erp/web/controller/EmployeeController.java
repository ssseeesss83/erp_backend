package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.EmployeeVO;
import com.nju.edu.erp.service.EmployeeService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping(value = "/employee-make")
    @Authorized(roles = {Role.INVENTORY_MANAGER, Role.ADMIN})
    public Response makeEmployeeOrder(@RequestBody EmployeeVO employeeVO){
        employeeService.create(employeeVO);
        return Response.buildSuccess();
    }

    @GetMapping(value = "/show-all")
    public Response showAll(){
        return Response.buildSuccess(employeeService.getAll());
    }

    /**
     * 注意：创建时除个人信息外，只允许对工资岗位和级别进行选择，其余属性将自动补充
     * 只要不符合salarySystem就算填写了也无效
     */
    @PostMapping(value = "/create")
    @Authorized(roles = {Role.HR, Role.ADMIN})
    public Response createEmployee(@RequestBody EmployeeVO employeeVO){
        return Response.buildSuccess(employeeService.create(employeeVO));
    }

    @GetMapping(value = "/delete")
    @Authorized(roles = {Role.HR, Role.ADMIN})
    public Response deleteById(@RequestParam(value = "id") Integer employeeId){
        employeeService.deleteById(employeeId);
        return Response.buildSuccess();
    }

    @PostMapping(value = "/update")
    @Authorized(roles = {Role.HR, Role.ADMIN})
    public Response updateEmployee(@RequestBody EmployeeVO employeeVO){
        return Response.buildSuccess(employeeService.update(employeeVO));
    }

}
