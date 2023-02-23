package com.nju.edu.erp.model.vo.gift;

import com.nju.edu.erp.enums.sheetState.GiftSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiftSheetVO {
    /**
     * ZSD
     */
    private String id;
    /**
     * 对应销售单id
     */
    private String saleSheetId;
    /**
     * 客户id
     */
    private Integer customerId;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 赠送列表
     */
    private List<GiftSheetContentVO> giftSheetContentVOS;
}
