package com.nju.edu.erp.utils.excelListener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.nju.edu.erp.dao.AccountDao;;
import com.nju.edu.erp.model.po.AccountPO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountListener extends AnalysisEventListener<AccountPO> {

    private final AccountDao accountDao;

    public AccountListener(AccountDao accountDao) {
        this.accountDao = accountDao;
        accountDao.deleteAll();
    }

    @Override
    public void invoke(AccountPO accountPO, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(accountPO));
        saveData(accountPO);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("customer数据解析完成");
    }

    private void saveData(AccountPO accountPO){
        accountDao.createAccount(accountPO);
    }
}