package com.nju.edu.erp.model.vo.warehouse;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.nju.edu.erp.config.converter.ProductInfoVOConverter;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 修改以完成导出Excel任务
 * @author 201250208
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseCountingVO {
    /**
     * 库存id
     */
    @ExcelProperty(value = "库存id",index = 0)
    private Integer id;

    /**
     * 商品编号
     */
    @ExcelProperty(value = "商品编号",index = 1,converter = ProductInfoVOConverter.class)
    private ProductInfoVO product;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量",index = 2)
    private Integer quantity;

    /**
     * 进价
     */
    @ExcelProperty(value = "进价",index = 3)
    private BigDecimal purchasePrice;

    /**
     * 批次
     */
    @ExcelProperty(value = "批次",index = 4)
    private Integer batchId;

    /**
     * 出厂日期
     */
    @ExcelProperty(value = "出厂日期",index = 5)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date productionDate;
}
