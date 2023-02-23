package com.nju.edu.erp.utils;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.StationName;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.po.voucher.VoucherPO;
import com.nju.edu.erp.model.vo.*;
import com.nju.edu.erp.model.vo.clock.ClockVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetVO;
import com.nju.edu.erp.model.vo.voucher.VoucherVO;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Conversion {

    public static List<AccountVO> conversion(List<AccountPO> accountPOS){
        List<AccountVO> accountVOS = new ArrayList<>();
        for (AccountPO accountPO:accountPOS){
            AccountVO accountVO = new AccountVO();
            BeanUtils.copyProperties(accountPO,accountVO);
            accountVOS.add(accountVO);
        }
        return accountVOS;
    }
    public static AccountVO conversion(AccountPO accountPO) {
        if (accountPO==null) return null;
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(accountPO, accountVO);
        return accountVO;
    }
    public static SalarySystemSheetPO conversion(SalarySystemSheetVO salarySystemSheetVO){
        SalarySystemSheetPO salarySystemSheetPO = new SalarySystemSheetPO();
        BeanUtils.copyProperties(salarySystemSheetVO,salarySystemSheetPO);
        return salarySystemSheetPO;
    }
    public static SalarySystemSheetVO conversion(SalarySystemSheetPO salarySystemSheetPO){
        SalarySystemSheetVO salarySystemSheetVO = new SalarySystemSheetVO();
        if (salarySystemSheetPO!=null) {
            BeanUtils.copyProperties(salarySystemSheetPO, salarySystemSheetVO);
        }else {
            salarySystemSheetVO = null;
        }
        return salarySystemSheetVO;
    }
    public static EmployeePO conversion(EmployeeVO employeeVO){
        if (employeeVO==null) return null;
        EmployeePO employeePO = new EmployeePO();
        BeanUtils.copyProperties(employeeVO,employeePO);
        return employeePO;
    }
    public static EmployeeVO conversion(EmployeePO employeePO,SalarySystemSheetVO salarySystemSheetVO){
        EmployeeVO employeeVO = new EmployeeVO();
        BeanUtils.copyProperties(employeePO,employeeVO);
        if (salarySystemSheetVO!=null) {
            BeanUtils.copyProperties(salarySystemSheetVO, employeeVO);
        }
        return employeeVO;
    }

    private static final Map<StationName,Role> roleStationNameMap = new HashMap<StationName,Role>(){{
        put(StationName.SALE_STAFF,Role.SALE_STAFF);
        put(StationName.INVENTORY_MANAGER,Role.INVENTORY_MANAGER);
        put(StationName.GM,Role.GM);
        put(StationName.SALE_MANAGER,Role.SALE_MANAGER);
        put(StationName.FINANCIAL_STAFF,Role.FINANCIAL_STAFF);
        put(StationName.HR,Role.HR);
    }};

    public static Role conversion(StationName stationName){
        return roleStationNameMap.get(stationName);
    }

    public static ClockVO conversion(ClockPO clockPO){
        if (clockPO==null) return null;
        ClockVO clockVO = new ClockVO();
        BeanUtils.copyProperties(clockPO,clockVO);
        return clockVO;
    }
    public static ClockPO conversion(ClockVO clockVO){
        if (clockVO==null) return null;
        ClockPO clockPO = new ClockPO();
        BeanUtils.copyProperties(clockVO,clockPO);
        return clockPO;
    }
    public static String getYearAndMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH)+1);
    }
    public static String getYearAndMonth(Integer year,Integer month){
        return String.valueOf(year)+String.valueOf(month);
    }
    public static Integer getMonthDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    public static Integer getYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
    public static Date getBeginDateOfMonth(String yearAndMonth) {
        if (yearAndMonth.length()==5){
            yearAndMonth = yearAndMonth.substring(0,4)+"0"+yearAndMonth.substring(4);
        }
        yearAndMonth = yearAndMonth+"00";
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyyMMdd");
        Date date;
        try {
            date = formatter.parse(yearAndMonth);
        }catch (ParseException e){
            throw new MyServiceException("A0007","日期错误");
        }
        return date;
    }
    public static Date getEndDateOfMonth(String yearAndMonth) {
        if (yearAndMonth.length()==5){
            yearAndMonth = yearAndMonth.substring(0,4)+"0"+yearAndMonth.substring(4);
        }
        if (Integer.parseInt(yearAndMonth.substring(4,6))==2){
            yearAndMonth = yearAndMonth+"28";
        }else {
            yearAndMonth = yearAndMonth + "30";
        }
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyyMMdd");
        Date date;
        try {
            date = formatter.parse(yearAndMonth);
        }catch (ParseException e){
            throw new MyServiceException("A0007","日期错误");
        }
        return date;
    }
    public static SalarySheetVO conversion(SalarySheetPO salarySheetPO){
        if (salarySheetPO==null) return null;
        SalarySheetVO salarySheetVO = new SalarySheetVO();
        BeanUtils.copyProperties(salarySheetPO,salarySheetVO);
        return salarySheetVO;
    }
    public static TransferVO conversion(TransferPO transferPO){
        if (transferPO==null) return null;
        TransferVO transferVO = new TransferVO();
        BeanUtils.copyProperties(transferPO,transferVO);
        return transferVO;
    }
    public static TransferPO conversion(TransferVO transferVO){
        if (transferVO==null) return null;
        TransferPO transferPO = new TransferPO();
        BeanUtils.copyProperties(transferVO,transferPO);
        return transferPO;
    }

    public static AnnualBonusPO conversion(AnnualBonusVO annualBonusVO){
        if (annualBonusVO==null) return null;
        AnnualBonusPO annualBonusPO = new AnnualBonusPO();
        BeanUtils.copyProperties(annualBonusVO,annualBonusPO);
        return annualBonusPO;
    }
    public static AnnualBonusVO conversion(AnnualBonusPO annualBonusPO){
        if (annualBonusPO==null) return null;
        AnnualBonusVO annualBonusVO = new AnnualBonusVO();
        BeanUtils.copyProperties(annualBonusPO,annualBonusVO);
        return annualBonusVO;
    }
    public static VoucherVO conversion(VoucherPO voucherPO){
        if (voucherPO==null) return null;
        VoucherVO voucherVO = new VoucherVO();
        BeanUtils.copyProperties(voucherPO,voucherVO);
        return voucherVO;
    }

    public static List<String> getYearAndMonths(BusinessProcessFilterVO filterVO){
        int beginYear,beginMonth,endMonth,endYear;
        if (filterVO.getBegin()==null){
            beginYear = 2022;
            beginMonth = 1;
        }else {
            beginYear = getYear(filterVO.getBegin());
            beginMonth = getMonthDay(filterVO.getBegin());
        }
        if (filterVO.getEnd()==null){
            endYear = getYear(new Date());
            endMonth = getMonthDay(new Date());
        }else {
            endYear = getYear(filterVO.getEnd());
            endMonth = getMonthDay(filterVO.getEnd());
        }
        List<String> yearAndMonths = new ArrayList<>();
        while (beginYear*100+beginMonth<=endYear*100+endMonth){
            yearAndMonths.add(getYearAndMonth(beginYear,beginMonth));
            if (++beginMonth==13){
                beginYear++;
                beginMonth = 1;
            }
        }
        return yearAndMonths;
    }


}
