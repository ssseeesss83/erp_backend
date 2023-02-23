package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.BusinessVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;

import java.util.List;

public interface BusinessService {

    BusinessVO getMonthBusinessSheet(String YearAndMonth);

    List<BusinessVO> getBusinessSheet(BusinessProcessFilterVO filterVO);
}
