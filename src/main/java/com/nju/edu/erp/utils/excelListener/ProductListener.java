package com.nju.edu.erp.utils.excelListener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.model.po.ProductPO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductListener extends AnalysisEventListener<ProductPO> {

    private final ProductDao productDao;
    public ProductListener(ProductDao productDao) {
        this.productDao = productDao;
        productDao.deleteAll();
    }

    @Override
    public void invoke(ProductPO productPO, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(productPO));
        productPO.setRecentPp(null);
        productPO.setRecentRp(null);
        saveData(productPO);
        }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("product数据解析完成");
    }

    private void saveData(ProductPO productPO){
        productDao.createProduct(productPO);
    }
}
