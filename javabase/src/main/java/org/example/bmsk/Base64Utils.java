package org.example.bmsk;

import java.util.Base64;

public class Base64Utils {
//    public static String encodeHexBase64(String str) {
////        进行Base64转化
//        String hexBase64 = Base64.getEncoder().encodeToString(str.getBytes());
//        return hexBase64;
//    }
    public static final byte[] hexToByteArray(String hex)
    {
        int l = hex.length();
        byte[] data = new byte[l / 2];
        for (int i = 0; i < l; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    public static final String encodeHexBase64(String string) {
        String str = null;
        try {
            byte[] binaryData = hexToByteArray(string);
            byte[] encodeBase64 = Base64.getEncoder().encode(binaryData);
            str = new String(encodeBase64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
