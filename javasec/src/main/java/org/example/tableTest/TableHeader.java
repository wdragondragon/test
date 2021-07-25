package org.example.tableTest;

import java.lang.annotation.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.06.23 22:32
 * @Description:
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableHeader {

    String value(); // 字段中文名称

    int width() default 100; // 宽度

    boolean sortable() default false; // 是否可排序
}