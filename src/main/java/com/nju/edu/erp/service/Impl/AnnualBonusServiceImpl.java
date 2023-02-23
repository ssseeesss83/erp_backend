package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.AnnualBonusDao;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.AnnualBonusPO;
import com.nju.edu.erp.model.vo.AnnualBonusVO;
import com.nju.edu.erp.service.AnnualBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.nju.edu.erp.utils.Conversion.conversion;
@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {

    private final AnnualBonusDao annualBonusDao;

    @Autowired
    public AnnualBonusServiceImpl(AnnualBonusDao annualBonusDao) {
        this.annualBonusDao = annualBonusDao;
    }

    @Override
    public void create(AnnualBonusVO annualBonusVO) {
        if (annualBonusDao.create(conversion(annualBonusVO))==0) throw new MyServiceException("A0002","对象年终奖已创建");
    }

    @Override
    public void delete(String employeeName, Integer year) {
        annualBonusDao.delete(employeeName,year);
    }

    @Override
    public AnnualBonusVO getOneByCustomerNameAndYear(String employeeName, Integer year) {
        return conversion(annualBonusDao.getOneByEmployeeNameAndYear(employeeName,year));
    }

    @Override
    public List<AnnualBonusVO> getAll() {
        List<AnnualBonusVO> annualBonusVOS = new ArrayList<>();
        for (AnnualBonusPO annualBonusPO: annualBonusDao.getAll()){
            annualBonusVOS.add(conversion(annualBonusPO));
        }
        return annualBonusVOS;
    }
}
