package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.TransferSheetState;
import com.nju.edu.erp.model.vo.TransferVO;

import java.util.List;

public interface TransferService {

    List<TransferVO> getAllByState(TransferSheetState state);

    List<TransferVO> getAll();

    void updateStateToSuccess(String id);

    void create(TransferVO transferVO);

    void deleteById(String id);

    TransferVO getLatest();

}
