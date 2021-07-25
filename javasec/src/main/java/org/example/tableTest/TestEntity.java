package org.example.tableTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.06.23 22:33
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {

    @TableHeader("名字")
    public String name;

    @TableHeader("年龄")
    public int age;

    @TableHeader("身份证")
    public String idCard;
}
