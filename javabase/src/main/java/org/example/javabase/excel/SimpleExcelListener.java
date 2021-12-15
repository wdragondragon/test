package org.example.javabase.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.16 10:06
 * @Description:
 */
public class SimpleExcelListener<T> extends AnalysisEventListener<T> {

    @Getter
    List<T> resultData = new ArrayList<>();

    @Override
    public void invoke(T data, AnalysisContext context) {
        this.resultData.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
