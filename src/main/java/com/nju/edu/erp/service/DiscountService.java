package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.DiscountVO;

import java.util.List;

public interface DiscountService {

    void create(DiscountVO discountVO);

    void delete(String discountId);
    /**
     * 根据客户等级，查找可用折扣
     */
    List<DiscountVO> getAllByLevel(Integer level);
    /**
     * 获取所有有效折扣设置
     */
    List<DiscountVO> getAll();
}
