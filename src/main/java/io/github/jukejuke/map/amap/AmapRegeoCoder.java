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
import java.util.Map;
import java.util.List;

/**
 * 高德地图逆地理编码工具类
 * 封装高德地图API的逆地理编码查询功能
 * 使用{@link Builder}类创建实例以支持灵活配置
 */
public class AmapRegeoCoder {
    private final String apiKey;
    private final OkHttpClient httpClient;
    private final String baseUrl = "https://restapi.amap.com/v3/geocode/regeo";

    /**
     * 私有构造函数，通过Builder创建实例
     */
    private AmapRegeoCoder(Builder builder) {
        this.apiKey = builder.apiKey;
        this.httpClient = builder.httpClient != null ? builder.httpClient : new OkHttpClient();
    }

    /**
     * 构建者模式，用于灵活配置AmapRegeoCoder实例
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

        public AmapRegeoCoder build() {
            return new AmapRegeoCoder(this);
        }
    }

    /**
     * 执行逆地理编码查询
     * @param longitude 经度
     * @param latitude 纬度
     * @return 地理编码响应结果
     * @throws Exception 可能抛出的异常
     */
    public AmapResponse reverseGeocode(double longitude, double latitude) throws Exception {
        // 构建请求参数
        Map<String, String> params = new HashMap<>();
        params.put("location", longitude + "," + latitude);
        params.put("key", apiKey);
        params.put("extensions", "base");
        params.put("batch", "false");
        params.put("roadlevel", "0");

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
            return JSON.parseObject(responseBody, AmapResponse.class);
        }
    }

    /**
     * 高德地图API响应结果封装类
     */
    @Data
    public static class AmapResponse {
        private String status;
        private String info;
        private String infocode;
        private Regeocode regeocode;

        @Data
        public static class Regeocode {
            private String formattedAddress;
            private AddressComponent addressComponent;
        }

        @Data
        public static class AddressComponent {
            private String province;
            private String city;
            private String citycode;
            private String district;
            private String township;
            private String adcode;
            private String township_code;
            private StreetNumber streetNumber;
            private List<BusinessArea> businessAreas;
        }

        @Data
        public static class StreetNumber {
            private String number;
            private String location;
            private String direction;
            private String distance;
            private String street;
        }

        @Data
        public static class BusinessArea {
            private String location;
            private String name;
            private String id;
        }
    }
}