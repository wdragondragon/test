package org.example.bmsk.GzUrl;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.19 13:18
 * @Description:
 */
public class GzEnTokenUrl {
    public static void main(String[] args) throws Exception {
        String token = NewRSAUtil.getToken();
        String baseParam = NewRSAUtil.getBaseParams(token);//基础参数加密

        String url = "http://12345.zhongtianinfo.com:9080/szg/epidemicSituation/queryPneumonia.do";
        String urlParam = baseParam + "&hotspot=0";
        //再次加密
        String aesUrlParam = NewRSAUtil.encrypt(urlParam, token, token);
        String rsaToken = NewRSAUtil.encrypt(token, NewRSAUtil.publicKey);

        String endurl = url + "?param=" + NewRSAUtil.UrlEncoder(aesUrlParam, rsaToken);
        System.out.println(endurl);
    }
}
