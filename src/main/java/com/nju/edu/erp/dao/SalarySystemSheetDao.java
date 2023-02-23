package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.StationName;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SalarySystemSheetState;
import com.nju.edu.erp.model.po.SalarySystemSheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SalarySystemSheetDao {

    int create(SalarySystemSheetPO salarySystemSheetPO);

    /**
     * 非FAILURE状态
     */
    int updateByNameAndLevel(SalarySystemSheetPO salarySystemSheetPO);
    /**
     * 非FAILURE状态
     */
    int updateState(StationName stationName,Integer stationLevel, SalarySystemSheetState state);
    /**
     * 非FAILURE状态
     */
    int updateStateV2(StationName stationName,Integer stationLevel, SalarySystemSheetState preState, SalarySystemSheetState state);

    SalarySystemSheetPO getLatest();

    List<SalarySystemSheetPO> getAllByState(SalarySystemSheetState state);

    /**
     *获取非FAILURE状态的单据
     */
    SalarySystemSheetPO getOneByNameAndLevel(StationName stationName, Integer stationLevel);

    /**
     * 使得所有name和level相同的单据失效
     */
    int failAllByNameAndLevel(StationName stationName,Integer stationLevel);
}
