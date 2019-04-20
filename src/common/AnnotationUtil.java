package common;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationUtil {


    /**
     * 通过method获取对应的完整url
     *
     * @param method
     * @return
     */
    public static String getRequestMappingUrl(Method method) {
        RequestMapping baseMapping = method.getDeclaringClass().getAnnotation(RequestMapping.class);
        String base = "";
        if (baseMapping != null) {
            base = baseMapping.value()[0];
        }

        for (Annotation annotation : method.getAnnotations()) {
            if (annotation.annotationType().getName().endsWith("Mapping")) {
                String[] args = (String[]) AnnotationUtils.getValue(annotation, "value");
                if (args == null || args.length < 1) {
                    return base;
                } else {
                    base = base.endsWith("/") ? base : base + "/";
                }
                return base + args[0];
            }
        }
        return null;
    }


    /**
     * 直接通过HttpServletRequest 获取对应的url pattern
     *
     * @param request
     * @return
     */
    public static String getRequestMappingUrl(HttpServletRequest request) {
        Object value = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        if (value != null) {
            return value.toString();
        }
        return null;
    }

}
