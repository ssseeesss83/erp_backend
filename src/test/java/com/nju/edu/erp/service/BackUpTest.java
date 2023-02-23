package com.nju.edu.erp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BackUpTest {

    @Autowired
    BackUpService backUpService;

    @Test
    public void backUpTest(){
        if(backUpService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    @Test
    public void backUp(){
        backUpService.backUp("backupTest");
    }

    @Test
    public void load(){
        backUpService.load("backupTest");
    }

    @Test
    public void getNamesTest(){
        List<String> names = backUpService.getNames();
        Assertions.assertEquals(1,names.size());
        Assertions.assertEquals("backupTest",names.get(0));
    }

}
