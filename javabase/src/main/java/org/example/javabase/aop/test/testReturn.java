package org.example.javabase.aop.test;

import com.jdragon.common.response.normal.Result;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.21 12:35
 * @Description:
 */
public class testReturn {
    public static void main(String[] args) throws Exception {

        IHttp iHttp = new DynaProxyHttp().bindInterface(IHttp.class);

        Result<RobotMsgResult> http = iHttp.http();

        System.out.println(http.getResult());
    }
}
