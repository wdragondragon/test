package org.example.operate.consts;

import lombok.Getter;
import org.example.operate.TransFormerLambda;
import org.example.operate.util.StringUtil;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.08 16:38
 * @Description:
 */
public enum Transformer {

    ADD_DEFAULT_VALUE("add_default_value", StringUtil::defaultValue,1),
    TEST("test",StringUtil::addValue,2);

    @Getter
    private String operateName;//算子名称
    @Getter
    private TransFormerLambda<String,List<String>> transformer;//算子操作接口
    @Getter
    private Integer paramSize; //算子参数数量

    Transformer(String operateName, TransFormerLambda<String,List<String>> transformer, Integer paramSize){
        this.operateName = operateName;
        this.transformer = transformer;
        this.paramSize = paramSize;
    }
}
