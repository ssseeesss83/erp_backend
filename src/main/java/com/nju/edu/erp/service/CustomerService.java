package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.vo.CustomerVO;

import java.util.List;

public interface CustomerService {
    /**
     * 根据id更新客户信息
     * @param customerPO 客户信息
     */
    void updateCustomer(CustomerPO customerPO);

    /**
     * 根据type查找对应类型的客户
     * @param type 客户类型
     * @return 客户列表
     */
    List<CustomerPO> getCustomersByType(CustomerType type);


    CustomerPO findCustomerById(Integer supplier);

    /**
     * 增加新客户
     * @param name 客户姓名
     * @return 创建的客户信息（其余信息用update更新）
     */
    CustomerVO createCustomer(String name);

    /**
     * 根据id 删除对应客户
     */
    void deleteById(Integer supplier);



//
//     根据客户信息 删除对应客户
//    void deleteCustomer(CustomerVO customerVO);
}

