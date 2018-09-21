package com.dulei.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class WxCodeImgUtil {

    public static InputStream getCodeStream(String accessToken, String scene, String path,Integer width) {

        RestTemplate rest = new RestTemplate();

        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken;
        Map<String,Object> param = new HashMap<>();
        param.put("scene", scene);
        param.put("path", path);
        param.put("width", width);
        param.put("auto_color", true);


        /*//auto_color为false时 line_color 生效
        Map<String,Object> line_color = new HashMap<>();
        line_color.put("r", 0);
        line_color.put("g", 0);
        line_color.put("b", 0);
        param.put("line_color", line_color);*/

        //System.out.println("调用生成微信URL接口传参:" + param);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HttpEntity requestEntity = new HttpEntity(param, headers);
        ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);

        //System.out.println("调用小程序生成微信永久小程序码URL接口返回结果:" + entity.getBody());

        byte[] result = entity.getBody();
        return new ByteArrayInputStream(result);

    }


}
