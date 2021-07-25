package org.example.tableTest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.06.23 23:01
 * @Description:
 */
public class TableRef<T> {

    /** 泛型参数 */
    private final Type type;

    /**
     * table body 数据
     */
    private List<T> list;

    /**
     * 构造
     */
    public TableRef() {
        this(new ArrayList<>());
    }

    public TableRef(List<T> list) {
        this.list = list;
        this.type = TableUtils.getTypeArgument(getClass());
    }


    /**
     * 获取用户定义的泛型参数
     *
     * @return 泛型参数
     */
    public Type getType() {
        return this.type;
    }

    /**
     * 获取泛型 T 的集合
     */
    public List<T> getList() {
        return list;
    }

    /**
     * 设置 list 集合
     */
    public TableRef<T> list(List<T> list) {
        this.list = list;
        return this;
    }

    @Override
    public String toString() {
        return type.getTypeName();
    }
}