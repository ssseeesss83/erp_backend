package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.enums.sheetState.TransferSheetState;
import com.nju.edu.erp.model.vo.*;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetContentVO;
import com.nju.edu.erp.service.PayableService;
import com.nju.edu.erp.dao.PayableSheetDao;
import com.nju.edu.erp.enums.sheetState.PayableSheetState;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.payable.PayableSheetContentPO;
import com.nju.edu.erp.model.po.payable.PayableSheetPO;
import com.nju.edu.erp.model.vo.payable.PayableSheetContentVO;
import com.nju.edu.erp.model.vo.payable.PayableSheetVO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.TransferService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PayableServiceImpl implements PayableService {
    private final PayableSheetDao payableSheetDao;

    private final AccountService accountService;

    private final CustomerService customerService;

    private final TransferService transferService;

    @Autowired
    public PayableServiceImpl(PayableSheetDao payableSheetDao, AccountService accountService, CustomerService customerService, TransferService transferService) {
        this.payableSheetDao = payableSheetDao;
        this.accountService = accountService;
        this.customerService = customerService;
        this.transferService = transferService;
    }


    @Override
    public void makePayableSheet(UserVO userVO, PayableSheetVO payableSheetVO) {
        PayableSheetPO payableSheetPO = new PayableSheetPO();
        BeanUtils.copyProperties(payableSheetVO,payableSheetPO);
        payableSheetPO.setOperator(userVO.getName());
        payableSheetPO.setCreateTime(new Date());
        PayableSheetPO latest = payableSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null?null: latest.getId(), "FKD");
        payableSheetPO.setId(id);
        payableSheetPO.setState(PayableSheetState.PENDING);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<PayableSheetContentPO> payableSheetContentPOS = new ArrayList<>();
        for (PayableSheetContentVO content:payableSheetVO.getPayableSheetContentVOS()){
            PayableSheetContentPO sContentPOs = new PayableSheetContentPO();
            BeanUtils.copyProperties(content,sContentPOs);
            sContentPOs.setPayableSheetId(payableSheetPO.getId());
            payableSheetContentPOS.add(sContentPOs);
            totalAmount = totalAmount.add(sContentPOs.getTransferAmount());
        }
        payableSheetPO.setTotalAmount(totalAmount);
        payableSheetDao.saveBatchSheetContent(payableSheetContentPOS);
        payableSheetDao.saveSheet(payableSheetPO);
    }

    @Override
    public List<PayableSheetVO> getPayableSheetByState(PayableSheetState payableSheetState) {
        List<PayableSheetVO> payableSheetVOS = new ArrayList<>();
        for (PayableSheetPO payableSheetPO:payableSheetDao.findAllSheetByState(payableSheetState)){
            PayableSheetVO payableSheetVO = new PayableSheetVO();
            BeanUtils.copyProperties(payableSheetPO,payableSheetVO);
            List<PayableSheetContentVO> payableSheetContentVOS = new ArrayList<>();
            for (PayableSheetContentPO rContentPO:payableSheetDao.findContentBySheetId(payableSheetVO.getId())){
                PayableSheetContentVO payableSheetContentVO = new PayableSheetContentVO();
                BeanUtils.copyProperties(rContentPO,payableSheetContentVO);
                payableSheetContentVOS.add(payableSheetContentVO);
            }
            payableSheetVO.setPayableSheetContentVOS(payableSheetContentVOS);
            payableSheetVOS.add(payableSheetVO);
        }
        return payableSheetVOS;
    }

    @Override
    public void approval(String payableSheetId, PayableSheetState state) {
        if(state.equals(PayableSheetState.FAILURE)) {
            PayableSheetPO payableSheetPO = payableSheetDao.findSheetById(payableSheetId);
            if(payableSheetPO.getState() == PayableSheetState.SUCCESS) throw new MyServiceException("A0003","更新参数错误");
            int effectLines = payableSheetDao.updateSheetState(payableSheetId, state);
            if(effectLines == 0) throw new MyServiceException("A0003","状态更新失败");
        } else {
            PayableSheetState preState;
            if (state.equals(PayableSheetState.SUCCESS)){
                preState = PayableSheetState.PENDING;
            }else {
                throw new MyServiceException("A0003","更新参数错误");
            }
            int effectLines = payableSheetDao.updateSheetStateOnPrev(payableSheetId,preState,state);
            if(effectLines == 0) throw new MyServiceException("A0003","状态更新失败");

            PayableSheetPO payableSheetPO = payableSheetDao.findSheetById(payableSheetId);
            CustomerPO customerPO = customerService.findCustomerById(payableSheetPO.getCustomer());
            BigDecimal payable= customerPO.getPayable();
            payable = payable.subtract(payableSheetPO.getTotalAmount());
            customerPO.setPayable(payable);
            customerService.updateCustomer(customerPO);

            for (PayableSheetContentPO payableSheetContentPO:payableSheetDao.findContentBySheetId(payableSheetId)) {
                AccountVO accountVO = accountService.findByName(payableSheetContentPO.getName());
                BigDecimal balance = accountVO.getBalance();
                balance = balance.subtract(payableSheetContentPO.getTransferAmount());
                accountVO.setBalance(balance);
                accountService.updateAccount(accountVO);

                //新增转账通知
                TransferVO transferVO = TransferVO.builder()
                        .sourceAccount(accountVO.getName())
                        .targetAccount("客户ID: "+payableSheetPO.getCustomer())
                        .state(TransferSheetState.PENDING)
                        .amount(payableSheetContentPO.getTransferAmount())
                        .build();
                transferService.create(transferVO);

            }
        }
    }

    @Override
    public PayableSheetVO getLatest() {
        PayableSheetPO payableSheetPO = payableSheetDao.getLatestSheet();
        PayableSheetVO  payableSheetVO = new PayableSheetVO();
        BeanUtils.copyProperties(payableSheetPO,payableSheetVO);
        List<PayableSheetContentVO> payableSheetContentVOS = new ArrayList<>();
        for (PayableSheetContentPO payableSheetContentPO:payableSheetDao.findContentBySheetId(payableSheetVO.getId())){
            ReceiveSheetContentVO receiveSheetContentVO = new ReceiveSheetContentVO();
            BeanUtils.copyProperties(payableSheetContentPO,receiveSheetContentVO);
        }
        payableSheetVO.setPayableSheetContentVOS(payableSheetContentVOS);
        return payableSheetVO;
    }

    @Override
    public PayableSheetVO getOneById(String id) {
        PayableSheetVO payableSheetVO = new PayableSheetVO();
        PayableSheetPO payableSheetPO = payableSheetDao.getOneById(id);
        BeanUtils.copyProperties(payableSheetPO,payableSheetVO);
        List<PayableSheetContentVO> payableSheetContentVOS = new ArrayList<>();
        for (PayableSheetContentPO payableSheetContentPO:payableSheetDao.findContentBySheetId(payableSheetVO.getId())){
            ReceiveSheetContentVO receiveSheetContentVO = new ReceiveSheetContentVO();
            BeanUtils.copyProperties(payableSheetContentPO,receiveSheetContentVO);
        }
        payableSheetVO.setPayableSheetContentVOS(payableSheetContentVOS);
        return payableSheetVO;
    }

    @Override
    public List<PayableSheetContentVO> getPayableSheetContentVOS(String sheetId){
        List<PayableSheetContentPO> payableSheetContentPOS = payableSheetDao.findContentBySheetId(sheetId);
        List<PayableSheetContentVO> payableSheetContentVOS = new ArrayList<>();
        for(PayableSheetContentPO po:payableSheetContentPOS){
            PayableSheetContentVO vo = PayableSheetContentVO.builder()
                    .name(po.getName())
                    .transferAmount(po.getTransferAmount())
                    .remark(po.getRemark())
                    .build();
            payableSheetContentVOS.add(vo);
        }
        return payableSheetContentVOS;
    }
    @Override
    public List<PayableSheetVO> getBusinessProcess(BusinessProcessFilterVO filterVO) {
        List<PayableSheetVO> res = new ArrayList<>();
        List<PayableSheetPO> all = payableSheetDao.getBusinessProcess(filterVO);
        for(PayableSheetPO po:all){
            PayableSheetVO vo = new PayableSheetVO();
            BeanUtils.copyProperties(po,vo);
            List<PayableSheetContentPO> alll = payableSheetDao.findContentBySheetId(po.getId());
            List<PayableSheetContentVO> vos = new ArrayList<>();
            for(PayableSheetContentPO p : alll){
                PayableSheetContentVO v = new PayableSheetContentVO();
                BeanUtils.copyProperties(p,v);
                vos.add(v);
            }
            vo.setPayableSheetContentVOS(vos);
            res.add(vo);
        }
        return res;
    }

    @Override
    public void redFlush(PayableSheetVO payableSheetVO){
        if(payableSheetVO.getState()!= PayableSheetState.SUCCESS) throw new MyServiceException("A005","状态未完成");
        PayableSheetPO payableSheetPO = PayableSheetPO.builder()
                .operator(payableSheetVO.getOperator())
                .customer(payableSheetVO.getCustomer())
                .totalAmount(payableSheetVO.getTotalAmount().negate())
                .state(PayableSheetState.SUCCESS)
                .createTime(new Date())
                .build();
        PayableSheetPO latest = payableSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null?null: latest.getId(), "FKD");
        payableSheetPO.setId(id);
        List<PayableSheetContentPO> payableSheetContentPOS = new ArrayList<>();
        for (PayableSheetContentVO content:payableSheetVO.getPayableSheetContentVOS()){
            PayableSheetContentPO sContentPOs = new PayableSheetContentPO();
            BeanUtils.copyProperties(content,sContentPOs);
            sContentPOs.setPayableSheetId(payableSheetPO.getId());
            sContentPOs.setTransferAmount(sContentPOs.getTransferAmount().negate());
            payableSheetContentPOS.add(sContentPOs);
        }
        payableSheetDao.saveBatchSheetContent(payableSheetContentPOS);
        payableSheetDao.saveSheet(payableSheetPO);
    }

    @Override
    public PayableSheetVO redFlushCopy(PayableSheetVO payableSheetVO){
        redFlush(payableSheetVO);
        PayableSheetVO res = new PayableSheetVO();
        BeanUtils.copyProperties(payableSheetVO,res);
        return res;
    }

    @Override
    public void copyIn(PayableSheetVO payableSheetVO){
        PayableSheetPO payableSheetPO = PayableSheetPO.builder()
                .operator(payableSheetVO.getOperator())
                .customer(payableSheetVO.getCustomer())
                .totalAmount(payableSheetVO.getTotalAmount().negate())
                .state(payableSheetVO.getState())
                .createTime(new Date())
                .build();
        PayableSheetPO latest = payableSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null?null: latest.getId(), "FKD");
        payableSheetPO.setId(id);
        List<PayableSheetContentPO> payableSheetContentPOS = new ArrayList<>();
        for (PayableSheetContentVO content:payableSheetVO.getPayableSheetContentVOS()){
            PayableSheetContentPO sContentPOs = new PayableSheetContentPO();
            BeanUtils.copyProperties(content,sContentPOs);
            sContentPOs.setPayableSheetId(payableSheetPO.getId());
            payableSheetContentPOS.add(sContentPOs);
        }
        payableSheetDao.saveBatchSheetContent(payableSheetContentPOS);
        payableSheetDao.saveSheet(payableSheetPO);
    }
}
