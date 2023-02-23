package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.SaleReturnSheetState;
import com.nju.edu.erp.model.po.saleReturn.SaleReturnSheetContentPO;
import com.nju.edu.erp.model.po.saleReturn.SaleReturnSheetPO;
import com.nju.edu.erp.model.vo.SaleDetailSheetVO;
import com.nju.edu.erp.model.vo.filter.BusinessProcessFilterVO;
import com.nju.edu.erp.model.vo.filter.SaleFilterVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author 201250208
 */
@Repository
@Mapper
public interface SaleReturnSheetDao {
    /**
     * 获取最近一条销售退货单
     * @return 最近一条销售退货单
     */
    SaleReturnSheetPO getLatest();

    /**
     * 存入一条XS退货单记录
     * @param toSave 一条XS退货单记录
     * @return 影响的行数
     */
    int save(SaleReturnSheetPO toSave);

    /**
     * 把XS退货单上的具体内容存入数据库
     * @param SaleReturnsSheetContent XS退货单上的具体内容
     */
    void saveBatch(List<SaleReturnSheetContentPO> SaleReturnsSheetContent);

    /**
     * 返回所有XS退货单
     * @return XS退货单列表
     */
    List<SaleReturnSheetPO> findAll();

    /**
     * 根据state返回XS退货单
     * @param state XS退货单状态
     * @return XS退货单列表
     */
    List<SaleReturnSheetPO> findAllByState(SaleReturnSheetState state);

    /**
     * 根据 purchaseReturnsSheetId 找到条目， 并更新其状态为state
     * @param saleReturnsSheetId 进货退货单id
     * @param state 进货退货单状态
     * @return 影响的条目数
     */
    int updateState(String saleReturnsSheetId, SaleReturnSheetState state);

    /**
     * 根据 saleReturnsSheetId 和 prevState 找到条目， 并更新其状态为state
     * @param saleReturnsSheetId XS退货单id
     * @param prevState XS退货单之前的状态
     * @param state XS退货单状态
     * @return 影响的条目数
     */
    int updateStateV2(String saleReturnsSheetId, SaleReturnSheetState prevState, SaleReturnSheetState state);

    /**
     * 通过saleReturnsSheetId找到对应条目
     * @param saleReturnsSheetId XS退货单id
     * @return XS退货单
     */
    SaleReturnSheetPO findSheetById(String saleReturnsSheetId);

    /**
     * 通过saleReturnsSheetId找到对应的content条目
     * @param saleReturnsSheetId
     * @return content条目列表
     */
    List<SaleReturnSheetContentPO> findContentSaleReturnsSheetId(String saleReturnsSheetId);

    /**
     * 通过saleSheetId找到退的货的对应批次
     * @param saleSheetId
     * @return 批次号
     */
    Integer findBatchId(String saleSheetId,String pid);

    /**
     * 获取销售明细表
     * @param saleFilterVO 筛选信息
     * @return
     */
    List<SaleDetailSheetVO> getDetailSaleSheet(SaleFilterVO saleFilterVO);

    List<SaleReturnSheetPO> findAllSheetByStateAndSaleMan(String employeeName, SaleReturnSheetState state, Date beginDateOfMonth, Date endDateOfMonth);

    List<SaleReturnSheetPO> getBusinessProcess(BusinessProcessFilterVO filterVO);

    List<SaleReturnSheetPO> getAllSheetByYearAndMonth(SaleReturnSheetState state, Date beginDateOfMonth, Date endDateOfMonth);

    SaleReturnSheetPO getOneBySaleSheetId(String saleSheetId);
}
