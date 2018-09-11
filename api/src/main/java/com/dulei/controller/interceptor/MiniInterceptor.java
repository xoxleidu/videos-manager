package com.dulei.controller.interceptor;

import com.dulei.utils.IMoocJSONResult;
import com.dulei.utils.JsonUtils;
import com.dulei.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class MiniInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperator redisUtil;
    @Value("${USER_REDIS_SESSION}")
    private String USER_REDIS_SESSION;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        IMoocJSONResult iMoocJSONResult = new IMoocJSONResult();
        String headerUserId = request.getHeader("headerUserId");
        String headerUserToken = request.getHeader("headerUserToken");
        if (StringUtils.isNotBlank(headerUserId) && StringUtils.isNotBlank(headerUserToken)){
            String unToken = redisUtil.get(USER_REDIS_SESSION + headerUserId);
            if (StringUtils.isEmpty(unToken) && StringUtils.isBlank(unToken)) {
                System.out.println("超时 请登录");
                returnErrorResponse(response,iMoocJSONResult.errorTokenMsg("超时 请登录"));
                return false;
            } else {
                if (!unToken.equals(headerUserToken)) {
                    System.out.println("设备更新登录");
                    returnErrorResponse(response,iMoocJSONResult.errorTokenMsg("设备更新登录"));
                    return false;
                }
            }
        } else {
            System.out.println("请先登录");
            returnErrorResponse(response, iMoocJSONResult.errorTokenMsg("请先登录"));
            return false;
        }

        System.out.println("通过拦截器");
        //通过拦截器
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 把错误信息写入response流返回
     * @param response 返回信息
     * @param result 输入流
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public void returnErrorResponse(HttpServletResponse response, IMoocJSONResult result)
            throws IOException, UnsupportedEncodingException {
        OutputStream out=null;
        try{
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } finally{
            if(out!=null){
                out.close();
            }
        }
    }
}
