package common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 系統配置文件工具类
 *
 * @author xiaojin.li
 * @create 2017-11-16 下午2:44
 */
public class PropertyUtil {
    private static final Logger logger= LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties properties;

    public static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    public static final String PROP_CONFIG_FILE_NAME = "application";
    public static final String PROP_CONFIG_FILE_NAME_SPLIT = "-";
    public static final String PROP_TYPE = ".properties";
    static{
        load();
    }

    public static String getValue(String key){
        return properties.getProperty(key);
    }

    public static void updateProperties(String key,String value) {
        properties.setProperty(key, value);
    }

    public static void  load(){
        properties = System.getProperties();
        String active=null;
        if(properties.getProperty(SPRING_PROFILES_ACTIVE)!=null){
            active=properties.getProperty(SPRING_PROFILES_ACTIVE);
        }
        properties=new Properties();
        String propPath = PROP_CONFIG_FILE_NAME + PROP_TYPE;
        try(InputStream inputStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(propPath)) {
            properties.load(inputStream);
            if(StringUtils.isEmpty(active)) {
                active=properties.getProperty(SPRING_PROFILES_ACTIVE);
            }else{
                properties.setProperty(SPRING_PROFILES_ACTIVE,active);
            }

            if(!StringUtils.isEmpty(active)) {
                propPath = PROP_CONFIG_FILE_NAME + PROP_CONFIG_FILE_NAME_SPLIT + active + PROP_TYPE;
                try(InputStream activeInputStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(propPath)){
                    properties.load(activeInputStream);
                }
            }
        }catch(Exception e) {
            logger.error("load property error", e);
        }
    }

    public void loadProperty(String fileName){
        InputStream input = null;
        try {
            input = new FileInputStream("bid.properties");
            properties.load(input);
        } catch (FileNotFoundException var1){
            logger.error("{} file not found！", fileName);
        } catch (IOException var2) {
            logger.error("{} file read error！", fileName);
        } finally {
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
