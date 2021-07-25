package org.example.operate;

import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.08 17:18
 * @Description:
 */
@FunctionalInterface
public interface TransFormerLambda<T,V> {
    T operate(T fieldValue, V paras);
}
