package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.DiscountDao;
import com.nju.edu.erp.model.po.DiscountPO;
import com.nju.edu.erp.model.vo.DiscountVO;
import com.nju.edu.erp.service.DiscountService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountDao discountDao;

    @Autowired
    public DiscountServiceImpl(DiscountDao discountDao) {
        this.discountDao = discountDao;
    }

    @Override
    public void create(DiscountVO discountVO) {
        DiscountPO discountPO = new DiscountPO();
        BeanUtils.copyProperties(discountVO,discountPO);
        DiscountPO latest = discountDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null?null: latest.getDiscountId(), "ZK");
        discountPO.setDiscountId(id);
        discountDao.create(discountPO);
    }

    @Override
    public void delete(String discountId) {
        discountDao.delete(discountId);
    }

    @Override
    public List<DiscountVO> getAllByLevel(Integer level) {
        List<DiscountPO> discountPOS = discountDao.getAvailableByLevel(level,new Date());
        List<DiscountVO> discountVOS = new ArrayList<>();
        for (DiscountPO discountPO:discountPOS){
            DiscountVO discountVO = new DiscountVO();
            BeanUtils.copyProperties(discountPO,discountVO);
            discountVOS.add(discountVO);
        }
        return discountVOS;
    }

    @Override
    public List<DiscountVO> getAll() {
        List<DiscountPO> discountPOS = discountDao.getAll(new Date());
        List<DiscountVO> discountVOS = new ArrayList<>();
        for (DiscountPO discountPO:discountPOS){
            DiscountVO discountVO = new DiscountVO();
            BeanUtils.copyProperties(discountPO,discountVO);
            discountVOS.add(discountVO);
        }
        return discountVOS;
    }
}
