package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.AnnualBonusPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AnnualBonusDao {

    int create(AnnualBonusPO annualBonusPO);

    int delete(String employeeName,Integer year);

    AnnualBonusPO getOneByEmployeeNameAndYear(String employeeName, Integer year);

    List<AnnualBonusPO> getAll();
}
