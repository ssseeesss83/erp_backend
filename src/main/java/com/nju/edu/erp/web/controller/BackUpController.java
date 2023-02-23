package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.service.BackUpService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/backup")
public class BackUpController {

    private final BackUpService backUpService;

    @Autowired
    public BackUpController(BackUpService backUpService) {
        this.backUpService = backUpService;
    }

    @GetMapping("/backup")
    public Response backup(@RequestParam("name")String name){
        backUpService.backUp(name);
        return Response.buildSuccess();
    }

    @GetMapping("/load")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response load(@RequestParam("name") String name){
        backUpService.load(name);
        return Response.buildSuccess();
    }

    /**
     * 获取备份的名字
     * @return
     */
    @GetMapping("/get_name")
    public Response getName(){
        return Response.buildSuccess(backUpService.getNames());
    }

}
