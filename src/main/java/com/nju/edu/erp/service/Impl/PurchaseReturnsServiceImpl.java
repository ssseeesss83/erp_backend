package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.dao.PurchaseReturnsSheetDao;
import com.nju.edu.erp.dao.PurchaseSheetDao;
import com.nju.edu.erp.dao.WarehouseDao;
import com.nju.edu.erp.enums.sheetState.PurchaseReturnsSheetState;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.po.purchase.PurchaseSheetContentPO;
import com.nju.edu.erp.model.po.purchase.PurchaseSheetPO;
import com.nju.edu.erp.model.po.purchaseReturn.PurchaseReturnsSheetContentPO;
import com.nju.edu.erp.model.po.purchaseReturn.PurchaseReturnsSheetPO;
import com.nju.edu.erp.model.po.warehouse.WarehousePO;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.purchaseReturns.PurchaseReturnsSheetContentVO;
import com.nju.edu.erp.model.vo.purchaseReturns.PurchaseReturnsSheetVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.ProductService;
import com.nju.edu.erp.service.PurchaseReturnsService;
import com.nju.edu.erp.service.WarehouseService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PurchaseReturnsServiceImpl implements PurchaseReturnsService {

    PurchaseReturnsSheetDao purchaseReturnsSheetDao;

    ProductService productService;

    ProductDao productDao;

    PurchaseSheetDao purchaseSheetDao;

    CustomerService customerService;

    WarehouseService warehouseService;

    WarehouseDao warehouseDao;

    @Autowired
    public PurchaseReturnsServiceImpl(PurchaseReturnsSheetDao purchaseReturnsSheetDao, ProductService productService, CustomerService customerService, WarehouseService warehouseService, ProductDao productDao,PurchaseSheetDao purchaseSheetDao,WarehouseDao warehouseDao) {
        this.purchaseReturnsSheetDao = purchaseReturnsSheetDao;
        this.productService = productService;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
        this.productDao = productDao;
        this.purchaseSheetDao = purchaseSheetDao;
        this.warehouseDao =  warehouseDao;
    }

    /**
     * ?????????????????????
     *
     * @param purchaseReturnsSheetVO ???????????????
     */
    @Override
    @Transactional
    public void makePurchaseReturnsSheet(UserVO userVO, PurchaseReturnsSheetVO purchaseReturnsSheetVO) {
        PurchaseReturnsSheetPO purchaseReturnsSheetPO = new PurchaseReturnsSheetPO();
        BeanUtils.copyProperties(purchaseReturnsSheetVO, purchaseReturnsSheetPO);
        // ?????????????????????????????????????????????
        purchaseReturnsSheetPO.setOperator(userVO.getName());
        purchaseReturnsSheetPO.setCreateTime(new Date());
        PurchaseReturnsSheetPO latest = purchaseReturnsSheetDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "JHTHD");
        purchaseReturnsSheetPO.setId(id);
        purchaseReturnsSheetPO.setState(PurchaseReturnsSheetState.PENDING_LEVEL_1);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<PurchaseSheetContentPO> purchaseSheetContent = purchaseSheetDao.findContentByPurchaseSheetId(purchaseReturnsSheetPO.getPurchaseSheetId());
        Map<String, PurchaseSheetContentPO> map = new HashMap<>();
        for(PurchaseSheetContentPO item : purchaseSheetContent) {
            map.put(item.getPid(), item);
        }
        List<PurchaseReturnsSheetContentPO> pContentPOList = new ArrayList<>();
        for(PurchaseReturnsSheetContentVO content : purchaseReturnsSheetVO.getPurchaseReturnsSheetContent()) {
            PurchaseReturnsSheetContentPO pContentPO = new PurchaseReturnsSheetContentPO();
            BeanUtils.copyProperties(content,pContentPO);
            pContentPO.setPurchaseReturnsSheetId(id);
            PurchaseSheetContentPO item = map.get(pContentPO.getPid());
            pContentPO.setUnitPrice(item.getUnitPrice());

            BigDecimal unitPrice = pContentPO.getUnitPrice();
            pContentPO.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(pContentPO.getQuantity())));
            pContentPOList.add(pContentPO);
            totalAmount = totalAmount.add(pContentPO.getTotalPrice());
        }
        purchaseReturnsSheetDao.saveBatch(pContentPOList);
        purchaseReturnsSheetPO.setTotalAmount(totalAmount);
        purchaseReturnsSheetDao.save(purchaseReturnsSheetPO);
    }

    /**
     * ?????????????????????????????????[?????????content??????](state == null ??????????????????????????????)
     *
     * @param state ?????????????????????
     * @return ???????????????
     */
    @Override
    public List<PurchaseReturnsSheetVO> getPurchaseReturnsSheetByState(PurchaseReturnsSheetState state) {
        List<PurchaseReturnsSheetVO> res = new ArrayList<>();
        List<PurchaseReturnsSheetPO> all;
        if(state == null) {
            all = purchaseReturnsSheetDao.findAll();
        } else {
            all = purchaseReturnsSheetDao.findAllByState(state);
        }
        for(PurchaseReturnsSheetPO po: all) {
            PurchaseReturnsSheetVO vo = new PurchaseReturnsSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<PurchaseReturnsSheetContentPO> alll = purchaseReturnsSheetDao.findContentByPurchaseReturnsSheetId(po.getId());
            List<PurchaseReturnsSheetContentVO> vos = new ArrayList<>();
            for (PurchaseReturnsSheetContentPO p : alll) {
                PurchaseReturnsSheetContentVO v = new PurchaseReturnsSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setPurchaseReturnsSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    /**
     * ?????????????????????id????????????(state == "???????????????"/"????????????"/"????????????")
     * ???controller?????????????????????
     *
     * @param purchaseReturnsSheetId ???????????????id
     * @param state           ?????????????????????????????????
     */
    @Override
    @Transactional
    public void approval(String purchaseReturnsSheetId, PurchaseReturnsSheetState state) {
        PurchaseReturnsSheetPO purchaseReturnsSheet = purchaseReturnsSheetDao.findOneById(purchaseReturnsSheetId);
        if(state.equals(PurchaseReturnsSheetState.FAILURE)) {
            if(purchaseReturnsSheet.getState() == PurchaseReturnsSheetState.SUCCESS) throw new RuntimeException("??????????????????");
            int effectLines = purchaseReturnsSheetDao.updateState(purchaseReturnsSheetId, state);
            if(effectLines == 0) throw new RuntimeException("??????????????????");
        } else {
            PurchaseReturnsSheetState prevState;
            if(state.equals(PurchaseReturnsSheetState.SUCCESS)) {
                prevState = PurchaseReturnsSheetState.PENDING_LEVEL_2;
            } else if(state.equals(PurchaseReturnsSheetState.PENDING_LEVEL_2)) {
                prevState = PurchaseReturnsSheetState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("??????????????????");
            }
            int effectLines = purchaseReturnsSheetDao.updateStateV2(purchaseReturnsSheetId, prevState, state);
            if(effectLines == 0) throw new RuntimeException("??????????????????");
            if(state.equals(PurchaseReturnsSheetState.SUCCESS)) {
                // ????????????, ?????????????????????
                // ???????????????id??? ??????????????????id ???   ???????????????id->?????????id->?????????id->??????id???
                Integer batchId = purchaseReturnsSheetDao.findBatchId(purchaseReturnsSheetId);

                //- ???????????????id-pid??? quantity ?????????id+pid -> ??????????????????????????????->????????????quantity???
                //- ??? pid -> ?????????????????????->??????????????*quantity=???????????????->??????payable????????????????????????
                List<PurchaseReturnsSheetContentPO> contents = purchaseReturnsSheetDao.findContentByPurchaseReturnsSheetId(purchaseReturnsSheetId);
                BigDecimal payableToDeduct = BigDecimal.ZERO;
                for (PurchaseReturnsSheetContentPO content:
                        contents) {
                    String pid = content.getPid();
                    Integer quantity = content.getQuantity();
                    WarehousePO warehousePO = warehouseDao.findOneByPidAndBatchId(pid, batchId);
                    if(warehousePO == null) throw new RuntimeException("??????????????????????????????????????????");
                    if(warehousePO.getQuantity() >= quantity) {
                        warehousePO.setQuantity(quantity);
                        warehouseDao.deductQuantity(warehousePO);
                        ProductInfoVO productInfoVO = productService.getOneProductByPid(pid);
                        productInfoVO.setQuantity(productInfoVO.getQuantity()-quantity);
                        productService.updateProduct(productInfoVO);
                        payableToDeduct = payableToDeduct.add(content.getUnitPrice().multiply(BigDecimal.valueOf(quantity))) ;
                        //TODO: ????????????warehouseInput
                    } else {
                        purchaseReturnsSheetDao.updateState(purchaseReturnsSheetId, PurchaseReturnsSheetState.FAILURE);
                        throw new RuntimeException("????????????????????????????????????");
                    }
                }

                PurchaseSheetPO purchaseSheetPO = purchaseSheetDao.findOneById(purchaseReturnsSheet.getPurchaseSheetId());
                Integer supplier = purchaseSheetPO.getSupplier();
                CustomerPO customer = customerService.findCustomerById(supplier);

                customer.setPayable(customer.getPayable().subtract(payableToDeduct));
                customerService.updateCustomer(customer);
            }
        }
    }
    @Override
    public PurchaseReturnsSheetVO getLatest() {
        PurchaseReturnsSheetPO purchaseReturnsSheetPO = purchaseReturnsSheetDao.getLatest();
        PurchaseReturnsSheetVO purchaseReturnsSheetVO = new PurchaseReturnsSheetVO();
        BeanUtils.copyProperties(purchaseReturnsSheetPO,purchaseReturnsSheetVO);
        List<PurchaseReturnsSheetContentVO> purchaseReturnsSheetContentVOS = new ArrayList<>();
        for (PurchaseReturnsSheetContentPO sContentPO:purchaseReturnsSheetDao.findContentByPurchaseReturnsSheetId(purchaseReturnsSheetVO.getId())){
            PurchaseReturnsSheetContentVO purchaseReturnsSheetContentVO = new PurchaseReturnsSheetContentVO();
            BeanUtils.copyProperties(sContentPO,purchaseReturnsSheetContentVO);
        }
        purchaseReturnsSheetVO.setPurchaseReturnsSheetContent(purchaseReturnsSheetContentVOS);
        return purchaseReturnsSheetVO;
    }
    @Override
    public List<PurchaseReturnsSheetVO> getBusinessProcess(BusinessProcessFilterVO filterVO) {
        List<PurchaseReturnsSheetVO> res = new ArrayList<>();
        List<PurchaseReturnsSheetPO> all = purchaseReturnsSheetDao.getBusinessProcess(filterVO);
        for(PurchaseReturnsSheetPO po:all){
            PurchaseReturnsSheetVO vo = new PurchaseReturnsSheetVO();
            BeanUtils.copyProperties(po,vo);
            List<PurchaseReturnsSheetContentPO> alll = purchaseReturnsSheetDao.findContentByPurchaseReturnsSheetId(po.getId());
            List<PurchaseReturnsSheetContentVO> vos = new ArrayList<>();
            for(PurchaseReturnsSheetContentPO p : alll){
                PurchaseReturnsSheetContentVO v = new PurchaseReturnsSheetContentVO();
                BeanUtils.copyProperties(p,v);
                vos.add(v);
            }
            vo.setPurchaseReturnsSheetContent(vos);
            res.add(vo);
        }
        return res;
    }
    @Override
    public void redFlush(PurchaseReturnsSheetVO purchaseReturnsSheetVO){
        if(purchaseReturnsSheetVO.getState()!= PurchaseReturnsSheetState.SUCCESS) throw new MyServiceException("A005","???????????????");
        PurchaseReturnsSheetPO purchaseReturnsSheetPO = new PurchaseReturnsSheetPO();
        BeanUtils.copyProperties(purchaseReturnsSheetVO,purchaseReturnsSheetPO);
        purchaseReturnsSheetPO.setTotalAmount(purchaseReturnsSheetPO.getTotalAmount().negate());
        purchaseReturnsSheetPO.setCreateTime(new Date());
        PurchaseReturnsSheetPO latest = purchaseReturnsSheetDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSD");
        purchaseReturnsSheetPO.setId(id);
        purchaseReturnsSheetPO.setState(PurchaseReturnsSheetState.SUCCESS);
        List<PurchaseReturnsSheetContentPO> sContentPOList = new ArrayList<>();
        for(PurchaseReturnsSheetContentVO content : purchaseReturnsSheetVO.getPurchaseReturnsSheetContent()){
            PurchaseReturnsSheetContentPO sContentPO = new PurchaseReturnsSheetContentPO();
            BeanUtils.copyProperties(content,sContentPO);
            sContentPO.setPurchaseReturnsSheetId(id);
            sContentPO.setQuantity(-sContentPO.getQuantity());
            sContentPO.setTotalPrice(sContentPO.getTotalPrice().negate());
            sContentPOList.add(sContentPO);
        }
        purchaseReturnsSheetDao.saveBatch(sContentPOList);
        purchaseReturnsSheetDao.save(purchaseReturnsSheetPO);
    }

    @Override
    public PurchaseReturnsSheetVO redFlushCopy(PurchaseReturnsSheetVO purchaseReturnsSheetVO){
        redFlush(purchaseReturnsSheetVO);
        PurchaseReturnsSheetVO res = new PurchaseReturnsSheetVO();
        BeanUtils.copyProperties(purchaseReturnsSheetVO,res);
        return res;
    }

    @Override
    public void copyIn(PurchaseReturnsSheetVO purchaseReturnsSheetVO){
        PurchaseReturnsSheetPO purchaseReturnsSheetPO = new PurchaseReturnsSheetPO();
        BeanUtils.copyProperties(purchaseReturnsSheetVO,purchaseReturnsSheetPO);
        purchaseReturnsSheetPO.setTotalAmount(purchaseReturnsSheetPO.getTotalAmount().negate());
        purchaseReturnsSheetPO.setCreateTime(new Date());
        PurchaseReturnsSheetPO latest = purchaseReturnsSheetDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSD");
        purchaseReturnsSheetPO.setId(id);
        List<PurchaseReturnsSheetContentPO> sContentPOList = new ArrayList<>();
        for(PurchaseReturnsSheetContentVO content : purchaseReturnsSheetVO.getPurchaseReturnsSheetContent()){
            PurchaseReturnsSheetContentPO sContentPO = new PurchaseReturnsSheetContentPO();
            BeanUtils.copyProperties(content,sContentPO);
            sContentPO.setPurchaseReturnsSheetId(id);
            sContentPOList.add(sContentPO);
        }
        purchaseReturnsSheetDao.saveBatch(sContentPOList);
        purchaseReturnsSheetDao.save(purchaseReturnsSheetPO);
    }
}