package com.nju.edu.erp.model.vo.gift;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiftSheetContentVO {
    /**
     * giftSheetId
     */
    private String giftSheetId;
    /**
     * 商品pid
     */
    private String pid;

    private Integer quantity;
}
