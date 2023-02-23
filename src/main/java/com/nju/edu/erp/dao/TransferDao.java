package com.nju.edu.erp.dao;


import com.nju.edu.erp.enums.sheetState.TransferSheetState;
import com.nju.edu.erp.model.po.TransferPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TransferDao {

    int create(TransferPO transferPO);

    int deleteById(String id);

    int update(TransferPO transferPO);

    TransferPO getLatest();

    TransferPO getById(String id);

    List<TransferPO> getAllByState(TransferSheetState state);

    List<TransferPO> getAll();

}
