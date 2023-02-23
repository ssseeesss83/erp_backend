package com.nju.edu.erp.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.nju.edu.erp.model.po.ProductPO;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author 201250208
 * 完成对excel导出格式即相关内容属性配置
 */
public class ExcelUtil {

    /**
     * 导出 Excel
     * @param response
     * @param data 需导出的实体类列表
     * @param fileName  导出的文件名
     * @param sheetName  sheet页名
     * @param clazz  实体类字节码对象
     * @throws Exception
     */
    public static void writeExcel(HttpServletResponse response, List<?> data, String fileName, String sheetName, Class clazz) throws Exception {
        EasyExcel.write(getOutputStream(fileName, response), clazz)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet(sheetName)
                .doWrite(data);
    }

    /**
     * 由调用者控制excelWriter流, 导出多个sheet
     */
    public static void writeMultipleExcel(ExcelWriter excelWriter, List<?> date, String sheetName, Class clazz) {
        WriteSheet writeSheet = EasyExcel
                .writerSheet(sheetName)
                .head(clazz)
                .build();
        excelWriter.write(date,writeSheet);
    }

    public static ServletOutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        response.addHeader("Access-Control-Expose-Headers", "Content-disposition");
        return response.getOutputStream();
    }

    public static void read(ExcelReader excelReader, String sheetName, AnalysisEventListener<?> listener, Class clazz){
        ReadSheet readSheet = EasyExcel
                .readSheet(sheetName)
                .head(clazz)
                .registerReadListener(listener)
                .build();
        excelReader.read(readSheet);
    }
}
