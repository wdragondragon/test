package org.example.javabase.bmsk.GzUrl;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hanben
 * @date 2020-09-29 15:21
 */
public class NewRSAUtil {

    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNQOVvTABXbfxiB2Zq8iRDRAkVIOYoHtov/nhteuXmy87STSm1cM9VVfXOfdE3mxcwr94z07jViBR/nuAPlnla/ILZ0bP1vbqH0Q+kCEcqOkwu+Sd2PIrr4E7GLydih12gDUIvPPwcLBaZ8m/WhCPNx0Dr+kYSBBBbBRbRvmC7fQIDAQAB";

    public static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI1A5W9MAFdt/GIHZmryJENECRUg5ige2i/+eG165ebLztJNKbVwz1VV9c590TebFzCv3jPTuNWIFH+e4A+WeVr8gtnRs/W9uofRD6QIRyo6TC75J3Y8iuvgTsYvJ2KHXaANQi88/BwsFpnyb9aEI83HQOv6RhIEEFsFFtG+YLt9AgMBAAECgYAjpIGAcQQRC67IqaddSxZ9ZriH/VI2Q3q465RFw+IpN7pLdK8Wmo2msdchsLvPNTJH1cjS+RTFxN2vSk6tgEHEo9vRVUb3NQUM30GnFzKO4MvrTh6AtUZ600hlgnekpfareBOkc1dZo+nwm9pdDEmV9bFy3e6jQTaOd83NA3ihgQJBANo9Icmd/g2WeTfbH+RE11waVGJXYZUfaG3m+0peYYnL/I95DC/hFxUr9ZFcR3gcg3udiBjcBsQB9OBVVQczfl0CQQClsbUatENTYMnYV3sqET7kh4ViWkMBf2t0tWYyD6egxjQZGWUTVO63WMr1rG38dfi1kIa8kB94UKkDdAagZR+hAkEAvW5Ow6jF+plgQqGLPyKaJLIeCPZ8F2qZxLu358egtnE/mnGhqHWjCZeMok4NIy7s2gHVPm7N2JkyKv8mvQdvaQJAZ1Es7U7T4Eys76J04SFJxzQd3tsLhwaN2YM3CyYpzx1n5PKpzOkbxOWwAIqZNl/VSlmpOjDf/qTFDqstapxDoQJBAJGra33MLVJvn3oMwNz2KduY076XUQcQ76ZCbFLoYAmi5O22kjBF2ki3/NlS22nVP+DjBOnyv74zSr/j+mEibYk=";

    public static AtomicBoolean initialized = new AtomicBoolean(false);

    private final static String  unitId = "1997";
    private final static String unitName = "ZT";
    private final static String ENCODER_UTF = "utf-8";
    public static String getToken() {
        return  new Date().getTime() +"000";
    }
    public static String getBaseParams(String t){
        return "unitId="+unitId+"&unitName="+unitName+"&token=" + t;
    }

    public static String UrlEncoder(String aesUrlParam,String rsaToken) throws UnsupportedEncodingException {
        return URLEncoder.encode(aesUrlParam,ENCODER_UTF) + "&token=" + URLEncoder.encode(rsaToken,ENCODER_UTF);
    }
     private static void initialize() {
        if (initialized.get()) {
            return;
        }
        initialized.set(true);;
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String encrypt(String data, String key, String iv) {
        initialize();
        try {
            byte[] originalContent = data.getBytes();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv.getBytes()));
            byte[] encrypted = cipher.doFinal(originalContent);
            return new String(Base64.encode(encrypted));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String encrypt(String str, String publicKey) throws Exception {
        // base64编码的公钥
        byte[] decoded = Base64.decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
        // RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.toBase64String(cipher.doFinal(str.getBytes("UTF-8")));
    }
}
