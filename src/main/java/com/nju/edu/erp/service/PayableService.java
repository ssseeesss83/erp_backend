package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.PayableSheetState;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.payable.PayableSheetContentVO;
import com.nju.edu.erp.model.vo.payable.PayableSheetVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetVO;

import java.util.List;

public interface PayableService {

    void makePayableSheet(UserVO userVO, PayableSheetVO payableSheetVO);

    List<PayableSheetVO> getPayableSheetByState(PayableSheetState payableSheetState);

    void approval(String payableSheetId,PayableSheetState state);

    PayableSheetVO getLatest();

    PayableSheetVO getOneById(String id);

    List<PayableSheetContentVO> getPayableSheetContentVOS(String sheetId);

    List<PayableSheetVO> getBusinessProcess(BusinessProcessFilterVO filterVO);

    void redFlush(PayableSheetVO payableSheetVO);

    PayableSheetVO redFlushCopy(PayableSheetVO payableSheetVO);

    void copyIn(PayableSheetVO payableSheetVO);
}