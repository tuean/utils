package common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by zhongxiaotian on 2018/2/28.
 */
@Component
public class ProfilesUtils {

    private static Logger logger = LoggerFactory.getLogger(ProfilesUtils.class);

    private static final String DEFAULT_ENV = "dev";

    @Autowired
    private Environment env;

    private static String CURRENT_ENV;

    private ProfilesUtils(){};

    @PostConstruct
    private void init(){
        String key = "spring.profiles.active";
        CURRENT_ENV = env.getProperty(key) == null ? DEFAULT_ENV : env.getProperty(key);
        logger.info("environment: " + CURRENT_ENV);
    }


    /**
     * 返回当前的环境名
     * @return
     */
    public static String getInstanceEnv(){
        return CURRENT_ENV;
    }
}
