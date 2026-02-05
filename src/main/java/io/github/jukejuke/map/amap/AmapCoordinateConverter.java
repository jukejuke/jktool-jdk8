package io.github.jukejuke.map.amap;

import com.alibaba.fastjson2.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import lombok.Data;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 高德地图坐标转换工具类
 * 封装高德地图API的坐标转换功能
 * 使用{@link Builder}类创建实例以支持灵活配置
 */
public class AmapCoordinateConverter {
    private final String apiKey;
    private final OkHttpClient httpClient;
    private final String baseUrl = "https://restapi.amap.com/v3/assistant/coordinate/convert";

    /**
     * 私有构造函数，通过Builder创建实例
     */
    private AmapCoordinateConverter(Builder builder) {
        this.apiKey = builder.apiKey;
        this.httpClient = builder.httpClient != null ? builder.httpClient : new OkHttpClient();
    }

    /**
     * 构建者模式，用于灵活配置AmapCoordinateConverter实例
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

        public AmapCoordinateConverter build() {
            return new AmapCoordinateConverter(this);
        }
    }

    /**
     * 执行坐标转换
     * @param locations 坐标对，格式为"经度,纬度|经度,纬度"
     * @param coordsys 源坐标系，可选值：gps、mapbar、baidu、autonavi
     * @return 坐标转换响应结果
     * @throws Exception 可能抛出的异常
     */
    /**
     * 坐标系枚举，定义高德地图API支持的坐标系类型
     */
    public enum CoordinateSystem {
        GPS("gps"),
        MAPBAR("mapbar"),
        BAIDU("baidu"),
        AUTONAVI("autonavi"); // (不进行转换)

        private final String value;

        CoordinateSystem(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 执行坐标转换
     * @param locations 坐标对，格式为"经度,纬度|经度,纬度"
     * @param coordsys 源坐标系，使用CoordinateSystem枚举值
     * @return 坐标转换响应结果
     * @throws Exception 可能抛出的异常
     */
    public AmapCoordinateResponse convert(String locations, CoordinateSystem coordsys) throws Exception {
        // 构建请求参数
        Map<String, String> params = new HashMap<>();
        params.put("locations", locations);
        params.put("coordsys", coordsys.getValue());
        params.put("output", "json");
        params.put("key", apiKey);

        // 构建查询字符串
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (query.length() > 0) {
                query.append("&");
            }
            query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()))
                 .append("=")
                 .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()));
        }

        // 构建完整URL
        String requestUrl = baseUrl + "?" + query.toString();

        // 创建HTTP请求
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        // 执行请求并处理响应
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            return JSON.parseObject(responseBody, AmapCoordinateResponse.class);
        }
    }

    /**
     * 高德地图坐标转换API响应结果封装类
     */
    @Data
    public static class AmapCoordinateResponse {
        private String status;
        private String info;
        private String infocode;
        private String locations;
        private String coordsys;
    }
}