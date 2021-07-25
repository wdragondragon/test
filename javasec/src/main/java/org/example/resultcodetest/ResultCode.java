package org.example.resultcodetest;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.06.23 22:08
 * @Description:
 */
public enum ResultCode {

    NORMAL(20000L, "正常响应"),
    AUTH_FAIL(20001L, "认证失败"),
    PERMISSIONS_NOT_ENOUGH(20002L, "权限不足"),
    SYSTEM_ERROR(20003L, "预期异常"),
    PARAMS_ERROR(20004L,"参数非法"),
    SYSTEM_UN_KNOW_ERROR(20005L,"超预期异常");
    @Setter
    @Getter
    private Long code;

    @Setter
    @Getter
    private String message;

    ResultCode(Long code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }
}