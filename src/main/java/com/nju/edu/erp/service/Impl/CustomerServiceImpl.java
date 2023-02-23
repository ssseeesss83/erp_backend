package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.CategoryPO;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.vo.CategoryVO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * 根据id更新客户信息
     *
     * @param customerPO 客户信息
     */
    @Override
    public void updateCustomer(CustomerPO customerPO) {
        customerDao.updateOne(customerPO);
    }

    /**
     * 根据type查找对应类型的客户
     *
     * @param type 客户类型
     * @return 客户列表
     */
    @Override
    public List<CustomerPO> getCustomersByType(CustomerType type) {

        return customerDao.findAllByType(type);
    }

    @Override
    public CustomerPO findCustomerById(Integer supplier) {
        return customerDao.findOneById(supplier);
    }

    /**
     * 增加新客户
     * @param name 客户姓名
     */
    @Override
    public CustomerVO createCustomer(String name){
        // 创建PO并存入数据库
        CustomerPO savePO = new CustomerPO(null, CustomerType.UNDEFINED.getValue(), 0, name, null, null, null, null, null, null, null, null);
        int ans = customerDao.createCustomer(savePO);
        if (ans == 0) {
            throw new MyServiceException("A0002", "插入失败!");
        }
        // 构建返回值
        CustomerVO responseVO = new CustomerVO();
        BeanUtils.copyProperties(savePO, responseVO);
        return responseVO;
    };

    /**
     * 根据id 删除对应客户
     */
    @Override
    public void deleteById(Integer supplier){ customerDao.deleteById(supplier);};

//
//     根据客户信息 删除对应客户
//    public void deleteCustomer(CustomerVO customerVO){ customerDao.deleteOne(customerVO);};
}

