package com.example.interceptor;

import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.interceptor.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 拦截处理类
 *
 * @author Minbo.He
 */
@Component
@Slf4j
public class MyHttpInterceptor extends HandlerInterceptorAdapter {

    //相当于把impl装配进入service
    @Resource
    private DemoService demoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String url = request.getRequestURL().toString();
        String method = request.getMethod();//get还是Post
        String uri = request.getRequestURI();
        StringBuilder queryString = new StringBuilder();
        // 去掉最后一个空格
        Map<String, String[]> params = request.getParameterMap();
        for (String key : params.keySet()) {
            String[] values = params.get(key);
            for (String value : values) {
                queryString.append(key).append("=").append(value).append("&");
            }
        }
        queryString = new StringBuilder(queryString.toString().isEmpty() ? "" : queryString.substring(0, queryString.length() - 1));
        log.info(String.format("请求参数, url: %s, method: %s, params: %s", url, method, queryString.toString()));

        // hello不做拦截
        if (uri.equals("/hello")) {
            return true;
        }

        //实现注入，调用服务层方法代码
        log.info("【实现注入】调用服务层方法代码：demoService.sayHello()=" + this.demoService.sayHello());

        // 其他拦截请求（请求必须都带上用户id）
        String userId = request.getParameter("userId");
        if (userId != null) {
            return true;

        } else {
            this.output(response, "{\n"
                    + "\"code\": \"4001\",\n"
                    + "\"message\": \"参数错误\"\n"
                    + "}");
            return false;
        }
    }

    /**
     * 输出结果
     */
    private void output(HttpServletResponse response, String result) throws Exception {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(result);
    }
}
