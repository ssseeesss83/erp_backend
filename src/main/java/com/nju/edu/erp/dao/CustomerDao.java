package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.model.po.CustomerPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CustomerDao {
    int updateOne(CustomerPO customerPO);

    int createCustomer(CustomerPO customerPO);

    void deleteById(Integer supplier);

//    void deleteOne(CustomerPO customerPO);

    CustomerPO findOneById(Integer supplier);

    List<CustomerPO> findAllByType(CustomerType customerType);

    List<CustomerPO> findAll();

    void deleteAll();
}

