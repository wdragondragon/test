package org.example.javabase.xml;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author JDragon
 * @Date 2021.10.31 上午 10:53
 * @Email 1061917196@qq.com
 * @Des:
 */
public class WordUtil {

    XWPFDocument xwpfDocument;

    WordUtil(String filename) throws IOException {
        File tpl = new File(filename);
        if (tpl.exists()) {//模板文件存在
            //读取模板文件
            FileInputStream is = new FileInputStream(tpl);
            xwpfDocument = new XWPFDocument(is);

            //使用xwpfDocument对象操作word文档
            //。。。。。。。。。。。。。

        }
    }

    public void writer(String filename) throws IOException {
        xwpfDocument.write(new FileOutputStream(filename));
    }

    /**
     * @param dataMap（数据源）
     * @Method doParagraph
     * @Description 替换段落内容 (格式有要求：{字段} 为三个文本对象，找以'{'开始 '}'结尾 中间匹配的字段替换值)
     * @Return void
     * @Exception
     */
    public void doParagraph(Map<String, Object> dataMap) throws InvalidFormatException, IOException {
        if (dataMap != null && dataMap.size() > 0) {
            List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                int leftIndex = -1;//'{'的位置索引
                int rightIndex = -1;//'}'的位置索引
                List<XWPFRun> runs = paragraph.getRuns();
                int runIndex = 0;//当前位置游标
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    if (text != null) {
                        if (text.contains("{")) {
                            leftIndex = runIndex;
                        }
                        if (text.contains("}")) {
                            rightIndex = runIndex;
                        }
                        if (leftIndex > -1 && leftIndex <= rightIndex) {
                            //遍历{}之间的文本
                            for (int i = leftIndex; i <= rightIndex; i++) {
                                XWPFRun thisrun = runs.get(i);
                                Object value = dataMap.get(thisrun.getText(0));
                                if (value != null) {
                                    thisrun.setText(value.toString(), 0);
                                }
                            }
                            leftIndex = -1;
                            rightIndex = -1;
                        }
                    }
                    runIndex++;
                }
            }
        }
    }

    /**
     * @param index（表格索引：第几个表格），dataMap（数据源）
     * @Method doTable
     * @Description 替换表格内容
     * @Return void
     * @Exception
     */
    public void doTable(int index, List<Map<String, Object>> dataMap) throws InvalidFormatException, IOException {
        XWPFTable table = xwpfDocument.getTables().get(index);

        if (dataMap != null && dataMap.size() > 0) {
            List<XWPFTableRow> rows = table.getRows();
            int rowIndex = 0;//寻找字段绑定行索引
            String[] fields = null;////字段绑定行字段顺序（a,b,c）
            for (XWPFTableRow row : rows) {
                List<XWPFTableCell> cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    String key = cell.getText()
                            .replaceAll("\\{", "")
                            .replaceAll("}", "");

                    if (dataMap.get(0).get(key) != null) {//找到匹配
                        fields = new String[cells.size()];
                        break;
                    }
                }
                if (fields != null && fields.length > 0) {//找到,并获取字段
                    for (int i = 0; i < fields.length; i++) {
                        fields[i] = cells.get(i)
                                .getText()
                                .replaceAll("\\{", "")
                                .replaceAll("}", "");
                    }
                    break;
                } else {
                    rowIndex++;
                }
            }
            if (rowIndex >= 0 && fields != null) {
                //从字段绑定行开始插入
                for (Map<String, Object> rowdata : dataMap) {
                    XWPFTableRow row = null;
                    try {
                        row = rows.get(rowIndex);
                    } catch (Exception e) {
                        row = table.createRow();
                    }
                    if (row != null) {
                        List<XWPFTableCell> cells = row.getTableCells();
                        int cellIndex = 0;
                        for (XWPFTableCell cell : cells) {
                            cell.removeParagraph(0);
                            XWPFParagraph newPara = cell.addParagraph();
                            XWPFRun run = newPara.createRun();

                            Object value = rowdata.get(fields[cellIndex]);
                            if (value != null) {
                                run.setText(value.toString());
                            }

                            cellIndex++;
                        }
                    }
                    rowIndex++;
                }
            }
        }
    }

    /**
     * @param index（图表索引），categoryField 分类轴字段,seriesField 系列轴字段，dataMap（数据源）
     * @Method doChart
     * @Description 替换图表内容
     * @Return void
     * @Exception
     */
    public void doChart(int index,
                        String categoryField,//{"a"}
                        String[] seriesField, //{"b","c"}
                        List<Map<String, Object>> dataMap) throws InvalidFormatException, IOException {
        List<XWPFChart> charts = xwpfDocument.getCharts();
        for (XWPFChart chart : charts) {
            //根据图表索引
            String partname = chart.getPackagePart().getPartName().getName();
            if (partname.contains("chart" + (index + 1) + ".xml")) {

                String[] categories = new String[dataMap.size()];
                Number[][] seriesvalues = new Number[seriesField.length][dataMap.size()];

                int dataIndex = 0;
                for (Map<String, Object> rowdata : dataMap) {
                    categories[dataIndex] = rowdata.get(categoryField).toString();
                    int serieIndex = 0;
                    for (String field : seriesField) {
                        Object value = rowdata.get(field);
                        if (value != null) {
                            seriesvalues[serieIndex][dataIndex] = (Number) (value);
                        }
                        serieIndex++;
                    }
                    dataIndex++;
                }

                XDDFChartData chartData = chart.getChartSeries().get(0);//目前只做1个chartData情况

                String categoryDataRange = chart.formatRange(new CellRangeAddress(1, categories.length, 0, 0));
                XDDFDataSource<?> categoriesData = XDDFDataSourcesFactory.fromArray(categories, categoryDataRange, 0);

                List<XDDFChartData.Series> series = chartData.getSeries();
                int serieIndex = 0;
                for (XDDFChartData.Series serie : series) {
                    String valuesDataRange = chart.formatRange(new CellRangeAddress(1, categories.length, serieIndex + 1, serieIndex + 1));
                    XDDFNumericalDataSource<? extends Number> valuesData = XDDFDataSourcesFactory.fromArray(seriesvalues[serieIndex], valuesDataRange, serieIndex + 1);
                    //赋值
                    serie.replaceData(categoriesData, valuesData);
                    serie.plot();

                    serieIndex++;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        WordUtil wordUtil = new WordUtil("C:\\Users\\10619\\Desktop\\hehe2.docx");
        List<Map<String, Object>> sourceModelList = new ArrayList<>();
        Map<String, Object> publicEm = new HashMap<>();
        publicEm.put("系列一", "8.2");
        Map<String, Object> publicEm1 = new HashMap<>();
        publicEm1.put("系列一", "3.2");
        Map<String, Object> publicEm2 = new HashMap<>();
        publicEm2.put("系列一", "1.4");
        Map<String, Object> publicEm3 = new HashMap<>();
        publicEm3.put("系列一", "1.2");

        sourceModelList.add(publicEm);
        sourceModelList.add(publicEm1);
        sourceModelList.add(publicEm2);
        sourceModelList.add(publicEm3);

        wordUtil.doChart(0, "系列一", new String[]{"第一季度", "第二季度", "第三季度", "第四季度"}, sourceModelList);
        wordUtil.writer("C:\\Users\\10619\\Desktop\\test2.docx");
    }
}
