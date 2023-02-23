package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.SalarySystemSheetDao;
import com.nju.edu.erp.enums.StationName;
import com.nju.edu.erp.enums.sheetState.SalarySystemSheetState;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.SalarySystemSheetPO;
import com.nju.edu.erp.model.vo.SalarySystemSheetVO;
import com.nju.edu.erp.service.SalarySystemService;
import com.nju.edu.erp.enums.StationName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

import static com.nju.edu.erp.utils.Conversion.conversion;

@Service
public class SalarySystemServiceImpl implements SalarySystemService {

    private final SalarySystemSheetDao salarySystemSheetDao;

    @Autowired
    public SalarySystemServiceImpl(SalarySystemSheetDao salarySystemSheetDao) {
        this.salarySystemSheetDao = salarySystemSheetDao;
    }

    @Override
    public void makeSalarySystemSheet(SalarySystemSheetVO salarySystemSheetVO) {
        // 不能同时存在超过一个同一name、level的sheet
        // 会自动使得任意状态的sheet失效
        salarySystemSheetDao.failAllByNameAndLevel(salarySystemSheetVO.getStationName(), salarySystemSheetVO.getStationLevel());
        salarySystemSheetDao.create(conversion(salarySystemSheetVO));
    }

    @Override
    public SalarySystemSheetVO getLatest() {
        return conversion(salarySystemSheetDao.getLatest());
    }

    @Override
    public SalarySystemSheetVO getOneByNameAndLevel(StationName name, Integer level) {
        return conversion(salarySystemSheetDao.getOneByNameAndLevel(name,level));
    }

    @Override
    public List<SalarySystemSheetVO> getSheetByState(SalarySystemSheetState state) {
        List<SalarySystemSheetVO> salarySystemSheetVOS = new ArrayList<>();
        for (SalarySystemSheetPO sheetPO: salarySystemSheetDao.getAllByState(state)){
            salarySystemSheetVOS.add(conversion(sheetPO));
        }
        return salarySystemSheetVOS;
    }

    @Override
    public void approval(StationName name, Integer level, SalarySystemSheetState state) {
        if (state.equals(SalarySystemSheetState.FAILURE)){
            SalarySystemSheetPO sheetPO = salarySystemSheetDao.getOneByNameAndLevel(name,level);
            if (sheetPO.getState() == SalarySystemSheetState.SUCCESS) throw new MyServiceException("A0003","更新参数错误");
            if (salarySystemSheetDao.updateState(name,level,state)==0) throw new MyServiceException("A0003","状态更新失败");;
        }else {
            SalarySystemSheetState preState;
            if(state.equals(SalarySystemSheetState.SUCCESS)){
                preState = SalarySystemSheetState.PENDING_LEVEL_2;
            }else if (state.equals(SalarySystemSheetState.PENDING_LEVEL_2)){
                preState = SalarySystemSheetState.PENDING_LEVEL_1;
            }else {
                throw new MyServiceException("A0003","更新参数错误");
            }
            if (salarySystemSheetDao.updateStateV2(name,level,preState,state)==0) throw new MyServiceException("A0003","状态更新失败");
        }
    }
}
