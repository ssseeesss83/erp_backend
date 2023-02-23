package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.StationName;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SalarySystemSheetState;
import com.nju.edu.erp.model.vo.SalarySystemSheetVO;

import java.util.List;

public interface SalarySystemService {

    void makeSalarySystemSheet(SalarySystemSheetVO salarySystemSheetVO);

    SalarySystemSheetVO getLatest();

    SalarySystemSheetVO getOneByNameAndLevel(StationName name, Integer level);

    List<SalarySystemSheetVO> getSheetByState(SalarySystemSheetState state);

    void approval(StationName name,Integer level,SalarySystemSheetState state);

}
