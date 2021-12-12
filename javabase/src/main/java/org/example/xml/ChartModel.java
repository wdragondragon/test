package org.example.xml;

/**
 * @Author JDragon
 * @Date 2021.10.30 下午 4:36
 * @Email 1061917196@qq.com
 * @Des:
 */

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author BM_hyjw
 * 图表要填充的数据格式
 */
@Slf4j
@Getter
@ToString
public class ChartModel {

    /**
     * 标记 用来记录当前是否是单元素的图表
     */
    private Boolean isSingle = true;

    /**
     * 内置表格页名
     */
    private String sheetName;

    /**
     * 图表
     */
    private XWPFChart xwpfChart;

    /**
     * 具体图
     */
    private List<XmlObject> xmlObjectList = new ArrayList<>();

    /**
     * 绘制区域图
     */
    private CTChart ctChart;

    /**
     * 标题
     */
    private List<String> titleList;

    /**
     * 数据源对应的key
     */
    private List<String> numberList;

    /**
     * 填充的数据源
     */
    private List<Map<String, String>> sourceModelList;

    /**
     * 目标数据
     */
    private List<ChartsEnum> chartsEnumList;

    /**
     * 赋值 替换目标
     *
     * @param numberList
     */
    public void setNumberList(List<String> numberList) {
        this.numberList = numberList;
    }

    /**
     * 赋值 数据源
     *
     * @param sourceModelList
     */
    public void setSourceModelList(List<Map<String, String>> sourceModelList) {
        this.sourceModelList = sourceModelList;
    }

    /**
     * 赋值 标题
     *
     * @param titleList
     */
    public void setTitleList(List<String> titleList) {
        this.titleList = titleList;
    }

    /**
     * 单个赋值 图表
     *
     * @param charts     所有可绘制区域
     * @param chartSeat  要操作的图表中可绘制区域位置
     * @param xmlObjSeat 图表在可绘制区域中的位置
     * @param chartsEnum 目标的表格类型
     */
    public void setSingleChart(List<XWPFChart> charts, int chartSeat, int xmlObjSeat, ChartsEnum chartsEnum) {
        List<ChartsEnum> chartsEnumList = Arrays.asList(chartsEnum);
        /**
         * 封装基础数据
         */
        this.packageBasic(charts, chartSeat, chartsEnumList);
        /**
         * 获得目标图表
         */
        XmlObject targetChart = chartsEnum.getTargetChart(chartSeat, this.ctChart, xmlObjSeat);
        this.xmlObjectList = Arrays.asList(targetChart);
        //当前是单元素
        this.isSingle = true;
    }

    /**
     * 组合赋值 图表
     *
     * @param charts         所有可绘制区域
     * @param chartSeat      要操作的图表中可绘制区域位置
     * @param xmlObjSeat     图表在可绘制区域中的位置
     * @param chartsEnumList 目标的表格类型
     */
    public void setComBiChart(List<XWPFChart> charts, int chartSeat, int xmlObjSeat, List<ChartsEnum> chartsEnumList) {
        /**
         * 封装基础数据
         */
        this.packageBasic(charts, chartSeat, chartsEnumList);
        /**
         * 获得目标图表
         */
        this.xmlObjectList.clear();
        chartsEnumList.stream().forEach(x -> {
            XmlObject targetChart = x.getTargetChart(chartSeat, this.ctChart, xmlObjSeat);
            this.xmlObjectList.add(targetChart);
        });
        //当前不是单元素
        this.isSingle = false;
    }

    /**
     * 封装部分基础数据
     *
     * @param charts
     * @param chartSeat
     * @param chartsEnumList
     */
    private void packageBasic(List<XWPFChart> charts, int chartSeat, List<ChartsEnum> chartsEnumList) {
        if (CollectionUtils.isEmpty(charts)) {
            throw new RuntimeException("模板中图表元素为null； ！！！ctChart:null");
        }
        if (CollectionUtils.isEmpty(chartsEnumList)) {
            throw new RuntimeException("图表目标为null；！！！chartsEnum:null");
        }
        /**
         * 目标
         */
        this.chartsEnumList = chartsEnumList;

        /**
         * 第N个位置图表
         */
        this.xwpfChart = charts.get(chartSeat);

        /**
         * 第N个位置可绘制区域的图表
         */
        this.ctChart = this.xwpfChart.getCTChart();
    }

    /**
     * 执行模板数据源填充
     *
     * @param sheetName 展示数据excel页名字
     */
    public void executeFillModel(String sheetName) throws IOException, InvalidFormatException {
        this.sheetName = sheetName;
        //异常校验
        String s = this.isSingle ? this.abnormalCheckSingle() : this.abnormalCheckComBi();
        //执行填充数据
        ChartsEnum.refreshExcel(this);
        for (int i = 0; i < chartsEnumList.size(); i++) {
            ChartsEnum chartsEnum = chartsEnumList.get(i);
            chartsEnum.fillModel(this, this.getXmlObjectList().get(i), i);
        }
    }


    /**
     * 异常校验
     */
    private String abnormalCheckSingle() {
        if (CollectionUtils.isEmpty(this.numberList)) {
            throw new RuntimeException("数据源比对为null； ！！！numberList:null");
        }
        if (CollectionUtils.isEmpty(this.titleList)) {
            throw new RuntimeException("标题为null； ！！！titleList:null");
        }
        if (CollectionUtils.isEmpty(this.sourceModelList)) {
            throw new RuntimeException("数据源为null； ！！！sourceModelList:null");
        }
        if (Objects.isNull(this.xwpfChart)) {
            throw new RuntimeException("模板中图表元素为null； ！！！xwpfChart:null");
        }
        if (CollectionUtils.isEmpty(this.xmlObjectList)) {
            throw new RuntimeException("模板中具体图表为null；！！！xmlObjectList:null");
        }
        if (CollectionUtils.isEmpty(this.chartsEnumList)) {
            throw new RuntimeException("图表目标为null；！！！chartsEnum:null");
        }
        if (Objects.isNull(this.ctChart)) {
            throw new RuntimeException("图表绘制区域为null；！！！chartsEnum:null");
        }
        if (StringUtils.isEmpty(this.sheetName)) {
            throw new RuntimeException("内置excel页名为null；！！！sheetName:null");
        }
        return null;
    }

    /**
     * 异常校验
     */
    private String abnormalCheckComBi() {
        this.abnormalCheckSingle();
        if (this.xmlObjectList.size() < 2) {
            throw new RuntimeException("组合图中【图表】元素不足两个； ！！！xmlObjectList.size !> 2");
        }
        if (this.sourceModelList.stream().filter(x -> {
            return x.keySet().size() >= 3;
        }).collect(Collectors.toList()).size() < 0) {
            throw new RuntimeException("组合图中【数据源】元素不足两个； ！！！sourceModelList.map.keySet.size !>= 3");
        }
        if (this.numberList.size() < 3) {
            throw new RuntimeException("组合图中【数据源对应的key】元素不足两个； ！！！numberList.size !>= 3");
        }
        return null;
    }
}