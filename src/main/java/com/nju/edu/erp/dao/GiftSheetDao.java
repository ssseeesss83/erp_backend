package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.GiftSheetState;
import com.nju.edu.erp.model.po.gift.GiftSheetContentPO;
import com.nju.edu.erp.model.po.gift.GiftSheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GiftSheetDao {

    int create(GiftSheetPO giftSheetPO);

    int createBatchSheetContent(List<GiftSheetContentPO> giftSheetContentPOS);

    GiftSheetPO getLatest();

    List<GiftSheetPO> getAll();

    List<GiftSheetPO> getAllByState(GiftSheetState state);

    List<GiftSheetContentPO> getBatchSheetContent(String giftSheetId);

    GiftSheetPO getOneById(String id);

    GiftSheetPO getOneBySaleSheetId(String saleSheetId);

    int updateState(String id, GiftSheetState state);

    int updateStateV2(String id,GiftSheetState preState,GiftSheetState state);
}
