package com.jdragon.springboot.iframestream;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.30 11:38
 * @Description:
 */
@Controller
@RequestMapping("/iframe")
public class IframeSteam {

    @GetMapping("/test.html")
    public void test(HttpServletResponse response){
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
//        response.setHeader("Content-Disposition", "attachment;fileName=1.html");

        ServletOutputStream outputStream = null;
        InputStream decryptInputStream = getInputStream("https://blog.tyu.wiki/");
        try{
            response.reset();
            outputStream = response.getOutputStream();
            // 在http响应中输出流
            StringBuilder stringBuilder = new StringBuilder();

            byte[] cache = new byte[1024];
            int nRead = 0;
            while ((nRead = decryptInputStream.read(cache)) != -1) {
                stringBuilder.append(new String(cache));
//                outputStream.write(cache, 0, nRead);
//                outputStream.flush();
            }
            outputStream.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(decryptInputStream!=null) {
                try {
                    decryptInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //获取输入流
    public static InputStream getInputStream(String urlStr) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inputStream;
    }


    @GetMapping(value = "/baidu",produces="text/html;charset=UTF-8")
    public void baidu(HttpServletResponse response) throws IOException {
        WebClient wc=new WebClient(BrowserVersion.FIREFOX);
        wc.setJavaScriptTimeout(5000);
        wc.getOptions().setUseInsecureSSL(true);//接受任何主机连接 无论是否有有效证书
        wc.getOptions().setJavaScriptEnabled(true);//设置支持javascript脚本
        wc.getOptions().setCssEnabled(false);//禁用css支持
        wc.getOptions().setThrowExceptionOnScriptError(false);//js运行错误时不抛出异常
        wc.getOptions().setTimeout(100000);//设置连接超时时间
        wc.getOptions().setDoNotTrackEnabled(false);


        HtmlPage page=wc.getPage("https://www.baidu.com");
        String res=page.asText();

        System.out.println(res);

        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;fileName=1.html");
        ServletOutputStream outputStream = null;
//        PrintWriter writer = response.getWriter();
        try{
            response.reset();
            outputStream = response.getOutputStream();
            // 在http响应中输出流
            byte[] cache = new byte[1024];
//            writer.write(res);
            outputStream.write(res.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        return res;
    }
}
