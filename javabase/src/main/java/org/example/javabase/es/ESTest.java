package org.example.javabase.es;

import com.alibaba.fastjson.JSON;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.03 13:38
 * @Description:
 */
public class ESTest {
    static JestClient jestClient;
    public static void main(String[] args) throws IOException {
        jestClient = getClient();
        Bulk.Builder bulkAction = new Bulk.Builder()
//                .defaultIndex("geo_point_test")
//                .defaultType("_doc")
                .defaultIndex("std_sjk_camera_info")
                .defaultType("_doc");
        Map<String,Object> data = new HashMap<>();
        String location = "{\"lat\":23.174217,\"lon\":113.48171}";
//        data.put("text","香格里拉4");
//        data.put("location","23.6234,1133.4324");
        data.put("location_es",JSON.parse(location));
//        bulkInsert(bulkAction,data);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("_id", "sOCjs3MBRf4xENWg6HOW"));
        Search search = new Search.Builder(searchSourceBuilder.toString())
                // multiple index or types can be added.
                .addIndex("std_sjk_camera_info")
                .addType("_doc")
                .build();
        JestClient client =getClient();
        SearchResult result=  client.execute(search);
        String jsonString = result.getJsonString();
        System.out.println(jsonString);

        jestClient.shutdownClient();
    }
    public static JestClient getClient(){
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
//                .Builder("http://172.29.215.219:9200")
                .Builder("http://10.198.246.28:9400")
                .multiThreaded(true)
                .build());
        return  factory.getObject();
    }

    public static void bulkInsert(Bulk.Builder bulkAction,Map<String,Object> data) throws IOException {
        bulkAction.addAction(new Index.Builder(data).build());
        JestResult rst = jestClient.execute(bulkAction.build());
        if(rst.isSucceeded()){
            System.out.println("插入成功:"+rst.getJsonString());
        }
    }
}
