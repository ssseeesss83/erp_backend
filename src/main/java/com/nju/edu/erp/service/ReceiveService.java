package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import com.nju.edu.erp.model.po.receive.ReceiveSheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetContentVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetVO;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReceiveService {

    void makeReceiveSheet(UserVO userVO, ReceiveSheetVO receiveSheetVO);

    List<ReceiveSheetVO> getReceiveSheetByState(ReceiveSheetState receiveSheetState);

    ReceiveSheetVO getLatest();

    void approval(String receiveSheetId,ReceiveSheetState state);

    List<ReceiveSheetContentVO> getReceiveSheetContentVOS(String sheetId);

    List<ReceiveSheetVO> getBusinessProcess(BusinessProcessFilterVO filterVO);

    void redFlush(ReceiveSheetVO receiveSheetVO);

    ReceiveSheetVO redFlushCopy(ReceiveSheetVO receiveSheetVO);

    void copyIn(ReceiveSheetVO receiveSheetVO);
}
