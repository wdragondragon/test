package org.example.javabase.operate;

import org.example.javabase.operate.consts.Transformer;
import java.text.MessageFormat;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.08 17:12
 * @Description: 算子工具类
 */
public class TransformerUtil {

    /**
     * @param transformerName 算子名称
     * @param fieldValue 需要算子计算的值
     * @param paras 算子计算需要的参数
     * @return 经过算子处理后的字段结果
     * @throws Exception
     */
    public static String operate(String transformerName,String fieldValue,List<String> paras) throws Exception {
        for (Transformer transformerEntity : Transformer.values()) {
            if(transformerEntity.getOperateName().equals(transformerName)){
                checkFieldNum(transformerName,paras, transformerEntity.getParamSize());
                return transformerEntity.getTransformer().operate(fieldValue,paras);
            }
        }
        throw new Exception("没有这个算子");
    }

    /**
     * @param transformerName 算子名称，日志输出需要
     * @param paras 算子参数
     * @param fieldSize 该算子需要的参数数量
     * @throws Exception
     * @des 算子参数校验
     */
    public static void checkFieldNum(String transformerName,List<String> paras,Integer fieldSize) throws Exception {
        if(fieldSize==0){
            return;
        }
        if(paras.size()!=fieldSize){
            throw new Exception(MessageFormat.format("{0}:算子参数缺失！", transformerName));
        }
        for (String para : paras) {
            if(para==null){
                throw new Exception(MessageFormat.format("{0}:算子参数不能为空！",transformerName));
            }
        }
    }


}
