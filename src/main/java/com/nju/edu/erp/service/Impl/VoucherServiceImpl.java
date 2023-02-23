package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.VoucherDao;
import com.nju.edu.erp.enums.VoucherType;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.voucher.VoucherLimitPO;
import com.nju.edu.erp.model.po.voucher.VoucherPO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.voucher.VoucherLimitVO;
import com.nju.edu.erp.model.vo.voucher.VoucherVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.GiftSheetService;
import com.nju.edu.erp.service.VoucherService;
import com.nju.edu.erp.utils.Conversion;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherDao voucherDao;

    private final CustomerService customerService;

    @Autowired
    public VoucherServiceImpl(VoucherDao voucherDao, GiftSheetService giftSheetService, CustomerService customerService) {
        this.voucherDao = voucherDao;
        this.customerService = customerService;
    }

    @Override
    public List<VoucherVO> getAllByType(VoucherType type) {
        List<VoucherPO> voucherPOS= voucherDao.getAllByType(type,new Date());
        List<VoucherVO> voucherVOS = new ArrayList<>();
        for (VoucherPO voucherPO:voucherPOS){
            VoucherVO voucherVO = new VoucherVO();
            BeanUtils.copyProperties(voucherPO,voucherVO);
            List<VoucherLimitPO> voucherLimitPOS = voucherDao.getLimitByVoucherId(voucherPO.getVoucherId());
            List<VoucherLimitVO> voucherLimitVOS = new ArrayList<>();
            for (VoucherLimitPO voucherLimitPO:voucherLimitPOS){
                VoucherLimitVO voucherLimitVO = new VoucherLimitVO();
                BeanUtils.copyProperties(voucherLimitPO,voucherLimitVO);
                voucherLimitVOS.add(voucherLimitVO);
            }
            voucherVO.setVoucherLimitVOS(voucherLimitVOS);
            voucherVOS.add(voucherVO);
        }
        return voucherVOS;
    }

    @Override
    public List<VoucherVO> getAvailableVoucher(SaleSheetVO saleSheetVO, Integer customerId, VoucherType type) {
        CustomerPO customerPO = customerService.findCustomerById(customerId);
        List<VoucherPO> candidateVoucherPOS = voucherDao.getAllByTypeAndCustomerLevel(type,customerPO.getLevel(),new Date());
        List<VoucherVO> voucherVOS = new ArrayList<>();
        List<SaleSheetContentVO> saleSheetContentVOS = saleSheetVO.getSaleSheetContent();
        for (VoucherPO candidateVoucherPO:candidateVoucherPOS){
            List<VoucherLimitPO> voucherLimitPOS = voucherDao.getLimitByVoucherId(candidateVoucherPO.getVoucherId());
            int item = 0;
            // 判断商品条件是否满足使用该优惠券条件
            for (VoucherLimitPO voucherLimitPO:voucherLimitPOS){
                for (SaleSheetContentVO saleSheetContentVO:saleSheetContentVOS){
                    if (voucherLimitPO.getPid().equals(saleSheetContentVO.getPid())&voucherLimitPO.getQuantity()<=saleSheetContentVO.getQuantity()){
                        item++;
                        break;
                    }
                }
            }
            if (item==voucherLimitPOS.size()){
                VoucherVO voucherVO = new VoucherVO();
                BeanUtils.copyProperties(candidateVoucherPO,voucherVO);
                List<VoucherLimitVO> voucherLimitVOS = new ArrayList<>();
                for (VoucherLimitPO voucherLimitPO:voucherLimitPOS){
                    VoucherLimitVO voucherLimitVO = new VoucherLimitVO();
                    BeanUtils.copyProperties(voucherLimitPO,voucherLimitVO);
                    voucherLimitVOS.add(voucherLimitVO);
                }
                voucherVO.setVoucherLimitVOS(voucherLimitVOS);
                voucherVOS.add(voucherVO);
            }
        }
        return voucherVOS;
    }

    @Override
    public void create(VoucherVO voucherVO) {
        VoucherPO voucherPO = new VoucherPO();
        BeanUtils.copyProperties(voucherVO,voucherPO);
        VoucherPO latest = voucherDao.getLatest();
        String voucherId = IdGenerator.generateSheetId(latest == null ? null : latest.getVoucherId(), "DJQ");
        voucherPO.setVoucherId(voucherId);
        voucherPO.setCreateTime(new Date());

        List<VoucherLimitPO> voucherLimitPOS = new ArrayList<>();
        if (voucherVO.getVoucherLimitVOS()!=null) {
            for (VoucherLimitVO voucherLimitVO : voucherVO.getVoucherLimitVOS()) {
                VoucherLimitPO voucherLimitPO = new VoucherLimitPO();
                BeanUtils.copyProperties(voucherLimitVO, voucherLimitPO);
                voucherLimitPO.setVoucherId(voucherId);
                voucherLimitPOS.add(voucherLimitPO);
            }
        }

        if (voucherDao.create(voucherPO)==0) throw new MyServiceException("A0002","创建优惠券失败");
        if (voucherLimitPOS.size()>0){voucherDao.createVoucherLimit(voucherLimitPOS);}
    }

    @Override
    public void delete(String voucherId) {
        if (voucherDao.delete(voucherId)==0) throw new MyServiceException("A0004","删除优惠券失败");
    }

    @Override
    public VoucherVO getLatest() {
        return Conversion.conversion(voucherDao.getLatest());
    }

    @Override
    public VoucherVO getOneById(String voucherId) {
        return Conversion.conversion(voucherDao.getOneById(voucherId));
    }

}
