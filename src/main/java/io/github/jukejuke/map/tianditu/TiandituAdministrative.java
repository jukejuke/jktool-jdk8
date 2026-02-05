package io.github.jukejuke.map.tianditu;

//import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import lombok.Data;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 天地图行政区划服务工具类
 * 封装天地图API的行政区划查询功能
 * 使用{@link Builder}类创建实例以支持灵活配置
 */
public class TiandituAdministrative {
    private final String apiKey;
    private final OkHttpClient httpClient;
    private final String baseUrl = "http://api.tianditu.gov.cn/v2/administrative";

    /**
     * 私有构造函数，通过Builder创建实例
     */
    private TiandituAdministrative(Builder builder) {
        this.apiKey = builder.apiKey;
        this.httpClient = builder.httpClient != null ? builder.httpClient : new OkHttpClient();
    }

    /**
     * 构建者模式，用于灵活配置TiandituAdministrative实例
     */
    public static class Builder {
        private final String apiKey;
        private OkHttpClient httpClient;

        public Builder(String apiKey) {
            this.apiKey = apiKey;
        }

        public Builder httpClient(OkHttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public TiandituAdministrative build() {
            return new TiandituAdministrative(this);
        }
    }

    /**
     * 执行行政区划查询
     * @param keyword 关键字（行政区划代码或名称）
     * @param childLevel 子级级别（0-3）
     * @param extensions 是否返回扩展信息
     * @return 行政区划响应结果
     * @throws Exception 可能抛出的异常
     */
    public TiandituAdministrativeResponse queryAdministrative(String keyword, int childLevel, boolean extensions) throws Exception {
        // 构建请求URL
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.name());
        String requestUrl = String.format("%s?keyword=%s&childLevel=%d&extensions=%s&tk=%s",
                baseUrl, encodedKeyword, childLevel, extensions, apiKey);

        // 创建HTTP请求
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        // 执行请求并处理响应
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            return JSON.parseObject(responseBody, TiandituAdministrativeResponse.class);
        }
    }

    /**
     * 执行行政区划查询（使用默认参数：childLevel=0, extensions=true）
     * @param keyword 关键字（行政区划代码或名称）
     * @return 行政区划响应结果
     * @throws Exception 可能抛出的异常
     */
    public TiandituAdministrativeResponse queryAdministrative(String keyword) throws Exception {
        return queryAdministrative(keyword, 0, true);
    }

    /**
     * 天地图行政区划API响应结果封装类
     */
    @Data
    public static class TiandituAdministrativeResponse {
        private int status;
        private String message;
        private Data1 data;

        @Data
        public static class Data1 {
            private String[] suggestion;
            private District[] district;
        }

        @Data
        public static class District {
            private String gb;
            private String pgb;
            private String name;
            private String boundary;
            private Center center;
            private int level;
        }

        @Data
        public static class Center {
            private double lng;
            private double lat;
        }
    }
}