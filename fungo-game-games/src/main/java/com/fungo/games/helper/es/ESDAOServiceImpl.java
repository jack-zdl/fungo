package com.fungo.games.helper.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.config.AliESRestClient;
import com.fungo.games.config.NacosFungoCircleConfig;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameMarketSpy;
import org.apache.http.HttpHost;
//import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>ES搜索引擎</p>
 * @Author: dl.zhang
 * @Date: 2019/7/24
 */
@Repository
public class ESDAOServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESDAOServiceImpl.class);

    private RestHighLevelClient client;


    @Autowired
    private AliESRestClient aliESRestClient;

    @Autowired
    private NacosFungoCircleConfig nacosFungoCircleConfig;

//    @PostConstruct
//    public void init() {
//        client = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost(nacosFungoCircleConfig.getEsHttpIp(),   nacosFungoCircleConfig.getEsHttpPort(), "http")
//                        //        new HttpHost("localhost", 9201, "http")
//                ));
//    }

    @PreDestroy
    public void destroy(){
        try {
            client.close();
        } catch (IOException e) {

        }
    }

    public List<Game> addESPosts() {
//        Wrapper<CmmPost> wrapperCmmPost = new EntityWrapper<>();
//        List<CmmPost> posts =  postService.selectList(wrapperCmmPost);
        Game param = new Game();
        param.setId("b1f1f35d4b4242a0b794e17ed0d1d64a");
//        Game cmmPost = postService.selectById(param);
        Game cmmPost = new Game();
        try {
            // 创建索引
            IndexRequest request = new IndexRequest(nacosFungoCircleConfig.getIndex());
            // 准备文档数据
            String jsonStr = JSON.toJSONString(cmmPost);
            // 转成 MAP
            Map<String, Object> jsonMap = JSON.parseObject(jsonStr, Map.class);
//            jsonMap.put("createdAt", new Date());
            //Document source provided as a Map which gets automatically converted to JSON format
            request.source(jsonMap);

            client.indexAsync(request, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {
                }
                @Override
                public void onFailure(Exception e) {

                }
            });
        }catch (Exception e){
            LOGGER.error("获取es数据异常,索引id="+nacosFungoCircleConfig.getIndex(),e);
        }
        return null;
    }

    // String keyword, int page, int limit
    public Page<Game> getGameByES(int page, int limit, String keyword, String tag, String sort ) {
        Page<Game> postPage = new Page<>();
        RestHighLevelClient highClient = aliESRestClient.getAliEsHighClient();
        try {
//            searchGame(page,limit, keyword);
            // 1、创建search请求
            SearchRequest searchRequest = new SearchRequest(nacosFungoCircleConfig.getIndex());
            searchRequest.types(nacosFungoCircleConfig.getSearchIndexType());
            // 2、用SearchSourceBuilder来构造查询请求体 ,请仔细查看它的方法，构造各种查询的方法都在这。
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            if(keyword != null && !"".equals(keyword)){
//                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                //普通模糊匹配
//                boolQueryBuilder.must(QueryBuilders.wildcardQuery("title",keyword));
//                sourceBuilder.query(boolQueryBuilder);
                MatchQueryBuilder matchQueryBuilder1 = QueryBuilders.matchQuery("state",0);
                MatchQueryBuilder matchQueryBuilder2 = QueryBuilders.matchQuery("name",keyword).boost(10);
                MatchQueryBuilder matchQueryBuilder3 = QueryBuilders.matchQuery("intro",keyword);
                MatchQueryBuilder matchQueryBuilder4 = QueryBuilders.matchQuery("google_deputy_name",keyword);
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                BoolQueryBuilder childBoolQueryBuilder = new BoolQueryBuilder()
                        .should(matchQueryBuilder2)
                        .should(matchQueryBuilder3)
                        .should(matchQueryBuilder4);
                boolQueryBuilder.must(matchQueryBuilder1);
//                boolQueryBuilder.must(matchQueryBuilder2);
                boolQueryBuilder.must(childBoolQueryBuilder);

                sourceBuilder.query(boolQueryBuilder);
            }
//            sourceBuilder.query( QueryBuilders.termQuery("title", keyword));
            // 结果开始处
            sourceBuilder.from((page-1)*limit);//            sourceBuilder.from(0);
            // 查询结果终止处
            sourceBuilder.size(page*limit);// sourceBuilder.size(10);
            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

            //指定排序
            sourceBuilder.sort(new ScoreSortBuilder().order( SortOrder.DESC));
//            sourceBuilder.sort(new FieldSortBuilder("_id").order(SortOrder.DESC));

            //将请求体加入到请求中
            searchRequest.source(sourceBuilder);
            //3、发送请求
            SearchResponse searchResponse = highClient.search(searchRequest,RequestOptions.DEFAULT);
            //4、处理响应
            //搜索结果状态信息
            RestStatus status = searchResponse.status();
            TimeValue took = searchResponse.getTook();
            Boolean terminatedEarly = searchResponse.isTerminatedEarly();
            boolean timedOut = searchResponse.isTimedOut();

            //分片搜索情况
            int totalShards = searchResponse.getTotalShards();
            int successfulShards = searchResponse.getSuccessfulShards();
            int failedShards = searchResponse.getFailedShards();
            for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
                // failures should be handled here
            }

            //处理搜索命中文档结果
            SearchHits hits = searchResponse.getHits();
//            hits.totalHits;

//            TotalHits totalHits = hits.getTotalHits();
            float maxScore = hits.getMaxScore();

            SearchHit[] searchHits = hits.getHits();

             List<Game> list = new ArrayList<>();
            for (SearchHit hit : searchHits) {
                // do something with the SearchHit

                String index = hit.getIndex();
                String type = hit.getType();
                String id = hit.getId();
                float score = hit.getScore();

                //取_source字段值
                String sourceAsString = hit.getSourceAsString(); //取成json串
                JSONObject jsonObj = (JSONObject) JSON.parse(sourceAsString);
                Game cmmGame= JSONObject.toJavaObject(jsonObj,Game.class);
//                CmmPost cmmPost = (CmmPost) JSON.parse( sourceAsString );
                list.add(cmmGame);
//                Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 取成map对象
                //从map中取字段值
                /*
                String documentTitle = (String) sourceAsMap.get("title");
                List<Object> users = (List<Object>) sourceAsMap.get("user");
                Map<String, Object> innerObject = (Map<String, Object>) sourceAsMap.get("innerObject");
                */
//                LOGGER.info("index:" + index + "  type:" + type + "  id:" + id);
//                LOGGER.info(sourceAsString);

                //取高亮结果
                /*Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField highlight = highlightFields.get("title");
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();*/
            }
            postPage.setRecords(list);
            postPage.setTotal(Long.valueOf(hits.totalHits).intValue());



//            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//            SearchRequest rq = new SearchRequest();
//            //索引
//            rq.indices(index);
//            //各种组合条件
//            rq.source(sourceBuilder);
//
//            //请求
//            System.out.println(rq.source().toString());
//            SearchResponse rp = client.search(rq);

        }catch (Exception e){
            LOGGER.error("获取es数据异常,索引id="+nacosFungoCircleConfig.getIndex(),e);
        }
        return postPage;
    }


    public Page<Game> searchGame(int page, int limit, String keyword ){
        try {
            RestHighLevelClient highClient = aliESRestClient.getAliEsHighClient();
            RequestOptions COMMON_OPTIONS = AliESRestClient.getCommonOptions();
            // 创建request。
            Map<String, Object> jsonMap = new HashMap<>();
            // field_01、field_02为字段名，value_01、value_02为对应的值。
            jsonMap.put("state", "1");
            jsonMap.put("name", keyword);
            //index_name为索引名称；type_name为类型名称；doc_id为文档的id。
            IndexRequest indexRequest = new IndexRequest("dev_game", "dev_game").source(jsonMap);

            // 同步执行，并使用自定义RequestOptions（COMMON_OPTIONS）。
            IndexResponse indexResponse = highClient.index(indexRequest, COMMON_OPTIONS);

            long version = indexResponse.getVersion();

            System.out.println("Index document successfully! " + version);
            //index_name为索引名称；type_name为类型名称；doc_id为文档的id。与以上创建索引的名称和id相同。
//            DeleteRequest request = new DeleteRequest("{index_name}", "{type_name}", "{doc_id}");
//            DeleteResponse deleteResponse = highClient.delete(request, COMMON_OPTIONS);

//            System.out.println("Delete document successfully! \n" + deleteResponse.toString() + "\n" + deleteResponse.status());

            highClient.close();
        }catch (IOException e){
            LOGGER.error( "haode",e );
        }
        return null;
    }

}
