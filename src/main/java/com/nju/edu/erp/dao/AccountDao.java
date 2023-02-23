package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.AccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AccountDao {

    List<AccountPO> findAll();

    AccountPO findById(int id);

    AccountPO findByName(String name);

    AccountPO getLatest();//id最大者

    int createAccount(AccountPO accountPO);

    int deleteAccount(String name);

    int updateAccount(AccountPO accountPO);

    Integer getLatestId();

    void deleteAll();
}
