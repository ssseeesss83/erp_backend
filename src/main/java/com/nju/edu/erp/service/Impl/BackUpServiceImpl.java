package com.nju.edu.erp.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.nju.edu.erp.dao.AccountDao;
import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.ProductPO;
import com.nju.edu.erp.service.BackUpService;
import com.nju.edu.erp.utils.ExcelUtil;
import com.nju.edu.erp.utils.excelListener.AccountListener;
import com.nju.edu.erp.utils.excelListener.CustomerListener;
import com.nju.edu.erp.utils.excelListener.ProductListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackUpServiceImpl implements BackUpService {

    private final ProductDao productDao;

    private final CustomerDao customerDao;

    private final AccountDao accountDao;

    @Autowired
    public BackUpServiceImpl(ProductDao productDao, CustomerDao customerDao, AccountDao accountDao) {
        this.productDao = productDao;
        this.customerDao = customerDao;
        this.accountDao = accountDao;
    }

    @Override
    public void backUp(String fileName){
        ExcelWriter excelWriter = EasyExcel.write("src/main/resources/backup/"+fileName+".xlsx")
                .excelType(ExcelTypeEnum.XLSX)
                .build();
        ExcelUtil.writeMultipleExcel(excelWriter, productDao.findAll(),"product", ProductPO.class);
        ExcelUtil.writeMultipleExcel(excelWriter, customerDao.findAll(),"customer", CustomerPO.class);
        ExcelUtil.writeMultipleExcel(excelWriter, accountDao.findAll(),"account", AccountPO.class);
        excelWriter.finish();
    }

    @Override
    public void load(String fileName) {
        String path = "src/main/resources/backup/"+fileName+".xlsx";
        ExcelReader excelReader = EasyExcel.read(path).build();
        ExcelUtil.read(excelReader,"product",new ProductListener(productDao),ProductPO.class);
        ExcelUtil.read(excelReader,"customer",new CustomerListener(customerDao),CustomerPO.class);
        ExcelUtil.read(excelReader,"account",new AccountListener(accountDao),AccountPO.class);
        excelReader.finish();
    }

    @Override
    public List<String> getNames() {
        File file = new File("src/main/resources/backup/");
        File[] arr = file.listFiles();
        List<String> names = new ArrayList<>();
        if (arr==null){return names;}
        for (File file1:arr){
            if (file1.isFile()){
                names.add(file1.getName().split("\\.")[0]);
            }
        }
        return names;
    }

}
