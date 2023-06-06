package org.example.javabase;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author JDragon
 * @Date 2021.11.17 下午 10:09
 * @Email 1061917196@qq.com
 * @Des:
 */
public class Test7 {
    final String regex = "([^,]*?):(\\d*)";
    final String string = "192.168.1.1:9092,192.168.1.2:9092,192.168.1.2:9092";
    final String subst = "";

    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
    final Matcher matcher = pattern.matcher(string);

    public static void main(String[] args) throws InterruptedException {
//        String s = FileUtil.readUtf8String(new File("D:\\Desktop\\paas.cmft.com.har.txt"));
//        JSONArray read = (JSONArray)JSONPath.read(s, "$.log.entries[1]._webSocketMessages.data");
//        List<String> strings = new ArrayList<>();
//        for (Object o : read) {
//            String trim = ((String) o).trim();
//            strings.add(trim);
//        }
//        FileUtil.writeLines(strings,"D:\\Desktop\\dump.log", "UTF-8");
        String str =
                "V4nNWpgjO1O8yzpVLjc78+cRqHuNnHQJvkZI05aoyhS1j5g9T3Plgml4zKkG" +
                "trdXPcGG/4giRVCyMV89mVB6R9Sh6rhW6S2aj2AlqhI1nhhnun2eJ7SRxpkJ" +
                "9ZQ4uZECs1CGrO1PWEmqvVw7ko/0bSMTKnT4YFz9S2j98Zwbw7yo0uIDgC3Z" +
                "E4k6mN6RUCV7Cb+oIW/kLVE0ngkWOJjAksF+7UXRu2oVREryNix6pgPwnXIp" +
                "JcifwmuyrENa4s14kru7epMpC4W4FzB3d5QjGg4Y1hu3E5vsvN31Qkak6bAr" +
                "Yzf49B+66EqiosMSU+K4PobIHVBOTM/TCD2uJB8tkPaGl52dXCS91penFXyh" +
                "a3jmQXOwpvBal1CdGlWHwj/Bh+nHjbaNViaNb1MWal6pecHi7OI5oExFbvQp" +
                "oV90fu/jMmI9KyZ88g6hls8szZfVzBG5CqznIwrnIpG1mgzo3dngh/r5sKS/" +
                "oMxPjcq29+8LiHryzNDF8sfE70A78vfFU89AJJZIUYjEnEUWL6T5WMN08tOz" +
                "GZpNCORPzkIFG9ziTr+lm7G2ytAtaGBwUoGLNd+EgRVkJAsgMmn1ikIgfzUV" +
                "Ki2Wo7d9Wokp3bt7Hor55JaqpUxL9hnhWr4Xwi7ZZMH+mUVdEp2DbaJQ1Y/7" +
                "r4MTBBSvB2eG/OntpFbemgyYDG5LvcDsARBEyCGtXxzL5WzAa+kcI7yfMblW" +
                "w5AGWFpIZsP+zcmYIu6g1M0haNPLW1LRXair8DqiAuHoH3hgaPWgzfvPs0uv" +
                "3ghKmk2jfUOo6GmqUUbks4iYCda+ptJf7BxKtb2NkfrVkQDBZ8vhIN7KQzvA" +
                "YiXPQbY5BT8WxQtJlVNMCwXd4Qhpn5xI0wwpNSD63hJn6geIsetMruE8B5HK" +
                "xMcCNFWW2TYG/0LdX68JephroVmAgz/vk7HjNCOkoZFRFf4Efowz6rgI4NTc" +
                "GGbILP+B6YvDRa6menU/zaq35e8uMB3Ud4Mc1YKkFFAGY4INjAwgUtX45Igg" +
                "1EtBuCYe7mZP9lLitCZQUJJc53VwR5DD/IY4EfcElOv8HNKkGk4VJCTf1BlP" +
                "tWNrhOHRAxzNhs+1HUnLP7PUpkRKMoIzjpd0cRYGfoWMiOrJOSDsFbmu4OZW" +
                "9lrB/G3ZzK6/5XAIAjY1lIn7v+owpIW0Lfj7fJ02E9PCppk25eopb2z77IRf" +
                "lCVK0GDIqdt+9astc2pR3UulezusQWzrXfW0kvoBqddZrQfe9uhkB4uoxGWm" +
                "LJzdX2SojM2xzt8CJ2E/YpwCf7jDJkdu5wKsWL9VJBt/6se9Q+n+u+a/cv/6" +
                "fHH86zQ1NMf1ffPOn8fUgeLmuA77q/FeVr1wLdREaqQ/nPW6QHeRLCNrkdJM" +
                "C7G4MRxFHOGDk2gqXsKRtu53tggvP8ORBGHB2BVvmsq8Zsl8xHCH9ofGB/mO" +
                "/PYBxAOdrnx1I4exYYHGOsmVswHbt6FbLDFsGxQvixVQAfjwN/8wzaYpz5iM" +
                "beWrC3qJg5YvG59DKqxE//OwgwM+xE0alvRIxQHpr5XVvEV2cwL/pchPDXpg" +
                "e0nEgxeNua7DlFB+vyUhrIC4WvIk27S0/kWqMCMvSvoD0dz876s/+m8C4m8Q" +
                "rF5xEWy8SbbJWx95YbnCfk881ine3pfdKuXEU8cdcEg9wl1zXF0b/WZY5bk+" +
                "1jPNaLqYSuvb2r/vrg7WLTVo2Ysl8U3psSSRMCTtqhcVrpMPrLRwSMltCaqM" +
                "NMceR19XtBgYFAJfVcZnmgriiyBTZY1A8mbTMR/mNK5LHFt8/wGC3Fzv4iqd" +
                "cT6S6UWSzzqWO++Vl+Mvc0+d18zpnN0d1SFktpKx9GD32ARTpBf0op/82Yk5" +
                "ACO7oFeFmTHJLlyN2qLruGJIPUAUfmrLTukd0l88NZ7L6eKIHt7ldgfMjSFY" +
                "g2JvP4iYHz+nFOjdvsOTs3ahICD5188ikTrNkEHl0owtgu6yR9Wo/AALWrsj" +
                "T0UVjhbrN28KKqNfgjc7Q9dMv9UtdlJxw6T9Nfr57QvGFC1VAKIRO7FoLRNA" +
                "NeBPENVZ";
        StringBuilder stringBuilder = new StringBuilder(str);
        System.out.println(stringBuilder);
        System.out.println("V4nNWpgjO1O8yzpVLjc78+cRqHuNnHQJvkZI05aoyhS1j5g9T3Plgml4zKkG".length());
    }

    public void test() {
        while (matcher.find()) {
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }
    }


}
