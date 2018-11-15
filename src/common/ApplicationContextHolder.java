package common;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ApplicationContextHolder.context = context;
    }

    /**
     * 根据bean name 获取实例
     * @param beanName
     * @return
     */
    public static Object getBeanByName(String beanName) {
        if (beanName == null || context == null) {
            return null;
        }
        return context.getBean(beanName);
    }

    /**
     * 只适合一个class只被定义一次的bean（也就是说，根据class不能匹配出多个该class的实例）
     * @param clazz
     * @return
     */
    public static Object getBeanByType(Class clazz) {
        if (clazz == null || context == null) {
            return null;
        }
        return context.getBean(clazz);
    }
    public static String[] getBeanDefinitionNames() {
        return context.getBeanDefinitionNames();
    }

    public static ApplicationContext getApplicationContext(){
        return context;
    }

    /**
     * stop the server
     */
    public static void stopServer(){
        // spring boot 2.0
//        ServletWebServerApplicationContext context = (ServletWebServerApplicationContext) ApplicationContextHolder.getContext();
//        context.getWebServer().stop();

        // spring boot 1.5
        EmbeddedWebApplicationContext embeddedWebApplicationContext = (EmbeddedWebApplicationContext) ApplicationContextHolder.getApplicationContext();
        embeddedWebApplicationContext.stop();
    }
}
