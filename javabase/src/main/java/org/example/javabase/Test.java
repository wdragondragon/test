package org.example.javabase;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.16 09:47
 * @Description:
 */
public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println(hmacSHA1Encrypt("1-319.15-11.17-2.10", "1072970551"));
        String test = "{\"signature\":\"<bm_dyn_guangzhou_12345_hotLine_signature(gzhpq2,gz12345!@#,<bm_dyn_timestamp>)>\",\"appid\":\"10e4176997c811e995ff38adbed808fb\",\"startTime\":\"{bm_dyn_increment_start}\",\"endTime\":\"{bm_dyn_increment_end}\",\"timestamp\":\"<bm_dyn_timestamp>\"}";
        test = test.replaceAll("\\{bm_dyn_increment_start\\}", "${incrementStart}");
        System.out.println(test);
    }

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    public static String hmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] textBytes = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        String text = byte2hex(mac.doFinal(textBytes));
        return text.substring(text.length() - 8);
    }

    //二行制转字符串
    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }
}