package org.example.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.16 10:06
 * @Description:
 */
@Data
public class User {

    @ExcelProperty("名字")
    private String name;

}
