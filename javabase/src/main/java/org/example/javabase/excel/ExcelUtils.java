package org.example.javabase.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.16 10:31
 * @Description:
 */
public class ExcelUtils {

    public static <T> ExcelReaderBuilder readBuilder(FileInputStream fileInputStream, Class<T> clazz) {
        return EasyExcel.read(fileInputStream).head(clazz);
    }

    public static <T> List<T> simpleRead(FileInputStream fileInputStream, Class<T> clazz) {
        SimpleExcelListener<T> dataListener = new SimpleExcelListener<>();
        readBuilder(fileInputStream, clazz)
                .registerReadListener(dataListener)
                .sheet()
                .doRead();
        return dataListener.getResultData();
    }

    public static <T> List<T> readSheet(FileInputStream fileInputStream, String sheetName, Class<T> clazz) {
        ExcelReader excelReader = null;
        SimpleExcelListener<T> simpleExcelListener = new SimpleExcelListener<>();
        try {
            excelReader = readBuilder(fileInputStream, clazz).build();
            ReadSheet readSheet = EasyExcel.readSheet(sheetName)
                    .registerReadListener(simpleExcelListener)
                    .build();
            excelReader.read(readSheet);
            return simpleExcelListener.getResultData();
        } finally {
            if (excelReader != null) {
                excelReader.finish();
            }
        }
    }

    public static <T> void simpleWrite(FileOutputStream fileOutputStream,String sheetName,Class<T> tClass){
        ExcelWriter excelWriter = EasyExcel.write(fileOutputStream).excelType(ExcelTypeEnum.XLSX).build();
        WriteSheet writeSheet = EasyExcel.writerSheet(0,sheetName)
                .head(tClass)
                .registerWriteHandler(getSheetStyleStrategy())
                .build();
        excelWriter.write(null, writeSheet);
        excelWriter.finish();
    }


    /**
     * 获取excel表头统一样式
     *
     * @return
     */
    private static HorizontalCellStyleStrategy getSheetStyleStrategy() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.DARK_TEAL.index);
        //设置样式
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("Dejavu Sans Mono");
        /*headWriteFont.setBold(false);*/
        headWriteFont.setColor(IndexedColors.WHITE.index);
        headWriteFont.setFontHeightInPoints((short) 11);
        headWriteCellStyle.setWriteFont(headWriteFont);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, new WriteCellStyle());
    }
}
