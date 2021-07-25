package org.example;

import com.jdragon.common.http.HttpUtils;

import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.11 17:02
 * @Description:
 */
public class DownLoadFdfs {
    public static void main(String[] args) {
        HttpUtils httpUtils = HttpUtils.initJson();
        httpUtils.setSocketTimeout(10000);
        httpUtils.setConnectTimeout(10000);
        int mistake = 0;
        int num = 0;
        while (true) {
            num++;
            try {
//                Thread.sleep(2000);
                httpUtils.get("http://10.197.139.55/group2/M00/01/C9/CsWLOV8emQWAXP1XAAZM6nwmFlA394.jpg");
//                httpUtils.get("http://10.197.139.55/group1/M00/01/CC/CsWLOF8emQSAV9x2AAbVtkh94Q8173.jpg");
//                httpUtils.get("http://10.197.149.145:18888/group3/M00/87/34/CsWVLF_SYnSAEGCzAAqqbmeAJ-0611.png");
//                httpUtils.get("http://10.198.246.24:8888/group1/M00/27/B2/Csb2GF-ZH_qAaNgmAAmIG7nF3Lg399.jpg");
//                httpUtils.get("http://10.197.149.42:8888/group1/M00/01/CC/CsWLOF8emQSAV9x2AAbVtkh94Q8173.jpg");
//                httpUtils.get("https://csdnimg.cn/release/blogv2/dist/pc/img/original.png");
            } catch (Exception e) {
                e.printStackTrace();
                mistake++;
            }
            System.out.println("执行次数："+num+"  错误: "+mistake);
        }


    }
}
