package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.enums.VoucherType;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.po.sale.SaleSheetContentPO;
import com.nju.edu.erp.model.po.sale.SaleSheetPO;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.SaleDetailSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.filter.SaleFilterVO;
import com.nju.edu.erp.model.vo.gift.GiftSheetContentVO;
import com.nju.edu.erp.model.vo.gift.GiftSheetVO;
import com.nju.edu.erp.model.vo.voucher.VoucherLimitVO;
import com.nju.edu.erp.model.vo.voucher.VoucherVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormVO;
import com.nju.edu.erp.service.*;
import com.nju.edu.erp.utils.Conversion;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleSheetDao saleSheetDao;

    private final ProductDao productDao;

    private final SaleReturnService saleReturnService;

    private final ProductService productService;

    private final CustomerService customerService;

    private final WarehouseService warehouseService;

    private final GiftSheetService giftSheetService;

    private final VoucherService voucherService;

    @Autowired
    public SaleServiceImpl(SaleSheetDao saleSheetDao, ProductDao productDao, SaleReturnService saleReturnService, ProductService productService, CustomerService customerService, WarehouseService warehouseService, GiftSheetService giftSheetService, VoucherService voucherService) {
        this.saleSheetDao = saleSheetDao;
        this.productDao = productDao;
        this.saleReturnService = saleReturnService;
        this.productService = productService;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
        this.giftSheetService = giftSheetService;
        this.voucherService = voucherService;
    }

    @Override
    @Transactional
    public void makeSaleSheet(UserVO userVO, SaleSheetVO saleSheetVO, String voucherId) {
        SaleSheetPO saleSheetPO = new SaleSheetPO();
        BeanUtils.copyProperties(saleSheetVO,saleSheetPO);
        saleSheetPO.setOperator(userVO.getName());
        saleSheetPO.setCreateTime(new Date());
        SaleSheetPO latest = saleSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSD");
        saleSheetPO.setId(id);
        saleSheetPO.setState(SaleSheetState.PENDING_LEVEL_1);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SaleSheetContentPO> sContentPOList = new ArrayList<>();
        for(SaleSheetContentVO content : saleSheetVO.getSaleSheetContent()){
            SaleSheetContentPO sContentPO = new SaleSheetContentPO();
            BeanUtils.copyProperties(content,sContentPO);
            sContentPO.setSaleSheetId(id);
            BigDecimal unitPrice = sContentPO.getUnitPrice();
            if(unitPrice == null){
                ProductPO product = productDao.findById(content.getPid());
                unitPrice = product.getPurchasePrice();
                sContentPO.setUnitPrice(unitPrice);
            }
            sContentPO.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(sContentPO.getQuantity())));
            sContentPOList.add(sContentPO);
            totalAmount = totalAmount.add(sContentPO.getTotalPrice());
        }
        saleSheetDao.saveBatchSheetContent(sContentPOList);
        saleSheetPO.setRawTotalAmount(totalAmount);
        if (totalAmount.equals(BigDecimal.ZERO)){
            saleSheetPO.setFinalAmount(BigDecimal.ZERO);
        }else {
            saleSheetPO.setFinalAmount(saleSheetPO.getRawTotalAmount().multiply(saleSheetPO.getDiscount()).subtract(saleSheetPO.getVoucherAmount()));
        }
        saleSheetDao.saveSheet(saleSheetPO);
        // ???????????????
        VoucherVO voucherVO = voucherService.getOneById(voucherId);
        if (voucherVO!=null && voucherVO.getVoucherType().equals(VoucherType.GIFT)){
            GiftSheetVO giftSheetVO = GiftSheetVO.builder()
                    .saleSheetId(saleSheetPO.getId())
                    .customerId(saleSheetPO.getSupplier())
                    .createTime(new Date())
                    .operator(saleSheetPO.getOperator())
                    .build();
            List<GiftSheetContentVO> giftSheetContentVOS = new ArrayList<>();
            for (VoucherLimitVO voucherLimitVO:voucherVO.getVoucherLimitVOS()){
                GiftSheetContentVO giftSheetContentVO = new GiftSheetContentVO();
                BeanUtils.copyProperties(voucherLimitVO,giftSheetContentVO);
                giftSheetContentVOS.add(giftSheetContentVO);
            }
            GiftSheetVO giftLatest = giftSheetService.getLatest();
            String giftId = IdGenerator.generateSheetId(giftLatest == null ? null : giftLatest.getId(), "XSD");

            giftSheetVO.setGiftSheetContentVOS(giftSheetContentVOS);
            giftSheetVO.setId(giftId);
            giftSheetService.makeGiftSheet(giftSheetVO);
        }
    }

    @Override
    @Transactional
    public List<SaleSheetVO> getSaleSheetByState(SaleSheetState state) {
        List<SaleSheetVO> res = new ArrayList<>();
        List<SaleSheetPO> all;
        if(state == null){
            all = saleSheetDao.findAllSheet();
        }
        else{
            all = saleSheetDao.findAllSheetByState(state);
        }
        for(SaleSheetPO po:all){
            SaleSheetVO vo = new SaleSheetVO();
            BeanUtils.copyProperties(po,vo);
            List<SaleSheetContentPO> alll = saleSheetDao.findContentBySheetId(po.getId());
            List<SaleSheetContentVO> vos = new ArrayList<>();
            for(SaleSheetContentPO p : alll){
                SaleSheetContentVO v = new SaleSheetContentVO();
                BeanUtils.copyProperties(p,v);
                vos.add(v);
            }
            vo.setSaleSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    /**
     * ???????????????id????????????(state == "???????????????"/"????????????"/"????????????")
     * ???controller?????????????????????
     *
     * @param saleSheetId ?????????id
     * @param state       ???????????????????????????
     */
    @Override
    @Transactional
    public void approval(String saleSheetId, SaleSheetState state) {
        if(state.equals(SaleSheetState.FAILURE)) {
            SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
            if(saleSheet.getState() == SaleSheetState.SUCCESS) throw new RuntimeException("??????????????????");
            int effectLines = saleSheetDao.updateSheetState(saleSheetId, state);
            if(effectLines == 0) throw new RuntimeException("??????????????????");
        } else {
            SaleSheetState prevState;
            if(state.equals(SaleSheetState.SUCCESS)) {
                prevState = SaleSheetState.PENDING_LEVEL_2;
            } else if(state.equals(SaleSheetState.PENDING_LEVEL_2)) {
                prevState = SaleSheetState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("??????????????????");
            }
            int effectLines = saleSheetDao.updateSheetStateOnPrev(saleSheetId, prevState, state);
            if(effectLines == 0) throw new RuntimeException("??????????????????");
            if(state.equals(SaleSheetState.SUCCESS)) {
                // ????????????, ?????????????????????
                // ??????????????????????????????
                // ??????saleSheetId???????????????content -> ????????????id?????????
                // ????????????id?????????????????????????????????recentPp
                List<SaleSheetContentPO> saleSheetContent =  saleSheetDao.findContentBySheetId(saleSheetId);
                List<WarehouseOutputFormContentVO> warehouseOutputFormContentVOS = new ArrayList<>();

                for(SaleSheetContentPO content : saleSheetContent) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    productInfoVO.setId(content.getPid());
                    productInfoVO.setRecentPp(content.getUnitPrice());
                    productService.updateProduct(productInfoVO);

                    WarehouseOutputFormContentVO wiContentVO = new WarehouseOutputFormContentVO();
                    wiContentVO.setSalePrice(content.getUnitPrice());
                    wiContentVO.setQuantity(content.getQuantity());
                    wiContentVO.setRemark(content.getRemark());
                    wiContentVO.setPid(content.getPid());
                    warehouseOutputFormContentVOS.add(wiContentVO);
                }
                // ???????????????(??????????????????)
                // ???????????? payable
                SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
                CustomerPO customerPO = customerService.findCustomerById(saleSheet.getSupplier());
                customerPO.setPayable(customerPO.getPayable().add(saleSheet.getFinalAmount()));
//                customerPO.setReceivable(customerPO.getReceivable().add(saleSheet.getFinalAmount()));
                customerService.updateCustomer(customerPO);
                // ?????????????????????(????????????????????????)
                // ??????????????????????????????
                WarehouseOutputFormVO warehouseOutputFormVO = new WarehouseOutputFormVO();
                warehouseOutputFormVO.setOperator(null); // ?????????????????????(??????????????????????????????)
                warehouseOutputFormVO.setSaleSheetId(saleSheetId);
                warehouseOutputFormVO.setList(warehouseOutputFormContentVOS);
                warehouseService.productOutOfWarehouse(warehouseOutputFormVO);
            }
        }
        // ?????????service???dao???????????????????????????????????????????????????????????????
        /* ??????????????????
            1. ????????????????????????????????????
                 1. ??????????????????
                 2. ???????????????
                 3. ???????????????
                 4. ??????????????????
            2. ?????????????????????????????????????????????????????? ????????????????????????????????????????????????
         */
    }

    /**
     * ?????????????????????????????????????????????????????????????????????(?????????????????????,??????????????????????????????,????????????????????????????????????????????????)
     * @param salesman ?????????????????????
     * @param beginDateStr ?????????????????????
     * @param endDateStr ?????????????????????
     * @return
     */
    public CustomerPurchaseAmountPO getMaxAmountCustomerOfSalesmanByTime(String salesman,String beginDateStr,String endDateStr){
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return null;
            }else{
                return saleSheetDao.getMaxAmountCustomerOfSalesmanByTime(salesman,beginTime,endTime);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ???????????????Id?????????????????????
     * @param saleSheetId ?????????Id
     * @return ?????????????????????
     */
    @Override
    public SaleSheetVO getSaleSheetById(String saleSheetId) {
        SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleSheetId);
        if(saleSheetPO == null) return null;
        List<SaleSheetContentPO> contentPO = saleSheetDao.findContentBySheetId(saleSheetId);
        SaleSheetVO sVO = new SaleSheetVO();
        BeanUtils.copyProperties(saleSheetPO, sVO);
        List<SaleSheetContentVO> saleSheetContentVOList = new ArrayList<>();
        for (SaleSheetContentPO content:
                contentPO) {
            SaleSheetContentVO sContentVO = new SaleSheetContentVO();
            BeanUtils.copyProperties(content, sContentVO);
            saleSheetContentVOList.add(sContentVO);
        }
        sVO.setSaleSheetContent(saleSheetContentVOList);
        return sVO;
    }
    /**
     * ?????????????????????
     * @param saleFilterVO ????????????
     * @return
     */
    @Override
    public List<SaleDetailSheetVO> getDetailSaleSheet(SaleFilterVO saleFilterVO) {
        List<SaleDetailSheetVO> saleDetailSheetVOS = saleSheetDao.getDetailSaleSheet(saleFilterVO);
        saleDetailSheetVOS.addAll(saleReturnService.getDetailSaleSheet(saleFilterVO));
        return saleDetailSheetVOS;
    }

    @Override
    public BigDecimal getSaleManTotalAmount(String employeeName, String yearAndMonth) {
        List<SaleSheetPO> saleSheetPOS = saleSheetDao.findAllSheetByStateAndSaleMan(employeeName,SaleSheetState.SUCCESS,
                Conversion.getBeginDateOfMonth(yearAndMonth),Conversion.getEndDateOfMonth(yearAndMonth));
        BigDecimal totalSaleAmount = BigDecimal.ZERO;
        for (SaleSheetPO sheetPO:saleSheetPOS){
            totalSaleAmount = totalSaleAmount.add(sheetPO.getRawTotalAmount());//????????????????????????
        }
        return totalSaleAmount;
    }


    @Override
    public BigDecimal getRawTotalAmountByMonth(String yearAndMonth) {
        List<SaleSheetPO> saleSheetPOS = saleSheetDao.getAllSheetByYearAndMonth(SaleSheetState.SUCCESS,
                Conversion.getBeginDateOfMonth(yearAndMonth),Conversion.getEndDateOfMonth(yearAndMonth));
        BigDecimal totalSaleAmount = BigDecimal.ZERO;
        for (SaleSheetPO sheetPO:saleSheetPOS){
            totalSaleAmount = totalSaleAmount.add(sheetPO.getRawTotalAmount());//????????????????????????
        }
        return totalSaleAmount;
    }

    @Override
    public BigDecimal getFinalTotalAmountByMonth(String yearAndMonth) {
        List<SaleSheetPO> saleSheetPOS = saleSheetDao.getAllSheetByYearAndMonth(SaleSheetState.SUCCESS,
                Conversion.getBeginDateOfMonth(yearAndMonth),Conversion.getEndDateOfMonth(yearAndMonth));
        BigDecimal totalSaleAmount = BigDecimal.ZERO;
        for (SaleSheetPO sheetPO:saleSheetPOS){
            totalSaleAmount = totalSaleAmount.add(sheetPO.getFinalAmount());//????????????????????????
        }
        return totalSaleAmount;
    }

    @Override
    public List<SaleSheetVO> getBusinessProcess(BusinessProcessFilterVO filterVO) {
        List<SaleSheetVO> res = new ArrayList<>();
        List<SaleSheetPO> all = saleSheetDao.getBusinessProcess(filterVO);
        for(SaleSheetPO po:all){
            SaleSheetVO vo = new SaleSheetVO();
            BeanUtils.copyProperties(po,vo);
            List<SaleSheetContentPO> alll = saleSheetDao.findContentBySheetId(po.getId());
            List<SaleSheetContentVO> vos = new ArrayList<>();
            for(SaleSheetContentPO p : alll){
                SaleSheetContentVO v = new SaleSheetContentVO();
                BeanUtils.copyProperties(p,v);
                vos.add(v);
            }
            vo.setSaleSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    @Override
    public List<SaleSheetContentVO> getSaleSheetContentVOS(String sheetId){
        List<SaleSheetContentPO> saleSheetContentPOS = saleSheetDao.findContentBySheetId(sheetId);
        List<SaleSheetContentVO> saleSheetContentVOS = new ArrayList<>();
        for(SaleSheetContentPO po:saleSheetContentPOS){
            SaleSheetContentVO vo = SaleSheetContentVO.builder()
                    .pid(po.getPid())
                    .quantity(po.getQuantity())
                    .unitPrice(po.getUnitPrice())
                    .totalPrice(po.getTotalPrice())
                    .remark(po.getRemark())
                    .build();
            saleSheetContentVOS.add(vo);
        }
        return saleSheetContentVOS;
    }
    @Override
    public SaleSheetVO getLatest() {
        SaleSheetPO saleSheetPO = saleSheetDao.getLatestSheet();
        SaleSheetVO saleSheetVO = new SaleSheetVO();
        BeanUtils.copyProperties(saleSheetPO,saleSheetVO);
        List<SaleSheetContentVO> saleSheetContentVOS = new ArrayList<>();
        for (SaleSheetContentPO sContentPO:saleSheetDao.findContentBySheetId(saleSheetVO.getId())){
            SaleSheetContentVO saleSheetContentVO = new SaleSheetContentVO();
            BeanUtils.copyProperties(sContentPO,saleSheetContentVO);
        }
        saleSheetVO.setSaleSheetContent(saleSheetContentVOS);
        return saleSheetVO;
    }
    @Override
    public void redFlush(SaleSheetVO saleSheetVO){
        if(saleSheetVO.getState()!= SaleSheetState.SUCCESS) throw new MyServiceException("A005","???????????????");
        SaleSheetPO saleSheetPO = new SaleSheetPO();
        BeanUtils.copyProperties(saleSheetVO,saleSheetPO);
        saleSheetPO.setFinalAmount(saleSheetPO.getFinalAmount().negate());
        saleSheetPO.setRawTotalAmount(saleSheetPO.getRawTotalAmount().negate());
        saleSheetPO.setCreateTime(new Date());
        SaleSheetPO latest = saleSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSD");
        saleSheetPO.setId(id);
        saleSheetPO.setState(SaleSheetState.SUCCESS);
        List<SaleSheetContentPO> sContentPOList = new ArrayList<>();
        for(SaleSheetContentVO content : saleSheetVO.getSaleSheetContent()){
            SaleSheetContentPO sContentPO = new SaleSheetContentPO();
            BeanUtils.copyProperties(content,sContentPO);
            sContentPO.setSaleSheetId(id);
            sContentPO.setQuantity(-sContentPO.getQuantity());
            sContentPO.setTotalPrice(sContentPO.getTotalPrice().negate());
            sContentPOList.add(sContentPO);
        }
        saleSheetDao.saveBatchSheetContent(sContentPOList);
        saleSheetDao.saveSheet(saleSheetPO);
    }

    //
    @Override
    public SaleSheetVO redFlushCopy(SaleSheetVO saleSheetVO){
        redFlush(saleSheetVO);
        SaleSheetVO res = new SaleSheetVO();
        BeanUtils.copyProperties(saleSheetVO,res);
        return res;
    }

    @Override
    public void copyIn(SaleSheetVO saleSheetVO){
        SaleSheetPO saleSheetPO = new SaleSheetPO();
        BeanUtils.copyProperties(saleSheetVO,saleSheetPO);
        saleSheetPO.setCreateTime(new Date());
        SaleSheetPO latest = saleSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSD");
        saleSheetPO.setId(id);
        List<SaleSheetContentPO> sContentPOList = new ArrayList<>();
        for(SaleSheetContentVO content : saleSheetVO.getSaleSheetContent()){
            SaleSheetContentPO sContentPO = new SaleSheetContentPO();
            BeanUtils.copyProperties(content,sContentPO);
            sContentPO.setSaleSheetId(id);
            sContentPOList.add(sContentPO);
        }
        saleSheetDao.saveBatchSheetContent(sContentPOList);
        saleSheetDao.saveSheet(saleSheetPO);
    }
}
