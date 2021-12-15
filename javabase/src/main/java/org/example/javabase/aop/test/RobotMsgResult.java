package org.example.javabase.aop.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.09.06 20:57
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RobotMsgResult {

    private String message;

    private Long senderId;

    private Long acceptId;

    private String param;
}
