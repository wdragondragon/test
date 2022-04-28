/*
 * Copyright (c) 2001-2020 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package org.example.javabase.health;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.example.javabase.health.Constant.*;


public class APIDemoTest {
    /**
     * 具体业务参数
     * <p>
     * host
     * 测试环境：
     * https://openapi.guahao-test.com/openapi  对应测试环境的AccessKey ID/AccessKey Secret
     * <p>
     * 预发环境（请谨慎使用，仅供测试验证使用）：
     * http://pre-openapi.guahao.com/openapi  对应生产环境的AccessKey ID/AccessKey Secret
     * <p>
     * 生产环境：
     * http://openapi.guahao.com/openapi  对应测试环境的AccessKey ID/AccessKey Secret
     */
    private static String host = "https://openapi.guahao.com/openapi";
    /**
     * 测试与生产环境不同，访问凭证，也就是微医云控制台“权限管理-人员权限管理”菜单下针对子账号的设置的访问凭证的AccessKey ID
     * TODO 请补充填入
     */
    private static String appKey = "wyKY837Qn199Y8S0";
    /**
     * 测试与生产环境不同，AccessKey ID 对应的AccessKey Secret
     * TODO 请补充填入
     */
    private static String appSecret = "wY2x79Uy333j24GFn7dJC629fkDG2406";
    /**
     * 微医云控制台“权限管理-人员权限管理”菜单下针对子账号的角色管理中“API预定义角色”或“API自定义角色”中关联API的id
     * TODO 请补充填入
     */
    private static String method = "guahao.adapter.healthrecord.query";
    /**
     * 版本信息，此处填入2.0即可
     */
    private static String signVersion = "2.0";
    /**
     * 测试与生产环境不同，version为2.0时必须：调用的API归属产品对应的产品编码
     * 微医云控制台“权限管理-人员权限管理”菜单下针对子账号的角色管理中“API预定义角色”或“API自定义角色”中关联API的所属产品对应的产品编码
     * TODO 请补充填入
     */
    private static String productCode = "16eJwM1mx";
    /**
     * version为2.0时必须：请求标识唯一id，长度不超过36位；用户须保证不同请求的唯一性
     */
    private static String messageId = UUID.randomUUID().toString();


    /**
     * 以请求参数为例：
     * {
     * "queryParam":{
     * "pageNumber":"1",
     * "pageSize":10
     * }
     * }
     * <p>
     * 提供两种方式的样例，可任选一种进行，推荐使用Json方式请求
     */
    public static void main(String[] args) throws Exception {
        /**
         * 以请求参数为例：
         * {
         *     "queryParam":{
         *         "pageNumber":"1",
         *         "pageSize":10
         *     }
         * }
         */
        // 1. Content-type为默认的application/json;/
        postJSON(host, buildHeader(), buildJsonParams(), appSecret);

//        byte[] infoMd5 = SignUtil.encryptMD5("123".getBytes(StandardCharsets.UTF_8));
//        String contentMD5 = SignUtil.byte2hex(infoMd5);
//        System.out.println(contentMD5);
        // 2. Content-type为默认的application/x-www-form-urlencoded;
//         postForm(host,buildHeader(),buildFromParams(),appSecret);


    }


    private static Map<String, String> buildHeader() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(APPKEY, appKey);
        headerMap.put(METHOD, method);
        headerMap.put(TIMESTAMP, String.valueOf(Instant.now().toEpochMilli()));
        headerMap.put(VERSION, signVersion);
        headerMap.put(PRODUCT_CODE, productCode);
        headerMap.put(MESSAGE_ID, messageId);
//        headerMap.put(X_GLOBAL_ROUTER_TAG,"");
        System.out.println("header:" + JSON.toJSONString(headerMap));
        return headerMap;
    }

    /**
     * 以请求参数为例：
     * {
     * "queryParam":{
     * "pageNumber":"1",
     * "pageSize":10
     * }
     * }
     *
     * @return
     */
    private static Map<String, String> buildFromParams() {
        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("pageNumber", "1");
        queryParam.put("pageSize", "10");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("queryParam", JSON.toJSONString(queryParam));
        System.out.println("params:" + JSON.toJSONString(parameters));
        return parameters;
    }

    /**
     * 以请求参数为例：
     * {
     * "queryParam":{
     * "pageNumber":"1",
     * "pageSize":10
     * }
     * }
     *
     * @return
     */
    private static String buildJsonParams() {
//        Map<String, String>  queryParam = new HashMap<>();
//        queryParam.put("pageNumber", "1");
//        queryParam.put("pageSize", "10");

        HealthIndicatorRequestBO healthIndicatorRequestBO = new HealthIndicatorRequestBO();
        healthIndicatorRequestBO.setSourceId("41194");
        healthIndicatorRequestBO.setStartDate("2022-02-02");
        healthIndicatorRequestBO.setEndDate("2022-02-03");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("healthIndicatorRequestBO", healthIndicatorRequestBO);
        String jsonParams = jsonObject.toJSONString();
        System.out.println("jsonParams:" + JSON.toJSONString(jsonParams));
        return jsonParams;
    }

    private static void postForm(String url, Map<String, String> headerMap, Map<String, String> parameters, String appSecret) {
        try {
            String clientSign = SignUtil.getSign(headerMap, parseInUrlParameters(url, parameters), appSecret);
            System.out.println("sign:" + clientSign);
            headerMap.put(SIGN, clientSign);
            String result = HttpClientUtil.doPost(url, headerMap, parameters);
            System.out.println("result:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void postJSON(String url, Map<String, String> headerMap, String json, String appSecret) {
        try {
            if (null != json && !json.isEmpty()) {
                byte[] infoMd5 = SignUtil.encryptMD5(json.getBytes(StandardCharsets.UTF_8));
                String contentMD5 = SignUtil.byte2hex(infoMd5);
                headerMap.put(CONTENT_MD5, contentMD5);
            }
            String clientSign = SignUtil.getSign(headerMap, parseInUrlParameters(url, null), appSecret);
            System.out.println("sign:" + clientSign);
            headerMap.put(SIGN, clientSign);
            String result = HttpClientUtil.doPostJson(url, headerMap, json);
            System.out.println("result:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> parseInUrlParameters(String url, Map<String, String> parameters) {
        if (StringUtils.isBlank(url)) {
            return parameters;
        }
        url = url.trim();
        String[] urlParts = url.split("\\?");
        //没有参数
        if (urlParts.length == 1) {
            return parameters;
        }
        //有参数
        String[] params = urlParts[1].split("&");
        Map<String, String> result = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            result.put(keyValue[0], keyValue[1]);
        }
        if (null != parameters && !parameters.isEmpty()) {
            result.putAll(parameters);
        }
        return result;
    }

}
