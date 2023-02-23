package com.nju.edu.erp.model.po.gift;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiftSheetContentPO {
    /**
     * 自增id
     */
    private Integer id;

    private String giftSheetId;
    /**
     * 商品pid
     */
    private String pid;

    private Integer quantity;

}
