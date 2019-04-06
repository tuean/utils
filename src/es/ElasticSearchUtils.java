package es;

import com.alibaba.fastjson.JSONObject;
import common.JSONObjectUtil;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class ElasticSearchUtils {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchUtils.class);

    @Autowired
    private TransportClient transportClient;

    private static TransportClient client;

//    @PostConstruct
    public void init(){
        client = this.transportClient;
    }

    private static String highLightField = "";

    /**
     * 创建索引并设置mapping
     *
     * @param index
     * @return
     */
    public static boolean createIndexWithMapping(String index, String type, XContentBuilder mapping) {
        if (!isIndexExist(index)) {
            logger.info("Index is not exits!");
        }
        CreateIndexRequestBuilder cib = client.admin().indices().prepareCreate(index);
        if(mapping != null){
            cib.addMapping(type, mapping);
        }
        CreateIndexResponse res = cib.execute().actionGet();
        return res.isAcknowledged();
    }

    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     */
    public static boolean isIndexExist(String index) {
        IndicesExistsResponse inExistsResponse = client.admin().indices().exists(new IndicesExistsRequest(index)).actionGet();
        if (inExistsResponse.isExists()) {
            logger.info("Index [" + index + "] is exist!");
        } else {
            logger.info("Index [" + index + "] is not exist!");
        }
        return inExistsResponse.isExists();
    }

    /**
     * 数据添加，正定ID
     *
     * @param jsonObject 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @param id         数据ID
     * @return
     */
    public static String addData(JSONObject jsonObject, String index, String type, String id) {
        if(isIndexExist(index)) {
            IndexResponse response = client.prepareIndex(index, type, id).setSource(jsonObject).get();
            logger.info("添加文档成功ID:" + id);
            return response.getId();
        } else {
            logger.info("索引" + index + "不存在！");
            return null;
        }
    }

    /**
     * 通过ID删除数据
     *
     * @param index 索引，类似数据库
     * @param type  类型，类似表
     * @param id    数据ID
     */
    public static void deleteDataById(String index, String type, String id) {
        if(isIndexExist(index)) {
            DeleteResponse response = client.prepareDelete(index, type, id).execute().actionGet();
            logger.info("deleteDataById response status:{},id:{}", response.getType(), response.getId());
        } else {
            logger.info("索引" + index + "不存在！");
        }
    }

    /**
     * test
     * @param text
     * @return
     */
    public static SearchResponse testSearchAllByText(String text, String index, String type){
        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(text);
        booleanQuery.must(queryBuilder);
        SearchRequestBuilder searchRequestBuilder = client
                .prepareSearch(index)
                .setTypes(type)
                .setQuery(booleanQuery)
                //es 2.4.0 高亮设置
                //所有字段都高亮
//                .addHighlightedField("*")
                //高亮前缀
//                .setHighlighterPreTags("<em>")
                //高亮后缀
//                .setHighlighterPostTags("</em>")
                //高亮无效解决
//                .setHighlighterRequireFieldMatch(false)
                //分页
                .setFrom(1)
                .setSize(10)
                //可以返回一个带有查询语句的可阅读描述
                .setExplain(true);

        //es 5.6.1 高亮设置
        if(StringUtils.isNotEmpty(highLightField)){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field(highLightField);
            highlightBuilder.requireFieldMatch(false);
            searchRequestBuilder.highlighter(highlightBuilder);
        }



        logger.info("\n{}", searchRequestBuilder);

        // 执行搜索,返回搜索响应信息
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        if (searchResponse.status().getStatus() == 200) {

            return searchResponse;
        }
        return null;
    }


}
