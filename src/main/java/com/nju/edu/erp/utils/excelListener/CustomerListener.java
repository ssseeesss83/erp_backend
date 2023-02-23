package com.nju.edu.erp.utils.excelListener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.model.po.CustomerPO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerListener extends AnalysisEventListener<CustomerPO> {

    private final CustomerDao customerDao;

    public CustomerListener(CustomerDao customerDao) {
        this.customerDao = customerDao;
        customerDao.deleteAll();
    }

    @Override
    public void invoke(CustomerPO customerPO, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(customerPO));
        saveData(customerPO);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("customer数据解析完成");
    }

    private void saveData(CustomerPO customerPO){
        customerDao.createCustomer(customerPO);
    }
}

