package com.lkj.onetimetreatment.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

@Slf4j
@Component
public class OKHttpUtil {

    private static OkHttpClient okHttpClient;

    @Autowired
    public OKHttpUtil(OkHttpClient okHttpClient) {
        OKHttpUtil.okHttpClient = okHttpClient;
    }

    /**
     * get
     *
     * @param url 请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return
     */
    public static String get(String url, Map<String, String> queries) {
        String responseBody = "";
        StringBuffer sb = new StringBuffer(url);
        if (MapUtils.isNotEmpty(queries)) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        Request request = new Request.Builder().url(sb.toString()).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            //int status = response.code();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }

    /**
     * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"} 参数一：请求Url 参数二：请求的JSON
     * 参数三：请求回调
     */
    public static String postJsonParams(String url, String jsonParams) {
        String responseBody = "";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            //            int status = response.code();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }

}

