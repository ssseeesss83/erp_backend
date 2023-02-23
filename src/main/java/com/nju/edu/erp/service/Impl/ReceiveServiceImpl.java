package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.ReceiveSheetDao;
import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetContentPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetPO;
import com.nju.edu.erp.model.vo.AccountVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetContentVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetVO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.ReceiveService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReceiveServiceImpl implements ReceiveService {

    private final ReceiveSheetDao receiveSheetDao;

    private final AccountService accountService;

    private final CustomerService customerService;

    @Autowired
    public ReceiveServiceImpl(ReceiveSheetDao receiveSheetDao, AccountService accountService, CustomerService customerService) {
        this.receiveSheetDao = receiveSheetDao;
        this.accountService = accountService;
        this.customerService = customerService;
    }


    @Override
    public void makeReceiveSheet(UserVO userVO, ReceiveSheetVO receiveSheetVO) {
        ReceiveSheetPO receiveSheetPO = new ReceiveSheetPO();
        BeanUtils.copyProperties(receiveSheetVO,receiveSheetPO);
        receiveSheetPO.setOperator(userVO.getName());
        receiveSheetPO.setCreateTime(new Date());
        ReceiveSheetPO latest = receiveSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null?null: latest.getId(), "SKD");
        receiveSheetPO.setId(id);
        receiveSheetPO.setState(ReceiveSheetState.PENDING);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<ReceiveSheetContentPO> receiveSheetContentPOS = new ArrayList<>();
        for (ReceiveSheetContentVO content:receiveSheetVO.getReceiveSheetContentVOS()){
            ReceiveSheetContentPO sContentPOs = new ReceiveSheetContentPO();
            BeanUtils.copyProperties(content,sContentPOs);
            sContentPOs.setReceiveSheetId(receiveSheetPO.getId());
            receiveSheetContentPOS.add(sContentPOs);
            totalAmount = totalAmount.add(sContentPOs.getTransferAmount());
        }
        receiveSheetPO.setTotalAmount(totalAmount);
        receiveSheetDao.saveBatchSheetContent(receiveSheetContentPOS);
        receiveSheetDao.saveSheet(receiveSheetPO);
    }

    @Override
    public List<ReceiveSheetVO> getReceiveSheetByState(ReceiveSheetState receiveSheetState) {
        List<ReceiveSheetVO> receiveSheetVOS = new ArrayList<>();
        for (ReceiveSheetPO receiveSheetPO:receiveSheetDao.findAllSheetByState(receiveSheetState)){
            ReceiveSheetVO receiveSheetVO = new ReceiveSheetVO();
            BeanUtils.copyProperties(receiveSheetPO,receiveSheetVO);
            List<ReceiveSheetContentVO> receiveSheetContentVOS = new ArrayList<>();
            for (ReceiveSheetContentPO rContentPO:receiveSheetDao.findContentBySheetId(receiveSheetVO.getId())){
                ReceiveSheetContentVO receiveSheetContentVO = new ReceiveSheetContentVO();
                BeanUtils.copyProperties(rContentPO,receiveSheetContentVO);
                receiveSheetContentVOS.add(receiveSheetContentVO);
            }
            receiveSheetVO.setReceiveSheetContentVOS(receiveSheetContentVOS);
            receiveSheetVOS.add(receiveSheetVO);
        }
        return receiveSheetVOS;
    }

    @Override
    public ReceiveSheetVO getLatest() {
        ReceiveSheetPO receiveSheetPO = receiveSheetDao.getLatestSheet();
        ReceiveSheetVO receiveSheetVO = new ReceiveSheetVO();
        BeanUtils.copyProperties(receiveSheetPO,receiveSheetVO);
        List<ReceiveSheetContentVO> receiveSheetContentVOS = new ArrayList<>();
        for (ReceiveSheetContentPO rContentPO:receiveSheetDao.findContentBySheetId(receiveSheetVO.getId())){
            ReceiveSheetContentVO receiveSheetContentVO = new ReceiveSheetContentVO();
            BeanUtils.copyProperties(rContentPO,receiveSheetContentVO);
        }
        receiveSheetVO.setReceiveSheetContentVOS(receiveSheetContentVOS);
        return receiveSheetVO;
    }

    @Override
    public void approval(String receiveSheetId, ReceiveSheetState state) {
        if(state.equals(ReceiveSheetState.FAILURE)) {
            ReceiveSheetPO receiveSheetPO = receiveSheetDao.findSheetById(receiveSheetId);
            if(receiveSheetPO.getState() == ReceiveSheetState.SUCCESS) throw new MyServiceException("A0003","更新参数错误");
            int effectLines = receiveSheetDao.updateSheetState(receiveSheetId, state);
            if(effectLines == 0) throw new MyServiceException("A0003","状态更新失败");
        } else {
            ReceiveSheetState preState;
            if (state.equals(ReceiveSheetState.SUCCESS)){
                preState = ReceiveSheetState.PENDING;
            }else {
                throw new MyServiceException("A0003","更新参数错误");
            }
            int effectLines = receiveSheetDao.updateSheetStateOnPrev(receiveSheetId,preState,state);
            if(effectLines == 0) throw new MyServiceException("A0003","状态更新失败");

            ReceiveSheetPO receiveSheetPO = receiveSheetDao.findSheetById(receiveSheetId);
            CustomerPO customerPO = customerService.findCustomerById(receiveSheetPO.getCustomer());
            BigDecimal receivable = customerPO.getReceivable();
            receivable = receivable.subtract(receiveSheetPO.getTotalAmount());
            customerPO.setReceivable(receivable);
            customerService.updateCustomer(customerPO);

            for (ReceiveSheetContentPO receiveSheetContentPO:receiveSheetDao.findContentBySheetId(receiveSheetId)){
                AccountVO accountVO = accountService.findByName(receiveSheetContentPO.getName());
                BigDecimal balance = accountVO.getBalance();
                balance = balance.add(receiveSheetContentPO.getTransferAmount());
                accountVO.setBalance(balance);
                accountService.updateAccount(accountVO);
            }

        }
    }

    @Override
    public List<ReceiveSheetContentVO> getReceiveSheetContentVOS(String sheetId){
        List<ReceiveSheetContentPO> receiveSheetContentPOS = receiveSheetDao.findContentBySheetId(sheetId);
        List<ReceiveSheetContentVO> receiveSheetContentVOS = new ArrayList<>();
        for(ReceiveSheetContentPO po:receiveSheetContentPOS){
            ReceiveSheetContentVO vo = ReceiveSheetContentVO.builder()
                    .name(po.getName())
                    .transferAmount(po.getTransferAmount())
                    .remark(po.getRemark())
                    .build();
            receiveSheetContentVOS.add(vo);
        }
        return receiveSheetContentVOS;
    }

    @Override
    public List<ReceiveSheetVO> getBusinessProcess(BusinessProcessFilterVO filterVO) {
        List<ReceiveSheetVO> res = new ArrayList<>();
        List<ReceiveSheetPO> all = receiveSheetDao.getBusinessProcess(filterVO);
        for(ReceiveSheetPO po:all){
            ReceiveSheetVO vo = new ReceiveSheetVO();
            BeanUtils.copyProperties(po,vo);
            List<ReceiveSheetContentPO> alll = receiveSheetDao.findContentBySheetId(po.getId());
            List<ReceiveSheetContentVO> vos = new ArrayList<>();
            for(ReceiveSheetContentPO p : alll){
                ReceiveSheetContentVO v = new ReceiveSheetContentVO();
                BeanUtils.copyProperties(p,v);
                vos.add(v);
            }
            vo.setReceiveSheetContentVOS(vos);
            res.add(vo);
        }
        return res;
    }

    @Override
    public void redFlush(ReceiveSheetVO receiveSheetVO){
        if(receiveSheetVO.getState()!= ReceiveSheetState.SUCCESS) throw new MyServiceException("A005","状态未完成");
        ReceiveSheetPO receiveSheetPO = ReceiveSheetPO.builder()
                .operator(receiveSheetVO.getOperator())
                .customer(receiveSheetVO.getCustomer())
                .totalAmount(receiveSheetVO.getTotalAmount().negate())
                .state(ReceiveSheetState.SUCCESS)
                .createTime(new Date())
                .build();
        ReceiveSheetPO latest = receiveSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null?null: latest.getId(), "SKD");
        receiveSheetPO.setId(id);
        List<ReceiveSheetContentPO> receiveSheetContentPOS = new ArrayList<>();
        for (ReceiveSheetContentVO content:receiveSheetVO.getReceiveSheetContentVOS()){
            ReceiveSheetContentPO sContentPOs = new ReceiveSheetContentPO();
            BeanUtils.copyProperties(content,sContentPOs);
            sContentPOs.setReceiveSheetId(receiveSheetPO.getId());
            sContentPOs.setTransferAmount(sContentPOs.getTransferAmount().negate());
            receiveSheetContentPOS.add(sContentPOs);
        }
        receiveSheetDao.saveBatchSheetContent(receiveSheetContentPOS);
        receiveSheetDao.saveSheet(receiveSheetPO);
    }

    @Override
    public ReceiveSheetVO redFlushCopy(ReceiveSheetVO receiveSheetVO){
        redFlush(receiveSheetVO);
        ReceiveSheetVO res = new ReceiveSheetVO();
        BeanUtils.copyProperties(receiveSheetVO,res);
        return res;
    }

    @Override
    public void copyIn(ReceiveSheetVO receiveSheetVO){
        ReceiveSheetPO receiveSheetPO = ReceiveSheetPO.builder()
                .operator(receiveSheetVO.getOperator())
                .customer(receiveSheetVO.getCustomer())
                .totalAmount(receiveSheetVO.getTotalAmount().negate())
                .state(receiveSheetVO.getState())
                .createTime(new Date())
                .build();
        ReceiveSheetPO latest = receiveSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null?null: latest.getId(), "FKD");
        receiveSheetPO.setId(id);
        List<ReceiveSheetContentPO> receiveSheetContentPOS = new ArrayList<>();
        for (ReceiveSheetContentVO content:receiveSheetVO.getReceiveSheetContentVOS()){
            ReceiveSheetContentPO sContentPOs = new ReceiveSheetContentPO();
            BeanUtils.copyProperties(content,sContentPOs);
            sContentPOs.setReceiveSheetId(receiveSheetPO.getId());
            receiveSheetContentPOS.add(sContentPOs);
        }
        receiveSheetDao.saveBatchSheetContent(receiveSheetContentPOS);
        receiveSheetDao.saveSheet(receiveSheetPO);
    }
}
