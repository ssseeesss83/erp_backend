package com.nju.edu.erp.service;

import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.vo.AccountVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Test
    public void accountServiceTest(){
        if(accountService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void findAll(){
        List<AccountVO> accountVOS = accountService.findAll();
        Assertions.assertEquals(4,accountVOS.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void findByName(){
        AccountVO accountVO = accountService.findByName("测试账户2");
        Assertions.assertEquals(0,accountVO.getBalance().compareTo(BigDecimal.valueOf(500000.00)));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void createAccount(){
        AccountVO accountVO = AccountVO.builder()
                .name("测试账户3")
                .balance(BigDecimal.valueOf(10000.00))
                .build();
        accountService.createAccount(accountVO);
        accountVO = accountService.findByName("测试账户3");
        Assertions.assertEquals("测试账户3",accountVO.getName());
        Assertions.assertEquals(0,accountVO.getBalance().compareTo(BigDecimal.valueOf(10000.00)));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void updateAccount(){
        AccountVO accountVO = accountService.findByName("测试账户1");
        accountVO.setBalance(BigDecimal.valueOf(1.00));
        accountService.updateAccount(accountVO);
        accountVO = accountService.findByName("测试账户1");
        Assertions.assertEquals(0,accountVO.getBalance().compareTo(BigDecimal.valueOf(1.00)));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void deleteAccount(){
        accountService.deleteAccount("测试账户1");
        Assertions.assertEquals(3,accountService.findAll().size());
        accountService.deleteAccount("测试账户2");
        Assertions.assertEquals(2,accountService.findAll().size());
    }
}
