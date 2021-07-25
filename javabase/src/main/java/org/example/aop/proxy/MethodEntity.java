package org.example.aop.proxy;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.16 10:51
 * @Description:
 */
public class MethodEntity {
    /**
     * 方法所属全类名
     **/
    private String className;

    /**
     * 方法名
     **/
    private String methodName;

    /**
     * 方法参数类型全类名列表
     **/
    private List<String> paramList;

    /**
     * 返回类型全类名
     **/
    private String retType;

    /**
     * 返回的基本类型强转包装类的全类名
     **/
    private String transferType;


    public MethodEntity(Class<?> clazz, String methodName, List<String> paramList, Class<?> retType) {
        if (paramList == null) {
            paramList = new ArrayList<>();
        }
        this.className = clazz.getName();
        this.methodName = methodName;
        this.paramList = paramList;
        this.retType = retType.getName();
        this.transferType = TypeReturn.returnPrimitive(retType).getName();
    }

    public MethodEntity() {

    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParamList() {
        return paramList;
    }

    public void setParamList(List<String> paramList) {
        this.paramList = paramList;
    }


    @Override
    public String toString() {
        return "MethodEntity{" +
                "methodName='" + methodName + '\'' +
                ", paramList=" + paramList +
                '}';
    }

    public String getRetType() {
        return retType;
    }

    public void setRetType(Class<?> retType) {
        this.retType = retType.getName();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(Class<?> clazz) {
        this.className = clazz.getName();
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(Class<?> transferType) {
        this.transferType = TypeReturn.returnPrimitive(transferType).getName();
    }
}
