package es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.net.InetAddress;

/**
 * es 设置
 * 来源： http://souyunku.com/2017/11/06/ElasticSearch-example/
 */
//@Configuration
public class ElasticsearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);

    /**
     * elk集群地址
     */
//    @Value("${elasticsearch.ip}")
    public String hostName;
    /**
     * 端口
     */
//    @Value("${elasticsearch.port}")
    public String port;
    /**
     * 集群名称
     */
//    @Value("${elasticsearch.cluster.name}")
    public String clusterName;

    /**
     * 连接池
     */
//    @Value("${elasticsearch.pool}")
    public String poolSize;

//    @Bean
    public TransportClient init() {

        TransportClient transportClient = null;

        try {
            // 配置信息
            Settings esSetting = Settings.builder()
                    .put("cluster.name", clusterName)
                    //增加嗅探机制，找到ES集群
                    .put("client.transport.sniff", true)
                    //增加线程池个数，暂时设为5
                    .put("thread_pool.search.size", Integer.parseInt(poolSize))
                    .build();

            //es 2.4.0 连接方式
//            transportClient = TransportClient.builder().settings(esSetting).build();
//            InetSocketTransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(InetAddress.getByName(hostName), Integer.valueOf(port));
//            transportClient.addTransportAddresses(inetSocketTransportAddress);

            //es 5.6.1 连接方式
            transportClient = new PreBuiltTransportClient(esSetting);
            InetSocketTransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(InetAddress.getByName(hostName), Integer.valueOf(port));
            transportClient.addTransportAddresses(inetSocketTransportAddress);



        } catch (Exception e) {
            logger.error("elasticsearch TransportClient create error!!!", e);
        }

        logger.info("elasticsearch TransportClient has successfully created");
        return transportClient;
    }
}
