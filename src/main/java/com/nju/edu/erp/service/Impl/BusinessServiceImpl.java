package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.SaleReturnSheetDao;
import com.nju.edu.erp.dao.WarehouseDao;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.model.po.warehouse.WarehousePO;
import com.nju.edu.erp.model.vo.BusinessVO;
import com.nju.edu.erp.model.vo.SalarySheetVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnSheetContentVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnSheetVO;
import com.nju.edu.erp.service.*;
import com.nju.edu.erp.utils.Conversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.nju.edu.erp.utils.Conversion.*;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final SaleService saleService;

    private final SaleReturnSheetDao saleReturnSheetDao;

    private final SaleReturnService saleReturnService;

    private final SalarySheetService salarySheetService;

    private final WarehouseService warehouseService;

    private final WarehouseDao warehouseDao;

    @Autowired
    public BusinessServiceImpl(SaleService saleService, SaleReturnSheetDao saleReturnSheetDao, SaleReturnService saleReturnService, SalarySheetService salarySheetService, WarehouseService warehouseService, WarehouseDao warehouseDao) {
        this.saleService = saleService;
        this.saleReturnSheetDao = saleReturnSheetDao;
        this.saleReturnService = saleReturnService;
        this.salarySheetService = salarySheetService;
        this.warehouseService = warehouseService;
        this.warehouseDao = warehouseDao;
    }

    @Override
    public BusinessVO getMonthBusinessSheet(String yearAndMonth) {
        // 收入类
        BigDecimal rawSaleAmount = saleService.getRawTotalAmountByMonth(yearAndMonth);
        BigDecimal finalSaleAmount = saleService.getFinalTotalAmountByMonth(yearAndMonth);
        BigDecimal rawSaleReturnAmount = saleReturnService.getRawTotalAmountByMonth(yearAndMonth);
        BigDecimal finalSaleReturnAmount = saleReturnService.getFinalTotalAmountByMonth(yearAndMonth);
        BigDecimal rawIncome = rawSaleAmount.subtract(rawSaleReturnAmount);
        BigDecimal finalIncome = finalSaleAmount.subtract(finalSaleReturnAmount);
        BigDecimal allowance = rawIncome.subtract(finalIncome);
        // 支出类
        BusinessProcessFilterVO filterVO = BusinessProcessFilterVO.builder()
                .begin(getBeginDateOfMonth(yearAndMonth))
                .end(getEndDateOfMonth(yearAndMonth))
                .build();
        List<SaleSheetVO> saleSheetVOS = saleService.getBusinessProcess(filterVO);// 获取当月销售商品列表
        BigDecimal productTotalCost = BigDecimal.ZERO;
        for (SaleSheetVO saleSheetVO:saleSheetVOS){
            SaleReturnSheetVO saleReturnSheetVO = saleReturnService.getOneBySaleSheetId(saleSheetVO.getId());
            for (SaleSheetContentVO saleSheetContentVO:saleSheetVO.getSaleSheetContent()){
                String pid = saleSheetContentVO.getPid();
                Integer batchId = saleReturnSheetDao.findBatchId(saleSheetVO.getId(),pid);
                WarehousePO warehousePO = warehouseDao.findOneByPidAndBatchId(pid,batchId);//找到对应商品库存
                if (warehousePO==null){continue;}
                BigDecimal purchasePrice = warehousePO.getPurchasePrice();
                Integer quantity = saleSheetContentVO.getQuantity();
                // 查找并减去退货单部分
                if (saleReturnSheetVO!=null && saleReturnSheetVO.getState()== SaleReturnSheetState.SUCCESS){
                    for (SaleReturnSheetContentVO saleReturnSheetContentVO:saleReturnSheetVO.getSaleReturnSheetContent()){
                        if (saleReturnSheetContentVO.getPid().equals(pid)){
                            quantity = quantity-saleReturnSheetContentVO.getQuantity();
                        }
                    }
                }
                productTotalCost = productTotalCost.add(purchasePrice.multiply(BigDecimal.valueOf(quantity)));
            }
        }
        List<SalarySheetVO> salarySheetVOS = salarySheetService.getBusinessProcess(filterVO);
        // 计算应发工资
        BigDecimal shouldTotalPay = BigDecimal.ZERO;
        for (SalarySheetVO salarySheetVO:salarySheetVOS){
            shouldTotalPay = shouldTotalPay.add(salarySheetVO.getShouldPay());
        }
        BigDecimal cost = productTotalCost.add(shouldTotalPay);
        BigDecimal profits = finalIncome.subtract(cost);
        return BusinessVO.builder()
                .rawIncome(rawIncome)
                .finalIncome(finalIncome)
                .allowance(allowance)
                .productTotalCost(productTotalCost)
                .employeeTotalCost(shouldTotalPay)
                .cost(cost)
                .profits(profits)
                .build();
    }
    @Override
    public List<BusinessVO> getBusinessSheet(BusinessProcessFilterVO filterVO) {
        List<BusinessVO> businessVOS = new ArrayList<>();
        for (String yearAndMonth:Conversion.getYearAndMonths(filterVO)){
            businessVOS.add(getMonthBusinessSheet(yearAndMonth));
        }
        return businessVOS;
    }
}
