package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.po.sale.SaleSheetPO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SalarySheetDao {

    SalarySheetPO getLatestSheet();

    int createSheet(SalarySheetPO salarySheetPO);

    List<SalarySheetPO> getAllSheet();

    List<SalarySheetPO> getAllSheetByState(SalarySheetState state);

    SalarySheetPO getOneById(String sheetId);

    int updateSheetState(String sheetId,SalarySheetState state);

    int updateSheetStateOnPrev(String sheetId,SalarySheetState prevState,SalarySheetState state);

    List<SalarySheetPO> getBusinessProcess(BusinessProcessFilterVO filterVO);

}
