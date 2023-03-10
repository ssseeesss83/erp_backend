package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.dao.SaleReturnSheetDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.dao.WarehouseDao;
import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.po.sale.SaleSheetContentPO;
import com.nju.edu.erp.model.po.sale.SaleSheetPO;
import com.nju.edu.erp.model.po.saleReturn.SaleReturnSheetContentPO;
import com.nju.edu.erp.model.po.saleReturn.SaleReturnSheetPO;
import com.nju.edu.erp.model.po.warehouse.WarehousePO;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.SaleDetailSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.filter.SaleFilterVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnSheetContentVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnSheetVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.ProductService;
import com.nju.edu.erp.service.SaleReturnService;
import com.nju.edu.erp.service.WarehouseService;
import com.nju.edu.erp.utils.Conversion;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author 201250208
 */
@Service
public class SaleReturnServiceImpl implements SaleReturnService {

    SaleReturnSheetDao saleReturnSheetDao;

    ProductService productService;

    ProductDao productDao;

    SaleSheetDao saleSheetDao;

    CustomerService customerService;

    WarehouseService warehouseService;

    WarehouseDao warehouseDao;

    @Autowired
    public SaleReturnServiceImpl(SaleReturnSheetDao saleReturnSheetDao, ProductService productService, ProductDao productDao, SaleSheetDao saleSheetDao, CustomerService customerService, WarehouseService warehouseService, WarehouseDao warehouseDao) {
        this.saleReturnSheetDao = saleReturnSheetDao;
        this.productService = productService;
        this.productDao = productDao;
        this.saleSheetDao = saleSheetDao;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
        this.warehouseDao = warehouseDao;
    }

    /**
     * ?????????????????????
     * @param saleReturnSheetVO ???????????????
     */
    @Override
    @Transactional
    public void makeSaleReturnSheet(UserVO userVO, SaleReturnSheetVO saleReturnSheetVO) {
        SaleReturnSheetPO saleReturnSheetPO = new SaleReturnSheetPO();
        BeanUtils.copyProperties(saleReturnSheetVO,saleReturnSheetPO);
        //??????????????????????????????????????????????????????
        SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleReturnSheetPO.getSaleSheetId());
        saleReturnSheetPO.setDiscount(saleSheetPO.getDiscount());
        saleReturnSheetPO.setSupplier(saleSheetPO.getSupplier());
        //????????????????????????????????????
        saleReturnSheetPO.setOperator(userVO.getName());
        saleReturnSheetPO.setCreateTime(new Date());
        SaleReturnSheetPO latest = saleReturnSheetDao.getLatest();
        String id = IdGenerator.generateSheetId(latest==null?null: latest.getId(), "XSTHD");
        saleReturnSheetPO.setId(id);
        saleReturnSheetPO.setState(SaleReturnSheetState.PENDING_LEVEL_1);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SaleSheetContentPO> saleSheetContent = saleSheetDao.findContentBySheetId(saleReturnSheetPO.getSaleSheetId());
        Map<String,SaleSheetContentPO> map = new HashMap<>();
        for(SaleSheetContentPO item:saleSheetContent){
            map.put(item.getPid(),item);//???????????????Pid??????????????????????????????
        }
        List<SaleReturnSheetContentPO> sContentPOList = new ArrayList<>();
        for (SaleReturnSheetContentVO content:saleReturnSheetVO.getSaleReturnSheetContent()){
            SaleReturnSheetContentPO sContentPO = new SaleReturnSheetContentPO();
            BeanUtils.copyProperties(content,sContentPO);
            sContentPO.setSaleReturnsSheetId(id);
            SaleSheetContentPO item = map.get(sContentPO.getPid());
            sContentPO.setUnitPrice(item.getUnitPrice());
            BigDecimal unitPrice = sContentPO.getUnitPrice();
            //?????????????????????????????????
            if (sContentPO.getQuantity()>item.getQuantity()) throw new RuntimeException("?????????????????????????????????");
            sContentPO.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(sContentPO.getQuantity())));
            sContentPOList.add(sContentPO);
            totalAmount = totalAmount.add(sContentPO.getTotalPrice());
        }
        saleReturnSheetPO.setTotalAmount(totalAmount);
        if (totalAmount.equals(BigDecimal.ZERO)){
            saleReturnSheetPO.setVoucherAmount(BigDecimal.ZERO);
            saleReturnSheetPO.setFinalAmount(BigDecimal.ZERO);
        }else {
            BigDecimal ratioVoucher = saleSheetPO.getVoucherAmount().multiply(totalAmount.divide(saleSheetPO.getRawTotalAmount(),2 ,RoundingMode.HALF_UP));
            saleReturnSheetPO.setVoucherAmount(ratioVoucher);
            saleReturnSheetPO.setFinalAmount(totalAmount.multiply(saleSheetPO.getDiscount()).subtract(ratioVoucher));
        }
        saleReturnSheetDao.saveBatch(sContentPOList);
        saleReturnSheetDao.save(saleReturnSheetPO);
    }

    @Override
    public List<SaleReturnSheetVO> getSaleReturnSheetByState(SaleReturnSheetState state) {
        List<SaleReturnSheetVO> res = new ArrayList<>();
        List<SaleReturnSheetPO> all;
        if(state==null){
            all = saleReturnSheetDao.findAll();
        }else {
            all = saleReturnSheetDao.findAllByState(state);
        }
        for (SaleReturnSheetPO po:all){
            SaleReturnSheetVO vo = new SaleReturnSheetVO();
            BeanUtils.copyProperties(po,vo);
            List<SaleReturnSheetContentPO> alll = saleReturnSheetDao.findContentSaleReturnsSheetId(po.getId());
            List<SaleReturnSheetContentVO> vos = new ArrayList<>();
            for (SaleReturnSheetContentPO p:alll){
                SaleReturnSheetContentVO v = new SaleReturnSheetContentVO();
                BeanUtils.copyProperties(p,v);
                vos.add(v);
            }
            vo.setSaleReturnSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    /**
     *
     * @param saleReturnSheetId XS?????????id
     * @param state XS???????????????????????????
     */
    @Override
    public void approval(String saleReturnSheetId, SaleReturnSheetState state) {
        SaleReturnSheetPO saleReturnSheet = saleReturnSheetDao.findSheetById(saleReturnSheetId);
        if (state.equals(SaleReturnSheetState.FAILURE)){
            if (saleReturnSheet.getState() == SaleReturnSheetState.SUCCESS) throw new RuntimeException("??????????????????");
            int effectLines = saleReturnSheetDao.updateState(saleReturnSheetId,state);
            if (effectLines==0) throw new RuntimeException("??????????????????");
        }else{
            SaleReturnSheetState preState;
            if(state.equals(SaleReturnSheetState.SUCCESS)){
                preState = SaleReturnSheetState.PENDING_LEVEL_2;
            }else if(state.equals(SaleReturnSheetState.PENDING_LEVEL_2)){
                preState = SaleReturnSheetState.PENDING_LEVEL_1;
            }else {
                throw new MyServiceException("A0003","??????????????????");
            }
            int effectLines = saleReturnSheetDao.updateStateV2(saleReturnSheetId,preState,state);
            if (effectLines==0) throw new RuntimeException("??????????????????");
            if (state.equals(SaleReturnSheetState.SUCCESS)) {
                // ????????????, ?????????????????????(???????????????????????????????????????)
                SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleReturnSheet.getSaleSheetId());
                SaleReturnSheetPO saleReturnSheetPO = saleReturnSheetDao.findSheetById(saleReturnSheetId);
                //???????????????-?????????-??????????????????-????????????????????????????????????????????????????????????????????????????????????
                // -??????????????????-??????payable????????????
                //???????????????-?????????--??????????????????
                List<SaleReturnSheetContentPO> contents = saleReturnSheetDao.findContentSaleReturnsSheetId(saleReturnSheetId);
                for (SaleReturnSheetContentPO content:contents){
                    String pid = content.getPid();
                    Integer quantity = content.getQuantity();
                    //????????????????????????????????????
//                    Integer saleQuantity = saleContents???????????????????????????
                    Integer batchId = saleReturnSheetDao.findBatchId(saleSheetPO.getId(),pid);
                    WarehousePO warehousePO = warehouseDao.findOneByPidAndBatchId(pid,batchId);//????????????????????????
                    if (warehousePO == null) throw new RuntimeException("??????????????????????????????????????????");
                    warehousePO.setQuantity(quantity);
                    warehouseDao.addQuantity(warehousePO);
                    ProductInfoVO productInfoVO = productService.getOneProductByPid(pid);
                    productInfoVO.setQuantity(productInfoVO.getQuantity()+quantity);
                    productService.updateProduct(productInfoVO);
                    //TODO: ????????????warehouseOutput
                }
                Integer supplier = saleSheetPO.getSupplier();
                CustomerPO customer = customerService.findCustomerById(supplier);
                //??????????????????????????????
                customer.setReceivable(customer.getReceivable().add(saleReturnSheetPO.getFinalAmount()));
                customerService.updateCustomer(customer);
            }
        }
    }
    /**
     * ?????????????????????
     * @param saleFilterVO ????????????
     * @return
     */
    @Override
    public List<SaleDetailSheetVO> getDetailSaleSheet(SaleFilterVO saleFilterVO) {
        return saleReturnSheetDao.getDetailSaleSheet(saleFilterVO);
    }

    /**
     * ???????????????????????????????????????
     */
    @Override
    public BigDecimal getSaleManTotalAmount(String employeeName,String yearAndMonth) {
        List<SaleReturnSheetPO> sheetPOS = saleReturnSheetDao.findAllSheetByStateAndSaleMan(employeeName,SaleReturnSheetState.SUCCESS,
                Conversion.getBeginDateOfMonth(yearAndMonth),Conversion.getEndDateOfMonth(yearAndMonth));
        BigDecimal totalSaleAmount = BigDecimal.ZERO;
        for (SaleReturnSheetPO sheetPO:sheetPOS){
            totalSaleAmount = totalSaleAmount.add(sheetPO.getTotalAmount());//????????????????????????
        }
        return totalSaleAmount;
    }

    @Override
    public BigDecimal getFinalTotalAmountByMonth(String yearAndMonth) {
        List<SaleReturnSheetPO> sheetPOS = saleReturnSheetDao.getAllSheetByYearAndMonth(SaleReturnSheetState.SUCCESS,
                Conversion.getBeginDateOfMonth(yearAndMonth),Conversion.getEndDateOfMonth(yearAndMonth));
        BigDecimal totalSaleAmount = BigDecimal.ZERO;
        for (SaleReturnSheetPO sheetPO:sheetPOS){
            totalSaleAmount = totalSaleAmount.add(sheetPO.getFinalAmount());//????????????????????????
        }
        return totalSaleAmount;
    }

    @Override
    public BigDecimal getRawTotalAmountByMonth(String yearAndMonth) {
        List<SaleReturnSheetPO> sheetPOS = saleReturnSheetDao.getAllSheetByYearAndMonth(SaleReturnSheetState.SUCCESS,
                Conversion.getBeginDateOfMonth(yearAndMonth),Conversion.getEndDateOfMonth(yearAndMonth));
        BigDecimal totalSaleAmount = BigDecimal.ZERO;
        for (SaleReturnSheetPO sheetPO:sheetPOS){
            totalSaleAmount = totalSaleAmount.add(sheetPO.getTotalAmount());//????????????????????????
        }
        return totalSaleAmount;
    }

    @Override
    public List<SaleReturnSheetVO> getBusinessProcess(BusinessProcessFilterVO filterVO) {
        List<SaleReturnSheetVO> res = new ArrayList<>();
        List<SaleReturnSheetPO> all = saleReturnSheetDao.getBusinessProcess(filterVO);
        for (SaleReturnSheetPO po:all){
            SaleReturnSheetVO vo = new SaleReturnSheetVO();
            BeanUtils.copyProperties(po,vo);
            List<SaleReturnSheetContentPO> alll = saleReturnSheetDao.findContentSaleReturnsSheetId(po.getId());
            List<SaleReturnSheetContentVO> vos = new ArrayList<>();
            for (SaleReturnSheetContentPO p:alll){
                SaleReturnSheetContentVO v = new SaleReturnSheetContentVO();
                BeanUtils.copyProperties(p,v);
                vos.add(v);
            }
            vo.setSaleReturnSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    @Override
    public SaleReturnSheetVO getOneBySaleSheetId(String saleSheetId) {
        SaleReturnSheetPO po = saleReturnSheetDao.getOneBySaleSheetId(saleSheetId);
        SaleReturnSheetVO vo = new SaleReturnSheetVO();
        if (po!=null) {
            BeanUtils.copyProperties(po, vo);
            List<SaleReturnSheetContentPO> alll = saleReturnSheetDao.findContentSaleReturnsSheetId(po.getId());
            List<SaleReturnSheetContentVO> vos = new ArrayList<>();
            for (SaleReturnSheetContentPO p : alll) {
                SaleReturnSheetContentVO v = new SaleReturnSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setSaleReturnSheetContent(vos);
            return vo;
        }
        return null;
    }

    @Override
    public List<SaleReturnSheetContentVO> getSaleReturnSheetContentVOS(String sheetId){
        List<SaleReturnSheetContentPO> saleReturnSheetContentPOS = saleReturnSheetDao.findContentSaleReturnsSheetId(sheetId);
        List<SaleReturnSheetContentVO> saleReturnSheetContentVOS = new ArrayList<>();
        for(SaleReturnSheetContentPO po:saleReturnSheetContentPOS){
            SaleReturnSheetContentVO vo = SaleReturnSheetContentVO.builder()
                    .pid(po.getPid())
                    .quantity(po.getQuantity())
                    .unitPrice(po.getUnitPrice())
                    .totalPrice(po.getTotalPrice())
                    .remark(po.getRemark())
                    .build();
            saleReturnSheetContentVOS.add(vo);
        }
        return saleReturnSheetContentVOS;
    }
    @Override
    public SaleReturnSheetVO getLatest() {
        SaleReturnSheetPO saleReturnSheetPO = saleReturnSheetDao.getLatest();
        SaleReturnSheetVO saleReturnSheetVO = new SaleReturnSheetVO();
        BeanUtils.copyProperties(saleReturnSheetPO,saleReturnSheetVO);
        List<SaleReturnSheetContentVO> saleReturnSheetContentVOS = new ArrayList<>();
        for (SaleReturnSheetContentPO sContentPO:saleReturnSheetDao.findContentSaleReturnsSheetId(saleReturnSheetVO.getId())){
            SaleReturnSheetContentVO saleReturnSheetContentVO = new SaleReturnSheetContentVO();
            BeanUtils.copyProperties(sContentPO,saleReturnSheetContentVO);
        }
        saleReturnSheetVO.setSaleReturnSheetContent(saleReturnSheetContentVOS);
        return saleReturnSheetVO;
    }

    @Override
    public void redFlush(SaleReturnSheetVO saleReturnSheetVO){
        if(saleReturnSheetVO.getState()!= SaleReturnSheetState.SUCCESS) throw new MyServiceException("A005","???????????????");
        SaleReturnSheetPO saleReturnSheetPO = new SaleReturnSheetPO();
        BeanUtils.copyProperties(saleReturnSheetVO,saleReturnSheetPO);
        saleReturnSheetPO.setFinalAmount(saleReturnSheetPO.getFinalAmount().negate());
        saleReturnSheetPO.setTotalAmount(saleReturnSheetPO.getTotalAmount().negate());
        saleReturnSheetPO.setCreateTime(new Date());
        SaleReturnSheetPO latest = saleReturnSheetDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSTHD");
        saleReturnSheetPO.setId(id);
        saleReturnSheetPO.setState(SaleReturnSheetState.SUCCESS);
        List<SaleReturnSheetContentPO> sContentPOList = new ArrayList<>();
        for(SaleReturnSheetContentVO content : saleReturnSheetVO.getSaleReturnSheetContent()){
            SaleReturnSheetContentPO sContentPO = new SaleReturnSheetContentPO();
            BeanUtils.copyProperties(content,sContentPO);
            sContentPO.setSaleReturnsSheetId(id);
            sContentPO.setQuantity(-sContentPO.getQuantity());
            sContentPO.setTotalPrice(sContentPO.getTotalPrice().negate());
            sContentPOList.add(sContentPO);
        }
        saleReturnSheetDao.saveBatch(sContentPOList);
        saleReturnSheetDao.save(saleReturnSheetPO);
    }

    @Override
    public SaleReturnSheetVO redFlushCopy(SaleReturnSheetVO saleReturnSheetVO){
        redFlush(saleReturnSheetVO);
        SaleReturnSheetVO res = new SaleReturnSheetVO();
        BeanUtils.copyProperties(saleReturnSheetVO,res);
        return res;
    }

    @Override
    public void copyIn(SaleReturnSheetVO saleReturnSheetVO){
        SaleReturnSheetPO saleReturnSheetPO = new SaleReturnSheetPO();
        BeanUtils.copyProperties(saleReturnSheetVO,saleReturnSheetPO);
        saleReturnSheetPO.setCreateTime(new Date());
        SaleReturnSheetPO latest = saleReturnSheetDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSTHD");
        saleReturnSheetPO.setId(id);
        List<SaleReturnSheetContentPO> sContentPOList = new ArrayList<>();
        for(SaleReturnSheetContentVO content : saleReturnSheetVO.getSaleReturnSheetContent()){
            SaleReturnSheetContentPO sContentPO = new SaleReturnSheetContentPO();
            BeanUtils.copyProperties(content,sContentPO);
            sContentPO.setSaleReturnsSheetId(id);
            sContentPOList.add(sContentPO);
        }
        saleReturnSheetDao.saveBatch(sContentPOList);
        saleReturnSheetDao.save(saleReturnSheetPO);
    }
}
