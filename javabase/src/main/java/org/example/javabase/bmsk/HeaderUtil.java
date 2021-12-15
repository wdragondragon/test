package org.example.javabase.bmsk;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

public class HeaderUtil {
    private static String ISTRONG_CA_APPKEY = "Istrong-Ca-AppKey";
    private static String ISTRONG_CA_SIGNATURE = "Istrong-Ca-Signature";
    private static String ISTRONG_CA_DATE = "Istrong-Ca-Date";
    private static String ISTRONG_CA_NONCE = "Istrong-Ca-Nonce";
    private static String CONTENT_MD5 = "Content-MD5";

    private static String AppKey = "1000003";
    private static String AppSecret = "xmgH8FnNWTvpq_xQCGT0fkTgUiwKjPyV6nWAJA-QM5U";


    public static Map<String,String> getHeader(String body,String url, String param) throws Exception {
        Map<String,String> header = new HashMap<>();
        StringBuffer signature = new StringBuffer();
        String date = DateUtil.getNowStr("yyyy-MM-dd HH:mm:ss");
        String nonce = UUID.randomUUID().toString();
        String md5 = body == null ? "" : Objects.requireNonNull(MD5Util.MD5(body)).toLowerCase();
        signature.append(date).append(AppKey).append("get").append(md5).append(nonce).append(url.toLowerCase()).append("?").append(param);
        header.put(ISTRONG_CA_APPKEY,AppKey);
        header.put(ISTRONG_CA_DATE,date);
        header.put(ISTRONG_CA_NONCE,nonce);
        header.put(CONTENT_MD5,md5);
        header.put(ISTRONG_CA_SIGNATURE,Base64Utils.encodeHexBase64(HMACSHA256(signature.toString(),AppSecret)).split("=")[0].replaceAll("\\+","-").replaceAll("/","_"));
        return header;
    }

    public static String HMACSHA256(String data, String key) throws Exception {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");

        sha256_HMAC.init(secret_key);

        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();

        for (byte item : array) {

            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));

        }

        return sb.toString().toLowerCase();

    }

    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower)
    {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try
        {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>()
            {

                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)
                {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds)
            {
                if (StringUtils.isNotBlank(item.getKey()))
                {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlEncode)
                    {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    if (keyToLower)
                    {
                        buf.append(key.toLowerCase() + "=" + val);
                    } else
                    {
                        buf.append(key + "=" + val);
                    }
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false)
            {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e)
        {
            return null;
        }
        return buff;
    }

    //网关验证
    public static Map<String,String> getHeader2() throws Exception {
        Map<String,String> header = new HashMap<>();
        String paasId = "hptfyj";
        String paasToken = "L950GcMXgDfm1BvTH1NoP7Hajk1HVIYv";

        long now = new Date().getTime();
        String timestamp = Long.toString((long) Math.floor(now / 1000));
        String nonce = Long.toHexString(now) + "-" + Long.toHexString((long) Math.floor(Math.random() * 0xFFFFFF));
        String signature = toSHA256(timestamp + paasToken + nonce + timestamp);

        header.put("x-tif-paasid", paasId);
        header.put("x-tif-timestamp", timestamp);
        header.put("x-tif-signature", signature);
        header.put("x-tif-nonce", nonce);
        return header;
    }

    public static String toSHA256(String str) throws Exception {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (Exception e) {
            throw e;
        }
        return encodeStr;
    }

    // byte转换成16进制
    protected static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
    public static void main(String[] args) {
        try {
            String url = "/api/v1/strsvrr/list";
            String query = "beginTM=2020-08-06 12:00:00&endTM=2020-08-06 13:00:00&stcd=59487,90006777";
            Map<String,String> maps = HeaderUtil.getHeader(null, url, query);
            maps.putAll(HeaderUtil.getHeader2());
//            Map<String,String> maps = getHeader("","rest/api/v1/stpptnr/list","beginTM=2020-07-18 12:00:00&endTM=2020-07-18 13:00:00&stcd=59487,90006777");
            for (Map.Entry map:maps.entrySet()){
                System.out.println(map.getKey()+"="+map.getValue());
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

//    public static void main(String[] args) {
//
//        try {
//            Map<String,String> header = new HashMap<>();
//            StringBuffer signature = new StringBuffer();
//            String date = DateUtil.getNowStr("yyyy-MM-dd HH:mm:ss");
//            String nonce = UUID.randomUUID().toString();
//            String md5 = "";
//            signature.append(date).append(AppKey).append("get").append("").append(nonce).append("/api/v1/stpptnr/list".toLowerCase()).append("?").append("beginTM=2020-06-11 13:59:00&endTM=2020-06-11 14:01:00");
//            header.put(ISTRONG_CA_APPKEY,AppKey);
//            header.put(ISTRONG_CA_DATE,date);
//            header.put(ISTRONG_CA_NONCE,nonce);
//            header.put(CONTENT_MD5,md5);
//            header.put(ISTRONG_CA_SIGNATURE,Base64Utils.encodeHexBase64(HMACSHA256(signature.toString(),AppSecret)).split("=")[0].replaceAll("\\+","-").replaceAll("/","_"));
//            System.out.println(signature.toString());
//            System.out.println("hash:-->" + HMACSHA256(signature.toString(),AppSecret));
//            System.out.println("Base64:-->" + Base64Utils.encodeHexBase64(HMACSHA256(signature.toString(),AppSecret)));
//            System.out.println("SIGNATURE:-->" + Base64Utils.encodeHexBase64(HMACSHA256(signature.toString(),AppSecret)).split("=")[0].replaceAll("\\+","-").replaceAll("/","_"));
//
//            //目前只有小时级别的数据
//            String ip = "http://122.112.175.6:8106/rest";
//            String url = ip + "/api/v1/stpptnr/list";
//            /*String strNow = DateUtil.getNowStr();
//            strNow = strNow.substring(0,strNow.length()-5) + "00:00";
//            Date now = DateUtil.formatStringToDate(strNow,"yyyy-MM-dd HH:mm:ss");
//            Map<String, String> params = new HashMap<>();
//            params.put("beginTM",DateUtil.formatDate("yyyy-MM-dd HH:mm:ss",DateUtil.addMinute(now,-1)));
//            params.put("endTM",DateUtil.formatDate("yyyy-MM-dd HH:mm:ss",DateUtil.addMinute(now,1)));
//            String param =HeaderUtil.formatUrlMap(params, false, false);*/
//            JSONObject result = HttpUtil.get(url + "?" + "beginTM=2020-06-11 13:59:00&endTM=2020-06-11 14:01:00",header);
//            if(result.getBooleanValue("success")){
//                JSONArray datas = result.getJSONArray("data");
//                datas.forEach(obj->{
//                    JSONObject data = JSONObject.parseObject(obj.toString());
//                    String md52 = MD5Util.MD5(data.toJSONString());
//                });
//            } else {
//                //logger.error("降雨量接口请求失败,原因--->" + result.getJSONObject("error").getString("errorText"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
