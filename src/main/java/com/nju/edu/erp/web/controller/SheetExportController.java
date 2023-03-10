package com.nju.edu.erp.web.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.nju.edu.erp.model.vo.BusinessVO;
import com.nju.edu.erp.model.vo.SalarySheetVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.SaleDetailSheetVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.filter.SaleFilterVO;
import com.nju.edu.erp.model.vo.payable.PayableSheetVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetVO;
import com.nju.edu.erp.model.vo.purchaseReturns.PurchaseReturnsSheetVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnSheetVO;
import com.nju.edu.erp.service.*;
import com.nju.edu.erp.utils.Conversion;
import com.nju.edu.erp.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/excel_export")
public class SheetExportController {

    private final SaleService saleService;

    private final SaleReturnService saleReturnService;

    private final PurchaseService purchaseService;

    private final PurchaseReturnsService purchaseReturnsService;

    private final PayableService payableService;

    private final ReceiveService receiveService;

    private final SalarySheetService salarySheetService;

    private final BusinessService businessService;

    @Autowired
    public SheetExportController(SaleService saleService, SaleReturnService saleReturnService,PurchaseService purchaseService,
                                 PurchaseReturnsService purchaseReturnsService,PayableService payableService,ReceiveService receiveService,
                                 SalarySheetService salarySheetService,BusinessService businessService) {
        this.saleService = saleService;
        this.saleReturnService = saleReturnService;
        this.purchaseReturnsService = purchaseReturnsService;
        this.purchaseService = purchaseService;
        this.payableService = payableService;
        this.receiveService = receiveService;
        this.salarySheetService = salarySheetService;
        this.businessService = businessService;
    }

    /**
     * ?????????????????????
     */
    @PostMapping("/business_process")
    public void exportBusinessProcess(HttpServletResponse response,
                            @RequestBody BusinessProcessFilterVO filterVO) throws Exception {
        ExcelWriter excelWriter = EasyExcel.write(ExcelUtil.getOutputStream("???????????????",response))
                .excelType(ExcelTypeEnum.XLSX)
                .build();
        ExcelUtil.writeMultipleExcel(excelWriter,saleService.getBusinessProcess(filterVO),"?????????", SaleSheetVO.class);
        ExcelUtil.writeMultipleExcel(excelWriter,saleReturnService.getBusinessProcess(filterVO),"???????????????", SaleReturnSheetVO.class);
        ExcelUtil.writeMultipleExcel(excelWriter,purchaseService.getBusinessProcess(filterVO),"?????????", PurchaseSheetVO.class);
        ExcelUtil.writeMultipleExcel(excelWriter,purchaseReturnsService.getBusinessProcess(filterVO),"???????????????", PurchaseReturnsSheetVO.class);
        ExcelUtil.writeMultipleExcel(excelWriter,payableService.getBusinessProcess(filterVO),"?????????", PayableSheetVO.class);
        ExcelUtil.writeMultipleExcel(excelWriter,receiveService.getBusinessProcess(filterVO),"?????????", ReceiveSheetVO.class);
        ExcelUtil.writeMultipleExcel(excelWriter,salarySheetService.getBusinessProcess(filterVO),"?????????",SalarySheetVO.class);
        excelWriter.finish();
    }

    @PostMapping("/sale")
    public void exportSale(HttpServletResponse response,
                                      @RequestBody SaleFilterVO filterVO) throws Exception {
        ExcelWriter excelWriter = EasyExcel.write(ExcelUtil.getOutputStream("???????????????",response))
                .excelType(ExcelTypeEnum.XLSX)
                .build();
        ExcelUtil.writeMultipleExcel(excelWriter,saleService.getDetailSaleSheet(filterVO),"?????????", SaleDetailSheetVO.class);
        ExcelUtil.writeMultipleExcel(excelWriter,saleReturnService.getDetailSaleSheet(filterVO),"???????????????", SaleDetailSheetVO.class);
        excelWriter.finish();
    }

    @PostMapping("/business")
    public void exportBusiness(HttpServletResponse response,
                           @RequestBody BusinessProcessFilterVO filterVO) throws Exception {
        ExcelWriter excelWriter = EasyExcel.write(ExcelUtil.getOutputStream("???????????????",response))
                .excelType(ExcelTypeEnum.XLSX)
                .build();
        ExcelUtil.writeMultipleExcel(excelWriter,businessService.getBusinessSheet(filterVO),"?????????", BusinessVO.class);
        excelWriter.finish();
    }


    @GetMapping("/test")
    public void exportExcel(HttpServletResponse response) throws Exception {
        BusinessProcessFilterVO filterVO  = BusinessProcessFilterVO.builder().customerId(2).build();
        ExcelWriter excelWriter = EasyExcel.write(ExcelUtil.getOutputStream("???????????????",response))
                .excelType(ExcelTypeEnum.XLSX)
                .build();
        ExcelUtil.writeMultipleExcel(excelWriter,saleService.getBusinessProcess(filterVO),"?????????", SaleSheetVO.class);
        ExcelUtil.writeMultipleExcel(excelWriter,saleReturnService.getBusinessProcess(filterVO),"???????????????", SaleReturnSheetVO.class);
        excelWriter.finish();
    }

}
