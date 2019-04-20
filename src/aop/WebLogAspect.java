package aop;

import com.alibaba.fastjson.JSONObject;
import common.HttpUtil;
import common.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

//@WebFilter(filterName = "name", urlPatterns = "/*")
@Component
@Order(2)
public class WebLogAspect extends OncePerRequestFilter {

    @Autowired
    private List<HandlerMapping> handlerMappingList;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String ip = IpUtils.getLocalIp(httpServletRequest);

        String requestUrl = httpServletRequest.getRequestURI();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);
        HttpServletRequest request = new MineHttpServletRequestWrapper(httpServletRequest);

        String params = HttpUtil.getBodyString(request);
        JSONObject object = JSONObject.parseObject(params);

        String url = null;
        AntPathMatcher apm = new AntPathMatcher();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = this.requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> item : handlerMethods.entrySet()) {
            for (String urlPattern : item.getKey().getPatternsCondition().getPatterns()) {
                urlPattern = request.getContextPath() + urlPattern;
                if (apm.match(urlPattern, requestUrl)) {
                    url = urlPattern;
                    break;
                }
            }
        }
        if (url == null) {
            url = request.getRequestURI();
        }


        System.out.println("request:" + object.toJSONString());

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } catch (Exception var) {

        } finally {
            String result = getResponseBody(responseWrapper);
            System.out.println("response:" + result);

            stopWatch.stop();
            System.out.println("timeCost:" + stopWatch.getTotalTimeMillis());
        }
    }


    public static String getResponseBody(ContentCachingResponseWrapper responseWrapper) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(responseWrapper, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                String payLoad;
                try {
                    payLoad = new String(buf, 0 ,buf.length, wrapper.getCharacterEncoding());
                } catch (Exception var) {
                    payLoad = "unknown";
                }
                return payLoad;
            }
        }
        return null;
    }
}
