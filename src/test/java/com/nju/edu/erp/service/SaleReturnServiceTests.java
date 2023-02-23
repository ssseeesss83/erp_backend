package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.*;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.saleReturn.SaleReturnSheetContentPO;
import com.nju.edu.erp.model.po.saleReturn.SaleReturnSheetPO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnSheetContentVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnSheetVO;
import com.nju.edu.erp.utils.IdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
public class SaleReturnServiceTests {

    @Autowired
    WarehouseService warehouseService;
    @Autowired
    SaleReturnService saleReturnService;

    @Autowired
    SaleReturnSheetDao saleReturnSheetDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    WarehouseDao warehouseDao;

    @Test
    public void WarehouseServiceTest(){
        if(warehouseService==null){
            System.out.println("service也是空的");
        }else{
            System.out.println("service不是空的");
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void makeSaleReturnSheet(){
        UserVO userVO = UserVO.builder()
                .name("xiaoshoujingli")
                .role(Role.SALE_MANAGER)
                .build();
        List<SaleReturnSheetContentVO> saleReturnSheetContentVOS = new ArrayList<>();
        saleReturnSheetContentVOS.add(SaleReturnSheetContentVO.builder()
                .pid("0000000000400000")
                .quantity(50)
                .remark("Test1-product1")
                .build());
        saleReturnSheetContentVOS.add(SaleReturnSheetContentVO.builder()
                .pid("0000000000400001")
                .quantity(50)
                .remark("Test1-product2")
                .build());
        SaleReturnSheetVO saleReturnSheetVO = SaleReturnSheetVO.builder()
                .saleReturnSheetContent(saleReturnSheetContentVOS)
                .supplier(2)
                .saleSheetId("XSD-20220524-00001")
                .remark("Test1")
                .build();
        SaleReturnSheetPO preSheet = saleReturnSheetDao.getLatest();
        String realSheetId = IdGenerator.generateSheetId(preSheet==null?null:preSheet.getId(),"XSTHD");

        saleReturnService.makeSaleReturnSheet(userVO,saleReturnSheetVO);
        SaleReturnSheetPO latestSheet = saleReturnSheetDao.getLatest();
        Assertions.assertNotNull(latestSheet);
        Assertions.assertEquals(realSheetId,latestSheet.getId());
        Assertions.assertEquals(0,latestSheet.getTotalAmount().compareTo(BigDecimal.valueOf(310000.00)));
        Assertions.assertEquals(0,latestSheet.getFinalAmount().compareTo(BigDecimal.valueOf(247900.00)));

        String sheetId = latestSheet.getId();
        Assertions.assertNotNull(sheetId);
        List<SaleReturnSheetContentPO> content = saleReturnSheetDao.findContentSaleReturnsSheetId(sheetId);
        content.sort(Comparator.comparing(SaleReturnSheetContentPO::getPid));
        Assertions.assertEquals(2, content.size());
        Assertions.assertEquals("0000000000400000", content.get(0).getPid());
        Assertions.assertEquals(0, content.get(0).getTotalPrice().compareTo(BigDecimal.valueOf(110000.00)));
        Assertions.assertEquals("0000000000400001", content.get(1).getPid());
        Assertions.assertEquals(0, content.get(1).getTotalPrice().compareTo(BigDecimal.valueOf(200000.00)));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void approval_1() { // 测试一级审批
        saleReturnService.approval("XSTHD-20220629-00001", SaleReturnSheetState.PENDING_LEVEL_2);
        SaleReturnSheetPO sheet = saleReturnSheetDao.findSheetById("XSTHD-20220629-00001");
        Assertions.assertEquals(SaleReturnSheetState.PENDING_LEVEL_2,sheet.getState());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void approval_2() { // 测试二级审批
        // 1.检测状态修改
        // 2.检测商品修改
        // 3.检测库存单修改
        // 4.检测客户钱包
        saleReturnService.approval("XSTHD-20220629-00000", SaleReturnSheetState.SUCCESS);
        SaleReturnSheetPO saleReturnSheetPO = saleReturnSheetDao.findSheetById("XSTHD-20220629-00000");
        Assertions.assertEquals(SaleReturnSheetState.SUCCESS,saleReturnSheetPO.getState());

        Assertions.assertEquals(550,productDao.findById("0000000000400000").getQuantity());
        Assertions.assertEquals(1050,productDao.findById("0000000000400001").getQuantity());

        Assertions.assertEquals(250,warehouseDao.findOneByPidAndBatchId("0000000000400000",1).getQuantity());
        Assertions.assertEquals(250,warehouseDao.findOneByPidAndBatchId("0000000000400001",2).getQuantity());

        CustomerPO customerPO = customerDao.findOneById(2);
        Assertions.assertEquals(0,customerPO.getReceivable().compareTo(BigDecimal.valueOf(4431400.00+247900.00)));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void redFlushSaleReturnSheet() {
        SaleReturnSheetContentVO saleReturnSheetContentVO = SaleReturnSheetContentVO.builder()
                .pid("0000000000400001")
                .quantity(100)
                .unitPrice(BigDecimal.valueOf(4200))
                .totalPrice(BigDecimal.valueOf(420000))
                .build();
        List<SaleReturnSheetContentVO> saleReturnSheetContentVOS = new ArrayList<>();
        saleReturnSheetContentVOS.add(saleReturnSheetContentVO);
        SaleReturnSheetVO saleReturnSheetVO = SaleReturnSheetVO.builder()
                .id("XSTHD-20220524-00002")
                .supplier(2)
                .salesman("xiaoshoujingli")
                .state(SaleReturnSheetState.SUCCESS)
                .operator("xiaoshoujingli")
                .totalAmount(BigDecimal.valueOf(100000))
                .finalAmount(BigDecimal.valueOf(90000))
                .voucherAmount(BigDecimal.valueOf(0))
                .discount(BigDecimal.valueOf(0.9))
                .saleReturnSheetContent(saleReturnSheetContentVOS)
                .build();
        saleReturnService.redFlush(saleReturnSheetVO);
        SaleReturnSheetVO latest = saleReturnService.getLatest();
        Assertions.assertEquals(0,latest.getFinalAmount().compareTo(BigDecimal.valueOf(-90000)));
        Assertions.assertEquals(0,latest.getTotalAmount().compareTo(BigDecimal.valueOf(-100000)));
        SaleReturnSheetContentVO latestContent = saleReturnService.getSaleReturnSheetContentVOS(latest.getId()).get(0);
        Assertions.assertEquals(0,latestContent.getQuantity().compareTo(-100));
    }
}
