package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.GiftSheetDao;
import com.nju.edu.erp.enums.sheetState.GiftSheetState;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.gift.GiftSheetContentPO;
import com.nju.edu.erp.model.po.gift.GiftSheetPO;
import com.nju.edu.erp.model.vo.gift.GiftSheetContentVO;
import com.nju.edu.erp.model.vo.gift.GiftSheetVO;
import com.nju.edu.erp.service.GiftSheetService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GiftSheetServiceImpl implements GiftSheetService {

    private final GiftSheetDao giftSheetDao;

    @Autowired
    public GiftSheetServiceImpl(GiftSheetDao giftSheetDao) {
        this.giftSheetDao = giftSheetDao;
    }

    @Override
    public void makeGiftSheet(GiftSheetVO giftSheetVO) {
        GiftSheetPO giftSheetPO = new GiftSheetPO();
        BeanUtils.copyProperties(giftSheetVO,giftSheetPO);
        giftSheetPO.setCreateTime(new Date());
        GiftSheetPO latest = giftSheetDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null?null : latest.getId(),"ZSD");
        giftSheetPO.setId(id);
        giftSheetPO.setState(GiftSheetState.PENDING);
        List<GiftSheetContentPO> giftSheetContentPOS = new ArrayList<>();
        for (GiftSheetContentVO giftSheetContentVO: giftSheetVO.getGiftSheetContentVOS()){
            GiftSheetContentPO giftSheetContentPO = new GiftSheetContentPO();
            BeanUtils.copyProperties(giftSheetContentVO,giftSheetContentPO);
            giftSheetContentPO.setGiftSheetId(id);
            giftSheetContentPOS.add(giftSheetContentPO);
        }
        giftSheetDao.create(giftSheetPO);
        giftSheetDao.createBatchSheetContent(giftSheetContentPOS);
    }

    @Override
    public List<GiftSheetVO> getGiftSheetByState(GiftSheetState giftSheetState) {
        List<GiftSheetPO> giftSheetPOS = giftSheetDao.getAllByState(giftSheetState);
        List<GiftSheetVO> giftSheetVOS = new ArrayList<>();
        for (GiftSheetPO giftSheetPO:giftSheetPOS){
            GiftSheetVO giftSheetVO = new GiftSheetVO();
            BeanUtils.copyProperties(giftSheetPO,giftSheetVO);
            List<GiftSheetContentPO> giftSheetContentPOS = giftSheetDao.getBatchSheetContent(giftSheetPO.getId());
            List<GiftSheetContentVO> giftSheetContentVOS = new ArrayList<>();
            for (GiftSheetContentPO giftSheetContentPO:giftSheetContentPOS){
                GiftSheetContentVO giftSheetContentVO = new GiftSheetContentVO();
                BeanUtils.copyProperties(giftSheetContentPO,giftSheetContentVO);
                giftSheetContentVOS.add(giftSheetContentVO);
            }
            giftSheetVO.setGiftSheetContentVOS(giftSheetContentVOS);
            giftSheetVOS.add(giftSheetVO);
        }
        return giftSheetVOS;
    }

    @Override
    public void approval(String saleSheetId, GiftSheetState state) {
        GiftSheetPO giftSheetPO = giftSheetDao.getOneBySaleSheetId(saleSheetId);
        String giftSheetId = giftSheetPO.getId();
        if (state.equals(GiftSheetState.FAILURE)){
            if (giftSheetPO.getState()==GiftSheetState.SUCCESS) throw new MyServiceException("A0003","更新参数错误");
            if (giftSheetDao.updateState(giftSheetId,state)==0)throw new MyServiceException("A0003","状态更新失败");
        }else {
            GiftSheetState preState;
            if (state.equals(GiftSheetState.SUCCESS)){
                preState = GiftSheetState.PENDING;
            }else {
                throw new MyServiceException("A0003","更新参数错误");
            }
            if (giftSheetDao.updateStateV2(giftSheetId,preState,state)==0) throw new MyServiceException("A0003","状态更新失败");
            //没有什么用，货物实际上随着销售单完成审批，就已经赠送出去了
        }
    }

    @Override
    public GiftSheetVO getLatest() {
        GiftSheetVO giftSheetVO = new GiftSheetVO();
        GiftSheetPO latest = giftSheetDao.getLatest();
        BeanUtils.copyProperties(latest,giftSheetVO);
        List<GiftSheetContentPO> giftSheetContentPOS = giftSheetDao.getBatchSheetContent(latest.getId());
        List<GiftSheetContentVO> giftSheetContentVOS = new ArrayList<>();
        for (GiftSheetContentPO giftSheetContentPO:giftSheetContentPOS){
            GiftSheetContentVO giftSheetContentVO = new GiftSheetContentVO();
            BeanUtils.copyProperties(giftSheetContentPO,giftSheetContentVO);
            giftSheetContentVOS.add(giftSheetContentVO);
        }
        giftSheetVO.setGiftSheetContentVOS(giftSheetContentVOS);
        return giftSheetVO;
    }
}
