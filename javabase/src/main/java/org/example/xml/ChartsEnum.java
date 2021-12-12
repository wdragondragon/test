package org.example.xml;

/**
 * @Author JDragon
 * @Date 2021.10.30 下午 4:36
 * @Email 1061917196@qq.com
 * @Des:
 */

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.impl.CTBarChartImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.impl.CTLineChartImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.impl.CTPieChartImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.impl.CTScatterChartImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author BM_hyjw
 * 解析world 中图表
 */
@Slf4j
@Getter
public enum ChartsEnum {

    /**
     * 饼图
     */
    PIE(CTPieChart.class, CTPieChartImpl.class) {
        /**
         * 填充模板数据
         * @param chartModel 图表和数据源
         * @param xmlObject 当前元素
         * @param bias 偏向值
         */
        @Override
        public void fillModel(ChartModel chartModel, XmlObject xmlObject, int bias) {
            if (!this.chartImplClazz.getName().equals(xmlObject.getClass().getName())) {
                //当前循环中图表操作不属于当前枚举
                ChartsEnum.getEnumByChartImplClazz(((XmlComplexContentImpl) xmlObject).getClass())
                        .fillModel(chartModel, xmlObject, bias);
                return;
            }
            CTPieChart pieChart = (CTPieChart) xmlObject;
            List<CTPieSer> serList = pieChart.getSerList();
            //更新数据区域
            for (int i = 0; i < serList.size(); i++) {
                //数据填充
                //
                CTPieSer ser = pieChart.getSerArray(i);
                CTAxDataSource cat = ser.getCat();
                CTNumDataSource val = ser.getVal();
                this.dataAnalysisFill(chartModel, i, bias, cat, val);
            }
        }

        /**
         * 得到目标位置的图表
         * @param ctChart 可绘制区域图表
         * @param xmlObjSeat 目标图标位置位置
         * @return
         */
        @Override
        public XmlObject getTargetChart(int chartSeat, CTChart ctChart, int xmlObjSeat) {
            try {
                CTPieChart pieChart = ctChart.getPlotArea().getPieChartArray(xmlObjSeat);
                return pieChart;
            } catch (Exception e) {
                throw new RuntimeException("当前位置【" + chartSeat + "】不存在【饼图】！！！");
            }
        }
    },

    /**
     * 柱图
     */
    COLUMN(CTBarChart.class, CTBarChartImpl.class) {
        /**
         * 填充模板数据
         * @param chartModel 图表和数据源
         * @param bias 偏向值
         */
        @Override
        public void fillModel(ChartModel chartModel, XmlObject xmlObject, int bias) {
            if (!this.chartImplClazz.getName().equals(xmlObject.getClass().getName())) {
                //当前循环中图表操作不属于当前枚举
                ChartsEnum.getEnumByChartImplClazz(((XmlComplexContentImpl) xmlObject).getClass())
                        .fillModel(chartModel, xmlObject, bias);
                return;
            }
            CTBarChart chart = (CTBarChart) xmlObject;
            List<CTBarSer> serList = chart.getSerList();
            //更新数据区域
            for (int i = 0; i < serList.size(); i++) {
                //数据填充
                //
                CTBarSer ser = chart.getSerArray(i);
                CTAxDataSource cat = ser.getCat();
                CTNumDataSource val = ser.getVal();
                this.dataAnalysisFill(chartModel, i, bias, cat, val);
            }
        }

        /**
         * 得到目标位置的图表
         * @param ctChart 可绘制区域图表
         * @param xmlObjSeat 目标图标位置位置
         * @return
         */
        @Override
        public XmlObject getTargetChart(int chartSeat, CTChart ctChart, int xmlObjSeat) {
            try {
                CTBarChart barChart = ctChart.getPlotArea().getBarChartArray(xmlObjSeat);
                return barChart;
            } catch (Exception e) {
                throw new RuntimeException("当前位置【" + chartSeat + "】不存在【柱状图】！！！");
            }
        }
    },

    /**
     * 折线图
     */
    LINE_CHART(CTLineChart.class, CTLineChartImpl.class) {
        /**
         * 填充模板数据
         * @param chartModel 图表和数据源
         * @param xmlObject 当前元素
         * @param bias 偏向值
         */
        @Override
        public void fillModel(ChartModel chartModel, XmlObject xmlObject, int bias) {
            if (!this.chartImplClazz.getName().equals(xmlObject.getClass().getName())) {
                //当前循环中图表操作不属于当前枚举
                ChartsEnum.getEnumByChartImplClazz(((XmlComplexContentImpl) xmlObject).getClass())
                        .fillModel(chartModel, xmlObject, bias);
                return;
            }
            CTLineChart chart = (CTLineChart) xmlObject;
            List<CTLineSer> serList = chart.getSerList();
            //更新数据区域
            for (int i = 0; i < serList.size(); i++) {
                //数据填充
                //
                CTLineSer ser = chart.getSerArray(i);
                CTAxDataSource cat = ser.getCat();
                CTNumDataSource val = ser.getVal();
                this.dataAnalysisFill(chartModel, i, bias, cat, val);
            }
        }

        /**
         * 得到目标位置的图表
         * @param ctChart 可绘制区域图表
         * @param xmlObjSeat 目标图标位置位置
         * @return
         */
        @Override
        public XmlObject getTargetChart(int chartSeat, CTChart ctChart, int xmlObjSeat) {
            try {
                CTLineChart lineChart = ctChart.getPlotArea().getLineChartArray(xmlObjSeat);
                return lineChart;
            } catch (Exception e) {
                throw new RuntimeException("当前位置【" + chartSeat + "】不存在【折线图】！！！");
            }
        }
    },

    /**
     * 散点图
     */
    SCATTER(CTScatterChart.class, CTScatterChartImpl.class) {
        /**
         * 填充模板数据
         * @param chartModel 图表和数据源
         * @param xmlObject 当前元素
         * @param bias 偏向值
         */
        @Override
        public void fillModel(ChartModel chartModel, XmlObject xmlObject, int bias) {
            if (!this.chartImplClazz.getName().equals(xmlObject.getClass().getName())) {
                //当前循环中图表操作不属于当前枚举
                ChartsEnum.getEnumByChartImplClazz(((XmlComplexContentImpl) xmlObject).getClass())
                        .fillModel(chartModel, xmlObject, bias);
                return;
            }
            CTScatterChart chart = (CTScatterChart) xmlObject;
            List<CTScatterSer> serList = chart.getSerList();
            //更新数据区域
            for (int i = 0; i < serList.size(); i++) {
                //数据填充
                //
                CTScatterSer ser = chart.getSerArray(i);
                CTAxDataSource cat = ser.getXVal();
                CTNumDataSource val = ser.getYVal();
                this.dataAnalysisFill(chartModel, i, bias, cat, val);
            }
        }

        /**
         * 得到目标位置的图表
         * @param ctChart 可绘制区域图表
         * @param xmlObjSeat 目标图标位置位置
         * @return
         */
        @Override
        public XmlObject getTargetChart(int chartSeat, CTChart ctChart, int xmlObjSeat) {
            try {
                CTScatterChart scatterChart = ctChart.getPlotArea().getScatterChartArray(xmlObjSeat);
                return scatterChart;
            } catch (Exception e) {
                throw new RuntimeException("当前位置【" + chartSeat + "】不存在【散点图】！！！");
            }
        }
    },

    ;
    /**
     * 图表对象
     */
    public Class<? extends XmlObject> chartClazz;

    /**
     * 图表实现对象
     */
    public Class<? extends XmlComplexContentImpl> chartImplClazz;

    ChartsEnum(Class<? extends XmlObject> chartClazz,
               Class<? extends XmlComplexContentImpl> chartImplClazz) {
        this.chartClazz = chartClazz;
        this.chartImplClazz = chartImplClazz;
    }


    /**
     * 填充模板数据
     *
     * @param chartModel 图表和数据源
     * @param xmlObject  当前元素
     * @param bias       偏向值
     */
    public abstract void fillModel(ChartModel chartModel, XmlObject xmlObject, int bias);

    /**
     * 得到目标位置的图表
     *
     * @param chartSeat  位置
     * @param ctChart    可绘制区域图表
     * @param xmlObjSeat 目标图标位置位置
     */
    public abstract XmlObject getTargetChart(int chartSeat, CTChart ctChart, int xmlObjSeat);


    /**
     * 根据值来源得到对应的 图表实现对象
     *
     * @param chartImplClazz 图表实现对象
     * @return
     */
    public static ChartsEnum getEnumByChartImplClazz(Class<? extends XmlComplexContentImpl> chartImplClazz) {
        for (ChartsEnum value : ChartsEnum.values()) {
            if (value.getChartImplClazz().equals(chartImplClazz)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 根据值来源得到对应的 图表对象
     *
     * @param chartClazz 图表对象
     * @return
     */
    public static ChartsEnum getEnumByChartClazz(Class<? extends XmlObject> chartClazz) {
        for (ChartsEnum value : ChartsEnum.values()) {
            if (value.getChartClazz().equals(chartClazz)) {
                return value;
            }
        }
        return null;
    }


    /**
     * 刷新内置excel数据
     *
     * @return
     */
    public static boolean refreshExcel(ChartModel chartModel) throws IOException, InvalidFormatException {
        List<String> titleList = chartModel.getTitleList();
        List<String> numberList = chartModel.getNumberList();
        List<Map<String, String>> sourceModelList = chartModel.getSourceModelList();
        XWPFChart xwpfChart = chartModel.getXwpfChart();
        boolean result = true;
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(chartModel.getSheetName());
        //根据数据创建excel第一行标题行
        for (int i = 0; i < titleList.size(); i++) {
            if (sheet.getRow(0) == null) {
                sheet.createRow(0).createCell(i).setCellValue(titleList.get(i) == null ? "" : titleList.get(i));
            } else {
                sheet.getRow(0).createCell(i).setCellValue(titleList.get(i) == null ? "" : titleList.get(i));
            }
        }

        //遍历数据行
        for (int i = 0; i < sourceModelList.size(); i++) {
            Map<String, String> baseFormMap = sourceModelList.get(i);//数据行
            //fldNameArr字段属性
            for (int j = 0; j < numberList.size(); j++) {
                if (sheet.getRow(i + 1) == null) {
                    if (j == 0) {
                        try {
                            sheet.createRow(i + 1)
                                    .createCell(j)
                                    .setCellValue(baseFormMap.get(numberList.get(j)) == null ?
                                            "" : baseFormMap.get(numberList.get(j)));
                        } catch (Exception e) {
                            if (baseFormMap.get(numberList.get(j)) == null) {
                                sheet.createRow(i + 1).createCell(j).setCellValue("");
                            } else {
                                sheet.createRow(i + 1)
                                        .createCell(j)
                                        .setCellValue(baseFormMap.get(numberList.get(j)));
                            }
                        }
                    }
                } else {
                    BigDecimal b = new BigDecimal(baseFormMap.get(numberList.get(j)));
                    double value = 0D;
                    if (b != null) {
                        value = b.doubleValue();
                    }
                    if (value == 0D) {
                        sheet.getRow(i + 1).createCell(j);
                    } else {
                        sheet.getRow(i + 1).createCell(j).setCellValue(b.doubleValue());
                    }
                }
            }

        }
        // 更新嵌入的workbook

        List<POIXMLDocumentPart> pxdList = xwpfChart.getRelations();
        if (pxdList != null && pxdList.size() > 0) {
            for (int i = 0; i < pxdList.size(); i++) {
                if (pxdList.get(i).toString().contains("sheet")) {
                    POIXMLDocumentPart xlsPart = xwpfChart.getRelations().get(0);
                    OutputStream xlsOut = xlsPart.getPackagePart().getOutputStream();

                    try {
                        wb.write(xlsOut);
                        xlsOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        result = false;
                    } finally {
                        if (wb != null) {
                            try {
                                wb.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                result = false;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }


    /**
     * 数据分析
     *
     * @param chartModel
     * @param i
     * @param bias
     * @param cat
     * @param val
     */
    public void dataAnalysisFill(ChartModel chartModel,
                                 int i,
                                 int bias,
                                 CTAxDataSource cat,
                                 CTNumDataSource val) {
        //数据源
        List<Map<String, String>> sourceModelList = chartModel.getSourceModelList();
        //数据源key
        List<String> numberList = chartModel.getNumberList();
        //
        CTStrData strData = cat.getStrRef().getStrCache();
        CTNumData numData = val.getNumRef().getNumCache();

        long idx = 0;
        for (int j = 0; j < sourceModelList.size(); j++) {
            //判断获取的值是否为空
            String value = "0";
            if (new BigDecimal(sourceModelList.get(j).get(numberList.get(i + 1))) != null) {
                value = new BigDecimal(sourceModelList.get(j).get(numberList.get(i + 1))).toString();
            }
            if (!"0".equals(value)) {
                CTNumVal numVal = numData.addNewPt();//序列值
                numVal.setIdx(idx);
                numVal.setV(value);
            }
            CTStrVal sVal = strData.addNewPt();//序列名称
            sVal.setIdx(idx);
            sVal.setV(sourceModelList.get(j).get(numberList.get(0)));
            idx++;
        }
        numData.getPtCount().setVal(idx);
        strData.getPtCount().setVal(idx);

        //赋值横坐标数据区域
        String axisDataRange = new
                CellRangeAddress(1, sourceModelList.size(), 0, 0)
                .formatAsString(chartModel.getSheetName(), false);
        cat.getStrRef().setF(axisDataRange);

        //赋值纵坐标数据区域
        String numDataRange = new
                CellRangeAddress(1, sourceModelList.size(), i + 1 + bias, i + 1 + bias)
                .formatAsString(chartModel.getSheetName(), false);
        val.getNumRef().setF(numDataRange);
    }

}