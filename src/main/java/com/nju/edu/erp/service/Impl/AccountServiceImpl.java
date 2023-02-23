package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.AccountDao;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.vo.AccountVO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.utils.Conversion;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;

    @Autowired
    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public List<AccountVO> findAll() {
        List<AccountPO> accountPOS = accountDao.findAll();
        return Conversion.conversion(accountPOS);
    }

    @Override
    public AccountVO findByName(String name) {
        return Conversion.conversion(accountDao.findByName(name));
    }

    @Override
    public AccountVO createAccount(AccountVO accountVO) {
        AccountPO accountPO = new AccountPO();
        BeanUtils.copyProperties(accountVO,accountPO);
        // accountPO.setId(accountDao.getLatestId()+1); 实现变更到mapper
        if (accountDao.createAccount(accountPO)==0) throw new MyServiceException("A0002","插入失败");
        return Conversion.conversion(accountDao.getLatest());//防御式编程
    }

    @Override
    public AccountVO updateAccount(AccountVO accountVO) {
        AccountPO accountPO = accountDao.findByName(accountVO.getName());
        if (accountPO==null) throw new MyServiceException("A0004","更新账户失败,该账户不存在");
        accountPO.setBalance(accountVO.getBalance());
        if (accountDao.updateAccount(accountPO)==0) throw new MyServiceException("A0003","修改失败");
        return Conversion.conversion(accountDao.findByName(accountVO.getName()));//防御式编程
    }

    @Override
    public void deleteAccount(String name) {
        if(accountDao.deleteAccount(name)==0) throw new MyServiceException("A0004","删除失败");
    }
}
