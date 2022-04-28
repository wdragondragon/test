package org.example;

import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Hello world!
 */
public class ElasticSearchTest {

    @Test
    public void test() throws IOException {
        InitElasticSearchConfig esConfig = new InitElasticSearchConfig("http://39.96.83.89:9200");
        JestClient client = esConfig.getClient();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//        searchSourceBuilder.query(QueryBuilders.matchQuery("name","张三"));
        Search search = new Search.Builder(searchSourceBuilder.toString())
                // multiple index or types can be added.
                .addIndex("test_date")
                .addType("_doc")
                .build();

        SearchResult result = client.execute(search);
        Long total = result.getTotal();
        List<JsonObject> sourceAsObject = result.getSourceAsObjectList(JsonObject.class, false);
        System.out.println(total + ":" + sourceAsObject.toString());
    }
}
