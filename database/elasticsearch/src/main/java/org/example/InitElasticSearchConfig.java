package org.example;


import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.http.HttpHost;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @Author JDragon
 * @Date 2022.03.25 上午 10:12
 * @Email 1061917196@qq.com
 * @Des:
 */
public class InitElasticSearchConfig {

    private JestClient client;

    public JestClient getClient() {
        return client;
    }

    public InitElasticSearchConfig(String esUrl) {
        client = getClientConfig(esUrl);
    }

    public JestClient getClientConfig(String esUrl) {
        List<String> endPointList = Arrays.stream(esUrl.split(","))
                .map(e -> e.startsWith("http") ? e : "http://" + e)
                .collect(Collectors.toList());

        Set<HttpHost> httpHosts = endPointList.stream().map(HttpHost::create).collect(Collectors.toSet());

        JestClientFactory factory = new JestClientFactory();
        HttpClientConfig.Builder httpClientConfig = new HttpClientConfig.Builder(endPointList)
                .preemptiveAuthTargetHosts(httpHosts)
                .multiThreaded(true)
                .connTimeout(30000)
                .readTimeout(300000)
                .maxTotalConnection(200)
                .requestCompressionEnabled(false)
                .discoveryEnabled(false)
                .discoveryFrequency(5L, TimeUnit.MINUTES);
        httpClientConfig.defaultCredentials("", "");
        factory.setHttpClientConfig(httpClientConfig.build());
        return factory.getObject();
    }

}
