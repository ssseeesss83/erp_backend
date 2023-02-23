package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.AccountVO;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {

    List<AccountVO> findAll();

    AccountVO findByName(String name);

    AccountVO createAccount(AccountVO accountVO);

    AccountVO updateAccount(AccountVO accountVO);

    void deleteAccount(String name);

}
