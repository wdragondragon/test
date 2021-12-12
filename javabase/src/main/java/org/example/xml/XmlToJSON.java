package org.example.xml;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hjs
 * @version 1.0
 * @date 2020/7/10 16:50
 */
public class XmlToJSON {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        //获取word模板
        InputStream docis = new FileInputStream("C:\\Users\\10619\\Desktop\\hehe2.docx");
        //转成word
        XWPFDocument doc = new XWPFDocument(docis);
        //获取word中所有图表对象
        List<XWPFChart> charts = doc.getCharts();
        //数据源
        ChartModel chartModel = new ChartModel();
//标题
        List<String> titleList = new ArrayList<String>();
        titleList.add("value1");
        titleList.add("value2");
        chartModel.setTitleList(titleList);
        //字段名
        List<String> numberList = new ArrayList<String>();
        numberList.add("value1");
        numberList.add("value2");
        chartModel.setNumberList(numberList);
        //记录某本书销售多少册
        List<Map<String, String>> sourceModelList = new ArrayList<>();
        Map<String, String> publicEm = new HashMap<>();
        publicEm.put("value1", "第一季度");
        publicEm.put("value2", "8.2");
        Map<String, String> publicEm1 = new HashMap<>();
        publicEm1.put("value1", "第二季度");
        publicEm1.put("value2", "3.2");
        Map<String, String> publicEm2 = new HashMap<>();
        publicEm2.put("value1", "第三季度");
        publicEm2.put("value2", "1.4");
        Map<String, String> publicEm3 = new HashMap<>();
        publicEm3.put("value1", "第四季度");
        publicEm3.put("value2", "1.2");
        sourceModelList.add(publicEm);
        sourceModelList.add(publicEm1);
        sourceModelList.add(publicEm2);
        sourceModelList.add(publicEm3);
        chartModel.setSourceModelList(sourceModelList);

        //得到模板中第 N 个位置的图表可绘制区域中 条形图
//        chartModel.setSingleChart(charts,2,0,ChartsEnum.COLUMN);
//        chartModel.executeFillModel("sheet1");

        //得到模板中第 N 个位置的图表可绘制区域中 饼状图
        chartModel.setSingleChart(charts,0,0,ChartsEnum.PIE);
        chartModel.executeFillModel("sheet1");
//
//        //得到模板中第 N 个位置的图表可绘制区域中 折线图
//        chartModel.setSingleChart(charts,4,0,ChartsEnum.LINE_CHART);
//        chartModel.executeFillModel("sheet1");
        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\10619\\Desktop\\test2.docx")) {
            doc.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
