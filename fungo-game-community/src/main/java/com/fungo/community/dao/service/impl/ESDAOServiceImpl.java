package com.fungo.community.dao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.config.NacosFungoCircleConfig;
import com.fungo.community.controller.PostController;
import com.fungo.community.dao.service.CmmPostDaoService;
import com.fungo.community.entity.CmmPost;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
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
    private NacosFungoCircleConfig nacosFungoCircleConfig;
    @Autowired
    private CmmPostDaoService postService;

    @PostConstruct
    public void init() {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(nacosFungoCircleConfig.getEsHttpIp(),   nacosFungoCircleConfig.getEsHttpPort(), "http")
                        //        new HttpHost("localhost", 9201, "http")
                ));
    }

    @PreDestroy
    public void destroy(){
        try {
            client.close();
        } catch (IOException e) {

        }
    }

    public List<CmmPost> addESPosts() {
//        Wrapper<CmmPost> wrapperCmmPost = new EntityWrapper<>();
//        List<CmmPost> posts =  postService.selectList(wrapperCmmPost);
        CmmPost param = new CmmPost();
        param.setId("b1f1f35d4b4242a0b794e17ed0d1d64a");
        CmmPost cmmPost = postService.selectById(param);
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

    public Page<CmmPost> getAllPosts(String keyword, int page, int limit ) {
        Page<CmmPost> postPage = new Page<>();
        try {

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
                MatchQueryBuilder matchQueryBuilder1 = QueryBuilders.matchQuery("state",1);
                MatchQueryBuilder matchQueryBuilder2 = QueryBuilders.matchQuery("title",keyword);
                MatchQueryBuilder matchQueryBuilder3 = QueryBuilders.matchQuery("content",keyword);
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                BoolQueryBuilder childBoolQueryBuilder = new BoolQueryBuilder()
                        .should(matchQueryBuilder2)
                        .should(matchQueryBuilder3);
                boolQueryBuilder.must(childBoolQueryBuilder);
                boolQueryBuilder.must(matchQueryBuilder1);
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
            sourceBuilder.sort(new FieldSortBuilder("watch_num").order(SortOrder.DESC));

            //将请求体加入到请求中
            searchRequest.source(sourceBuilder);


            //3、发送请求
            SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);



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

            TotalHits totalHits = hits.getTotalHits();
            float maxScore = hits.getMaxScore();

            SearchHit[] searchHits = hits.getHits();

            List<CmmPost> list = new ArrayList<>();
            for (SearchHit hit : searchHits) {
                // do something with the SearchHit

                String index = hit.getIndex();
                String type = hit.getType();
                String id = hit.getId();
                float score = hit.getScore();

                //取_source字段值
                String sourceAsString = hit.getSourceAsString(); //取成json串
                JSONObject jsonObj = (JSONObject) JSON.parse(sourceAsString);
                CmmPost cmmPost= JSONObject.toJavaObject(jsonObj,CmmPost.class);
//                CmmPost cmmPost = (CmmPost) JSON.parse( sourceAsString );
                list.add(cmmPost);
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
            postPage.setTotal(Long.valueOf(totalHits.value).intValue());



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
//        finally {
//            try {
//                client.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return postPage;
    }


}
