package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.AnnualBonusVO;

import java.util.List;

public interface AnnualBonusService {

    void create(AnnualBonusVO annualBonusVO);

    void delete(String employeeName, Integer year);

    AnnualBonusVO getOneByCustomerNameAndYear(String employeeName, Integer year);

    List<AnnualBonusVO> getAll();

}
