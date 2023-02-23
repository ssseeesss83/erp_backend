package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetContentPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetPO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReceiveSheetDao {

    ReceiveSheetPO getLatestSheet();

    int saveSheet(ReceiveSheetPO receiveSheetPO);

    int saveBatchSheetContent(List<ReceiveSheetContentPO> receiveSheetContentPOS);

    List<ReceiveSheetPO> findAllSheet();

    List<ReceiveSheetPO> findAllSheetByState(ReceiveSheetState state);

    ReceiveSheetPO findSheetById(String SheetId);

    List<ReceiveSheetContentPO> findContentBySheetId(String sheetId);

    int updateSheetState(String sheetId,ReceiveSheetState state);

    int updateSheetStateOnPrev(String sheetId, ReceiveSheetState prev, ReceiveSheetState state);

    List<ReceiveSheetPO> getBusinessProcess(BusinessProcessFilterVO filterVO);
}
