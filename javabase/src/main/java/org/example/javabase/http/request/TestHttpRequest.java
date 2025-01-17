package org.example.javabase.http.request;

import java.io.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 15:49
 * @Description:
 */
public class TestHttpRequest {
    public static void main(String[] args) throws IOException {
        String invoke = HttpUtil.init().url("http://localhost:8081/test/http").get();
        System.out.println(invoke);


        invoke = HttpUtil.init()
                .url("http://localhost:8081/test/http2")
                .addParam("id", "1").get();
        System.out.println(invoke);


        invoke = HttpUtil.init()
                .url("http://localhost:8081/test/http4").post();
        System.out.println(invoke);

        invoke = HttpUtil.init()
                .url("http://localhost:8081/test/http6")
                .addParam("id", "1").post();
        System.out.println(invoke);


        invoke = HttpUtil.initJson()
                .url("http://localhost:8081/test/http7")
                .body("{\"id\":1}").post();
        System.out.println(invoke);
    }
}
