package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/findByType")
    public Response findByType(@RequestParam CustomerType type) {
        return Response.buildSuccess(customerService.getCustomersByType(type));
    }

    @GetMapping("/createCustomer")
    public Response createCustomer(@RequestParam(value = "name") String name) {
        return Response.buildSuccess(customerService.createCustomer(name));
    }

    @GetMapping("/deleteCustomer")
    public Response deleteCustomer(@RequestParam(value = "id") Integer id){
        customerService.deleteById(id);
        return  Response.buildSuccess();
    }

    @PostMapping("/updateCustomer")
    public Response updateCustomer(@RequestBody CustomerPO customerPO){
        customerService.updateCustomer(customerPO);
        return Response.buildSuccess();
    }

}
