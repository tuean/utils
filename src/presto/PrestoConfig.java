package presto;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Presto
 * @date 2018/2/26
 * NotImplementedException: Method Connection.prepareStatement is not yet implemented
 * https://github.com/prestodb/presto/issues/1195
 */
@Configuration
public class PrestoConfig {

    private static Logger logger = LoggerFactory.getLogger(PrestoConfig.class);

    @Value("${presto.url}")
    private String url;

    @Value("${presto.driver-class-name}")
    private String driverClassName;

    @Value("${presto.username}")
    private String userName;

    @Value("${presto.pwd}")
    private String pwd;

    @Bean(name = "prestoDataSource")
    @Qualifier("prestoDataSource")
    public DataSource prestoDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(userName);
//        dataSource.setPassword(pwd);
        logger.info("hive configuration inititalization success");
        return dataSource;
    }

}
