package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.TransferDao;
import com.nju.edu.erp.enums.sheetState.TransferSheetState;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.TransferPO;
import com.nju.edu.erp.model.vo.TransferVO;
import com.nju.edu.erp.service.TransferService;
import com.nju.edu.erp.utils.Conversion;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.nju.edu.erp.utils.Conversion.conversion;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferDao transferDao;

    @Autowired
    public TransferServiceImpl(TransferDao transferDao) {
        this.transferDao = transferDao;
    }


    @Override
    public List<TransferVO> getAllByState(TransferSheetState state) {
        List<TransferVO> transferVOS = new ArrayList<>();
        List<TransferPO> transferPOS = transferDao.getAllByState(state);
        for (TransferPO transferPO:transferPOS){
            transferVOS.add(conversion(transferPO));
        }
        return transferVOS;
    }

    @Override
    public List<TransferVO> getAll() {
        List<TransferVO> transferVOS = new ArrayList<>();
        List<TransferPO> transferPOS = transferDao.getAll();
        for (TransferPO transferPO:transferPOS){
            transferVOS.add(conversion(transferPO));
        }
        return transferVOS;
    }

    /**
     * 已完成一笔线下转账，更改单据状态
     */
    @Override
    public void updateStateToSuccess(String id) {
        TransferPO transferPO = transferDao.getById(id);
        if(transferPO.getState().equals(TransferSheetState.PENDING)) transferPO.setState(TransferSheetState.SUCCESS);
        else{throw new MyServiceException("A0003","状态更新失败");}
        transferDao.update(transferPO);
    }

    @Override
    public void create(TransferVO transferVO) {
        TransferPO transferPO = conversion(transferVO);
        TransferVO latest = getLatest();
        String id = IdGenerator.generateSheetId(latest==null?null: latest.getId(), "CWZZD");
        transferPO.setId(id);
        transferDao.create(transferPO);
    }

    @Override
    public void deleteById(String id) {
        transferDao.deleteById(id);
    }

    @Override
    public TransferVO getLatest(){
        return conversion(transferDao.getLatest());
    }
}
