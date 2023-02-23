package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.payable.PayableSheetPO;
import com.nju.edu.erp.enums.sheetState.PayableSheetState;
import com.nju.edu.erp.model.po.payable.PayableSheetContentPO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface PayableSheetDao {
    PayableSheetPO getLatestSheet();

    int saveSheet(PayableSheetPO payableSheetPO);

    int saveBatchSheetContent(List<PayableSheetContentPO> payableSheetContentPOS);

    List<PayableSheetPO> findAllSheet();

    List<PayableSheetPO> findAllSheetByState(PayableSheetState state);

    PayableSheetPO findSheetById(String SheetId);

    List<PayableSheetContentPO> findContentBySheetId(String sheetId);

    int updateSheetState(String sheetId,PayableSheetState state);

    int updateSheetStateOnPrev(String sheetId, PayableSheetState prev, PayableSheetState state);

    List<PayableSheetPO> getBusinessProcess(BusinessProcessFilterVO filterVO);

    PayableSheetPO getOneById(String id);
}
