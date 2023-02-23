package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.DiscountPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface DiscountDao {

    int create(DiscountPO discountPO);

    int delete(String discountId);

    DiscountPO getLatest();
    /**
     * 有效时间内
     */
    List<DiscountPO> getAvailableByLevel(Integer customerLevel, Date date);
    /**
     * 有效时间内
     */
    List<DiscountPO> getAll(Date date);
}
