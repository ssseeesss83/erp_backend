package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.enums.StationName;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.TransferSheetState;
import com.nju.edu.erp.enums.taxCalculation.TaxCalculation;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.vo.*;
import com.nju.edu.erp.model.vo.clock.ClockVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.service.*;
import com.nju.edu.erp.utils.Conversion;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.nju.edu.erp.utils.Conversion.conversion;

@Service
public class SalarySheetServiceImpl implements SalarySheetService {

    private final SalarySheetDao salarySheetDao;

    private final EmployeeService employeeService;

    private final SalarySystemService salarySystemService;

    private final SaleService saleService;

    private final SaleReturnService saleReturnService;

    private final AccountService accountService;

    private final ClockService clockService;

    private final TransferService transferService;

    private final AnnualBonusService annualBonusService;

    @Autowired
    public SalarySheetServiceImpl(SalarySheetDao salarySheetDao, EmployeeService employeeService, SalarySystemService salarySystemService, SaleService saleService, SaleReturnService saleReturnService, AccountService accountService, ClockService clockService, TransferService transferService, AnnualBonusService annualBonusService) {
        this.salarySheetDao = salarySheetDao;
        this.employeeService = employeeService;
        this.salarySystemService = salarySystemService;
        this.saleService = saleService;
        this.saleReturnService = saleReturnService;
        this.accountService = accountService;
        this.clockService = clockService;
        this.transferService = transferService;
        this.annualBonusService = annualBonusService;
    }

    /**
     * 制定指定月份的工资单
     */
    @Override
    public void makeSalarySheet(UserVO userVO, String employeeName, String companyAccount, Integer year, Integer month) {
        EmployeeVO employeeVO = employeeService.getOneByName(employeeName);
        if (employeeVO.getStationName()== StationName.GM & month!=12){
            throw new MyServiceException("A0008","总经理工资单只能在12月末创建");
        }
        SalarySystemSheetVO salarySystemSheetVO = salarySystemService.getOneByNameAndLevel(employeeVO.getStationName(),employeeVO.getStationLevel());
        if (salarySystemSheetVO==null) throw new MyServiceException("A0004","对应薪酬制度缺失");
        SalarySheetPO salarySheetPO = SalarySheetPO.builder()
                .employeeName(employeeName)
                .operator(userVO.getName())
                .employeeAccount(employeeVO.getEmployeeAccount())
                .companyAccount(companyAccount)
                .payMethod(salarySystemSheetVO.getPayMethod())
                .state(SalarySheetState.PENDING)
                .createTime(new Date())
                .build();
        SalarySheetPO latest = salarySheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null?null : latest.getId(),"GZD");
        salarySheetPO.setId(id);
        //计算应付工资、税收、实付工资
        BigDecimal shouldPay = salarySystemSheetVO.getSalaryCalculationType()
                .getSalary(employeeVO,salarySystemSheetVO,saleService,saleReturnService,String.valueOf(year)+month);
        // 加上年终奖: 基础工资*发放月数
        if (month==12) {
            AnnualBonusVO annualBonusVO = annualBonusService.getOneByCustomerNameAndYear(employeeName, year);
            if (annualBonusVO!=null) {
                shouldPay = shouldPay.add(salarySystemSheetVO.getBasisSalary().multiply(BigDecimal.valueOf(annualBonusVO.getBasisSalaryAmount())));
            }
        }
        BigDecimal baseSalary = salarySystemSheetVO.getBasisSalary();
        //扣除缺勤罚款，得到应付工资
        ClockVO clockVO = clockService.getEmployeeClockByNameAndTime(employeeName, Conversion.getYearAndMonth(year,month));
        BigDecimal deduction = baseSalary.multiply(BigDecimal.valueOf(clockVO.getAbsenceTimes())).divide(BigDecimal.valueOf(30),2,RoundingMode.HALF_UP);
        if (employeeVO.getStationName()!=StationName.GM) {//总经理不参与打卡
            shouldPay = shouldPay.subtract(deduction);
        }
        List<TaxCalculation> taxCalculations = salarySystemSheetVO.getTaxType().getTaxCalculationList();
        BigDecimal tax = BigDecimal.ZERO;
        for (TaxCalculation taxCalculation:taxCalculations){
            tax = tax.add(taxCalculation.getTax(shouldPay));
        }
        BigDecimal realPay = shouldPay.subtract(tax);
        salarySheetPO.setShouldPay(shouldPay);
        salarySheetPO.setTax(tax);
        salarySheetPO.setRealPay(realPay);
        if (salarySheetDao.createSheet(salarySheetPO)==0) throw new MyServiceException("A0002","工资单创建失败");;
    }

    @Override
    public List<SalarySheetVO> getSalarySheetByState(SalarySheetState state) {
        List<SalarySheetVO> salarySheetVOS = new ArrayList<>();
        for (SalarySheetPO salarySheetPO: salarySheetDao.getAllSheetByState(state)){
            SalarySheetVO salarySheetVO = new SalarySheetVO();
            BeanUtils.copyProperties(salarySheetPO,salarySheetVO);
            salarySheetVOS.add(salarySheetVO);
        }
        return salarySheetVOS;
    }


    @Override
    public SalarySheetVO getSheetById(String sheetId){
        return conversion(salarySheetDao.getOneById(sheetId));
    }
    @Override
    public SalarySheetVO getLatestSheet() {
        return conversion(salarySheetDao.getLatestSheet());
    }

    @Override
    public void approval(String salarySheetId, SalarySheetState state) {
        SalarySheetPO salarySheetPO = salarySheetDao.getOneById(salarySheetId);
        if (state.equals(SalarySheetState.FAILURE)){
            if (salarySheetPO.getState() == SalarySheetState.SUCCESS) throw new MyServiceException("A0003","更新参数错误");
            if (salarySheetDao.updateSheetState(salarySheetId,state)==0)throw new MyServiceException("A0003","状态更新失败");
        }else {
            SalarySheetState preState;
            if (state.equals(SalarySheetState.SUCCESS)){
                preState = SalarySheetState.PENDING;
            }else {
                throw new MyServiceException("A0003","更新参数错误");
            }
            if (salarySheetDao.updateSheetStateOnPrev(salarySheetId,preState,state)==0) throw new MyServiceException("A0003","状态更新失败");
            AccountVO accountVO = accountService.findByName(salarySheetPO.getCompanyAccount());
            BigDecimal balance = accountVO.getBalance();
            //公司支出应付、个人收到实付
            accountVO.setBalance(balance.subtract(salarySheetPO.getShouldPay()));
            accountService.updateAccount(accountVO);
            //转账信息,个人收到的是实付金额
            //TODO:测试
            TransferVO transferVO = TransferVO.builder()
                    .sourceAccount(salarySheetPO.getCompanyAccount())
                    .targetAccount(salarySheetPO.getEmployeeAccount())
                    .state(TransferSheetState.PENDING)
                    .amount(salarySheetPO.getRealPay())
                    .build();
            transferService.create(transferVO);
        }
    }

    public List<SalarySheetVO> getBusinessProcess(BusinessProcessFilterVO filterVO){
        List<SalarySheetVO> res = new ArrayList<>();
        List<SalarySheetPO> all = salarySheetDao.getBusinessProcess(filterVO);
        for(SalarySheetPO po:all){
            SalarySheetVO vo = new SalarySheetVO();
            BeanUtils.copyProperties(po,vo);
            res.add(vo);
        }
        return res;
    }
    @Override
    public void redFlush(SalarySheetVO salarySheetVO){
        if(salarySheetVO.getState()!=SalarySheetState.SUCCESS) throw new MyServiceException("A005","状态未完成");
        EmployeeVO employeeVO = employeeService.getOneByName(salarySheetVO.getEmployeeName());
        SalarySystemSheetVO salarySystemSheetVO = salarySystemService.getOneByNameAndLevel(employeeVO.getStationName(),employeeVO.getStationLevel());
        SalarySheetPO salarySheetPO = SalarySheetPO.builder()
                .employeeName(salarySheetVO.getEmployeeName())
                .operator(salarySheetVO.getOperator())
                .employeeAccount(salarySheetVO.getEmployeeAccount())
                .companyAccount(salarySheetVO.getCompanyAccount())
                .payMethod(salarySystemSheetVO.getPayMethod())
                .shouldPay(salarySheetVO.getShouldPay().negate())
                .realPay(salarySheetVO.getRealPay().negate())
                .tax(salarySheetVO.getTax())
                .state(SalarySheetState.SUCCESS)
                .createTime(new Date())
                .build();
        SalarySheetPO latest = salarySheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null?null : latest.getId(),"GZD");
        salarySheetPO.setId(id);
        if (salarySheetDao.createSheet(salarySheetPO)==0) throw new MyServiceException("A0002","工资单创建失败");
    }

    @Override
    public SalarySheetVO redFlushCopy(SalarySheetVO salarySheetVO){
        redFlush(salarySheetVO);
        SalarySheetVO res = new SalarySheetVO();
        BeanUtils.copyProperties(salarySheetVO,res);
        return res;
    }

    @Override
    public void copyIn(SalarySheetVO salarySheetVO){
        EmployeeVO employeeVO = employeeService.getOneByName(salarySheetVO.getEmployeeName());
        SalarySystemSheetVO salarySystemSheetVO = salarySystemService.getOneByNameAndLevel(employeeVO.getStationName(),employeeVO.getStationLevel());
        SalarySheetPO salarySheetPO = SalarySheetPO.builder()
                .employeeName(salarySheetVO.getEmployeeName())
                .operator(salarySheetVO.getOperator())
                .employeeAccount(salarySheetVO.getEmployeeAccount())
                .companyAccount(salarySheetVO.getCompanyAccount())
                .payMethod(salarySystemSheetVO.getPayMethod())
                .shouldPay(salarySheetVO.getShouldPay())
                .realPay(salarySheetVO.getRealPay())
                .tax(salarySheetVO.getTax())
                .state(salarySheetVO.getState())
                .createTime(new Date())
                .build();
        SalarySheetPO latest = salarySheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null?null : latest.getId(),"GZD");
        salarySheetPO.setId(id);
        if (salarySheetDao.createSheet(salarySheetPO)==0) throw new MyServiceException("A0002","工资单创建失败");
    }
}
