package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.GiftSheetState;
import com.nju.edu.erp.model.vo.gift.GiftSheetVO;

import java.util.List;

public interface GiftSheetService {

    /**
     * operator已知
     */
    void makeGiftSheet(GiftSheetVO giftSheetVO);

    List<GiftSheetVO> getGiftSheetByState(GiftSheetState giftSheetState);

    void approval(String giftSheetId,GiftSheetState giftSheetState);

    GiftSheetVO getLatest();

}
