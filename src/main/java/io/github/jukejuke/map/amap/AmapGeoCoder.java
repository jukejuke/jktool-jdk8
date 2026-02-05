package io.github.jukejuke.map.amap;

//import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import lombok.Data;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高德地图地理编码工具类
 * 封装高德地图API的地理编码查询功能（地址转坐标）
 * 使用{@link Builder}类创建实例以支持灵活配置
 */
public class AmapGeoCoder {
    private final String apiKey;
    private final OkHttpClient httpClient;
    private final String baseUrl = "https://restapi.amap.com/v3/geocode/geo";

    /**
     * 私有构造函数，通过Builder创建实例
     */
    private AmapGeoCoder(Builder builder) {
        this.apiKey = builder.apiKey;
        this.httpClient = builder.httpClient != null ? builder.httpClient : new OkHttpClient();
    }

    /**
     * 构建者模式，用于灵活配置AmapGeoCoder实例
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

        public AmapGeoCoder build() {
            return new AmapGeoCoder(this);
        }
    }

    /**
     * 执行地理编码查询（地址转坐标）
     * @param address 地址字符串
     * @param city 可选城市，用于缩小查询范围
     * @return 地理编码响应结果
     * @throws Exception 可能抛出的异常
     */
    public AmapGeoResponse geoCode(String address, String city) throws Exception {
        // 构建请求参数
        Map<String, String> params = new HashMap<>();
        params.put("address", address);
        if (city != null && !city.isEmpty()) {
            params.put("city", city);
        }
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
            return JSON.parseObject(responseBody, AmapGeoResponse.class);
        }
    }

    /**
     * 地理编码简化调用方法（不指定城市）
     * @param address 地址字符串
     * @return 地理编码响应结果
     * @throws Exception 可能抛出的异常
     */
    public AmapGeoResponse geoCode(String address) throws Exception {
        return geoCode(address, null);
    }

    /**
     * 高德地图地理编码API响应结果封装类
     */
    @Data
    public static class AmapGeoResponse {
        private String status;
        private String info;
        private String infocode;
        private int count;
        private List<Geocode> geocodes;

        @Data
        public static class Geocode {
            private String formatted_address;
            private String country;
            private String province;
            private String city;
            private String citycode;
            private String district;
            private String township;
            private String adcode;
            private String street;
            private Object number;
            private String location;
            private String level;
            private Neighborhood neighborhood;
            private Building building;
        }

        @Data
        public static class Neighborhood {
            private List<String> name;
            private List<String> type;
        }

        @Data
        public static class Building {
            private List<String> name;
            private List<String> type;
        }
    }
}