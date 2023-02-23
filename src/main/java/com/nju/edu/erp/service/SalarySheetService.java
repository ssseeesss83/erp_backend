package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.vo.SalarySheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;

import java.util.List;

public interface SalarySheetService {

    /**
     * 基于员工姓名来建立工资单
     */
    void makeSalarySheet(UserVO userVO,String employeeName,String companyAccount,Integer month,Integer year);

    List<SalarySheetVO> getSalarySheetByState(SalarySheetState state);

    SalarySheetVO getSheetById(String sheetId);

    SalarySheetVO getLatestSheet();

    void approval(String salarySheetId,SalarySheetState state);

    List<SalarySheetVO> getBusinessProcess(BusinessProcessFilterVO filterVO);

    void redFlush(SalarySheetVO salarySheetVO);

    SalarySheetVO redFlushCopy(SalarySheetVO salarySheetVO);

    void copyIn(SalarySheetVO salarySheetVO);
}
